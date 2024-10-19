package hello.login.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationForm {
    private Long id;
    private Long ophthalmologyId;      // 안과 ID

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate reservationDate; // 예약 날짜

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime reservationTime; // 예약 시간

    @Override
    public String toString() {
        return "ReservationForm{" +
                "id=" + id +
                ", ophthalmologyId=" + ophthalmologyId +
                ", reservationDate=" + reservationDate +
                ", reservationTime=" + reservationTime +
                '}';
    }
}
