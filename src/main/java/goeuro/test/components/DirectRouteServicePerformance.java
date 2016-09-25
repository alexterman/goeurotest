package goeuro.test.components;

import com.google.common.collect.Sets;
import goeuro.test.components.api.DirectlyAccessibleStations;
import goeuro.test.components.api.RoutesDataResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

//@Service
public class DirectRouteServicePerformance implements DirectlyAccessibleStations {

    private Map<Integer, List<Integer>> routesToStations;
    private Map<Integer, Set<Integer>> stationToRouteIds;


    @Autowired
    public DirectRouteServicePerformance(RoutesDataResolver routesDataResolver) {

        stationToRouteIds = new HashMap<>();
        routesToStations = new HashMap<>();
        prepareData(routesDataResolver);
    }

    private void prepareData(RoutesDataResolver routesDataResolver) {

        Collection<String> routesRawData = routesDataResolver.getRoutesData();

        String firstElement = routesRawData.iterator().next();
        routesRawData.remove(firstElement);

        routesRawData.forEach(this::buildRoute);

    }

    private void buildRoute(String raw) {

        final String[] splited = raw.split(" ");

        final List<Integer> routeData = Arrays.stream(splited)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Integer routeId = routeData.remove(0);

        mapStationsToRouteId(routeId, routeData);
        mapRouteIdToRouteStations(routeId, routeData);
    }

    private void mapStationsToRouteId(Integer routeId, List<Integer> routeData) {

        routeData.forEach(stationId -> {
            BiFunction<Set<Integer>, Set<Integer>, Set<Integer>> remappingRouteToSet = (oldValue, newValue) -> {
                oldValue.addAll(newValue);
                return oldValue;
            };
            stationToRouteIds
                    .merge(stationId, Sets.newHashSet(routeId), remappingRouteToSet);
        });

    }

    private void mapRouteIdToRouteStations(Integer routeId, List<Integer> routeData) {
        routesToStations.put(routeId, routeData);
    }

    @Override
    public boolean areConnected(int departureStationId, int arrivalStationId) {

        Set<Integer> departureRoutes = stationToRouteIds.getOrDefault(departureStationId, new HashSet<>());
        Set<Integer> arrivalRoutes = stationToRouteIds.getOrDefault(arrivalStationId, new HashSet<>());

        Set<Integer> commonRouteIds = departureRoutes.stream().
                filter(arrivalRoutes::contains).collect(Collectors.toSet());

        Optional<List<Integer>> result = commonRouteIds.stream().map(routesToStations::get)
                .filter(stations -> stations.indexOf(departureStationId) < stations.indexOf(arrivalStationId))
                .findFirst();
        return result.isPresent();
    }
}
