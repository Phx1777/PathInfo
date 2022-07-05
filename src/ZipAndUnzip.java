import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipAndUnzip {
    public static void main(String[] args) throws IOException {
        //some necessary variables
        String destDir = null;
        ZipEntry currentEntry = null;
        Path newDirectory;
        String getAbsoluteDir;
        String strZip;
        String pathToUnzip = null;
        ZipEntry zipEntry = null;
        String firstAsk;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //program information
        System.out.println("""
                Привет эта программа предназначена для архивирования и разархивирования файлов\s
                Введите абсолютный путь к zip куда будет архивирован файл
                Пример C:\\Users\\myPc\\Desktop\\файлы\\архив.zip
                Если вы еще не создали абсолютный путь к zip или вы ввели неверный путь,
                то программа создаст его за вас, главное в конце прописать .zip
                """);

        //ask the user want to zip file
        System.out.println("Хотите ли вы архивировать файл? Yes|No?");
        firstAsk = reader.readLine();

        //ZIP AND UNZIP
        if (firstAsk.equalsIgnoreCase("yes")) {
            //readLine zip
            System.out.println();
            System.out.println("Введите абсолютный путь к zip для архивирования либо введите новый\n");

            strZip = reader.readLine();

            //readLine file
            System.out.println("Введите абсолютный путь к файлу для архивирования\n");
            String strFile = reader.readLine();

            //archive file
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(strZip))) {
                List<Content> contentList = getContent(strZip);
                File file = new File(strFile);
                zipOutputStream.putNextEntry(new ZipEntry("/new" + file.getName()));
                Files.copy(file.toPath(), zipOutputStream);
                for (Content content : contentList) {
                    if (!(content.getFileName().equals("/new" + file.getName()))) {
                        content.saveToZip(zipOutputStream);
                    }
                }
            }

            //I ask the user if he wants to unzip current file
            System.out.println("Хотите ли вы разархивировать данный файл Yes|No?\n");
            String yesOrNo = reader.readLine();

            if (yesOrNo.equalsIgnoreCase("yes")) {
                try {
                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(strZip))) {
                        //some necessary variables
                        byte[] buffer = new byte[1024];
                        int len;

                        System.out.println("Введите папку для разархивирования zip\n");
                        destDir = reader.readLine();
                        while ((currentEntry = zis.getNextEntry()) != null) {
                            File newFile = newFile(new File(destDir), currentEntry);
                            FileOutputStream fos = new FileOutputStream(newFile);
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                        //if the folder is not found, create a new folder and unzip into it
                    }
                } catch (FileNotFoundException e) {
                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(strZip))) {
                        //some necessary variables
                        assert destDir != null;
                        assert currentEntry != null;
                        byte[] buffer = new byte[1024];

                        newDirectory = Files.createDirectory(Paths.get(destDir));
                        getAbsoluteDir = newDirectory.toFile().getAbsolutePath();

                        while ((currentEntry = zis.getNextEntry()) != null) {
                            File newFile = newFile(new File(String.valueOf(newDirectory.toFile())), currentEntry);
                            FileOutputStream fos = new FileOutputStream(newFile);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                    System.out.println("Введенный вами путь был не найден!\n" +
                            "Была создана новая директория которую вы ввели и файл был разархивирован туда\n" +
                            "Файл находится по этому пути - " + getAbsoluteDir);
                }
            }
        }

        //ONLY UNZIP
        if (firstAsk.equalsIgnoreCase("no")) {
            System.out.println();
            System.out.println("Вы хотите просто разархивировать файл? Yes|No");
            String secondAsk = reader.readLine();
            if (secondAsk.equalsIgnoreCase("yes")) {
                System.out.println();
                System.out.println("Введите путь к zip");
                String s = reader.readLine();
                try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(s))) {
                    byte[] buffer = new byte[1024];
                    int len;
                    System.out.println();
                    System.out.println("Введите папку для разархивирования zip");
                    pathToUnzip = reader.readLine();

                    while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                        assert false;
                        File newFile = newFile(new File(pathToUnzip), zipEntry);
                        FileOutputStream fos = new FileOutputStream(newFile);
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    //if the folder is not found, create a new folder and unzip into it
                } catch (FileNotFoundException e) {
                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(s))) {
                        //some necessary variables
                        byte[] buffer = new byte[1024];
                        assert pathToUnzip != null;
                        newDirectory = Files.createDirectory(Paths.get(pathToUnzip));
                        getAbsoluteDir = newDirectory.toFile().getAbsolutePath();

                        while (zis.getNextEntry() != null) {
                            assert false;
                            File newFile = newFile(new File(String.valueOf(newDirectory.toFile())), zipEntry);
                            FileOutputStream fos = new FileOutputStream(newFile);
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                    System.out.println("Введенный вами путь был не найден!\n" +
                            "Была создана новая директория которую вы ввели и файл был разархивирован туда\n" +
                            "Файл находится по этому пути - " + getAbsoluteDir);
                }
            }
        }
    }

    //check files
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Запись находится за пределами целевого каталога: " + zipEntry.getName());
        }
        return destFile;
    }

    public static List<Content> getContent(String s) {
        //some necessary variables
        List<Content> contentList = new ArrayList<>();
        int length;
        byte[] buffer = new byte[1024];
        ZipEntry currentZipEntry;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(s));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while ((currentZipEntry = zis.getNextEntry()) != null) {
                while ((length = zis.read(buffer)) >= 0) {
                    baos.write(buffer, 0, length);
                }
                contentList.add(new Content(currentZipEntry.getName(), baos.toByteArray()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentList;
    }

    //class to create fileNames and data and write this
    static class Content {
        private final String fileName;
        private final byte[] data;

        public Content(String fileName, byte[] data) {
            this.fileName = fileName;
            this.data = data;
        }

        public String getFileName() {
            return fileName;
        }

        void saveToZip(ZipOutputStream zos) throws IOException {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
            zos.flush();
        }
    }
}
