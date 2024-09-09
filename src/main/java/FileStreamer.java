import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileStreamer {
    public void streamFile(String path) throws IOException {
        Stream<String> lines = null;
        try {
            Files.lines(Path.of(path));
        } catch (IOException ioException) {
        }
    }
}
