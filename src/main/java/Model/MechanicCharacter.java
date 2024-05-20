package Model;

import java.util.Objects;

/**
 * Speciális karakter, aki a játékban a csöveket és a pumpákat tudja javítani / állítani.
 */
public class MechanicCharacter extends Character {
    /**
     * A pumpa, amit a szerelo felvehet a ciszternatol
     */
    private Pump pickedPump = null;

    /**
     * Konstruktor - beállítja a szerelo nevet
     *
     * @param name a szerelo neve
     */
    public MechanicCharacter(String name) {
        super(name);
    }

    /**
     * Konstruktor - beállítja a szerelo nevet, a kezdomezojet
     *
     * @param name a szerelo neve
     * @param f    a mezo, amin a jatek kezdeten all
     */
    public MechanicCharacter(String name, Field f) {
        super(name, f);
    }

    /**
     * Konstruktor - beállítja a szerelo nevet, a kezdomezojet, a nala levo csovet es pumpat
     *
     * @param name       a szerelo neve
     * @param f          a mezo, amin a jatek kezdeten all
     * @param pickedPipe a cso, ami nala van
     * @param pickedPump a pumpa, ami nala van
     */
    public MechanicCharacter(String name, Field f, Pipe pickedPipe, Pump pickedPump) {
        this(name, f);
        this.pickedPump = pickedPump;
        this.pickedPipe = pickedPipe;
    }

    /**
     * Megpróbál egy csövet vagy pumpát megjavítani.
     *
     * @return ha sikerult megjavitani akkor true, ha nem akkor false
     */
    public boolean fix() {
        return this.field.fix();
    }

    /**
     * Ha a szerelő egy ciszternán áll, akkor felvehet egy ott készült új pumpát
     *
     * @return Ha sikeresen megkapta a pumpat, azaz a pickedPump már nem null, igazat ad vissza. Minden más esetben hamisat.
     */
    public boolean pickupPump() {
        // If no pump is picked up already
        if (pickedPump == null) {
            // Try to pick up one
            boolean success = this.field.pickupPump(this);
            // Return true if the pickup was successful
            return success && pickedPump != null;
        }

        // Otherwise return false
        return false;
    }

    /**
     * Ha a szerelő egy csövön áll, lerakja a nála lévő pumpát a cső
     * közepére.
     *
     * @return ha lerakta akkor true, ha nem akkor false
     */
    public boolean placePumpToPipe() {
        // If the mechanic has a pump
        if (pickedPump != null) {
            // Try to place it
            boolean success = this.field.placePump(this);
            // If it was successful, set the picked pump to null
            if (success) pickedPump = null;
            return success;
        }
        // Otherwise return false
        return false;
    }

    /**
     * A szerelo atadja a nala levo pumpat
     *
     * @return a szerelonel levo pumpa
     */
    public Pump givePumpToPipe() {
        return pickedPump;
    }

    /**
     * Ha a szerelő egy ciszternán áll, a ciszternánál készített cső szabad végét fel tudja venni
     *
     * @return Ha sikeresen megkapta a csövet, azaz a pickedPipe már nem null, igazat ad vissza. Minden más esetben hamisat.
     */
    public boolean pickupNewPipe_mc() {
        // If no pipe is picked up already
        if (pickedPipe == null) {
            // Try to pick up one
            boolean success = this.field.pickupNewPipe(this);
            // Return true if the pickup was successful
            return success && pickedPipe != null;
        }

        // Otherwise return false
        return false;
    }

    public boolean makeSlippery() {
        return false;
    }

    /**
     * A karakter megkapja a csovet a ciszternatol
     *
     * @param p a cso, amit megkap
     * @return Ha sikeresen átvette a pumpát true, egyebkent false
     */
    public boolean receivePump(Pump p) {
        if (pickedPump == null) {
            pickedPump = p;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pickedPump);
    }

    @Override
    public String toString() {
        String fieldName = this.getField() == null ? "-" : this.getField().getName();
        String pickedPipeName = this.pickedPipe == null ? "-" : this.pickedPipe.getName();
        String pickedPumpName = this.pickedPump == null ? "-" : this.pickedPump.getName();

        return "MechanicCharacter\n"
                + "\tName: " + this.getName() + "\n"
                + "\tField: " + fieldName + "\n"
                + "\tPipe in hand: "+ pickedPipeName + "\n"
                + "\tPump in hand: "+ pickedPumpName + "\n";
    }


    public Pump getPickedPump() {
        return pickedPump;
    }
}