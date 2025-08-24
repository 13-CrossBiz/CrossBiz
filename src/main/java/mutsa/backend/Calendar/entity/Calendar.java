package mutsa.backend.Calendar.entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.backend.Calendar.dto.CalendarDto;
import mutsa.backend.Users.entity.Users;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
    private String color;
    private LocalDate date;
    private LocalTime time;
    private String title;


    public static Calendar from(CalendarDto dto, Users user) {
        return Calendar.builder()
                .user(user)
                .color(dto.color())
                .date(dto.date())
                .time(dto.time())
                .title(dto.title())
                .build();
    }


    @Builder
    public Calendar(Users user, String color, LocalDate date, LocalTime time, String title) {
        this.user = user;
        this.color = color;
        this.date = date;
        this.time = time;
        this.title = title;
    }
}
