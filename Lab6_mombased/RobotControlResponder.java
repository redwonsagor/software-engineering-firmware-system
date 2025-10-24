import javax.jms.*;
import serobjs.PerfumeSettings;

/**
 * Listens on one queue and forwards each message to the real implementation.
 * One consumer per remote-method using a JMS selector on the "Method" property.
 * Replies with a simple “OK” TextMessage that carries the same correlation-ID.
 */
public class RobotControlResponder extends JMSClient {

    private final RobotControlInterface impl;

    private MessageConsumer startCleaningC;
    private MessageConsumer setPerfumeC;
    private MessageConsumer registerCbC;
    private MessageConsumer unregisterCbC;
    private MessageConsumer eotC;

    private MessageProducer replyProducer;

    public RobotControlResponder(RobotControlInterface impl,
                                 String queueName) throws JMSException {
        super(queueName);
        this.impl = impl;
    }

    @Override
    protected boolean connect() throws JMSException {
        if (connected) return true;

        super.connect();                                  // open Session
        Destination q = session.createQueue(destination);

        startCleaningC   = session.createConsumer(q,
                          "Method = '" + MessageKeys.START_CLEANING + "'");
        setPerfumeC      = session.createConsumer(q,
                          "Method = '" + MessageKeys.SET_PERFUME + "'");
        registerCbC      = session.createConsumer(q,
                          "Method = '" + MessageKeys.REGISTER_CALLBACK + "'");
        unregisterCbC    = session.createConsumer(q,
                          "Method = '" + MessageKeys.UNREGISTER_CALLBACK + "'");
        eotC             = session.createConsumer(q,
                          "Method = '" + MessageKeys.EOT + "'");

        replyProducer = session.createProducer(null);
        replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        connected = true;
        return true;
    }

    /* ------------------------------------------------------------------ */

    public void listen() throws JMSException {
        if (!connected) connect();

        // 1) startAdaptiveCleaning()
        startCleaningC.setMessageListener(request -> {
            try { impl.startAdaptiveCleaning(); sendAck(request); }
            catch (JMSException e) { e.printStackTrace(); }
        });

        // 2) setPerfumeSettings(dto)
        setPerfumeC.setMessageListener(request -> {
            try {
                PerfumeSettings dto =
                    (PerfumeSettings) ((ObjectMessage) request).getObject();
                impl.setPerfumeSettings(dto);
                sendAck(request);
            } catch (Exception e) { e.printStackTrace(); }
        });

        // 3) registerCallback(id)
        registerCbC.setMessageListener(request -> {
            try {
                impl.registerCallback(((TextMessage) request).getText());
                sendAck(request);
            } catch (JMSException e) { e.printStackTrace(); }
        });

        // 4) unregisterCallback(id)
        unregisterCbC.setMessageListener(request -> {
            try {
                impl.unregisterCallback(((TextMessage) request).getText());
                sendAck(request);
            } catch (JMSException e) { e.printStackTrace(); }
        });

        // 5) eot()
        eotC.setMessageListener(request -> {
            try { impl.eot(); sendAck(request); close(); }
            catch (JMSException e) { e.printStackTrace(); }
        });
    }

  private void sendAck(Message request) {
        try {
            Destination replyTo = request.getJMSReplyTo();
            if (replyTo == null) {
                
                return;
            }
            TextMessage reply = session.createTextMessage("OK");
            reply.setJMSCorrelationID(request.getJMSCorrelationID());
            replyProducer.send(replyTo, reply);
        } catch (InvalidDestinationException ex) {
            
            System.err.println("[Responder] Reply queue disappeared – client probably shut down.");
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
