package Model;

import Controller.Settings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

/**
 * Cső osztály a játékban amin kell a víz tud áramolni
 */
public class Pipe extends Field implements Steppable, Cloneable {

    /**
     * A cső két végére csatlakoztatott pumpa/ciszterna/forrás
     */
    private final ArrayList<Attachable> attachableList = new ArrayList<>();
    private boolean holey = false;
    int sticky = 0;
    int slippery = 0;
    int breakable = 0;

    /**
     * Paraméter nélküli konstruktor
     */
    public Pipe() {
        super();
    }

    /**
     * Konstruktor - beállítja a cső nevét
     *
     * @param name a cső neve
     */
    public Pipe(String name) {
        super(name);
    }

    /**
     * Konstruktor - beállítja a cső nevét és a cső két végére csatlakoztatott pumpa/ciszterna/forrás
     *
     * @param name        a cső neve
     * @param attachables a cső két végére csatlakoztatott pumpa/ciszterna/forrás
     */
    public Pipe(String name, Attachable[] attachables) {
        this(name);
        int maxElement = 2;
        for (Attachable a : attachables) {
            attachableList.add(a);
            if (attachableList.size() == maxElement) break;
        }
    }

    /**
     * Konstruktor - beállítja a cső nevét, a cső két végére csatlakoztatott pumpa/ciszterna/forrás, a cső lyukas-e, a cső ragadós-e, a cső csúszós-e
     *
     * @param name     a cső neve
     * @param holey    a cső lyukas-e
     * @param sticky   a cső ragadós-e
     * @param slippery a cső csúszós-e
     */
    public Pipe(String name, boolean holey, int slippery, int sticky) {
        this(name);
        this.holey = holey;
        this.sticky = sticky;
        this.slippery = slippery;
    }

    /**
     * Két paraméteres konstruktor
     *
     * @param a1 a cső egyik végére csatlakoztatott pumpa/ciszterna/forrás
     * @param a2 a cső másik végére csatlakoztatott pumpa/ciszterna/forrás
     */
    public Pipe(Attachable a1, Attachable a2) {
        super();
        attachableList.add(a1);
        attachableList.add(a2);
    }

    /**
     * Visszaadja a csőhöz csatlakoztatott elemeket
     *
     * @return a csőhöz csatlakoztatott elemek
     */
    public ArrayList<Attachable> getAttachables() {
        return attachableList;
    }

    /**
     * A szerelő lehelyez a csőre egy új pumpát
     *
     * @param mec3 a szerelő aki lehelyezi a pumpát
     * @return sikeres volt-e a pumpa lehelyezése
     */
    @Override
    public boolean placePump(MechanicCharacter mec3) {
        // Get pump from mechanic
        Pump pu2 = mec3.givePumpToPipe();
        if (pu2 == null) return false;

        // Get my first attachable
        Attachable a1 = attachableList.get(0);
        if (a1 == null) return false;

        // Try to split pipe and add pump to it
        return pu2.splitPipe(this, a1);
    }

    /**
     * Ha lyukas a cső, vagyis a holey értéke igaz, akkor azt beállítja hamisra. Ha nem lyukas, akkor nem javítja meg
     * Visszaadja hogy sikerült-e megfoltozni a csövet
     *
     * @return sikeres volt-e a megfoltoztatás
     */
    @Override
    public boolean fix() {
        if (holey) {
            holey = false;
            breakable = Settings.pipe_breakable();
            return true;
        }
        return false;
    }

    public boolean setToSticky() {
        slippery = 0;
        sticky = Settings.pipe_sticky();
        return true;
    }

    public boolean setToSlippery() {
        sticky = 0;
        slippery = Settings.pipe_slippery();
        return true;
    }

    /**
     * Ha nincs lyuk a csövön, akkor kilyukasztja, vagyis beállítja a holey értékét true-ra.
     * Ha lyukas a cső NEM lesz rajta több lyuk.
     *
     * @return sikeres volt-e a lyukasztás
     */
    @Override
    public boolean makeHole() {
        if (holey || breakable > 0) {
            return false;
        }
        holey = true;
        return true;
    }

