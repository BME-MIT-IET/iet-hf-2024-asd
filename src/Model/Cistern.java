package Model;

import Controller.Board;
import Controller.Settings;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Ciszterna osztály a játékban ami a víz befogadásáért felelős
 */
public class Cistern extends Field implements Attachable {
    /**
     * A játék táblája
     */
    Board map;
    /**
     * A ciszternába befolyt víz mennyisége
     */
    private int acceptedWater = 0;

    /**
     * Számláló, amely megadja, hogy hány kör múlva generál újabb csövet, pumpát a ciszterna
     */
    private int creatingCounter = Settings.cistern_roundsUntilNew();

    /**
     * Csövek amik a ciszternához vannak csatlakoztatva
     */
    private ArrayList<Pipe> pipes = new ArrayList<>();
    /**
     * Cső amit a ciszterna generált
     */
    private Pipe newPipe;
    /**
     * Pumpa amit a ciszterna generált
     */
    private Pump newPump;

    /**
     * Konstruktor - beállítja a ciszterna nevét
     *
     * @param name - A ciszterna neve
     */
    public Cistern(String name, Board map) {
        super(name);
        this.map = map;
    }

    /**
     * Konstruktor - beállítja a ciszterna nevét és a csatlakoztatott csöveket
     *
     * @param name  - A ciszterna neve
     * @param pipes - A csatlakoztatott csövek listája
     */
    public Cistern(String name, Board map, ArrayList<Pipe> pipes) {
        this(name, map);

        this.pipes = pipes;
        for (Pipe pipe : pipes) {
            if (pipe != null) pipe.attached(this);
        }
    }

    /**
     * Konstruktor - beállítja a ciszterna nevét, a csatlakoztatott csöveket, a generált csövet és pumpát
     *
     * @param name          - A ciszterna neve
     * @param acceptedWater - A ciszterna által befogadott víz mennyisége
     * @param newPipeName   - A generált cső neve
     * @param newPumpName   - A generált pumpa neve
     */
    public Cistern(String name, Board map, int acceptedWater, String newPipeName, String newPumpName) {
        this(name, map);

        this.acceptedWater = acceptedWater;

        if (newPipeName != null && !newPipeName.equals("-")) {
            this.newPipe = new Pipe(newPipeName);
            //this.attachPipe(newPipe);
        }
        if (newPumpName != null && !newPumpName.equals("-")) {
            this.newPump = new Pump(newPumpName, this.map);
        }
    }

    /**
     * A csőhálózatból a víz a ciszternába lett pumpálva.
     *
     * @return boolean - igaz, ha sikerült a víz pumpálása
     */
    public boolean pushWater() {
        acceptedWater++;
        return true;
    }

    /**
     * A csőhálózatból a víz a ciszternába lett pumpálva egy csön keresztül.
     *
     * @param iamPushing - a cső amiből a víz pumpálódik
     * @return boolean - igaz, ha sikerült a víz pumpálása
     */
    @Override
    public boolean pushWater(Pipe iamPushing) {
        return pushWater();
    }

    /**
     * Mindig 0-val tér vissza, mert a ciszterna nem pumpál vízbe
     *
     * @return int - 0
     */
    @Override
    public int getPushedWater() {
        return 0;
    }

    /**
     * Visszaadja a ciszternába beérkezett víz mennyiségét
     *
     * @return int - az eddig beérkezett víz mennyisége
     */
    public int getAcceptedWater() {
        return acceptedWater;
    }

    /**
     * Új cső és új pumpa képződik
     */
    public void step() {
        creatingCounter--;
        if (creatingCounter <= 0) {
            // If there is no pipe, create one
            if (newPipe == null) {
                newPipe = new Pipe("pi" + (++this.pi_count) + "_" + this.name);
            }

            // If there is no pump, create one
            if (newPump == null) {
                newPump = new Pump("pu" + (++this.pu_count) + "_" + this.name, this.map);
            }

            // Reset the creating counter
            creatingCounter = Settings.cistern_roundsUntilNew();
        }
    }

