package calendarpersistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKey {

    @Column(name = "employee_id")
    String empId;
    @Column(name = "meeting_id")
    String meetId;

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
}
