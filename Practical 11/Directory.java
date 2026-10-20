import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Directory {

    public static void main(String[] args) {

        Path folder = Path.of("MyFolder");
        Path report = Path.of("directory_report.txt");

        try {
           
            Files.createDirectories(folder.resolve("Documents"));
            Files.createDirectories(folder.resolve("Images"));

           
            Files.writeString(
                folder.resolve("notes.txt"),
                "Java NIO practical"
            );

            Files.writeString(
                folder.resolve("Documents/homework.txt"),
                "Java homework"
            );

            Files.writeString(
                folder.resolve("Images/photo.txt"),
                "Sample image information"
            );

            
            try (java.io.BufferedWriter writer =
                     Files.newBufferedWriter(report)) {

                writer.write("DIRECTORY TREE REPORT");
                writer.newLine();
                writer.write("=====================");
                writer.newLine();

            
                Files.walkFileTree(folder,
                    new SimpleFileVisitor<Path>() {

                        @Override
                        public FileVisitResult visitFile(
                                Path file,
                                BasicFileAttributes attributes)
                                throws IOException {

                            String name =
                                file.getFileName().toString();

                            long size = attributes.size();

                            String modifiedTime =
                                attributes.lastModifiedTime().toString();

                            String details =
                                "Name: " + name
                                + " | Size: " + size + " bytes"
                                + " | Modified: " + modifiedTime;

                            writer.write(details);
                            writer.newLine();

                            System.out.println(details);

                            return FileVisitResult.CONTINUE;
                        }
                    });

                writer.write("\nReport generated successfully.");
                writer.newLine();
            }

            System.out.println(
                "\nReport saved to: " + report.toAbsolutePath());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}