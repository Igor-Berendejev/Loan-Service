package com.example.loan.service.repository;

import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {

    RequestService service;

    @BeforeEach
    void initService() {
        service = new RequestService(new HashMap<>());
    }

    @Test
    void addRequestTest() {
        Request expectedRequest = new Request("00-117L-PPL", 55.90f, List.of(Manager.ANGELA_WHITE, Manager.JOHN_JOHNSON));
        Request result = service.addRequest(expectedRequest);
        assertEquals(expectedRequest, result);

    }

    @Test
    void getManagersPendingRequestsTest() {
        List<Request> johnPendingRequests = new ArrayList<>();
        // two PENDING requests for JOHN JOHNSON
        Request request1 = new Request("00-117L-PPL", 55.90f, List.of(Manager.ANGELA_WHITE, Manager.JOHN_JOHNSON));
        Request request2 = new Request("11-117L-PPL", 55.90f, List.of(Manager.JOHN_JOHNSON, Manager.PETER_PETERS));
        johnPendingRequests.add(request1);
        johnPendingRequests.add(request2);

        // request without JOHN JOHNSON
        Request request3 = new Request("22-117L-PPL", 55.90f, List.of(Manager.EMMY_ANDERSON, Manager.PETER_PETERS));
        // request APPROVED by JOHN JOHNSON
        Request request4 = new Request("33-117L-PPL", 55.90f, List.of(Manager.EMMY_ANDERSON, Manager.JOHN_JOHNSON));
        request4.getApprovers().put(Manager.JOHN_JOHNSON, Decision.APPROVED);

        service.addRequest(request1);
        service.addRequest(request2);
        service.addRequest(request3);
        service.addRequest(request4);

        List<Request> requests = service.getManagersPendingRequests(Manager.JOHN_JOHNSON);
        assertIterableEquals(johnPendingRequests, requests);

    }

    @Test
    void updateRequestDecisionTest() {
        Request request = new Request("00-117L-PPL", 55.90f, List.of(Manager.ANGELA_WHITE, Manager.JOHN_JOHNSON));
        service.addRequest(request);
        service.updateRequestDecision(1, Manager.ANGELA_WHITE, "00-117L-PPL", Decision.DECLINED);

        Decision result = service.getRequestsRepository().get(1).getApprovers().get(Manager.ANGELA_WHITE);

        assertEquals(Decision.DECLINED, result);
    }

    @Test
    void managerCannotUpdateWhenNotAssigned() {
        Request request = new Request("00-117L-PPL", 55.90f, List.of(Manager.ANGELA_WHITE, Manager.JOHN_JOHNSON));
        service.addRequest(request);
        service.updateRequestDecision(1, Manager.ANGELA_WHITE, "00-117L-PPL", Decision.DECLINED);
    }

    @Test
    void getRequestByIDTest() {
        Request expectedRequest = new Request("00-117L-PPL", 55.90f, List.of(Manager.ANGELA_WHITE, Manager.JOHN_JOHNSON));
        int id = expectedRequest.getId();
        service.addRequest(expectedRequest);
        Request result = service.getRequestByID(id);
        assertEquals(expectedRequest, result);
    }
}