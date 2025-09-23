package com.manicheva.TimeTrackingSystem.repositories;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    List<TimeEntry> findAllByUserAndDate(User user, LocalDate date);
    List<TimeEntry> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<TimeEntry> findByUserAndDate(User user, LocalDate date);
    List<TimeEntry> findByUserUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
}
