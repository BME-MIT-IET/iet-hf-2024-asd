package Model;

/**
 * Speciális karakter, aki a játékban a csöveket és a pumpákat tudja tönkretenni / állítani.
 */
public class SaboteurCharacter extends Character {

    /**
     * Konstruktor - beállítja a szabotor nevet
     *
     * @param name a szabotor neve
     */
    public SaboteurCharacter(String name) {
        super(name);
    }

    /**
     * Konstruktor - beállítja a szabotor nevet es kezdomezojet
     *
     * @param name a szabotor neve
     * @param f    a mezo, amin a jatek kezdeten all
     */
    public SaboteurCharacter(String name, Field f) {
        super(name, f);
    }

    /**
     * Konstruktor - beállítja a szabotor nevet, a kezdomezojet es a nala levo csovet
     *
     * @param name       a szabotor neve
     * @param f          a mezo, amin a jatek kezdeten all
     * @param pickedPipe a cso, ami nala van
     */
    public SaboteurCharacter(String name, Field f, Pipe pickedPipe) {
        this(name, f);
        this.pickedPipe = pickedPipe;
    }

    /**
     * A szabotőr megpróbál egy csövet csúszóssá tenni.
     *
     * @return Ha sikerült csúszóssá tenni igazat ad vissza, egyebkent hamisat
     */
    public boolean makeSlippery() {
        return field.setToSlippery();
    }

    @Override
    public String toString() {
        String fieldName = this.getField() == null ? "-" : this.getField().getName();
        String pickedPipeName = this.pickedPipe == null ? "-" : this.pickedPipe.getName();

        return "SaboteurCharacter\n"
                + "\tName: " + this.getName() + "\n"
                + "\tField: " + fieldName + "\n"
                + "\tPipe in hand: "+ pickedPipeName+ "\n";
    }

    public boolean fix() {
        return false;
    }

    public boolean pickupPump() {
        return false;
    }

    public boolean placePumpToPipe() {
        return false;
    }

    public boolean pickupNewPipe_mc() {
        return false;
    }
}
