package com.geno.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class WeatherControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void weather_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void weatherAnalysisCoordinates_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/coordinates")
                .param("latitude", "41.70907")
                .param("longitude", "44.79610"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void weatherAnalysisIpAddress_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/ip")
                .param("ip", "5.178.192.251"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void weatherAnalysisCoordinates_with_throwEx_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/coordinates")
                        .param("latitude", "4170907")
                        .param("longitude", "44.79610"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void weatherAnalysisIpAddress_with_throwEx_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/ip")
                        .param("ip", "0.0.0.0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void weatherAnalysisCoordinates_MissingParams_test() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/coordinates?latitude=41.70907,longitude=44.79610"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
