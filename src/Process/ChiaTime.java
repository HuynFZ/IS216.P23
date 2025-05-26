
package Process;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChiaTime {
    public static String[] tachTime(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, inputFormatter);
            String formattedDateTime = parsedDateTime.format(outputFormatter);

            // Tách ngày và giờ
            return formattedDateTime.split(" ");
        } catch (Exception e) {
            System.out.println("Lỗi định dạng: " + e.getMessage());
            return null;
        }
    }
}
