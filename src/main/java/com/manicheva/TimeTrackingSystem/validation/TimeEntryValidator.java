package com.manicheva.TimeTrackingSystem.validation;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class TimeEntryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TimeEntry.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TimeEntry timeEntry = (TimeEntry) target;

        // Validate date is not in the future
        if (timeEntry.getDate() != null && timeEntry.getDate().isAfter(LocalDate.now())) {
            errors.rejectValue("date", "", "Date cannot be in the future");
        }

        // Validate time range
        if (timeEntry.getStartTime() != null && timeEntry.getEndTime() != null) {
            if (!timeEntry.getStartTime().isBefore(timeEntry.getEndTime())) {
                errors.rejectValue("endTime", "", "End time must be after start time");
            }
        }

        // Validate activity is provided if end time is set
        if (timeEntry.getEndTime() != null && 
            (timeEntry.getActivity() == null || timeEntry.getActivity().trim().isEmpty())) {
            errors.rejectValue("activity", "", "Activity description is required when ending a time entry");
        }
    }
}