package com.example.CalendarThriftServer.calendarpersistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CompositeKey implements Serializable {

    @Column(name = "employee_id")
    String empId;
    @Column(name = "meeting_id")
    int meetId;

    public CompositeKey() {
    }

    public String getEmpId() {
        return empId;
    }

    public int getMeetId() {
        return meetId;
    }

    public CompositeKey(String empId, int meetId) {
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
