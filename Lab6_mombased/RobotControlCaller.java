import javax.jms.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import serobjs.PerfumeSettings;

/**
 * Client-side proxy sending RobotControlInterface calls over JMS.
 */
public class RobotControlCaller extends JMSClient implements RobotControlInterface {

    /** How long to wait for a reply before failing (ms). */
    private static final long TIMEOUT_MS = 5_000;

    private final MessageProducer producer;
    private final TemporaryQueue  replyDest;
    private final AtomicInteger   messageId = new AtomicInteger();



    /**
     * @param queueName JMS queue to send commands to (e.g. "RobotControlQueue")
     */
    public RobotControlCaller(String queueName) throws JMSException {
        super(queueName);

        Destination dest = session.createQueue(destination);
        producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        replyDest = session.createTemporaryQueue();   // shared by all calls
    }



    /**
     * Sends a message and waits for the matching acknowledgement.
     */
    private void sendAndAwaitAck(Message msg) throws JMSException {

        String corrId = UUID.randomUUID().toString();

        msg.setJMSCorrelationID(corrId);
        msg.setJMSReplyTo(replyDest);
        msg.setIntProperty("id", messageId.incrementAndGet());

    
        try (MessageConsumer consumer =
                 session.createConsumer(replyDest,
                         "JMSCorrelationID = '" + corrId + "'")) {

            producer.send(msg);

            Message reply = consumer.receive(TIMEOUT_MS);
            if (reply == null) {
                throw new JMSException("Timed out waiting for reply (" + TIMEOUT_MS + " ms)");
            }
            
        }
    }


    @Override
    public void startAdaptiveCleaning() throws JMSException {
        TextMessage msg = session.createTextMessage();
        msg.setStringProperty("Method", MessageKeys.START_CLEANING.toString());
        sendAndAwaitAck(msg);
    }

    @Override
    public void setPerfumeSettings(PerfumeSettings settings) throws JMSException {
        ObjectMessage msg = session.createObjectMessage(settings);
        msg.setStringProperty("Method", MessageKeys.SET_PERFUME.toString());
        sendAndAwaitAck(msg);
    }

    @Override
    public void registerCallback(String clientId) throws JMSException {
        TextMessage msg = session.createTextMessage(clientId);
        msg.setStringProperty("Method", MessageKeys.REGISTER_CALLBACK.toString());
        sendAndAwaitAck(msg);
    }

    @Override
    public void unregisterCallback(String clientId) throws JMSException {
        TextMessage msg = session.createTextMessage(clientId);
        msg.setStringProperty("Method", MessageKeys.UNREGISTER_CALLBACK.toString());
        sendAndAwaitAck(msg);
    }

    @Override
    public void eot() throws JMSException {
        TextMessage msg = session.createTextMessage();
        msg.setStringProperty("Method", MessageKeys.EOT.toString());
        sendAndAwaitAck(msg);

        close();
    }
}
