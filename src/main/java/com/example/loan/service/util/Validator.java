package com.example.loan.service.util;

import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;
import com.example.loan.service.repository.RequestRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private Validator() {
    }

    private static final Pattern pattern = Pattern.compile("\\w{2}-\\w{4}-\\w{3}");

    public static boolean isCustomerIdValid(Request request) {
        Matcher matcher = pattern.matcher(request.getCustomerID());
        return matcher.matches();
    }

    public static boolean isRequestAmountValid(Request request) {
        return request.getAmount() >= 0;
    }

    public static boolean noPendingRequests(Request request, RequestRepository repository) {
        for (Request r : repository.getRequests().values()) {
            if (r.getCustomerID().equals(request.getCustomerID()) &&
                    r.getApprovers().containsValue(Decision.PENDING)) return false;
        }
        return true;
    }

    public static boolean isValidRequestId(RequestRepository repository, int id) {
        for (Request r : repository.getRequests().values()) {
            if (r.getId() == id) return true;
        }
        return false;
    }

    public static boolean requestBelongsToCustomer(int requestId, String customerID, RequestRepository repository) {
        return repository.getRequests().get(requestId).getCustomerID().equals(customerID);
    }

    public static boolean requestBelongsToManager(int requestId, Manager manager, RequestRepository repository) {
        return repository.getRequests().get(requestId).getApprovers().containsValue(manager);
    }
}
