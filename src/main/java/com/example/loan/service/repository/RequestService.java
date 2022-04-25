package com.example.loan.service.repository;

import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestService {

    private final Map<Integer, Request> requestsRepository;

    public RequestService(Map<Integer, Request> requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    public Request addRequest(Request request) {
        requestsRepository.put(request.getId(), request);
        return request;
    }

    public List<Request> getManagersPendingRequests(Manager manager) {
        List<Request> pendingRequests = new ArrayList<>();
        for (Integer r : requestsRepository.keySet()) {
            if (requestsRepository.get(r).getApprovers().containsKey(manager) &&
                    requestsRepository.get(r).getApprovers().get(manager).equals(Decision.PENDING))
                pendingRequests.add(requestsRepository.get(r));
        }
        return pendingRequests;
    }

    public Request updateRequestDecision(Integer id, Manager manager, String customerID, Decision decision) {
        Request request = requestsRepository.get(id);
        if (request.getApprovers().containsKey(manager) &&
                request.getCustomerID().equals(customerID) &&
                request.getApprovers().get(manager).equals(Decision.PENDING)) {
            request.getApprovers().put(manager, decision);
        }
        return request;
    }

    public Map<Integer, Request> getRequestsRepository() {
        return requestsRepository;
    }

    public Request getRequestByID(int id) {
        return requestsRepository.get(id);
    }
}
