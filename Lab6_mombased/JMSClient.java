import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract base for all JMS callers & responders.
 * Handles connecting to the ActiveMQ broker and creating a Session.
 */
public abstract class JMSClient implements AutoCloseable {

    protected Session  session;
    protected String   destination;   // queue or topic name
    protected volatile boolean connected;

    private Connection                 connection;
    private final ActiveMQConnectionFactory factory;
    private final String               user;
    private final String               password;

    

    /**
     * Full constructor.
     *
     * @param brokerUrl  e.g. "tcp://localhost:61616"
     * @param user       broker username (default "admin")
     * @param password   broker password (default "password")
     * @param queueName  JMS queue or topic to use
     */
    public JMSClient(String brokerUrl,
                     String user,
                     String password,
                     String queueName) throws JMSException {

        this.factory     = new ActiveMQConnectionFactory(brokerUrl);
        this.user        = user;
        this.password    = password;
        this.destination = queueName;

        configureTrustedPackages();
        connect();                     // sets connected = true
    }

    /** Convenience constructor using localhost defaults. */
    public JMSClient(String queueName) throws JMSException {
        this("tcp://localhost:61616", "admin", "password", queueName);
    }

  

    /** Let ActiveMQ know which packages it may deserialize into. */
    private void configureTrustedPackages() {
        List<String> pkgs = Arrays.asList(
            "serobjs",     // PerfumeSettings DTO
            "java.util",
            "java.time"
        );
        factory.setTrustedPackages(pkgs);
    }

    /**
     * Establishes the connection, starts it, and opens a Session.
     * Safe to call repeatedly; only the first call does actual work.
     */
    protected synchronized boolean connect() throws JMSException {
        if (connected) return true;

        connection = factory.createConnection(user, password);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        connected = true;
        return true;
    }


    /** Close the underlying connection (implements AutoCloseable). */
    @Override
    public synchronized void close() throws JMSException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
        connected = false;
    }
}
