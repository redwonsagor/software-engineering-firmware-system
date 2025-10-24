/**
 * JMS message “types” for selectors and correlation.
 */
public enum MessageKeys {
    // Client → Robot commands
    START_CLEANING("StartAdaptiveCleaning"),
    SET_PERFUME("SetPerfumeSettings"),
    REGISTER_CALLBACK("RegisterCallback"),
    UNREGISTER_CALLBACK("UnregisterCallback"),

    // Robot → App callbacks
    NOTIFY_STATUS("NotifyStatus"),

    // End-of-transmission
    EOT("EOT");

    private final String key;
    MessageKeys(String key) { this.key = key; }
    @Override
    public String toString() { return key; }
}
