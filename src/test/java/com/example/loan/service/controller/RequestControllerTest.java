package com.example.loan.service.controller;

import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;
import com.example.loan.service.repository.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    RequestService service;

    @BeforeEach
    void setCountInRequest() {
        Class clazz = Request.class;
        try {
            Field countField = clazz.getDeclaredField("count");
            countField.setAccessible(true);
            countField.setInt(null, 1);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addRequestTest() throws Exception {
        Request request = new Request("00-117L-PPL", 55.96f, List.of(Manager.JOHN_JOHNSON, Manager.PETER_PETERS));
        request.setId(2);
        when(service.addRequest(any(Request.class))).thenReturn(request);

        String jsonS = "{\n" +
                "    \"customerID\": \"00-117L-PPL\",\n" +
                "    \"amount\": 55.96,\n" +
                "    \"approvers\": [\n" +
                "        \"JOHN_JOHNSON\",\n" +
                "        \"PETER_PETERS\"\n" +
                "    ]\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonS))
                .andExpect(status().isOk())
                .andExpect(content().string(request.toString()));
    }

    @Test
    void badRequestWhenInvalidIDTest() throws Exception {
        String jsonS = "{\n" +
                "    \"customerID\": \"00-05/*-PPL\",\n" +
                "    \"amount\": 55.96,\n" +
                "    \"approvers\": [\n" +
                "        \"JOHN_JOHNSON\",\n" +
                "        \"PETER_PETERS\"\n" +
                "    ]\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonS))
                .andExpect(status().isBadRequest());
    }
}