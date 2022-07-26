package calendarpersistence.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKey {

    @Column(name = "employee_id")
    String empId;
    @Column(name = "meeting_id")
    String meetId;
}
