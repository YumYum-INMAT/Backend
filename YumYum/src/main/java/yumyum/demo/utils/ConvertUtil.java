package yumyum.demo.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class ConvertUtil {
    public static String convertCreatedAt(Temporal createAt) {
        if (ChronoUnit.YEARS.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.YEARS.between(createAt, LocalDateTime.now())).concat("년 전");
        }
        else if (ChronoUnit.MONTHS.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.MONTHS.between(createAt, LocalDateTime.now())).concat("달 전");
        }
        else if (ChronoUnit.WEEKS.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.WEEKS.between(createAt, LocalDateTime.now())).concat("주 전");
        }
        else if (ChronoUnit.DAYS.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.DAYS.between(createAt, LocalDateTime.now())).concat("일 전");
        }
        else if (ChronoUnit.HOURS.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.HOURS.between(createAt, LocalDateTime.now())).concat("시간 전");
        }
        else if (ChronoUnit.MINUTES.between(createAt, LocalDateTime.now()) >= 1) {
            return Long.toString(ChronoUnit.MINUTES.between(createAt, LocalDateTime.now())).concat("분 전");
        }
        else {
            return Long.toString(ChronoUnit.SECONDS.between(createAt, LocalDateTime.now())).concat("초 전");
        }
    }
}
