package Model;

/**
 * Az Attachable interfész, a megvalósító osztályok felülírják a függvényeket.
 */
public interface Attachable extends Steppable, Nameable {

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @return sikeres volt-e
     */
    boolean pushWater();

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @param iamPushing A cső, amelyik a vizet továbbítja.
     * @return sikeres volt-e
     */
    boolean pushWater(Pipe iamPushing);

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @return Kiküldött víz mennyisége.
     */
    int getPushedWater();

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @return Befogadott víz mennyisége.
     */
    int getAcceptedWater();

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @param c A karakter, aki a csővet rá akarja csatlakoztatni.
     * @return sikeres volt-e a csatlakoztatás
     */
    boolean attachPipe(Character c);

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @param p A cső, amit rá akarunk csatlakoztatni.
     * @return sikeres volt-e a csatlakoztatás
     */
    boolean attachPipe(Pipe p);

    /**
     * Üres függvény, a megvalósító osztályok felülírják.
     *
     * @param p A cső, amit le akarunk csatlakoztatni.
     * @return sikeres volt-e a lecsatlakoztatás
     */
    boolean deattachPipe(Pipe p);

}
