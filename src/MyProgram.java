import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MyProgram {
    public static void main(String[] args) throws IOException {
        System.out.println("Введите путь к директории");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String dirName = reader.readLine();
        Path directory = Paths.get(dirName);

        if (!Files.isDirectory(directory)) {
            System.out.println(directory + " - не папка");
        } else {
            AtomicInteger folderCounter = new AtomicInteger();
            AtomicInteger filesCounter = new AtomicInteger();
            AtomicLong size = new AtomicLong();

            Files.walkFileTree(directory, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) {
                    if (!path.equals(directory)) folderCounter.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
                    filesCounter.incrementAndGet();
                    size.addAndGet(basicFileAttributes.size());
                    return FileVisitResult.CONTINUE;
                }

            });

            double megabyte = (double) size.get() / (1024 * 1024);

            double gb = (double) megabyte / 1024;

            String formattedDoubleMb = new DecimalFormat("#0.00").format(megabyte);
            String formattedDoubleGb = new DecimalFormat("#0.00").format(gb);

            // можно и так
            // String formattedDoubleGB = String.format("%.2f", gb);

            System.out.println("Всего папок - " + folderCounter);
            System.out.println("Всего файлов - " + filesCounter);
            System.out.println("Общий размер - " + formattedDoubleMb + " мегабайт");
            System.out.println("Общий размер - " + formattedDoubleGb + " гигабайт");
            //System.out.println("Общий размер - " + size + " байт");
        }
    }
}