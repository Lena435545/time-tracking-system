package com.manicheva.TimeTrackingSystem.services;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.repositories.TimeEntryRepository;
import com.manicheva.TimeTrackingSystem.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final UserRepository userRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository, UserRepository userRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TimeEntry> getTodayEntries(User user) {
        return timeEntryRepository.findAllByUserAndDate(user, LocalDate.now());
    }

    public void startTimeEntry() {
        User user = getCurrentUser();
        TimeEntry entry = new TimeEntry();
        entry.setUser(user);
        entry.setDate(LocalDate.now());
        entry.setStartTime(LocalTime.now());
        timeEntryRepository.save(entry);
    }

    public void endTimeEntry(Long id, String activity) {
        TimeEntry entry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        entry.setEndTime(LocalTime.now());
        entry.setActivity(activity);
        timeEntryRepository.save(entry);
    }

    public List<TimeEntry> getEntriesByDate(User user, LocalDate date) {
        return timeEntryRepository.findByUserAndDate(user, date);
    }

    public List<TimeEntry> getEntriesByRange(User user, LocalDate startDate, LocalDate endDate) {
        return timeEntryRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    public TimeEntry getEntryById(Long id) {
        return timeEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    public void updateTimeEntry(Long id, TimeEntry updatedEntry) {
        TimeEntry entry = getEntryById(id);
        entry.setDate(updatedEntry.getDate());
        entry.setStartTime(updatedEntry.getStartTime());
        entry.setEndTime(updatedEntry.getEndTime());
        entry.setActivity(updatedEntry.getActivity());
        timeEntryRepository.save(entry);
    }

    public void deleteTimeEntry(Long id) {
        timeEntryRepository.deleteById(id);
    }


    private Duration calculateDuration(TimeEntry entry) {
        LocalTime start = entry.getStartTime();
        LocalTime end = entry.getEndTime();

        if (start != null && end != null) {
            return Duration.between(start, end);
        }
        return Duration.ZERO;
    }


    public long calculateDurationInMinutes(TimeEntry entry) {
        return calculateDuration(entry).toMinutes();
    }

    public double calculateDurationInHours(TimeEntry entry) {
        return calculateDuration(entry).toMinutes() / 60.0;
    }
}
