/**
 * Decorator that adds special effect logic (e.g., burst, aroma pattern).
 */
public class SpecialPerfumeDecorator extends PerfumeDispenserDecorator {
    private final String effectName;

    public SpecialPerfumeDecorator(PerfumeDispenser wrapped, String effectName) {
        super(wrapped);
        this.effectName = effectName;
    }

    @Override
    public void dispense() {
        // Pre-processing: announce special effect
        System.out.println("[SpecialDecorator] Activating effect: " + effectName);
        // Delegate to previous decorator
        super.dispense();
        // Post-processing: confirm effect
        System.out.println("[SpecialDecorator] Effect " + effectName + " applied.");
    }
}
