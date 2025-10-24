import javax.jms.JMSException;
import java.util.InputMismatchException;
import java.util.Scanner;
import serobjs.PerfumeSettings;

/**
 * Console-driven client:
 * • asks the human for a few settings
 * • sends them to the robot via JMS
 * • shows every status callback received
 */
public class AppClient {

    public static void main(String[] args) {

        try (Scanner in = new Scanner(System.in)) {

            
            int cleaningIntensity = readInt(in,
                    "Enter cleaning intensity (1-10): ", 1, 10);

            boolean perfumeEnabled = readBoolean(in,
                    "Enable perfume? (true/false): ");

            String perfumeType = null;
            int perfumeIntensity = 0;
            if (perfumeEnabled) {
                perfumeType = readString(in,
                        "Perfume type (standard/special): ",
                        new String[]{"standard", "special"});
                perfumeIntensity = readInt(in,
                        "Perfume intensity (1-3): ", 1, 3);
            }

            
            AppStatusCallbackResponder callbackResponder =
                new AppStatusCallbackResponder(
                    message -> System.out.println("Server says - " + message),
                    "CallbackQueue");
            callbackResponder.listen();

            
            RobotControlInterface robot =
                new RobotControlCaller("RobotControlQueue");

            
            robot.registerCallback("ConsoleClient");

            
            PerfumeSettings settings =
                new PerfumeSettings(perfumeIntensity, perfumeEnabled,
                                    perfumeType, cleaningIntensity);

            String corrId = java.util.UUID.randomUUID().toString();
            System.out.println("- Sent PerfumeSettings with ID: " + corrId);
            robot.setPerfumeSettings(settings);

            
            System.out.println("- Sent StartCleaning command with same ID.");
            robot.startAdaptiveCleaning();

            
            Thread.sleep(5_000);

            // shutdown
            robot.eot();
            callbackResponder.close();
            System.out.println("[App] Finished - bye.");

        } catch (JMSException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    private static int readInt(Scanner in, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = in.nextInt();
                if (v >= min && v <= max) { in.nextLine(); return v; }
            } catch (InputMismatchException ignored) { }
            in.nextLine(); // clear bad token
            System.out.println("   Please enter a number between " + min + " and " + max + ".");
        }
    }

    private static boolean readBoolean(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim().toLowerCase();
            if (s.equals("true") || s.equals("t") || s.equals("yes") || s.equals("y")) return true;
            if (s.equals("false") || s.equals("f") || s.equals("no")  || s.equals("n")) return false;
            System.out.println("   Type true / false (or yes / no).");
        }
    }

    private static String readString(Scanner in, String prompt, String[] allowed) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim().toLowerCase();
            for (String a : allowed)
                if (a.equals(s)) return s;
            System.out.print("   Allowed: ");
            for (int i = 0; i < allowed.length; i++)
                System.out.print((i == 0 ? "" : ", ") + allowed[i]);
            System.out.println(".");
        }
    }
}
