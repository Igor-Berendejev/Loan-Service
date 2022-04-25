package com.example.loan.service.util;

import com.example.loan.service.model.Decision;
import com.example.loan.service.model.Manager;
import com.example.loan.service.model.Request;
import com.example.loan.service.repository.RequestService;

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

    public static boolean customerHasPendingRequests(Request request, RequestService service) {
        for (Request r : service.getRequestsRepository().values()) {
            if (r.getCustomerID().equals(request.getCustomerID()) &&
                    r.getApprovers().containsValue(Decision.PENDING)) return true;
        }
        return false;
    }

    public static boolean isValidRequestId(RequestService repository, int id) {
        for (Request r : repository.getRequestsRepository().values()) {
            if (r.getId() == id) return true;
        }
        return false;
    }

    public static boolean requestBelongsToCustomer(String customerID, Request request) {
        return request.getCustomerID().equals(customerID);
    }

    public static boolean requestBelongsToManager(Manager manager, Request request) {
        return request.getApprovers().containsKey(manager);
    }
}
