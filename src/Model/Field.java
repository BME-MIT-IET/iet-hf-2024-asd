package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Absztrakt mező osztály a játékban. Ezeken tudnak a karakterek mozogni. A leszármoazottaknak extra tulajdonságai vannak.
 */
public abstract class Field implements Nameable, Comparable<Field> {
    /**
     * A mezőn lévő karakterek listája.
     */
    protected final ArrayList<Character> characters = new ArrayList<>();
    /**
     * A mezővel szomszédos mezők listája.
     */
    private final ArrayList<Field> neighbours = new ArrayList<>();
    /**
     * A mező neve.
     */
    protected String name = "?";
    protected int pi_count = 0;
    protected int pu_count = 0;


    /**
     * Konstruktor - paraméter nélküli
     **/
    public Field() {
    }

    /**
     * Konstruktor - beállítja a mező nevét.
     *
     * @param name - A mező neve.
     */
    public Field(String name) {
        this.name = name;
    }

    /**
     * Beállítja a mező nevét.
     *
     * @param n - A mező új neve.
     */
    public void setName(String n) {
        this.name = n;
    }

    /**
     * Visszaadja a mező nevét.
     *
     * @return A mező neve.
     */
    public String getName() {
        return this.name;
    }

    public ArrayList<Character> getCharacters() {
        return this.characters;
    }

    /**
     * Karakter átlép egy mezőről a másikra.
     *
     * @param c - A karakter, amit a mezőre szeretnénk rakni.
     * @return Igaz értéket ad vissza, ha a karaktert sikeresen átlépett a mezőre.
     */
    public boolean accept(Character c) {
        // Get the current field from the character
        Field current_field = c.getField();

        // Check if the character is on a neighbour field or nowhere
        if (current_field != null && !this.neighbours.contains(current_field)) {
            return false;
        }

        // If character is not on a field --> can move to any field
        if (current_field == null) {
            this.characters.add(c);
            c.setField(this);
            return true;
        }

        // If character is on a field --> can move to a neighbour field
        if (this.neighbours.contains(current_field)) {
            current_field.characterAccepted(c);
            this.characters.add(c);
            c.setField(this);
            return true;
        }

        return false;
    }

    /**
     * Eltávolítja a mezőről a paraméterként kapott karaktert.
     *
     * @param c - A karakter, amit a mezőről szeretnénk eltávolítani.
     */
    private void remove(Character c) {
        this.characters.remove(c);
    }

    /**
     * Jelzés, hogy a karaktert elfogadta egy másik mező, így a mezőről törölni kell.
     *
     * @param c - A karakter, amit a mezőről szeretnénk eltávolítani mert át lett lépve.
     */
    public void characterAccepted(Character c) {
        this.remove(c);
    }

    /**
     * Visszaadja a mezővel szomszédos mezők listáját.
     *
     * @return A mezővel szomszédos mezők listája.
     */
    public ArrayList<Field> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Hozzáadja a paraméterként kapott mezőt a szomszédos mezők listájához.
     *
     * @param f - A mező, amit a szomszédos mezők listájához szeretnénk hozzáadni.
     */
    public void addNeighbour(Field f) {
        this.neighbours.add(f);
    }

    /**
     * Eltávolítja a paraméterként kapott mezőt a szomszédos mezők listájából.
     *
     * @param f - A mező, amit a szomszédos mezők listájából szeretnénk eltávolítani.
     */
    public void removeNeighbour(Field f) {
        this.neighbours.remove(f);
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @return Mindig hamisat ad vissza.
     */
    public boolean makeHole() {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @return Mindig hamisat ad vissza.
     */
    public boolean fix() {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @param number A beállítandó szám.
     * @return Mindig hamisat ad vissza.
     */
    public boolean setInput(int number) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad. Csak a leszármazottai írhatják felül.
     *
     * @param number A beállítandó szám.
     * @return Mindig hamisat ad vissza.
     */
    public boolean setOutput(int number) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @param number A beállítandó szám.
     * @param c      A karakter, aki a csőt fel akarja venni.
     */
    public boolean pickupPipe(int number, Character c) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @param c A karakter, aki a csőt fel akarja venni.
     */
    public boolean pickupNewPipe(MechanicCharacter c) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @param c A karakter, aki a pumpát fel akarja venni.
     */
    public boolean pickupPump(MechanicCharacter c) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad vissza. Csak a leszármazottai írhatják felül.
     *
     * @param c A karakter, aki a pumpát le akarja helyezni.
     */
    public boolean placePump(MechanicCharacter c) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad. Csak a leszármazottai írhatják felül.
     *
     * @param c A karakter, aki a csőt le akarja helyezni.
     * @return Mindig hamisat ad vissza.
     */
    public boolean attachPipe(Character c) {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad. Csak a leszármazottai írhatják felül.
     *
     * @return Mindig hamisat ad vissza.
     */
    public boolean setToSticky() {
        return false;
    }

    /**
     * Alapértelmezetten nem csinál semmit és hamisat ad. Csak a leszármazottai írhatják felül.
     *
     * @return Mindig hamisat ad vissza.
     */
    public boolean setToSlippery() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characters, neighbours, name);
    }

    @Override
    public String toString() {
        // TODO
        return "Field{" +
                "characters=" + characters +
                ", neighbours=" + neighbours +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int compareTo(Field that) {
        if (this.getName() == null && that.getName() == null) {
            return 0;
        } else if (this.getName() == null) {
            return -1;
        } else if (that.getName() == null) {
            return 1;
        } else {
            return this.getName().compareTo(that.getName());
        }
    }
}

