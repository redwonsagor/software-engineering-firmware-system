/**
 * Decorator that adjusts dispensing amount based on an intensity level.
 */
public class IntensityPerfumeDecorator extends PerfumeDispenserDecorator {
    private final int intensity; 

    public IntensityPerfumeDecorator(PerfumeDispenser wrapped, int intensity) {
        super(wrapped);
        this.intensity = intensity;
    }

    @Override
    public void dispense() {
        // mention intensity
        System.out.println("[IntensityDecorator] Intensity level: " + intensity);
        // Delegate to base or next decorator
        super.dispense();
        //simulate volume
        System.out.println("[IntensityDecorator] Dispensed at intensity " + intensity);
    }
}
