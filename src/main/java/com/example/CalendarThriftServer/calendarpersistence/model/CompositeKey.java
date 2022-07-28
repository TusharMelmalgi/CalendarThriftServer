package com.example.CalendarThriftServer.calendarpersistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CompositeKey implements Serializable {

    @Column(name = "employee_id")
    String empId;
    @Column(name = "meeting_id")
    String meetId;

    public CompositeKey() {
    }

    public String getEmpId() {
        return empId;
    }

    public String getMeetId() {
        return meetId;
    }

    public CompositeKey(String empId, String meetId) {
        this.empId = empId;
        this.meetId = meetId;
    }

    @Override
    public String toString() {
        return "CompositeKey{" +
                "empId='" + empId + '\'' +
                ", meetId='" + meetId + '\'' +
                '}';
    }
}
