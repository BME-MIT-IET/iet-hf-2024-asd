package Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Vízforrás osztály a játékban ami a víz létrehozásáért és elindításáért felelős.
 * Folyamatosan ellátja vízzel a hozzá csatlakoztatott csöveket.
 */
public class WaterSource implements Attachable, Nameable, Comparable<WaterSource> {
    /**
     * A vízforrás neve
     */
    protected String name;

    /**
     * Viz mennyiség amit átpumpált
     */
    private int pushedWater = 0;
    /**
     * Csövek amik a vízforráshoz vannak csatlakoztatva
     */
    private ArrayList<Pipe> pipes = new ArrayList<>();

    /**
     * Konstruktor - beállítja a vízforrás nevét
     *
     * @param name - A vízforrás neve
     */
    public WaterSource(String name) {
        this.name = name;
    }

    /**
     * Konstruktor - beállítja a vízforrás nevét és a továbbított víz mennyiségét
     *
     * @param name        - A vízforrás neve
     * @param pushedWater - A vízforrás által átpumpált víz mennyisége
     */
    public WaterSource(String name, int pushedWater) {
        this(name);
        this.pushedWater = pushedWater;
    }

    /**
     * Konstruktor - beállítja a vízforrás nevét és a hozzacsatolt csöveket
     *
     * @param name  - A vízforrás neve
     * @param pipes - A csatlakoztatott csövek listája
     */
    public WaterSource(String name, ArrayList<Pipe> pipes) {
        this(name);

        this.pipes = pipes;
        for (Pipe pipe : pipes) {
            pipe.attached(this);
        }
    }

    /**
     * Egy körben átpumpál minden hozzá csatlakoztatott csőn egy egységnyi vizet
     */
    public void step() {
        for (Pipe pipe : pipes) {
            boolean success = pipe.flowWater(this);
            if (success) pushedWater++;
        }
    }

    /**
     * Beérkezik a víz
     *
     * @return mindig hamis, mert a vízforrás nem fogad be vizet
     */
    public boolean pushWater() {
        return false;
    }

    /**
     * Beérkezik a víz a csőből
     *
     * @param iamPushing - A cső amiből a víz átpumpálódik
     * @return mindig hamis, mert a vízforrás nem fogad be vizet
     */
    @Override
    public boolean pushWater(Pipe iamPushing) {
        return false;
    }

    /**
     * Lekéri a víz mennyiségét amit átpumpált
     *
     * @return int - A víz mennyisége
     */
    public int getPushedWater() {
        return this.pushedWater;
    }

    /**
     * A beérkező víz mennyiségét adja vissza
     *
     * @return mindig 0, mert a vízforrás nem fogad be vizet
     */
    @Override
    public int getAcceptedWater() {
        return 0;
    }

    /**
     * Beállítja a mező nevét.
     *
     * @param n - A mező új neve.
     */
    @Override
    public void setName(String n) {
        this.name = n;
    }

    /**
     * Visszaadja a mező nevét.
     *
     * @return A mező neve.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * A vízforrásra csövet csatlakoztat
     *
     * @param p A cső, amit rá akarunk csatlakoztatni.
     * @return true, ha sikerült a csatlakoztatás, false, ha nem.
     */
    @Override
    public boolean attachPipe(Pipe p) {
        // If the pipe is already attached, return false
        if (pipes.contains(p)) return false;

        // Try to attach the pipe
        boolean success = p.attached(this);

        // If the pipe was attached, add it to the list
        pipes.add(p);

        // Return the result of the attachment
        return success;
    }

    /**
     * A vízforrásra csövet csatlakoztat
     *
     * @param c A karakter, aki a csővet rá akarja csatlakoztatni.
     * @return mindig hamis, mert a vízforrásra nem lehet csővet csatlakoztatni a karakterrel
     */
    @Override
    public boolean attachPipe(Character c) {
        return false;
    }

    /**
     * Lecsatlakoztatja a csövet a vízforrásról
     *
     * @param p A cső, amit le akarunk csatlakoztatni.
     * @return mindig hamis, mert a vízforrásról nem lehet csővet lecsatlakoztatni
     */
    @Override
    public boolean deattachPipe(Pipe p) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterSource that = (WaterSource) o;
        return pushedWater == that.pushedWater && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pushedWater, pipes);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("WaterSource " + this.getName() + " ");
        for (int i = 0; i < pipes.size(); i++) {
            if (i == pipes.size() - 1) string.append(pipes.get(i).getName());
            else string.append(pipes.get(i).getName()).append(",");
        }
        if (pipes.size() == 0) string.append("- ");
        string.append(" ").append(this.getPushedWater());
        return string.toString();
    }


    @Override
    public int compareTo(WaterSource that) {
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

    public ArrayList<Pipe> getPipes() {
        return this.pipes;
    }
}
