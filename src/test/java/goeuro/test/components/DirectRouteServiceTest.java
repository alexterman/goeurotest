package goeuro.test.components;

import goeuro.test.components.api.RoutesDataResolver;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by alex on 9/24/16.
 */
public class DirectRouteServiceTest {


    @Test
    public void testValidRouteData (){

        DirectRouteService directRouteService = new DirectRouteService(new RoutesDataResolver() {
            @Override
            public Collection<String> getRoutesData() {
                return new ArrayList<String>(){{add("3");add("0 0 1 2 3 4");add( "1 3 1 6 5");add("2 0 6 4");}};
            }
        });

        Assertions.assertThat(directRouteService.areConnected(0,1)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(0,2)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(0,3)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(0,3)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(0,4)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(0,5)).isFalse();
        Assertions.assertThat(directRouteService.areConnected(0,6)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(1,2)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(1,3)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(1,4)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(2,3)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(2,4)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(2,6)).isFalse();
        Assertions.assertThat(directRouteService.areConnected(3,1)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(3,4)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(3,5)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(3,6)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(3,7)).isFalse();
        Assertions.assertThat(directRouteService.areConnected(4,1)).isFalse();
        Assertions.assertThat(directRouteService.areConnected(5,1)).isFalse();
        Assertions.assertThat(directRouteService.areConnected(6,4)).isTrue();
        Assertions.assertThat(directRouteService.areConnected(6,5)).isTrue();

    }

    @Test
    public void testMissingStationId (){

        DirectRouteService directRouteService = new DirectRouteService(new RoutesDataResolver() {
            @Override
            public Collection<String> getRoutesData() {
                return new ArrayList<String>(){{add("3");add("0 0 1 2 3 4");add( "1 3 1 6 5");add("2 0 6 4");}};
            }
        });

        Assertions.assertThat(directRouteService.areConnected(100,1)).isFalse();
    }

    @Test
    public void testOnlySizeInRouteData (){

        try {

            DirectRouteService directRouteService = new DirectRouteService(new RoutesDataResolver() {
                @Override
                public Collection<String> getRoutesData() {
                    return new ArrayList<String>(){{add("3");}};
                }
            });
        }catch (RuntimeException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("The route data is not valid: 3");
        }
    }

    @Test
    public void testEmptyRouteData (){

        try {

            DirectRouteService directRouteService = new DirectRouteService(new RoutesDataResolver() {
                @Override
                public Collection<String> getRoutesData() {
                    return new ArrayList<>();
                }
            });
        }catch (RuntimeException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("The route data is not valid: ");
        }
    }

    @Test
    public void testNullRouteData (){

        try {

            DirectRouteService directRouteService = new DirectRouteService(new RoutesDataResolver() {
                @Override
                public Collection<String> getRoutesData() {
                    return null;
                }
            });
        }catch (RuntimeException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("The route data can't be NULL");
        }
    }
}
