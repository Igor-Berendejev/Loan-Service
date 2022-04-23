package com.example.loan.service.repository;

import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestRepository {

    private final Map<Integer, Request> requests;

    public RequestRepository(Map<Integer, Request> requests) {
        this.requests = requests;
    }

    public Request addRequest(Request request) {
        requests.put(request.getId(), request);
        return request;
    }

    public List<Request> getManagersPendingRequests(Manager manager) {
        List<Request> pendingRequests = new ArrayList<>();
        for (Integer r : requests.keySet()) {
            if (requests.get(r).getApprovers().containsKey(manager) &&
                    requests.get(r).getApprovers().get(manager).equals(Decision.PENDING))
                pendingRequests.add(requests.get(r));
        }
        return pendingRequests;
    }

    public Request updateRequestDecision(Integer id, Manager manager, String customerID, Decision decision) {
        Request request = requests.get(id);
        if (request.getApprovers().containsKey(manager) &&
                request.getCustomerID().equals(customerID) &&
                request.getApprovers().get(manager).equals(Decision.PENDING)) {
            request.getApprovers().put(manager, decision);
        }
        return request;
    }

    public Map<Integer, Request> getRequests() {
        return requests;
    }
}
