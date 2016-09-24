package goeuro.test.rest.resource;


import goeuro.test.components.DirectRouteService;
import goeuro.test.exception.StationIdNotFound;
import goeuro.test.rest.api.DirectBusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BusRouteAccess {

    private final static Logger LOGGER = LoggerFactory.getLogger(BusRouteAccess.class);

    private DirectRouteService service;


    @Autowired
    public BusRouteAccess(DirectRouteService service) {
        this.service = service;
    }


    @RequestMapping (method = RequestMethod.GET, path = "/direct")
    public DirectBusResponse getDirectAccess (
            @RequestParam(value="dep_sid") int depSid, @RequestParam(value="arr_sid") int arrSid) {

        boolean areConnected = service.areConnected(depSid, arrSid);

        return new DirectBusResponse(depSid, arrSid, areConnected);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleBusStationNotFound(StationIdNotFound ex) {
        LOGGER.error("No station id {} is found",ex.notFoundStationId);
    }

}
