package com.manicheva.TimeTrackingSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

@Entity
@Table(name="time_entries")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_entry_id")
    private Long timeEntryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @Column(name = "date", nullable = false)
    @NotNull(message = "Date is required")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "activity")
    @Size(max = 500, message = "Activity description cannot exceed 500 characters")
    private String activity;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Business logic validation
    public boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return true; // Allow incomplete entries
        }
        return startTime.isBefore(endTime);
    }

    public Duration getDuration() {
        if (startTime == null || endTime == null) {
            return Duration.ZERO;
        }
        return Duration.between(startTime, endTime);
    }
}
