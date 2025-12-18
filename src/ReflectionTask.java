import java.lang.reflect.Field;
import java.util.Scanner;

public class ReflectionTask {
    public static void run(Scanner scanner) {
        System.out.println("\n--- REFLECTION STRING HACK ---");

        String literalString = "Java";

        System.out.print("Введіть будь-який рядок (створиться через new String): ");
        String inputString = new String(scanner.nextLine());

        System.out.print("На що замінити (введіть текст): ");
        String replacement = scanner.nextLine();

        System.out.println("\n[До змін]");
        System.out.println("Literal ('Java'): " + literalString);
        System.out.println("Input:            " + inputString);

        try {
            modifyStringInternal(literalString, replacement);
            modifyStringInternal(inputString, replacement);
            System.out.println("\n[Успішно змінено!]");
        } catch (Exception e) {
            System.err.println("\n[ПОМИЛКА] Не вдалося змінити рядок.");
            System.err.println("Причина: " + e.getMessage());
            System.err.println("Якщо у вас Java 9+, додайте VM Option: --add-opens java.base/java.lang=ALL-UNNAMED");
        }

        System.out.println("\n[Після змін]");
        System.out.println("Literal ('Java'): " + literalString);
        System.out.println("Input:            " + inputString);
    }

    private static void modifyStringInternal(String target, String newValue) throws NoSuchFieldException, IllegalAccessException {
        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);

        Object internalValue = valueField.get(target);

        if (internalValue instanceof char[]) {
            char[] newChars = newValue.toCharArray();
            valueField.set(target, newChars);
        } else if (internalValue instanceof byte[]) {
            byte[] newBytes = newValue.getBytes();
            valueField.set(target, newBytes);
        }
    }
}