import javax.jms.JMSException;
import serobjs.PerfumeSettings;

/**
 * Concrete firmware logic behind the RobotControlInterface.
 * -  stores the most-recent user settings
 * -  dispenses perfume via Factory/Decorator
 * -  sends callback messages back to the App
 */
public class RobotControlImpl implements RobotControlInterface {

    private AppStatusCallbackCaller callbackCaller;
    private PerfumeSettings currentSettings;   // last DTO received

   

    public void setCallbackCaller(AppStatusCallbackCaller caller) {
        this.callbackCaller = caller;
    }

    

    @Override
    public void setPerfumeSettings(PerfumeSettings settings) throws JMSException {
        this.currentSettings = settings;                 // remember for later
        System.out.println("[Impl] Received and applied settings: " + settings);

        // Factory → Decorator → runtime behaviour
        if (settings.isPerfumeEnabled()) {
            PerfumeDispenser dispenser =
                PerfumeDispenserFactory.createDispenser(settings);
            dispenser.dispense();
        } else {
            System.out.println("[Impl] Perfume disabled - nothing dispensed.");
        }

        if (callbackCaller != null) {
            callbackCaller.notifyStatus("Settings updated: " + settings);
        }
    }

    @Override
    public void startAdaptiveCleaning() throws JMSException {
        
        if (currentSettings == null) {
            currentSettings = new PerfumeSettings(/*perfumeIntensity*/1,
                                                  /*enabled*/false,
                                                  /*type*/"standard",
                                                  /*cleaningIntensity*/5);
        }

        System.out.println("[Impl] Cleaning started with: " + currentSettings);

        if (callbackCaller != null) {
            callbackCaller.notifyStatus("Cleaning started with intensity: "
                                        + currentSettings.getCleaningIntensity());
        }
    }

    @Override
    public void registerCallback(String clientId) throws JMSException {
        System.out.println("[Impl] Callback client registered: " + clientId);
    }

    @Override
    public void unregisterCallback(String clientId) throws JMSException {
        System.out.println("[Impl] Callback client unregistered: " + clientId);
    }

    @Override
    public void eot() throws JMSException {
        System.out.println("[Impl] EOT received - shutting down.");
        if (callbackCaller != null) {
            callbackCaller.notifyStatus("Robot firmware shutting down.");
        }
    }
}
