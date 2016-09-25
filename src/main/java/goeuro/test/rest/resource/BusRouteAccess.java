package goeuro.test.rest.resource;


import goeuro.test.components.DirectRouteService;
import goeuro.test.rest.api.DirectBusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BusRouteAccess {

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
}
