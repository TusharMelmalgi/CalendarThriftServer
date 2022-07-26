package calendarpersistence.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class EmployeeMeeting {
    @EmbeddedId
    CompositeKey compositeKey;

    @Column
    String status;

    @Column
    LocalDate date;
}
