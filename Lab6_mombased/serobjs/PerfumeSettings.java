package serobjs;

import java.io.Serializable;
import java.util.Locale;

/**
 * Data-transfer object carrying ALL runtime settings
 * that the robot needs to start a cleaning-and-perfume cycle.
 *
 * The constructor now validates the arguments and throws
 * IllegalArgumentException if something is off.
 */
public final class PerfumeSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private final int     perfumeIntensity;  
    private final boolean perfumeEnabled;
    private final String  perfumeType;        
    private final int     cleaningIntensity; 

    

    /**
     * @param perfumeIntensity 1–3 (ignored / must be 0 when perfume disabled)
     * @param perfumeEnabled   true to dispense perfume
     * @param perfumeType      "standard", "special" or null
     * @param cleaningIntensity 1–10
     *
     * @throws IllegalArgumentException if the combination is invalid
     */
    public PerfumeSettings(int perfumeIntensity,
                           boolean perfumeEnabled,
                           String  perfumeType,
                           int     cleaningIntensity) {

        /* -------- validate cleaning ---------- */
        if (cleaningIntensity < 1 || cleaningIntensity > 10) {
            throw new IllegalArgumentException(
                "cleaningIntensity must be 1–10");
        }

        /* -------- validate perfume ---------- */
        if (!perfumeEnabled) {
            if (perfumeIntensity != 0) {
                throw new IllegalArgumentException(
                    "perfumeIntensity must be 0 when perfumeEnabled = false");
            }
            if (perfumeType != null) {
                throw new IllegalArgumentException(
                    "perfumeType must be null when perfume is disabled");
            }
        } else {                   // perfume enabled
            if (perfumeIntensity < 1 || perfumeIntensity > 3) {
                throw new IllegalArgumentException(
                    "perfumeIntensity must be 1–3 when perfume is enabled");
            }
            if (perfumeType == null) {
                throw new IllegalArgumentException(
                    "perfumeType must be \"standard\" or \"special\"");
            }
            String pt = perfumeType.toLowerCase(Locale.ROOT);
            if (!pt.equals("standard") && !pt.equals("special")) {
                throw new IllegalArgumentException(
                    "perfumeType must be \"standard\" or \"special\"");
            }
        }

      
        this.perfumeIntensity  = perfumeEnabled ? perfumeIntensity : 0;
        this.perfumeEnabled    = perfumeEnabled;
        this.perfumeType       = perfumeEnabled ? perfumeType.toLowerCase(Locale.ROOT)
                                                : null;
        this.cleaningIntensity = cleaningIntensity;
    }

    

    public int     getPerfumeIntensity() { return perfumeIntensity; }
    public boolean isPerfumeEnabled()   { return perfumeEnabled;   }
    public String  getPerfumeType()     { return perfumeType;      }
    public int     getCleaningIntensity(){ return cleaningIntensity; }

   

    @Override
    public String toString() {
        return "Settings(" +
               "perfumeIntensity="   + perfumeIntensity +
               ", perfumeType="      + perfumeType +
               ", cleaningIntensity=" + cleaningIntensity + ")";
    }
}
