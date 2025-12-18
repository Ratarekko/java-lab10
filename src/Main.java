import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            System.out.println("\n--- Main menu ---");
            System.out.println("1. Reflection (String Hack)");
            System.out.println("2. Crypto Tool (Logging + i18n)");
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    ReflectionTask.run(scanner);
                    break;
                case "2":
                    CryptoTask.run(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}