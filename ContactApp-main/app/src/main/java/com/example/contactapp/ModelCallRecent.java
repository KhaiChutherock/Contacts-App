package com.example.contactapp;

public class ModelCallRecent {
    private String callId, callerId, receiverId, callStartTime, callEndTime;
    private int durationSeconds;
    private int callType; // Thuộc tính mới để xác định loại cuộc gọi (đến hay đi)

    public ModelCallRecent(String callId, String callerId, String receiverId, String callStartTime, String callEndTime, int durationSeconds, int callType) {
        this.callId = callId;
        this.callerId = callerId;
        this.receiverId = receiverId;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.durationSeconds = durationSeconds;
        this.callType = callType;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
        this.callStartTime = callStartTime;
    }

    public String getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(String callEndTime) {
        this.callEndTime = callEndTime;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }
}
