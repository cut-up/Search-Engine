import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class SearchEngine {
    private final Path path;
    private final boolean reverse;
    private final String[] search;
    private int directory, file;

    private SearchEngine(String[] arguments) {
        path = Paths.get(arguments[0]);
        reverse = arguments[1].equals("-e");
        search = Arrays.copyOfRange(arguments, (reverse ? 2 : 1), arguments.length);
    }

    public static void main(String[] args) throws IOException {
        final SearchEngine searchEngine = new SearchEngine(args);
        Files.walkFileTree(searchEngine.path, searchEngine.new Visitor());
        System.out.printf("total: %s/%s%n", searchEngine.directory, searchEngine.file);
    }

    private class Visitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            result(path, basicFileAttributes);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
            result(path, basicFileAttributes);
            return FileVisitResult.CONTINUE;
        }

        private void result(Path path, BasicFileAttributes basicFileAttributes) {
            if (search("" + path.getFileName()) != reverse) {
                if (basicFileAttributes.isDirectory()) {
                    directory++;
                } else {
                    file++;
                }
                System.out.printf("found: %s%n", path);
            }
        }

        private boolean search(String name) {
            for (String string : search) {
                if (name.contains(string)) {
                    return true;
                }
            }
            return false;
        }
    }
}