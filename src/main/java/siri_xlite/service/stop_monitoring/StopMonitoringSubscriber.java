package siri_xlite.service.stop_monitoring;

import com.fasterxml.jackson.core.JsonGenerator;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bson.Document;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import siri_xlite.Configuration;
import siri_xlite.common.HttpStatus;
import siri_xlite.marshaller.json.*;
import siri_xlite.service.common.SiriException;
import siri_xlite.service.common.SiriStructureFactory;
import siri_xlite.service.common.SiriSubscriber;
import uk.org.siri.siri.StopVisitTypeEnumeration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StopMonitoringSubscriber implements SiriSubscriber<Document, StopMonitoringParameters>, HttpStatus {

    private Configuration configuration;
    private StopMonitoringParameters parameters;
    private final RoutingContext context;
    private ByteArrayOutputStream out;
    private JsonGenerator writer;
    private AtomicInteger count = new AtomicInteger();

    private StopMonitoringSubscriber(RoutingContext context) {
        this.context = context;
        this.out = new ByteArrayOutputStream();
        this.writer = createJsonWriter(out);
    }

    public static SiriSubscriber<Document, StopMonitoringParameters> create(RoutingContext context) {
        return new StopMonitoringSubscriber(context);
    }

    @Override
    public void configure(Configuration configuration, StopMonitoringParameters parameters) {
        this.configuration = configuration;
        this.parameters = parameters;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        writeStartDocument(writer);
        writeStartServiceDelivery(configuration, parameters, writer);
    }

    @Override
    public void onNext(Document t) {
        try {
            count.incrementAndGet();
            if (count.get() == 1) {
                writeStartStopMonitoringDelivery();
            }
            writeMonitoredStopVisit(t, 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onComplete() {
        try {
            if (count.get() == 0) {
                writeStartStopMonitoringDelivery();
            }
            writer.writeEndArray();
            writer.writeEndObject();
            writeEndDocument(writer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (t instanceof SiriException) {
                SiriException e = (SiriException) t;

                writeObject(writer, "StopPointsDelivery", e, value -> {
                    writeField(writer, "ResponseTimestamp", parameters.getNow());
                    writeField(writer, "Status", false);
                    writeObject(writer, "ErrorCondition", value,
                            exception -> wrapper(() -> SiriExceptionMarshaller.getInstance().write(writer, exception)));
                });
            }
            writeEndDocument(writer);
        } finally {
            this.context.response().setStatusCode(BAD_REQUEST);
        }
    }

    public void close() {
        try {
            if (writer != null) {
                writer.close();
                this.context.response().putHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .putHeader(HttpHeaders.CACHE_CONTROL, "max-age=30").end(out.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    private void writeStartStopMonitoringDelivery() {
        try {
            writer.writeStartObject("StopMonitoringDelivery");
            {
                writeField(writer, "ResponseTimestamp",
                        SiriStructureFactory.createXMLGregorianCalendar(parameters.getNow()));
                String messageIdentifier = parameters.getMessageIdentifier();
                if (messageIdentifier != null && !messageIdentifier.isEmpty()) {
                    writeField(writer, "RequestMessageRef", messageIdentifier);
                }
                writeField(writer, "Status", true);
                writeField(writer, "version", configuration.getVersion());
                writer.writeArrayFieldStart("MonitoredStopVisit");
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    private void writeMonitoredStopVisit(Document t, int index) {
        writeObject(writer, t, source -> {
            writeField(writer, "RecordedAtTime", source.getDate("recordedAtTime"));
            writeField(writer, "ItemIdentifier", source.getString("datedVehicleJourneyRef") + ":" + index);
            writeField(writer, "MonitoringRef", parameters.getMonitoringRef());

            // monitored vehicle journey
            writeMonitoredVehicleJourney(source, index);
        });
    }

    private void writeMonitoredVehicleJourney(Document t, int index) {
        writeObject(writer, "MonitoredVehicleJourney", t, source -> {
            LineIdentityGroupMarshaller.getInstance().write(writer, source);
            JourneyPatternInfoGroupMarshaller.getInstance().write(writer, source);
            ServiceInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndNamesGroupMarshaller.getInstance().write(writer, source);
            JourneyInfoGroupMarshaller.getInstance().write(writer, source);
            JourneyEndTimesGroupMarshaller.getInstance().write(writer, source);
            DisruptionGroupMarshaller.getInstance().write(writer, source);
            JourneyProgressGroupMarshaller.getInstance().write(writer, source);
            TrainOperationalInfoGroupMarshaller.getInstance().write(writer, source);

            List<Document> calls = source.get("calls", List.class);

            // previous calls
            if (parameters.getMaximumNumberOfCallsPrevious() != null
                    && parameters.getMaximumNumberOfCallsPrevious() > 0) {
                int max = parameters.getMaximumNumberOfCallsPrevious();
                int min = Math.max(0, index - max);
                writeArray(writer, "PreviousCalls", calls.subList(min, max), this::writePreviousCall);
            }

            // monitored call
            writeMonitoredCall(calls.get(index));

            // onward calls
            if (parameters.getMaximumNumberOfCallsOnwards() != null
                    && parameters.getMaximumNumberOfCallsOnwards() > 0) {
                int max = parameters.getMaximumNumberOfCallsOnwards();
                int length = Math.min(calls.size(), index + 1 + max);
                writeArray(writer, "OnwardCalls", calls.subList(index + 1, length), this::writeOnwardCall);
            }
        });
    }

    private void writePreviousCall(Document t) {
        writeObject(writer, t, call -> {
            StopPointInSequenceGroupMarshaller.getInstance().write(writer, call);

            // set vehicleAtStop
            writeField(writer, "VehicleAtStop", call.getBoolean("vehicleAtStop"));

            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ARRIVALS)) {
                VehicleArrivalTimesGroupMarshaller.getInstance().write(writer, call);
            }

            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.DEPARTURES)) {
                VehicleDepartureTimesGroupMarshaller.getInstance().write(writer, call);
            }
        });
    }

    private void writeMonitoredCall(Document t) {
        writeObject(writer, "MonitoredCall", t, call -> {
            StopPointInSequenceGroupMarshaller.getInstance().write(writer, call);
            CallRealtimeGroupMarshaller.getInstance().write(writer, call);
            CallRailGroupMarshaller.getInstance().write(writer, call);
            CallPropertyGroupMarshaller.getInstance().write(writer, call);
            CallNoteGroupMarshaller.getInstance().write(writer, call);
            DisruptionGroupMarshaller.getInstance().write(writer, call);
            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ARRIVALS)) {
                StopArrivalGroupMarshaller.getInstance().write(writer, call);
            }
            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.DEPARTURES)) {
                StopDepartureGroupMarshaller.getInstance().write(writer, call);
            }
            HeadwayIntervalGroupMarshaller.getInstance().write(writer, call);
            StopProximityGroupMarshaller.getInstance().write(writer, call);
        });
    }

    private void writeOnwardCall(Document t) {
        writeObject(writer, t, call -> {
            StopPointInSequenceGroupMarshaller.getInstance().write(writer, call);

            // set vehicleAtStop
            writeField(writer, "VehicleAtStop", call.getBoolean("vehicleAtStop"));

            // timingPoint :bool;

            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ARRIVALS)) {
                OnwardVehicleArrivalTimesGroupMarshaller.getInstance().write(writer, call);
                MonitoredStopArrivalStatusGroupMarshaller.getInstance().write(writer, call);
            }

            if (parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.ALL)
                    || parameters.getStopVisitTypes().equals(StopVisitTypeEnumeration.DEPARTURES)) {
                OnwardVehicleDepartureTimesGroupMarshaller.getInstance().write(writer, call);
                PassengerDepartureTimesGroupMarshaller.getInstance().write(writer, call);
                MonitoredStopDepartureStatusGroupMarshaller.getInstance().write(writer, call);
                HeadwayIntervalGroupMarshaller.getInstance().write(writer, call);
                // StopProximityGroupMarshaller.getInstance().write(writer, call);
            }
        });
    }

}