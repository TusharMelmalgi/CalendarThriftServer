package com.example.CalendarThriftServer.calendarpersistence.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meet_id")
    int meetId;

    @Column(name = "description")
    String description;

    @Column(name = "agenda")
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

    public Meeting() {
    }

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

    public int getMeetId() {
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

    public void setMeetId(int meetId) {
        this.meetId = meetId;
    }
}
