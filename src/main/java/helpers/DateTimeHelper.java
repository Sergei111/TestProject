package helpers;

import java.time.Instant;

import static java.lang.Thread.sleep;

public class DateTimeHelper {

    public static synchronized long getTimestamp(){
        Instant instant = Instant.now();
        return instant.toEpochMilli();
    }
}
