import javax.jms.JMSException;
import serobjs.PerfumeSettings;

/**
 * Defines the commands the App can send to the Robot over JMS.
 */
public interface RobotControlInterface {
    /**
     * Start adaptive cleaning based on surface detection.
     */
    void startAdaptiveCleaning() throws JMSException;

    /**
     * Update perfume settings on the robot.
     */
    void setPerfumeSettings(PerfumeSettings settings) throws JMSException;

    /**
     * Register this client ID to receive status callbacks.
     */
    void registerCallback(String clientId) throws JMSException;

    /**
     * Unregister this client ID so callbacks stop.
     */
    void unregisterCallback(String clientId) throws JMSException;

    /**
     * End of transmission / shutdown.
     */
    void eot() throws JMSException;
}
