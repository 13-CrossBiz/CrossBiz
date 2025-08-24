package mutsa.backend.Calendar.service;

import lombok.RequiredArgsConstructor;
import mutsa.backend.Calendar.dto.CalendarDto;
import mutsa.backend.Calendar.entity.Calendar;
import mutsa.backend.Calendar.repository.CalendarRepository;
import mutsa.backend.Users.entity.Users;
import mutsa.backend.Users.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UsersRepository usersRepository;
    public CalendarDto makeCalendar(CalendarDto dto){
        Users user = usersRepository.findById(dto.userId())
                .orElseThrow(()->new RuntimeException("no user"));
        Calendar calendar = Calendar.from(dto, user);
        Calendar saved = calendarRepository.save(calendar);
        return new CalendarDto(
                saved.getCalendarId(),
                saved.getUser().getUserId(),
                saved.getColor(),
                saved.getDate(),
                saved.getTime(),
                saved.getTitle()
        );
    }

}
