import javax.jms.*;

/**
 * Server-side helper: sends AppStatusCallback notifications over JMS.
 */
public class AppStatusCallbackCaller extends JMSClient implements AppStatusCallback {
    private MessageProducer producer;

    /**
     * @param queueName JMS queue where App listens for callbacks (e.g. "CallbackQueue")
     */
    public AppStatusCallbackCaller(String queueName) throws JMSException {
        super(queueName);
        
        Destination dest = session.createQueue(destination);
        producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    @Override
    public void notifyStatus(String message) throws JMSException {
        TextMessage msg = session.createTextMessage(message);
        msg.setStringProperty("Method", MessageKeys.NOTIFY_STATUS.toString());
        
        producer.send(msg);
    }
}
