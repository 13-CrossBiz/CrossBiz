package mutsa.backend.Calendar.controller;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Calendar.dto.CalendarDto;
import mutsa.backend.Calendar.entity.Calendar;
import mutsa.backend.Calendar.repository.CalendarRepository;
import mutsa.backend.Calendar.service.CalendarService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    private final CalendarRepository calendarRepository;
    @PostMapping
    public CalendarDto makeCalendar(@RequestBody CalendarDto dto){
        return calendarService.makeCalendar(dto);
    }
    @DeleteMapping("/{userId}/{calendarId}")
    public void deleteCalendar(@PathVariable Long userId, @PathVariable Long calendarId) {
        Calendar calendar = calendarRepository.findByCalendarIdAndUser_UserId(calendarId, userId)
                .orElseThrow(() -> new RuntimeException("no calendar"));
        calendarRepository.delete(calendar);
    }

    @GetMapping("/{userId}/{calendarId}")
    public CalendarDto getCalendar(@PathVariable Long userId, @PathVariable Long calendarId) {
        Calendar calendar = calendarRepository.findByCalendarIdAndUser_UserId(calendarId, userId)
                .orElseThrow(() -> new RuntimeException("no calendar"));
        return new CalendarDto(
                calendar.getCalendarId(),
                calendar.getUser().getUserId(),
                calendar.getColor(),
                calendar.getDate(),
                calendar.getTime(),
                calendar.getTitle()
        );
    }
}
