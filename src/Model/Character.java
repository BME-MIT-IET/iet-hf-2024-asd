package Model;

import java.util.Objects;

/**
 * A jatekban szereplo karaktereket reprezentalo absztrakt osztaly
 */
public abstract class Character implements Nameable, Comparable<Character> {
    /**
     * A cso aminek az egyik vege a karakternel van
     */
    protected Pipe pickedPipe = null;

    /**
     * A mezo, amin a karakter all
     */
    protected Field field = null;

    /**
     * A karakter neve
     */
    protected String name = "?";

    /**
     * Hány kör idejéig nem mozdulhat el a ragadós mezőről.
     */
    protected int stuck = 0;

    /**
     * Konstruktor - üres
     */
    public Character() {
    }

    /**
     * Konstruktor - beállítja a karakter nevét
     *
     * @param name a karakter neve
     */
    public Character(String name) {
        this();
        this.name = name;
    }

    /**
     * Konstruktor - beállítja a karakter nevét és a kezdőmezőjét
     *
     * @param f    a mezo amin a jatek kezdeten all
     * @param name a karakter neve
     */
    public Character(String name, Field f) {
        this(name);
        field = f;
    }

    /**
     * Beállítja a stuck értékét a paraméterként kapott számra.
     *
     * @param n a szam, amire allitja
     */
    public void setStuck(int n) {
        stuck = n;
    }

    /**
     * Beallitja a nevet
     *
     * @param n ez lesz a name erteke
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Visszaadja a karakter nevet
     *
     * @return a karakter neve
     */
    public String getName() {
        return name;
    }

    /**
     * Visszaadja hogy melyik mezon all a karakter
     *
     * @return mezo, amin a karakter all
     */
    public Field getField() {
        return field;
    }

    /**
     * Atallitja a mezot amin a karakter all
     *
     * @param f erre allitja at
     */
    public void setField(Field f) {
        field = f;
    }

    /**
     * Viszaadja a karakternél lévő csövet.
     *
     * @return a karakterhez tartozó cső
     */
    public Pipe getPickedPipe() {
        return pickedPipe;
    }

    /**
     * Elmozdítja a karaktert egy szomszédos mezőre. Megpróbál átlépni
     * a paraméterként átadott mezőre.
     *
     * @param f erre a mezore probal meg atlepni
     */
    public boolean move(Field f) {
        if (stuck <= 0) {
            return f.accept(this);
        }
        return false;
    }

    /**
     * Ha a karakter pumpan tartozkodik atallitja a cso bemenetet a megadott pumpara
     *
     * @param number a kivalasztott pumpa szama
     * @return ha sikeres volt az output atallitasa true, ha nem akkor false
     */
    public boolean setPumpInput(int number) {
        return field.setInput(number);
    }


    /**
     * Ha a karakter pumpan tartozkodik atallitja a cso kimenetet a megadott pumpara
     *
     * @param number a kivalasztott pumpa szama
     * @return ha sikeres volt az output atallitasa true, ha nem akkor false
     */
    public boolean setPumpOut(int number) {
        return field.setOutput(number);
    }

    /**
     * Ha a karakter egy pumpán vagy a ciszternán áll, akkor kiválasztja
     * azt a pumpára vagy a ciszternához csatlakoztatott csövet, amit el akar mozdítani. Ekkor
     * a pumpához csatlakoztatott végét tudja elmozdítani.
     *
     * @param number a kivalasztott pumpa szama
     * @return ha sikerult uj csovet felvenni, akor true, egyebkent false
     */
    public boolean pickupPipe_c(int number) {
        if (pickedPipe == null)
            return field.pickupPipe(number, this);

        return false;
    }

    /**
     * A karakter a nala levo csovet megprobalja racsatlakoztatni a
     * mezore, amin all
     *
     * @return ha sikerult csatlakozatni, akkor true, egyebkent false
     */
    //ezt elirtam, attach
    public boolean attachPipeToPump() {
        if (pickedPipe == null) return false;

        boolean success = field.attachPipe(this);
        if (success) pickedPipe = null;

        return success;
    }

    /**
     * A karakter kap egy csovet
     *
     * @param p a pumpa
     */
    public boolean recievePipe(Pipe p) {
        if (this.pickedPipe == null) {
            this.pickedPipe = p;
            return true;
        }
        return false;
    }

    /**
     * A karakter atadja a csovet, ami nala van
     *
     * @return a cso, ami nala van
     */
    public Pipe givePipeToAttach() {
        return pickedPipe;
    }

    /**
     * A karakter megprobal eltorni egy csovet (a mezot, amin all)
     *
     * @return ha sikerult kilyukasztani, akkor true, egyebkent false
     **/
    public boolean breakPipe() {
        return field.makeHole();
    }

    /**
     * A karakter megprobal egy csovet (a mezot, amin all) ragadossa tenni
     *
     * @return ha sikerult ragadossa tenni true, ha nem, akkor false
     */
    public boolean makeSticky() {
        return field.setToSticky();
    }

    /**
     * Minden kör végén lépteti a karaktert, a stuck attribútumát csökkenti eggyel, ha
     * az értéke nem 0.
     */
    public void step() {
        if (stuck > 0)
            stuck--;
    }

    /**
     * összehasonlítja a karaktereket
     *
     * @param o a masik karakter
     * @return true, ha egyenloek, false, ha nem
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return stuck == character.stuck && Objects.equals(pickedPipe, character.pickedPipe) && Objects.equals(name, character.name);
    }

    /**
     * Hashcode a karakterekhez
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(pickedPipe, field, name, stuck);
    }

    /**
     * Visszaadja a karakter adatait megfelelo formatumban
     *
     * @return a karakter adatai
     */
    @Override
    public String toString() {
        // TODO
        return "";
    }


    public int compareTo(Character that) {
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

    public boolean fix() {
        return false;
    }

    public boolean pickupPump() {
        return false;
    }

    public boolean placePumpToPipe() {
        return false;
    }

    public boolean makeSlippery() {
        return false;
    }

    public int getStuck() {
        return stuck;
    }

    public boolean pickupNewPipe_mc() {
        return false;
    }
}
