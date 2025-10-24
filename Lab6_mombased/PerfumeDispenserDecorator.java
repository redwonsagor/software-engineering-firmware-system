/**
 * Abstract decorator that wraps a PerfumeDispenser.
 */
public abstract class PerfumeDispenserDecorator implements PerfumeDispenser {
    protected PerfumeDispenser wrapped;

    public PerfumeDispenserDecorator(PerfumeDispenser wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void dispense() {
        // By default, delegate to wrapped
        wrapped.dispense();
    }
}
