package goeuro.test.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by alex on 9/24/16.
 */
public class DirectBusResponse {

    @JsonProperty("dep_sid")
    public int departureStationId;

    @JsonProperty("arr_sid")
    public int arrivalStationId;

    @JsonProperty("direct_bus_route")
    public boolean directBusRoute;

    public DirectBusResponse() {
    }

    public DirectBusResponse(int departureStationId, int arrivalStationId, boolean areConnected) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
        this.directBusRoute = areConnected;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("departureStationId", departureStationId)
                .add("arrivalStationId", arrivalStationId)
                .add("directBusRoute", directBusRoute)
                .toString();
    }
}
