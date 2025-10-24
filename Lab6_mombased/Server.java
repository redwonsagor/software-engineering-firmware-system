import javax.jms.JMSException;

/**
 * Server entry point: sets up the RobotControlImpl, callback sender,
 * and JMS responder for incoming commands.
 */
public class Server {
    public static void main(String[] args) {
        try {
            // firmware implementation (uses the Decorator/Factory)
            RobotControlImpl impl = new RobotControlImpl();

            // callback-sender for status updates
            AppStatusCallbackCaller callbackCaller =
                new AppStatusCallbackCaller("CallbackQueue");
            impl.setCallbackCaller(callbackCaller);

            // 3. JMS responder to listen for control commands
            RobotControlResponder responder =
                new RobotControlResponder(impl, "RobotControlQueue");
            responder.listen();

            System.out.println("Server started; listening on 'RobotControlQueue'.");
        

        } catch (JMSException e) {
            e.printStackTrace();
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
