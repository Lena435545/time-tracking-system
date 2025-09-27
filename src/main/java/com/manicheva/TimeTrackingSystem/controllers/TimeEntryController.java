package com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.services.TimeEntryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/time_entries")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    @GetMapping("/today")
    public String getTodayEntries(Model model) {
        User user = timeEntryService.getCurrentUser();
        List<TimeEntry> entries = timeEntryService.getTodayEntries(user);

        model.addAttribute("entries", entries);
        model.addAttribute("newEntry", new TimeEntry());
        return "time_entries/today";
    }

    @PostMapping("/start")
    public String startTimeEntry(RedirectAttributes redirectAttributes) {
        try {
            timeEntryService.startTimeEntry();
            redirectAttributes.addFlashAttribute("successMessage", "Time entry started successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to start time entry");
        }
        return "redirect:/time_entries/today";
    }

    @PostMapping("/end/{id}")
    public String endTimeEntry(@PathVariable Long id, @RequestParam String activity, 
                              RedirectAttributes redirectAttributes) {
        try {
            if (activity == null || activity.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Activity description is required");
                return "redirect:/time_entries/today";
            }
            timeEntryService.endTimeEntry(id, activity.trim());
            redirectAttributes.addFlashAttribute("successMessage", "Time entry ended successfully");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to end time entry");
        }
        return "redirect:/time_entries/today";
    }

    @GetMapping("/by_date")
    public String showByDay(
            @RequestParam(value="date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        User user = timeEntryService.getCurrentUser();
        if (date == null) {
            date = LocalDate.now();
        }
        List<TimeEntry> entries = timeEntryService.getEntriesByDate(user, date);

        model.addAttribute("entries", entries);
        model.addAttribute("date", date);

        return "time_entries/by_date";
    }


    @GetMapping("/by_range")
    public String showByRange(
            @RequestParam(value ="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        User user = timeEntryService.getCurrentUser();
        
        // Set default values if not provided
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        List<TimeEntry> entries = timeEntryService.getEntriesByRange(user, startDate, endDate);

        model.addAttribute("entries", entries);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "time_entries/by_range";
    }


    @GetMapping("/edit/{id}")
    public String editTimeEntryForm(@PathVariable Long id, Model model) {
        try {
            TimeEntry entry = timeEntryService.getEntryById(id);
            model.addAttribute("entry", entry);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Time entry not found");
            return "redirect:/time_entries/by_range";
        }
        return "time_entries/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateTimeEntry(@PathVariable Long id, @Valid @ModelAttribute TimeEntry updatedEntry,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("entry", updatedEntry);
            return "time_entries/edit";
        }
        
        try {
            timeEntryService.updateTimeEntry(id, updatedEntry);
            redirectAttributes.addFlashAttribute("successMessage", "Time entry updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update time entry");
        }
        return "redirect:/time_entries/by_range";
    }


    @PostMapping("/delete/{id}")
    public String deleteTimeEntry(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            timeEntryService.deleteTimeEntry(id);
            redirectAttributes.addFlashAttribute("successMessage", "Time entry deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete time entry");
        }
        return "redirect:/time_entries/by_range";
    }

}
