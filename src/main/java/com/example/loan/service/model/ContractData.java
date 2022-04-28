package com.example.loan.service.model;

import java.time.LocalDateTime;

public class ContractData {
    private int requestID;
    private LocalDateTime timeApproved;

    public ContractData(int requestID, LocalDateTime timeApproved) {
        this.requestID = requestID;
        this.timeApproved = timeApproved;
    }

    public int getRequestID() {
        return requestID;
    }

    public LocalDateTime getTimeApproved() {
        return timeApproved;
    }
}
