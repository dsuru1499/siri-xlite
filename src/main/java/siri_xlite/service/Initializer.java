package siri_xlite.service;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import gtfs.importer.GtfsImporter;
import gtfs.importer.Index;
import gtfs.model.*;
import gtfs.model.Stop.LocationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import siri_xlite.common.Color;
import siri_xlite.common.ZipUtils;
import siri_xlite.model.*;
import uk.org.siri.siri.CallStatusEnumeration;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Initializer {
    private static final String SEP = "-";
    private static final String ARCHIVE = "data.zip";
    private static final String OUTPUT_DIR = "siri";
    private static final int BULK_SIZE = 1000;

    private MongoDatabase database;

    @Autowired
    private Environment environment;

    @PostConstruct
    void onStartup() {
        String temp = environment.getProperty("java.io.tmpdir", "/tmp");
        Path path = Paths.get(temp, OUTPUT_DIR);
        if (Files.notExists(path)) {
            initialize();
        }
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Paris")
    boolean initialize() {

        Monitor monitor = MonitorFactory.start();
        log.info(Color.YELLOW + "[DSU] initialize model (~ 1mn)" + Color.NORMAL);

        String temp = environment.getProperty("java.io.tmpdir", "/tmp");

        Path path = Paths.get(temp, OUTPUT_DIR);
        try {
            if (Files.notExists(path)) {
                extractArchive(path);
            }
        } catch (Exception e) {

            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException ignored) {
            }
            log.error(e.getMessage(), e);
        }

        String host = environment.getProperty("spring.data.mongodb.host", "localhost");
        String database = environment.getProperty("spring.data.mongodb.database", "siri");
        MongoClient client = MongoClients.create("mongodb://" + host);
        this.database = client.getDatabase(database);

        GtfsImporter importer = new GtfsImporter(path.toString());
        SetValuedMap<String, Destination> destinations = new HashSetValuedHashMap<>();
        SetValuedMap<String, String> lineRefs = new HashSetValuedHashMap<>();
        fillVehicleJourney(importer, lineRefs, destinations);
        fillAnnotatedLine(importer, destinations);
        fillAnnotatedStopPoint(importer, lineRefs);

        importer.dispose();
        log.info(Color.YELLOW + "[DSU] model initialized : " + monitor.stop() + Color.NORMAL);

        return Boolean.TRUE;

    }

    private void fillVehicleJourney(GtfsImporter importer, SetValuedMap<String, String> lineRefs,
            SetValuedMap<String, Destination> destinations) {

        Monitor monitor = MonitorFactory.start("vehicle_journey");
        Monitor tripMonitor = MonitorFactory.start("trip");
        Monitor stopTimeMonitor = MonitorFactory.start("stop-time");

        try {

            MongoCollection<Document> collection = database.getCollection("vehicle_journey", Document.class);
            collection.drop();
            collection = database.getCollection("vehicle_journey", Document.class);
            collection.createIndex(Indexes.ascending("datedVehicleJourneyRef"), new IndexOptions().unique(true));
            collection.createIndex(Indexes.ascending("calls.stopPointRef"));
            // vehicleJourneyRepository.clearAll();

            List<VehicleJourneyDocument> documents = new ArrayList<>(BULK_SIZE);

            VehicleJourneyBuilder.VehicleJourneyDocumentBuilder builder = VehicleJourneyBuilder.builder();
            LocationBuilder.DocumentBuilder location = LocationBuilder.builder();
            CallBuilder.DocumentBuilder call = CallBuilder.builder();

            Date now = new Date();
            builder.recordedAtTime(now);

            Index<Calendar> calendars = importer.getCalendarByService();
            Index<CalendarDate> dates = importer.getCalendarDateByService();

            for (Route route : importer.getRouteById()) {

                Index<Agency> agencies = importer.getAgencyById();
                Agency agency = agencies.getValue(route.getAgencyId());
                Index<Trip> trips = importer.getTripByRoute();

                for (Trip trip : trips.values(route.getRouteId())) {

                    tripMonitor.start();
                    // log.info("TRIP {} " , trip.getTripId());

                    builder.delay(0L);
                    builder.recordedAtTime(now);
                    builder.bearing(0d);
                    builder.vehicleLocation(location.longitude(0d).latitude(0d).build());
                    builder.vehicleJourneyName(trip.getTripId());
                    builder.operatorRef(agency.getAgencyId());
                    builder.directionName(trip.getTripHeadSign());
                    builder.publishedLineName(route.getRouteLongName());
                    builder.routeRef(trip.getTripId());
                    builder.vehicleModes(new ArrayList<>(route.getRouteType().ordinal()));
                    builder.journeyPatternName(trip.getTripId());
                    builder.journeyPatternRef(trip.getTripId());
                    builder.datedVehicleJourneyRef(trip.getTripId());
                    builder.directionRef(trip.getDirectionId().name());
                    builder.lineRef(route.getRouteId());
                    builder.monitored(true);
                    List<Document> calls = new ArrayList<>();
                    builder.calls(calls);

                    Calendar calendar = calendars.getValue(trip.getServiceId());
                    CalendarDate calendarDate = dates.getValue(trip.getServiceId());

                    if (!filterByCalendar(calendar, calendarDate)) {
                        continue;
                    }

                    Index<StopTime> stopTimes = importer.getStopTimeByTrip();

                    Iterator<StopTime> stopTimesIterator = stopTimes.values(trip.getTripId()).iterator();
                    for (int i = 0; stopTimesIterator.hasNext(); i++) {
                        StopTime stopTime = stopTimesIterator.next();
                        stopTimeMonitor.start();

                        Index<Stop> stops = importer.getStopById();
                        Stop stop = stops.getValue(stopTime.getStopId());

                        lineRefs.put(stop.getStopId(), route.getRouteId());

                        if (stop.locationType == LocationType.Stop && stop.parentStation != null
                                && !stop.parentStation.isEmpty()) {
                            Stop station = stops.getValue(stop.parentStation);
                            if (station != null) {
                                lineRefs.put(station.getStopId(), route.getRouteId());
                            }
                        }
                        if (i == 0) {
                            builder.originRef(stop.getStopId());
                            builder.originName(stop.getStopName());
                            builder.originAimedDepartureTime(stopTime.getDepartureTime().getTime());
                            builder.originDisplay(stop.getStopName());
                        }

                        if (!stopTimesIterator.hasNext()) {
                            builder.destinationRef(stop.getStopId());
                            builder.destinationName(stop.getStopName());
                            builder.destinationAimedArrivalTime(stopTime.getArrivalTime().getTime());
                            builder.destinationDisplay(stop.getStopName());

                            destinations.put(route.getRouteId(), Destination.builder().destinationRef(stop.getStopId())
                                    .placeName(stop.getStopName()).build());
                        }

                        Date aimedArrivalTime = stopTime.getArrivalTime().getTime();
                        Date aimedDepartureTime = stopTime.getDepartureTime().getTime();
                        calls.add(call.aimedArrivalTime(aimedArrivalTime).expectedArrivalTime(aimedArrivalTime)
                                .actualArrivalTime(aimedArrivalTime).aimedDepartureTime(aimedDepartureTime)
                                .expectedDepartureTime(aimedDepartureTime).actualDepartureTime(aimedDepartureTime)
                                .destinationDisplay(stopTime.getStopHeadsign()).stopPointName(stop.getStopName())
                                .stopPointRef(stopTime.getStopId()).order(stopTime.getStopSequence() + 1)
                                .departureStatus(CallStatusEnumeration.ON_TIME.ordinal())
                                .arrivalStatus(CallStatusEnumeration.ON_TIME.ordinal()).build());

                        stopTimeMonitor.stop();
                    }

                    documents.add(builder.build());

                    if (documents.size() == BULK_SIZE) {
                        // vehicleJourneyRepository.saveAll(documents).then().subscribe();
                        collection.insertMany(documents);
                        documents.clear();
                    }

                    tripMonitor.stop();
                }
            }

            if (documents.size() > 0) {
                // vehicleJourneyRepository.saveAll(documents).then().subscribe();
                collection.insertMany(documents);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + stopTimeMonitor.stop() + Color.NORMAL);
            log.info(Color.YELLOW + tripMonitor.stop() + Color.NORMAL);
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);

            log.info(String.format("%s[DSU] trip : %d /(s) %s", Color.YELLOW,
                    Math.round(tripMonitor.getHits() / (tripMonitor.getTotal() / 1000)), Color.NORMAL));
            log.info(String.format("%s[DSU] stop time : %d /(s) %s", Color.YELLOW,
                    Math.round(stopTimeMonitor.getHits() / (stopTimeMonitor.getTotal() / 1000)), Color.NORMAL));
        }
    }

    private void fillAnnotatedLine(GtfsImporter importer, SetValuedMap<String, Destination> destinations) {

        Monitor monitor = MonitorFactory.start("annotated_line");
        Monitor routeMonitor = MonitorFactory.start("route");

        try {
            MongoCollection<Document> collection = database.getCollection("lines", Document.class);
            collection.drop();
            collection = database.getCollection("lines", Document.class);
            collection.createIndex(Indexes.ascending("lineRef"), new IndexOptions().unique(true));
            // linesRepository.clearAll();

            LineBuilder.LineDocumentBuilder builder = LineBuilder.builder();
            DestinationBuilder.DocumentBuilder destination = DestinationBuilder.builder();

            List<LineDocument> documents = new ArrayList<>(BULK_SIZE);
            Date now = new Date();

            for (Route route : importer.getRouteById()) {
                routeMonitor.start();
                LineDocument document = builder.recordedAtTime(now).lineRef(route.getRouteId())
                        .lineName(route.getRouteLongName())
                        .destinations(destinations.get(route.getRouteId()).stream().map(
                                t -> destination.destinationRef(t.destinationRef()).placeName(t.placeName()).build())
                                .collect(Collectors.toList()))
                        .build();

                documents.add(document);

                if (documents.size() == BULK_SIZE) {
                    // linesRepository.saveAll(documents).then().subscribe();
                    collection.insertMany(documents);
                    documents.clear();
                }

                routeMonitor.stop();
            }

            if (documents.size() > 0) {
                // linesRepository.saveAll(documents).then().subscribe();
                collection.insertMany(documents);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + routeMonitor.stop() + Color.NORMAL);
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private void fillAnnotatedStopPoint(GtfsImporter importer, SetValuedMap<String, String> lineRefs) {

        Monitor monitor = MonitorFactory.start("annotated_stoppoint");
        Monitor stopMonitor = MonitorFactory.start("stop");

        try {
            MongoCollection<Document> collection = database.getCollection("stoppoints", Document.class);
            collection.drop();
            collection = database.getCollection("stoppoints", Document.class);
            collection.createIndex(Indexes.ascending("stopPointRef"), new IndexOptions().unique(true));
            collection.createIndex(Indexes.ascending("parent"));
            collection.createIndex(Indexes.geo2dsphere("location"));

            // stopPointsRepository.clearAll();

            List<StopPointDocument> documents = new ArrayList<>(BULK_SIZE);
            StopPointBuilder.StopPointDocumentBuilder builder = StopPointBuilder.builder();
            LocationBuilder.DocumentBuilder location = LocationBuilder.builder();
            Date now = new Date();

            for (Stop stop : importer.getStopById()) {
                stopMonitor.start();

                StopPointDocument document = builder.recordedAtTime(now).stopPointRef(stop.getStopId())
                        .parent(stop.getParentStation()).stopName(stop.getStopName())
                        .lineRefs(lineRefs.get(stop.getStopId()))
                        .location(location.longitude(stop.getStopLon().doubleValue())
                                .latitude(stop.getStopLat().doubleValue()).build())
                        .build();

                documents.add(document);
                if (documents.size() == BULK_SIZE) {
                    // stopPointsRepository.saveAll(documents).then().subscribe();;
                    collection.insertMany(documents);
                    documents.clear();
                }

                stopMonitor.stop();
            }

            if (documents.size() > 0) {
                // stopPointsRepository.saveAll(documents).then().subscribe();
                collection.insertMany(documents);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.YELLOW + stopMonitor.stop() + Color.NORMAL);
            log.info(Color.YELLOW + monitor.stop() + Color.NORMAL);
        }
    }

    private boolean filterByCalendar(Calendar calendar, CalendarDate calendarDate) {
        boolean result = false;

        if (calendar != null) {

            LocalDate now = LocalDate.now();
            DayOfWeek day = now.getDayOfWeek();

            // LocalDate startDate = calendar.getStartDate().toLocalDate();
            // startDate = startDate.withYear(now.getYear());
            // LocalDate endDate = calendar.getEndDate().toLocalDate();
            // endDate = endDate.withYear(now.getYear());

            // if ((now.compareTo(startDate) >= 0) && (now.compareTo(endDate) <=
            // 0)) {
            switch (day) {
            case MONDAY:
                result = calendar.getMonday();
                break;
            case TUESDAY:
                result = calendar.getTuesday();
                break;
            case WEDNESDAY:
                result = calendar.getWednesday();
                break;
            case THURSDAY:
                result = calendar.getThursday();
                break;
            case FRIDAY:
                result = calendar.getFriday();
                break;
            case SATURDAY:
                result = calendar.getSaturday();
                break;
            case SUNDAY:
                result = calendar.getSunday();
                break;
            default:
                break;
            }

            // }
            /*
             * if (calendarDate != null) { LocalDate exceptionDate = calendarDate.getDate().toLocalDate();
             * exceptionDate.withYear(now.getYear()); if (now.compareTo(exceptionDate) == 0) { switch
             * (calendarDate.getExceptionType()) { case Added: result = true; break; case Removed: result = false;
             * break; default: break; } } }
             */
        }
        return result;
    }

    private void extractArchive(Path path) throws IOException {
        Monitor monitor = MonitorFactory.start();

        String temp = System.getProperty("java.io.tmpdir");
        Files.createDirectories(path);
        Path data = Paths.get(".", ARCHIVE);
        InputStream in = new BufferedInputStream(Files.newInputStream(data));

        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // InputStream in = new BufferedInputStream(loader.getResourceAsStream(ARCHIVE));
        File file = Paths.get(temp, OUTPUT_DIR, ARCHIVE).toFile();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        IOUtils.copy(in, out);
        out.close();
        in.close();
        log.info(Color.YELLOW + "[DSU] copy file : " + file + " " + Color.NORMAL);

        ZipUtils.unzipArchive(file, path.toFile());
        log.info(Color.YELLOW + "[DSU] extract archive : " + path + " " + monitor.stop() + Color.NORMAL);

    }

}
