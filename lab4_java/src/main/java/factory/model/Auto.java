package factory.model;

/**
 * Represents an auto assembled from a body, motor, and accessory. It extends the generic Part class and contains references to the specific parts used in its assembly.
 */
public class Auto extends Part {
    private final Body body;
    private final Motor motor;
    private final Accessory accessory;

    public Auto(Body body, Motor motor, Accessory accessory) {
        super();
        this.body = body;
        this.motor = motor;
        this.accessory = accessory;
    }

    public Body getBody() { return body; }
    public Motor getMotor() { return motor; }
    public Accessory getAccessory() { return accessory; }
}