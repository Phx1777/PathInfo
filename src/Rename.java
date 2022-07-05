import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Rename {
    public static void main(String[] args) {
        renameFile();
    }

    public static void renameFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите путь к папке для удаления цифр в имени файла");
            String folderPath = reader.readLine();
            File myFolder = new File(folderPath);
            File[] listFiles = myFolder.listFiles();

            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isFile()) {
                    File myFile = new File(folderPath + "\\" + listFiles[i].getName());
                    String fileName = listFiles[i].getName();
                    String[] changedNameArr = fileName.split("\\d");
                    for (int j = 0; j < changedNameArr.length; j++) {
                        myFile.renameTo(new File(folderPath +
                                "\\" + changedNameArr[j] + ".pdf"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Успешно выполнено!");
    }
}
