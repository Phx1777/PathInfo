import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        System.out.println("Введите сначало число, а затем в какую систему счисления хотите перевести");
        System.out.println(getInRadix(scanner.nextInt(), scanner.nextInt()));
        scanner.close();
    }

    public static String getInRadix(int number, int radix) {
        List<Character> digits = getDigitTable();
        if (radix < 2 || radix >= digits.size() || number < 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder valueStr = new StringBuilder();
        while (number > 0) {
            valueStr.insert(0, digits.get(number % radix));
            number = number / radix;
        }
        return valueStr.toString();
    }

    private static List<Character> getDigitTable() {
        ArrayList<Character> digits = new ArrayList<>();
        for (char i = '0'; i <= '9'; i++) {
            digits.add(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            digits.add(i);
        }
        return digits;
    }
}