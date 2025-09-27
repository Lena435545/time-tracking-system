package com.manicheva.TimeTrackingSystem.services;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.repositories.TimeEntryRepository;
import com.manicheva.TimeTrackingSystem.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final UserRepository userRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository, UserRepository userRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<TimeEntry> getTodayEntries() {
        User user = getCurrentUser();
        return timeEntryRepository.findAllByUserAndDate(user, LocalDate.now());
    }

    public List<TimeEntry> getTodayEntries(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return timeEntryRepository.findAllByUserAndDate(user, LocalDate.now());
    }

    public void startTimeEntry() {
        User user = getCurrentUser();
        
        // Check if there's already an active entry for today
        List<TimeEntry> todayEntries = timeEntryRepository.findAllByUserAndDate(user, LocalDate.now());
        boolean hasActiveEntry = todayEntries.stream()
                .anyMatch(entry -> entry.getStartTime() != null && entry.getEndTime() == null);
        
        if (hasActiveEntry) {
            throw new IllegalStateException("There is already an active time entry for today");
        }
        
        TimeEntry entry = new TimeEntry();
        entry.setUser(user);
        entry.setDate(LocalDate.now());
        entry.setStartTime(LocalTime.now());
        timeEntryRepository.save(entry);
    }

    public void endTimeEntry(Long id, String activity) {
        if (id == null) {
            throw new IllegalArgumentException("Entry ID cannot be null");
        }
        
        TimeEntry entry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        
        // Verify the entry belongs to the current user
        User currentUser = getCurrentUser();
        if (!entry.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You can only modify your own time entries");
        }
        
        if (entry.getEndTime() != null) {
            throw new IllegalStateException("This time entry has already been ended");
        }
        
        entry.setEndTime(LocalTime.now());
        entry.setActivity(activity);
        
        // Validate time range
        if (!entry.isValidTimeRange()) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        timeEntryRepository.save(entry);
    }

    public List<TimeEntry> getEntriesByDate(User user, LocalDate date) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (date == null) {
            date = LocalDate.now();
        }
        return timeEntryRepository.findByUserAndDate(user, date);
    }

    public List<TimeEntry> getEntriesByRange(User user, LocalDate startDate, LocalDate endDate) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        return timeEntryRepository.findByUserAndDateBetween(user, startDate, endDate);
    }

    public TimeEntry getEntryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Entry ID cannot be null");
        }
        return timeEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    public void updateTimeEntry(Long id, TimeEntry updatedEntry) {
        if (id == null || updatedEntry == null) {
            throw new IllegalArgumentException("Entry ID and updated entry cannot be null");
        }
        
        TimeEntry entry = getEntryById(id);
        
        // Verify the entry belongs to the current user
        User currentUser = getCurrentUser();
        if (!entry.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You can only modify your own time entries");
        }
        
        entry.setDate(updatedEntry.getDate());
        entry.setStartTime(updatedEntry.getStartTime());
        entry.setEndTime(updatedEntry.getEndTime());
        entry.setActivity(updatedEntry.getActivity());
        
        // Validate time range
        if (!entry.isValidTimeRange()) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        
        timeEntryRepository.save(entry);
    }

    public void deleteTimeEntry(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Entry ID cannot be null");
        }
        
        TimeEntry entry = getEntryById(id);
        
        // Verify the entry belongs to the current user
        User currentUser = getCurrentUser();
        if (!entry.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You can only delete your own time entries");
        }
        
        timeEntryRepository.deleteById(id);
    }


    private Duration calculateDuration(TimeEntry entry) {
        if (entry == null) {
            return Duration.ZERO;
        }
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
