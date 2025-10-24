import javax.jms.JMSException;

/**
 * Callback interface for robot → app status notifications.
 */
public interface AppStatusCallback {
    /**
     * Invoked by the robot to notify the app of status messages.
     *
     * message the status text
     */
    void notifyStatus(String message) throws JMSException;
}