    /**
     * Egy karakter rálép a csőre. Ha nem áll senki a csövön, akkor az új karakter ráléphet, true-t ad vissza,
     * ha már állnak rajta, nem léphet rá az új character, false-t ad vissza.
     *
     * @param c a karakter aki rálép a csőre
     * @return sikeres volt-e a lépés
     */
    @Override
    public boolean accept(Character c) {
        if (characters.size() > 0) {
            return false;
        }
        Field currentField = c.getField();
        boolean successInFieldAccept = super.accept(c);
        if (!successInFieldAccept) return false;
        if (sticky > 0) {
            c.setStuck(this.sticky);
            return true;
        }
        if (slippery > 0) {
            ArrayList<Field> neighbours = this.getNeighbours();

            //Ha tesztelünk
            if (Settings.isTesting) {
                if (neighbours.size() == 1) {
                    return neighbours.get(0).accept(c);
                } else if (neighbours.get(0).equals(currentField)) {
                    return neighbours.get(1).accept(c);
                } else {
                    return neighbours.get(0).accept(c);
                }
            }

            int randMax = neighbours.size();
            Random rand = new Random();
            return neighbours.get(rand.nextInt(0, randMax)).accept(c);
        }
        return true;
    }

    /**
     * Átnyomja a vizet az egyik hozzácsatlakoztatott elemből (paraméterben kapja) a másikba.
     * Ha sikerült true-t ad vissza
     *
     * @param input a cső egyik végére csatlakoztatott pumpa/ciszterna/forrás amelyik átadja a vizet
     * @return sikeres volt-e a viz átadása
     */
    public boolean flowWater(Attachable input) {
        if (holey) {
            return true;
        }

        // Find output attachable (the other end of the pipe)
        for (Attachable a : attachableList) {
            if (a != input) {
                return a.pushWater(this);
            }
        }

        // If the other end is not found, the pipe is someone hand --> water is lost
        return true;

    }

    /**
     * A csőhöz hozzácsatlakoztat egy új elemet
     *
     * @param to az új elem ahova csatlakoztatjuk a csövet
     * @return sikeres volt-e a csatlakoztatás
     */
    public boolean attached(Attachable to) {
        if (attachableList.size() >= 2 || attachableList.contains(to)) {
            return false;
        }

        attachableList.add(to);
        return true;
    }

    /**
     * A csőhöz csatlakoztatott elemet eltávolítja
     *
     * @param from az elem amit el akarunk távolítani
     * @return sikeres volt-e az eltávolítás
     */
    public boolean deattached(Attachable from) {
        if (!attachableList.contains(from)) {
            return false;
        }
        attachableList.remove(from);
        return true;
    }

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     */
    @Override
    public void step() {
        if (breakable > 0) {
            breakable--;
        }
        if (slippery > 0) {
            slippery--;
        }
        if (sticky > 0) {
            sticky--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pipe pipe = (Pipe) o;
        return Objects.equals(attachableList, pipe.attachableList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attachableList);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Pipe " + this.getName() + " ");
        attachableList.sort(Comparator.comparing(Nameable::getName));

        for (int i = 0; i < attachableList.size(); i++) {
            if (i == attachableList.size() - 1) string.append(attachableList.get(i).getName()).append(" ");
            else string.append(attachableList.get(i).getName()).append(",");
        }
        if (attachableList.size() == 0) string.append("- ");
        boolean is_slippery = slippery > 0;
        boolean is_sticky = sticky > 0;
        string.append(holey).append(",").append(is_slippery).append(",").append(is_sticky);
        return string.toString();
    }

    public int getSticky() {
        return sticky;
    }

    public int getSlippery() {
        return slippery;
    }

    public Object clone() throws CloneNotSupportedException {
        Pipe clone = (Pipe) super.clone();
        clone.attachableList.addAll(this.attachableList);
        clone.holey = this.holey;
        clone.breakable = this.breakable;
        clone.sticky = this.sticky;
        clone.slippery = this.slippery;
        return clone;
    }

    public boolean getHoley() {
        return holey;
    }
}
