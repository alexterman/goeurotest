package goeuro.test.exception;


public class StationIdNotFound extends RuntimeException {

    public int notFoundStationId;

    public StationIdNotFound(int departureStationId) {
        notFoundStationId = departureStationId;
    }
}
