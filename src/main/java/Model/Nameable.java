package Model;

public interface Nameable {
    /**
     * Beállítja a mező nevét.
     *
     * @param n - A mező új neve.
     */
    void setName(String n);

    /**
     * Visszaadja a mező nevét.
     *
     * @return A mező neve.
     */
    String getName();
}
