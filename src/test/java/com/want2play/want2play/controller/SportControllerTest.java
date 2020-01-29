package com.want2play.want2play.controller;

import com.want2play.want2play.dto.SportDto;
import com.want2play.want2play.service.SportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {SportController.class})
@WebMvcTest
public class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportService service;

    @Test
    public void shouldReturnAllSports() throws Exception {
        List<SportDto> expectedSports = new ArrayList<SportDto>();
        expectedSports.add(new SportDto("BSK", "Basketball", 5));
        expectedSports.add(new SportDto("SCC", "Soccer", 11));

        when(service.getAllSports()).thenReturn(expectedSports);

        this.mockMvc.perform(get("/sports/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("BSK")))
                .andExpect(jsonPath("$[1].id", is("SCC")));
    }

}
