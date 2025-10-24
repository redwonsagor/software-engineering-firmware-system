import serobjs.PerfumeSettings;

/**
 * Factory for building a perfume-dispenser chain that applies the decorators
 * chosen by the user (intensity & optional special-effect).
 */
public class PerfumeDispenserFactory {

    /**
     * Creates a dispenser according to the user settings.
     */
    public static PerfumeDispenser createDispenser(PerfumeSettings settings) {

        // ---- base object ---------------------------------------------------
        PerfumeDispenser dispenser = new BasePerfumeDispenser(settings.getPerfumeType());

        // intensity decorator 
        if (settings.isPerfumeEnabled()) {
            dispenser = new IntensityPerfumeDecorator(
                    dispenser,
                    settings.getPerfumeIntensity());
        }

        // add “special” decorator 
        if (settings.isPerfumeEnabled()
                && "special".equalsIgnoreCase(settings.getPerfumeType())) {
            dispenser = new SpecialPerfumeDecorator(dispenser, "Burst");
        }

        return dispenser;
    }
}
