# siri-xlite
## Service lines discovery
    /siri-xlite/lines-discovery
![50% center](./images/ld.png)

## Service stoppoints discovery
    /siri-xlite/stoppoints-discovery
        
    /siri-xlite/stoppoints-discovery/[xtile]/[ytile]
![](./images/sd.png)

### Pseudocode longitude/latitude -> xtile/ytile
    n = 2 ^ zoom
    xtile = n * ((lon_deg + 180) / 360)
    ytile = n * (1 - (log(tan(lat_rad) + sec(lat_rad)) / π)) / 2
    
###  Pseudocode xtile/ytile -> longitude/latitude
    n = 2 ^ zoom
    lon_deg = xtile / n * 360.0 - 180.0
    lat_rad = arctan(sinh(π * (1 - 2 * ytile / n)))
    lat_deg = lat_rad * 180.0 / π
    

## Service estimated timetable
    /siri-xlite/estimated-timetable/[lineRef]
![](./images/et.png)

## Service stop monitoring
    /siri-xlite/stop-monitoring/[stopPointRef]
![](./images/sm.png)

## Service estimated vehicle journey
    /siri-xlite/estimated-vehicle-journey/[datedVehicleJourneyRef]
![](./images/evj.png)
