package goeuro.test.components;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class BusRouteAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDirectAccess() throws Exception {

        this.mockMvc.perform(get("/api/direct").param("dep_sid", "1").param("arr_sid", "3"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value("1"))
                .andExpect(jsonPath("$.direct_bus_route").value("true"))
                .andExpect(jsonPath("$.arr_sid").value("3"));
    }

    @Test
    public void testNoDirectAccess() throws Exception {

        this.mockMvc.perform(get("/api/direct").param("dep_sid", "3").param("arr_sid", "2"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value("3"))
                .andExpect(jsonPath("$.direct_bus_route").value("false"))
                .andExpect(jsonPath("$.arr_sid").value("2"));
    }

    @Test
    public void testDepartureStationNotFoundRequest() throws Exception {

        this.mockMvc.perform(get("/api/direct").param("dep_sid", "100").param("arr_sid", "3"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.direct_bus_route").value("false")
        );
    }


    @Test
    public void testArrivalStationNotFoundRequest() throws Exception {

        this.mockMvc.perform(get("/api/direct").param("dep_sid", "3").param("arr_sid", "21"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.dep_sid").value("3"))
                .andExpect(jsonPath("$.direct_bus_route").value("false"))
                .andExpect(jsonPath("$.arr_sid").value("21"));
    }


}
