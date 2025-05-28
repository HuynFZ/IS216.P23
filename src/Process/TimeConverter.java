
package Process;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    public static String convertToISO(String oldFormatTime) {
        try {
            DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(oldFormatTime, oldFormat);
            
            DateTimeFormatter isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return dateTime.format(isoFormat);
        } catch (Exception e) {
            System.out.println("Lỗi chuyển định dạng: " + e.getMessage());
            return null;
        }
    }
}

