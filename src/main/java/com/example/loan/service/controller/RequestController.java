package com.example.loan.service.controller;

import com.example.loan.service.model.*;
import com.example.loan.service.repository.RequestService;
import com.example.loan.service.util.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class RequestController {

    private final RequestService service = new RequestService(new HashMap<>());

    @PostMapping("/api/requests")
    public ResponseEntity<String> addRequest(@RequestBody Request request) {
        if (!Validator.isCustomerIdValid(request))
            return new ResponseEntity<>("Invalid customer ID", HttpStatus.BAD_REQUEST);
        if (Validator.customerHasPendingRequests(request, service))
            return new ResponseEntity<>("Customer has pending requests", HttpStatus.CONFLICT);
        if (!Validator.isRequestAmountValid(request))
            return new ResponseEntity<>("Invalid loan amount", HttpStatus.BAD_REQUEST);
        if (!Validator.isValidNumberOfApprovers(request))
            return new ResponseEntity<>("There must be from 1 to 3 approvers", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.addRequest(request).toString(), HttpStatus.OK);
    }

    @GetMapping("/api/{manager}/pending")
    public List<Request> getPendingRequest(@PathVariable String manager) {
        Manager m = Manager.valueOf(manager);
        return service.getManagersPendingRequests(m);
    }

    @PutMapping("/api/{id}/{customerID}/{manager}")
    public ResponseEntity<String> updateDecision(@PathVariable String id, @PathVariable String customerID,
                                                 @PathVariable String manager, @RequestBody String decisionString) {

        int requestId;
        Manager m;
        Decision decision;
        try {
            requestId = Integer.parseInt(id);
            m = Manager.valueOf(manager);
            decision = Decision.valueOf(decisionString);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input for request id, manager or decision", HttpStatus.BAD_REQUEST);
        }

        if (!Validator.isValidRequestId(service, requestId))
            return new ResponseEntity<>("Invalid request ID", HttpStatus.BAD_REQUEST);
        if (!Validator.requestBelongsToCustomer(customerID, service.getRequestByID(requestId)))
            return new ResponseEntity<>("Request does not belong to specified customer", HttpStatus.BAD_REQUEST);
        if (!Validator.requestBelongsToManager(m, service.getRequestByID(requestId)))
            return new ResponseEntity<>("Request is not assigned to specified manager", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.updateRequestDecision(requestId, m, customerID, decision).toString(),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/statistics")
    public String getStats(@RequestParam(name = "period", defaultValue = "60") String period) {
        return service.getStatistics(Integer.parseInt(period));
    }
}
