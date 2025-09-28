package java.com.manicheva.TimeTrackingSystem.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.manicheva.TimeTrackingSystem.models.TimeEntry;
import com.manicheva.TimeTrackingSystem.models.User;
import com.manicheva.TimeTrackingSystem.services.TimeEntryService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfService {

    private final com.manicheva.TimeTrackingSystem.services.TimeEntryService timeEntryService;

    public PdfService(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    public byte[] generateDailyReport(User user, LocalDate date) throws Exception {
        List<TimeEntry> entries = timeEntryService.getEntriesByDate(user, date);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Zeitreport für: " + date.format(DateTimeFormatter.ISO_DATE)));
        document.add(new Paragraph("User: " + user.getFirstName() + " " +  user.getLastName()));
        document.add(new Paragraph("\n"));

        Table table = new Table(new float[]{3, 3, 2, 2});
        table.addHeaderCell(new Cell().add(new Paragraph("Aktivität")));
        table.addHeaderCell(new Cell().add(new Paragraph("Startzeit")));
        table.addHeaderCell(new Cell().add(new Paragraph("Endzeit")));
        table.addHeaderCell(new Cell().add(new Paragraph("Dauer")));

        Duration totalDuration = Duration.ZERO;

        for (TimeEntry entry : entries) {
            table.addCell(new Cell().add(new Paragraph(entry.getActivity() != null ? entry.getActivity() : "")));
            table.addCell(new Cell().add(new Paragraph(entry.getStartTime() != null ? entry.getStartTime().toString() : "")));
            table.addCell(new Cell().add(new Paragraph(entry.getEndTime() != null ? entry.getEndTime().toString() : "")));

            Duration duration = Duration.ZERO;
            if (entry.getStartTime() != null && entry.getEndTime() != null) {
                duration = Duration.between(entry.getStartTime(), entry.getEndTime());
                totalDuration = totalDuration.plus(duration);
            }
            table.addCell(new Cell().add(new Paragraph(formatDuration(duration))));
        }

        document.add(table);
        document.add(new Paragraph("\nGesamtzeit: " + formatDuration(totalDuration)));

        document.close();
        return baos.toByteArray();
    }

    public byte[] generateWeeklyIHKReport(User user, LocalDate startDate) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("IHK-Wochenreport"));
        document.add(new Paragraph("User: " + user.getFirstName() + " " +  user.getLastName()));
        document.add(new Paragraph("Woche ab: " + startDate.format(DateTimeFormatter.ISO_DATE)));
        document.add(new Paragraph("\n"));

        Table table = new Table(new float[]{2, 2, 4});
        table.addHeaderCell(new Cell().add(new Paragraph("Datum")));
        table.addHeaderCell(new Cell().add(new Paragraph("Gesamtzeit")));
        table.addHeaderCell(new Cell().add(new Paragraph("Aktivitäten")));

        Duration totalWeekDuration = Duration.ZERO;

        for (int i = 0; i < 5; i++) {
            LocalDate date = startDate.plusDays(i);
            List<TimeEntry> entries = timeEntryService.getEntriesByDate(user, date);

            Duration totalDuration = Duration.ZERO;
            String activities = entries.stream()
                    .map(TimeEntry::getActivity)
                    .filter(a -> a != null && !a.isEmpty())
                    .distinct()
                    .collect(Collectors.joining(", "));

            for (TimeEntry entry : entries) {
                if (entry.getStartTime() != null && entry.getEndTime() != null) {
                    totalDuration = totalDuration.plus(Duration.between(entry.getStartTime(), entry.getEndTime()));
                }
            }

            totalWeekDuration = totalWeekDuration.plus(totalDuration);

            table.addCell(new Cell().add(new Paragraph(date.format(DateTimeFormatter.ISO_DATE))));
            table.addCell(new Cell().add(new Paragraph(formatDuration(totalDuration))));
            table.addCell(new Cell().add(new Paragraph(activities)));
        }

        document.add(table);
        document.add(new Paragraph("\nGesamtzeit für die Woche: " + formatDuration(totalWeekDuration)));
        document.add(new Paragraph("\nUnterschrift: ____________________________"));

        document.close();
        return baos.toByteArray();
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%02d:%02d", hours, minutes);
    }
}

