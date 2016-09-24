package goeuro.test.components;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import goeuro.test.components.api.RoutesDataResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class RoutesDataFileReader implements RoutesDataResolver {

    private List<String> routesData;

    public RoutesDataFileReader(@Value("${routesDataFile}") String routesDataFile) throws IOException {
        readRoutesData(routesDataFile);
    }

    private void readRoutesData(String routesDataFile) throws IOException {
        routesData = Files.readLines(new File(routesDataFile), Charsets.UTF_8);
    }

    @Override
    public Collection<String> getRoutesData() {
        return routesData;
    }
}
