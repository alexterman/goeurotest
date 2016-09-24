package goeuro.test.components;


import goeuro.test.components.api.ConnectibleStations;
import goeuro.test.components.api.RoutesDataResolver;
import goeuro.test.data.Route;
import goeuro.test.exception.StationIdNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DirectRouteService implements ConnectibleStations {

    private Map<Integer, Set<Integer>> routesData;

    @Autowired
    public DirectRouteService(RoutesDataResolver routesDataResolver) {

        routesData = new HashMap<>();
        prepareData(routesDataResolver);
    }

    private void prepareData(RoutesDataResolver routesDataResolver) {

        Collection<String> rawRoutesData = routesDataResolver.getRoutesData();
        validateDataSize(rawRoutesData);

        List<Route> routes = new ArrayList<>();
        for (String rawRoute :rawRoutesData) {
            Route route = createRoute(rawRoute);
            routes.add(route);
        }

        validateRoutes(routes);

        routes.forEach(this::buildRouteData);

    }

    private Route createRoute(String rawRoute) {
        List<Integer> stations = new ArrayList<>();

        String[] split = rawRoute.split(" ");
        int routeId = Integer.valueOf(split[0]);
        for (int i = 1; i < split.length; i++) {
            stations.add(Integer.valueOf(split[i]));
        }
        return new Route(routeId, stations);
    }

    private void buildRouteData(Route route) {

        List<Integer> stations = route.stations;

        for (int i = 0; i < stations.size(); i++) {

            Integer key = stations.get(i);
            Set<Integer> connections = routesData.get(key);
            if (connections == null) {
                connections = new HashSet<>();
                routesData.put(key, connections);
            }

            for (int j = i + 1; j < stations.size(); j++) {
                connections.add(stations.get(j));
            }
        }
    }


    private void validateRoutes(List<Route> routes) {

        Set<Integer> routeIds = new HashSet<>();
        routes.forEach(route -> validateRoute(route, routeIds));
    }

    private void validateRoute(Route route, Set<Integer> routeIds) {

        int routeId = route.routeId;
        if(routeIds.contains(routeId)){
            throw new RuntimeException("Duplicate route id "+routeId+" found in : " +
                    route.stations.stream().map(Object::toString).collect(Collectors.joining(",")));
        }else{
            routeIds.add(routeId);
        }

        if(route.stations.size() <= 2){
            throw new RuntimeException("Invalid route found: " +
                    route.stations.stream().map(Object::toString).collect(Collectors.joining(",")));
        }

        Set<Integer> routeAsSet = route.stations.stream().collect(Collectors.toSet());

        if(routeAsSet.size() != route.stations.size()){
            throw new RuntimeException("Duplicate station id in route found: " +
                    route.stations.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
    }

    private void removeSizeElement(Collection<String> rawRoutesData) {
        String first = rawRoutesData.iterator().next();
        rawRoutesData.remove(first);
    }


    private void validateDataSize(Collection<String> rawRoutesData) {

        if(rawRoutesData == null || rawRoutesData.isEmpty() || rawRoutesData.size() <= 1){
            throw new RuntimeException("The route data is not valid: " +
                    collectionToString(rawRoutesData));
        }
        String firstElement = rawRoutesData.iterator().next();
        int numberOfRoutes = Integer.valueOf(firstElement);

        int actualNumberOfRoutes = rawRoutesData.size() - 1;
        if(numberOfRoutes != actualNumberOfRoutes){
            throw new RuntimeException("Size header " +
                    numberOfRoutes + " not equals to actual routes number in data file " + actualNumberOfRoutes);
        }

        removeSizeElement(rawRoutesData);
    }

    private String collectionToString(Collection<String> collection) {
        if(collection != null){
            return collection.stream().map(Object::toString).collect(Collectors.joining(","));
        }else{
            return "NULL";
        }
    }

    @Override
    public boolean areConnected(int departureStationId, int arrivalStationId) {
        Set<Integer> connectedStations = routesData.get(departureStationId);
        if(connectedStations == null){
            throw new StationIdNotFound(departureStationId);
        }

        return connectedStations.contains(arrivalStationId);
    }
}
