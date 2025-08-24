package mutsa.backend.Calendar.repository;
import mutsa.backend.Calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByCalendarIdAndUser_UserId(Long calendarId, Long userId);
}
