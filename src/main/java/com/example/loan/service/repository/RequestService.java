package com.example.loan.service.repository;

import com.example.loan.service.model.ContractData;
import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RequestService {

    private final Map<Integer, Request> requestsRepository;
    private final ArrayDeque<ContractData> contractsRepository = new ArrayDeque<>();

    public RequestService(Map<Integer, Request> requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    public Request addRequest(Request request) {
        synchronized (requestsRepository) {
            requestsRepository.put(request.getId(), request);
        }
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
        if (request.isApproved()) contractsRepository.add(new ContractData(request.getId(), LocalDateTime.now()));
        return request;
    }

    public Map<Integer, Request> getRequestsRepository() {
        return requestsRepository;
    }

    public ArrayDeque<ContractData> getContractsRepository() {
        return contractsRepository;
    }

    public Request getRequestByID(int id) {
        return requestsRepository.get(id);
    }

    public String getStatistics(int period) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minus(period, ChronoUnit.SECONDS);
        Iterator<ContractData> iterator = contractsRepository.descendingIterator();
        int count = 0;
        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal min = BigDecimal.valueOf(0);
        BigDecimal max = BigDecimal.valueOf(0);
        BigDecimal average = BigDecimal.valueOf(0);
        while (iterator.hasNext()) {
            ContractData contractData = iterator.next();
            if (contractData.getTimeApproved().isBefore(start)) break;
            else {
                BigDecimal amount = BigDecimal.valueOf(requestsRepository.get(contractData.getRequestID()).getAmount());
                count++;
                sum = sum.add(amount);
                if (min.equals(BigDecimal.ZERO)) min = amount;
                else {
                    if (amount.compareTo(min) < 0) min = amount;
                }
                if (amount.compareTo(max) > 0) max = amount;
            }
        }
        if (count > 0) average = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        return String.format("Number of sent contracts: %d, total amount: %f, smallest amount: %f, biggest amount: %f, average amount: %f"
                , count, sum.setScale(2, RoundingMode.HALF_UP), min.setScale(2, RoundingMode.HALF_UP)
                , max.setScale(2, RoundingMode.HALF_UP), average);
    }
}
