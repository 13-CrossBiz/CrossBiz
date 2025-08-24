package mutsa.backend.Calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarDto (
        Long calendarId,
        Long userId,
        String color,
        LocalDate date,
        LocalTime time,
        String title
){ }
