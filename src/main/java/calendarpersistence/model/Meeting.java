package calendarpersistence.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
public class Meeting {
    @Id
    @Column(name = "meet_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    String meetId;

    @Column
    String description;

    @Column
    String agenda;

    @Column(name = "owner_id")
    String ownerId;

    @Column(name = "date_of_meeting")
    LocalDate dateOfMeeting;

    @Column(name = "start_time")
    LocalTime startTime;

    @Column(name = "end_time")
    LocalTime endTime;

    @Column(name = "room_id")
    int roomId;

    @Column(name = "is_available")
    boolean isAvailable;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    LocalDateTime updatedDate;

    public Meeting(String description, String agenda, String ownerId, LocalDate dateOfMeeting, LocalTime startTime, LocalTime endTime, int roomId, boolean isAvailable) {
        this.description = description;
        this.agenda = agenda;
        this.ownerId = ownerId;
        this.dateOfMeeting = dateOfMeeting;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
        this.isAvailable = isAvailable;
    }

    public String getMeetId() {
        return meetId;
    }

    public String getDescription() {
        return description;
    }

    public String getAgenda() {
        return agenda;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public LocalDate getDateOfMeeting() {
        return dateOfMeeting;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }
}
