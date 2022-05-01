package com.example.loan.service.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Request {
    private static int count = 1;
    private int id;
    private String customerID;
    private float amount;
    private Map<Manager, Decision> approvers;

    public Request(String customerID, float amount, List<Manager> approvers) {
        this.id = count;
        this.customerID = customerID;
        this.amount = amount;
        this.approvers = new HashMap<>();
        approvers.forEach(v -> this.approvers.put(v, Decision.PENDING));
        count++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Map<Manager, Decision> getApprovers() {
        return approvers;
    }

    public void setApprovers(Map<Manager, Decision> approvers) {
        this.approvers = approvers;
    }

    public boolean isApproved() {
        return approvers.values().stream().allMatch(d -> d.equals(Decision.APPROVED));
    }

    @Override
    public String toString() {
        return "Request: [id: " + id +
                ", customer id: " + customerID +
                ", amount: " + amount +
                ", approvers: " + approvers.keySet().stream()
                .map(key -> key + " decision " + approvers.get(key))
                .collect(Collectors.joining(", ", "{", "}")) + "]";
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Request request = (Request) obj;
        return id == request.getId();
    }
}
