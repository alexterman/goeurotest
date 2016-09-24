package goeuro.test.data;


import com.google.common.base.MoreObjects;

import java.util.List;

public class Route {

    public int routeId;
    public List<Integer> stations;

    public Route(int routeId, List<Integer> stations) {
        this.routeId = routeId;
        this.stations = stations;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("routeId", routeId)
                .add("stations", stations)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (routeId != route.routeId) return false;
        return stations.equals(route.stations);

    }

    @Override
    public int hashCode() {
        int result = routeId;
        result = 31 * result + stations.hashCode();
        return result;
    }
}
