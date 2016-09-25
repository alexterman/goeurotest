package goeuro.test.components;


import goeuro.test.components.api.DirectlyAccessibleStations;
import goeuro.test.components.api.RoutesDataResolver;
import goeuro.test.data.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DirectRouteService implements DirectlyAccessibleStations {

    private Map<Integer, Set<Integer>> routesData;

    @Autowired
    public DirectRouteService(RoutesDataResolver routesDataResolver) {

        routesData = new HashMap<>();
        prepareData(routesDataResolver);
    }

    private void prepareData(RoutesDataResolver routesDataResolver) {

        Collection<String> rawRoutesData = routesDataResolver.getRoutesData();
        validateDataFormat(rawRoutesData);

        List<Route> routes = rawRoutesData.stream()
                .map(this::createRoute).collect(Collectors.toList());
        validateRoutes(routes);
        routes.forEach(this::buildRouteData);
    }

    private Route createRoute(String rawRoute) {

        String[] rawStations = rawRoute.split(" ");
        List<Integer> stations = Arrays.stream(rawStations)
                .map(Integer::parseInt).collect(Collectors.toList());
        Integer routeId = stations.remove(0);

        return new Route(routeId, stations);
    }

    private void buildRouteData(Route route) {
        List<Integer> stations = route.stations;

        for (int i = 0; i < stations.size(); i++) {

            Integer key = stations.get(i);
            Set<Integer> connections = routesData.getOrDefault(key, new HashSet<>());
            if (connections.isEmpty()) {
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
                    streamToString(route.stations.stream()));
        }else{
            routeIds.add(routeId);
        }

        if(route.stations.size() <= 2){
            throw new RuntimeException("Invalid route found: " +
                    streamToString(route.stations.stream()));
        }

        Set<Integer> routeAsSet = route.stations.stream().collect(Collectors.toSet());

        if(routeAsSet.size() != route.stations.size()){
            throw new RuntimeException("Duplicate station id in route found: " +
                    streamToString(route.stations.stream()));
        }
    }

    private void validateDataFormat(Collection<String> rawRoutesData) {

        if(rawRoutesData == null){
            throw new RuntimeException("The route data can't be NULL");
        }

        if( rawRoutesData.isEmpty() || rawRoutesData.size() <= 1){
            throw new RuntimeException("The route data is not valid: " +
                    streamToString(rawRoutesData.stream()));
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

    private void removeSizeElement(Collection<String> rawRoutesData) {
        String first = rawRoutesData.iterator().next();
        rawRoutesData.remove(first);
    }

    private String streamToString(Stream<?> stream) {
        return stream.map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public boolean areConnected(int departureStationId, int arrivalStationId) {
        Set<Integer> connectedStations = routesData.get(departureStationId);
        if(connectedStations == null){
            return false;
        }
        return connectedStations.contains(arrivalStationId);
    }
}
