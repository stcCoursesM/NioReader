import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("./src");
        WatchKey watchKey = path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.OVERFLOW
        );

        WatchKey watchKey2 = watchService.poll();

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(" An event has occured:" + event.kind()
                        + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }


        Path firstFile =
                Paths.get("./src/test.txt");

        Path secondFile =
                Paths.get("./src/test2.txt");

        System.out.println(secondFile.resolve("test.txt"));

        Set <PosixFilePermission> mySet =
                PosixFilePermissions.fromString("rw-rw-rw-");
        FileAttribute <Set <PosixFilePermission>> myFileAttribute =
                PosixFilePermissions.asFileAttribute(mySet);

        try {
            BufferedWriter bw = Files.newBufferedWriter(firstFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE
            );
            bw.write("Bye");
            bw.flush();
            bw.close();
            Path myPath2 = Files.createFile(secondFile, myFileAttribute);
            List<String> content = Files.readAllLines(firstFile);
            System.out.println(content);
        } catch (Exception e) {}

    }

}