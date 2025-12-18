import java.io.*;
import java.util.*;
import java.util.logging.*;

public class CryptoTask {
    private static final Logger logger = Logger.getLogger(CryptoTask.class.getName());
    private static ResourceBundle bundle;
    private static Locale currentLocale = Locale.getDefault();

    static {
        setupLogger();
    }

    private static void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("app_log.txt", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            System.err.println("Logger setup failed: " + e.getMessage());
        }
    }

    public static void run(Scanner scanner) {
        loadBundle("uk");

        while (true) {
            try {
                System.out.println("\n--- " + bundle.getString("menu.title") + " ---");
                System.out.println("1. " + bundle.getString("menu.encrypt"));
                System.out.println("2. " + bundle.getString("menu.decrypt"));
                System.out.println("3. " + bundle.getString("menu.language"));
                System.out.println("0. " + bundle.getString("menu.exit"));
                System.out.print("> ");

                String choice = scanner.nextLine();
                logger.log(Level.FINE, "User menu choice: {0}", choice);

                switch (choice) {
                    case "1": processFile(scanner, true); break;
                    case "2": processFile(scanner, false); break;
                    case "3": changeLanguage(scanner); break;
                    case "0": return;
                    default: System.out.println(bundle.getString("msg.invalid"));
                }
            } catch (MissingResourceException e) {
                System.err.println("Error: Resources not found. Check file structure.");
                return;
            }
        }
    }

    private static void changeLanguage(Scanner scanner) {
        System.out.println("1. English\n2. Українська");
        String lang = scanner.nextLine();
        if ("1".equals(lang)) loadBundle("en");
        else if ("2".equals(lang)) loadBundle("uk");
        else System.out.println("Unknown language / Невідома мова");
    }

    private static void loadBundle(String langCode) {
        try {
            currentLocale = new Locale(langCode);
            bundle = ResourceBundle.getBundle("resources.location.messages", currentLocale);

            logger.config("Language changed to: " + langCode);
        } catch (MissingResourceException e) {
            logger.severe("Cannot find resource bundle! Check folder 'src/resources/location/'");
            System.err.println("ERROR: Missing resource file for: " + langCode);
        }
    }

    private static void processFile(Scanner scanner, boolean encrypt) {
        System.out.println(bundle.getString("prompt.file.in"));
        String inputFile = scanner.nextLine();
        System.out.println(bundle.getString("prompt.file.out"));
        String outputFile = scanner.nextLine();
        System.out.println(bundle.getString("prompt.key"));

        try {
            String keyStr = scanner.nextLine();
            if (keyStr.isEmpty()) return;
            int key = keyStr.charAt(0);

            logger.info("Starting operation on: " + inputFile);

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

                int c;
                while ((c = reader.read()) != -1) {
                    int processed = encrypt ? (c + key) : (c - key);
                    writer.write(processed);
                }
                System.out.println(bundle.getString("msg.success"));
                logger.fine("File processed successfully.");

            } catch (IOException e) {
                logger.severe("File Error: " + e.getMessage());
                System.out.println(bundle.getString("err.file"));
            }
        } catch (Exception e) {
            logger.warning("Input Error: " + e.getMessage());
        }
    }
}