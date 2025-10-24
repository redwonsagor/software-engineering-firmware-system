/**
 * A basic implementation of PerfumeDispenser that simply prints dispensing.
 */
public class BasePerfumeDispenser implements PerfumeDispenser {
    private final String type;

    public BasePerfumeDispenser() {
        this.type = "standard";
    }

    public BasePerfumeDispenser(String type) {
        this.type = type;
    }

    @Override
    public void dispense() {
        if ("special".equalsIgnoreCase(type)) {
            System.out.println("[BaseDispenser] Dispensing perfume - Special type.");
        } else {
            System.out.println("[BaseDispenser] Dispensing perfume.");
        }
    }
}
