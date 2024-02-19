package com.darkpattern.detection.model;

public class Complaint {
    String key;
    String complaint;
    String complaintUrl;
    String complaintDis;

    public Complaint() {
    }

    public Complaint(String key, String complaint, String complaintUrl, String complaintDis) {
        this.key = key;
        this.complaint = complaint;
        this.complaintUrl = complaintUrl;
        this.complaintDis = complaintDis;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getComplaintUrl() {
        return complaintUrl;
    }

    public void setComplaintUrl(String complaintUrl) {
        this.complaintUrl = complaintUrl;
    }

    public String getComplaintDis() {
        return complaintDis;
    }

    public void setComplaintDis(String complaintDis) {
        this.complaintDis = complaintDis;
    }
}