    /**
     * Egy karakter felvesz egy csövet a ciszternából, a paraméterben megadott számnak megfeleően
     *
     * @param number - a cső sorszáma, amit felvesz a karakter
     * @param c      - a karakter, aki a csövet veszi fel
     * @return fel lehet e venni az adott csovet a ciszternatol
     */
    public boolean pickupPipe(int number, Character c) {
        int index = number - 1;
        if (index >= 0 && index < pipes.size()) {
            // Try to add the pipe to the character
            boolean success = c.receivePipe(pipes.get(index));
            // If it was successful, deattach the pipe from the cistern
            if (success) this.deattachPipe(pipes.get(index));
            return success;
        }
        return false;
    }

    /**
     * A szerelő karakter felvesz egy ciszternában létrejött új csövet
     *
     * @param mc - szerelő karakter, aki a csövet veszi fel
     * @return fel lehet e venni generalt csovet a ciszternatol
     */
    public boolean pickupNewPipe(MechanicCharacter mc) {
        if (newPipe != null) {
            // Try to add the pipe to the character
            boolean success = mc.receivePipe(newPipe);
            // If it was successful
            if (success) {
                // Attach the pipe to the cistern
                this.attachPipe(newPipe);
                // Add pipe to board
                if (map != null) map.addPipe(newPipe);
                // Remove the pipe from the newPipe
                this.newPipe = null;
            }
            return success;
        }
        return false;
    }

    /**
     * A szerelő karakter felveszi a kész pumpát a ciszternából
     *
     * @param mc - szerelő karakter, aki a pumpát veszi fel
     * @return fel lehet e venni a pumpat a ciszternatol
     */
    public boolean pickupPump(MechanicCharacter mc) {
        if (newPump != null) {
            // Try to add the pump to the character
            boolean success = mc.receivePump(newPump);
            // If it was successful, remove the pump from the cistern
            if (success) this.newPump = null;
            return success;
        }
        return false;
    }

    /**
     * A kapott csövet csatlakoztatja a ciszternához
     *
     * @param p - cső, amit csatlakoztatni kell
     * @return boolean - a sikerességtől függ
     */
    public boolean attachPipe(Pipe p) {
        if (pipes.contains(p)) {
            return false;
        }
        this.addNeighbour(p);
        pipes.add(p);
        p.addNeighbour(this);
        p.attached(this);
        return true;
    }

    /**
     * Elveszi a paraméterként kapott karakter pickedPipe-ját, és a ciszternára csatlakoztatja
     *
     * @param c - karakter, akinél a cső van
     * @return boolean - a sikerességtől függ
     */
    @Override
    public boolean attachPipe(Character c) {
        Pipe toAttach = c.givePipeToAttach();
        return this.attachPipe(toAttach);
    }

    /**
     * Eltávolítja a paraméterként kapott csövet a szomszédok közül, és hozzáadja a karakter pickedPipe-jához
     *
     * @param p - a lecsatolandó cső
     * @return sikeresseg
     */
    @Override
    public boolean deattachPipe(Pipe p) {
        if (!pipes.contains(p)) {
            return false;
        }
        pipes.remove(p);
        this.removeNeighbour(p);
        p.removeNeighbour(this);
        p.deattached(this);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cistern cistern = (Cistern) o;
        return acceptedWater == cistern.acceptedWater && creatingCounter == cistern.creatingCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), acceptedWater, creatingCounter, pipes, newPipe, newPump);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Cistern ");
        string.append(this.getName()).append(" ");
        for (Pipe pipe : pipes) {
            string.append(pipe.getName()).append(" ");
        }
        if (pipes.size() == 0) string.append("- ");
        String newPipeName = newPipe == null ? "-" : newPipe.getName();
        String newPumpName = newPump == null ? "-" : newPump.getName();
        string.append(acceptedWater).append(",").append(newPipeName).append(",").append(newPumpName);
        return string.toString();
    }

    public Pump getNewPump() {
        return this.newPump;
    }

    public Pipe getNewPipe() {
        return this.newPipe;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }
}
