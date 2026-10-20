import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class LogAnalyzer {

    public static void main(String[] args) {

        try {
           
            Files.write(
                Path.of("log1.txt"),
                List.of(
                    "INFO: Application started",
                    "ERROR: File not found",
                    "INFO: Application running"
                )
            );

            Files.write(
                Path.of("log2.txt"),
                List.of(
                    "ERROR: Connection failed",
                    "INFO: User logged in",
                    "ERROR: Timeout occurred"
                )
            );

            Files.write(
                Path.of("log3.txt"),
                List.of(
                    "INFO: Server started",
                    "WARNING: Low memory",
                    "INFO: Application closed"
                )
            );

            Path[] files = {
                Path.of("log1.txt"),
                Path.of("log2.txt"),
                Path.of("log3.txt")
            };

            int totalLines = 0;
            int keywordLines = 0;
            String keyword = "ERROR";

            for (Path file : files) {

              
                BasicFileAttributes attributes =
                    Files.readAttributes(
                        file, BasicFileAttributes.class);

                System.out.println("\nFile: " + file.getFileName());
                System.out.println(
                    "Size: " + attributes.size() + " bytes");

               
                List<String> lines = Files.readAllLines(file);

                for (String line : lines) {
                    totalLines++;

                    if (line.contains(keyword)) {
                        keywordLines++;
                    }
                }
            }

            System.out.println("\nTotal Lines: " + totalLines);
            System.out.println(
                "Lines containing ERROR: " + keywordLines);

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}