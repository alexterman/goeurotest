package goeuro.test.components;


import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class RoutesDataFileReaderTest {


    @Test(expected = IOException.class)
    public void testFileDoesNotExist () throws Exception {

        new RoutesDataFileReader("non");
    }

    @Test
    public void testFileDoesExists () throws Exception {

        RoutesDataFileReader routesDataFileReader = new RoutesDataFileReader("src/test/resources/routes.txt");

        Collection<String> routesData = routesDataFileReader.getRoutesData();

        Assertions.assertThat(routesData).isEqualTo(
                new ArrayList<String>(){{add("3");add("0 0 1 2 3 4");add( "1 3 1 6 5");add("2 0 6 4");}});
    }

}
