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

    public CompositeKey getCompositeKey() {
        return compositeKey;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public EmployeeMeeting(CompositeKey compositeKey, String status, LocalDate date) {
        this.compositeKey = compositeKey;
        this.status = status;
        this.date = date;
    }

    @Column
    LocalDate date;
}
