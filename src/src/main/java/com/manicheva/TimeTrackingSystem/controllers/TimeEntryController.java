package com.manicheva.TimeTrackingSystem.controllers;

import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.services.TimeEntryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String startTimeEntry() {
        timeEntryService.startTimeEntry();
        return "redirect:/time_entries/today";
    }

    @PostMapping("/end/{id}")
    public String endTimeEntry(@PathVariable Long id, @RequestParam String activity) {
        timeEntryService.endTimeEntry(id, activity);
        return "redirect:/time_entries/today";
    }

    @GetMapping("/by_date")
    public String showByDay(
            @RequestParam(value="date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        User user = timeEntryService.getCurrentUser();
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
        List<TimeEntry> entries = timeEntryService.getEntriesByRange(user, startDate, endDate);

        model.addAttribute("entries", entries);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "time_entries/by_range";
    }


    @GetMapping("/edit/{id}")
    public String editTimeEntryForm(@PathVariable Long id, Model model) {
        TimeEntry entry = timeEntryService.getEntryById(id);
        model.addAttribute("entry", entry);
        return "time_entries/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateTimeEntry(@PathVariable Long id, @ModelAttribute TimeEntry updatedEntry) {
        timeEntryService.updateTimeEntry(id, updatedEntry);
        return "redirect:/time_entries/by_range";
    }


    @PostMapping("/delete/{id}")
    public String deleteTimeEntry(@PathVariable Long id) {
        timeEntryService.deleteTimeEntry(id);
        return "redirect:/time_entries/by_range";
    }

}
