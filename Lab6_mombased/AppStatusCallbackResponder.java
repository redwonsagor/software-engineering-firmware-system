import javax.jms.*;

/**
 * Client-side listener: receives AppStatusCallback messages from the robot
 * and delegates them to the handler that the App provides.
 */
public class AppStatusCallbackResponder extends JMSClient {

    private final AppStatusCallback handler;
    private MessageConsumer         consumer;

    

    /**
     * @param handler   your implementation of AppStatusCallback (e.g. prints to console)
     * @param queueName JMS queue where the robot publishes callbacks (e.g. "CallbackQueue")
     */
    public AppStatusCallbackResponder(AppStatusCallback handler,
                                      String queueName) throws JMSException {
        super(queueName);
        this.handler = handler;
    }

    

    @Override
    protected synchronized boolean connect() throws JMSException {
        if (connected) return true;          

        super.connect();                      // opens Session + sets connected=true

        Destination dest = session.createQueue(destination);
        consumer = session.createConsumer(dest,
                "Method = '" + MessageKeys.NOTIFY_STATUS + "'");

        return true;
    }

    /**
     * Start listening asynchronously for status callbacks.
     * Safe to call multiple times; only the first call does the wiring.
     */
    public void listen() throws JMSException {
        if (!connected) connect();

        consumer.setMessageListener(msg -> {
            try {
                if (msg instanceof TextMessage) {
                    handler.notifyStatus(((TextMessage) msg).getText());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    

    @Override
    public synchronized void close() throws JMSException {
        if (consumer != null) {
            consumer.close();
            consumer = null;
        }
        super.close(); 
    }
}
