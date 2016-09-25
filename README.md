# goeurotest

# Direct Bus Assesment

### Tech
- Spring boot
- Java 8
- maven
- junit/assertj

### Assumptions
- Routes have one direction, given 1 2 3 4 route and calling API with 2,4 will return true, but 4,2 will return false
- The application should take care of data validation, on invalid format the service will throw exception and exit
- No database should be used
- No 404 should be returned in case of stationId does not exist, but direct_bus_route=false

### Known problems
 - Trying to push 100000 routes with 1000 stations per each is not working due to GC overhead limit exceeded


As possible solution I switched from building map of all stations to its neighbours on the right to 2 smaller maps: routeId to route list of stations and stationIds->all route ids its appears. The implementation of the second approach is in DirectRouteServicePerformance.java class (supplied in the code but not running), but it didn't solve the problem.
 
 
 - When running bash test run_test_local.sh
 ```
 bash run_test_local.sh ../../../dev/direct-bus-route/service.sh
 ```
 it is needed to set sleep (in simple_test.sh) more than 5 seconds (preferably 10), since it taking some time for springboot to start.
 
 
