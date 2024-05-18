package Model;

import Controller.Board;
import Controller.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Pumpa osztály a játékban ami a vizet pumpálja a csőhálózatban
 */
public class Pump extends Field implements Attachable {
    private final Board map;

    /**
     * A pumpa azon csöve amin a víz beérkezik
     */
    private Pipe input;
    /**
     * A pumpa azon csöve amin a víz kikerül
     */
    private Pipe output;
    /**
     * A pumpa azon állapotát írja le, hogy az adott körben pumpált-e már vizet az output csövébe vagy fogadott be vizet a tartályába.
     */
    private boolean stepped = false;
    /**
     * A pumpa állapotát írja le. Ha működik, akkor true, ha elromlott, akkor false az értéke.
     */
    private boolean working = true;
    /**
     * A befogadott víz mennyisége.
     */
    private int acceptedWater = 0;
    /**
     * A továbbtolt víz mennyisége.
     */
    private int pushedWater = 0;
    /**
     * A hátra lévő körök számát írja le a pumpa elromlásáig.
     * Ha lejár (=0 az értéke) a pumpa elromlik.
     */
    private int roundsUntilBreakdown = Settings.pump_roundsUntilBreakdown();
    /**
     * A pumpára csatlakoztatott csövek
     */
    ArrayList<Pipe> pipes = new ArrayList<>();

    /**
     * Paraméter nélküli konstruktor
     */
    public Pump(Board map) {
        super();
        this.map = map;
    }

    /**
     * Konstruktor - beállítja a pumpa nevét.
     *
     * @param name - A pumpa neve
     */
    public Pump(String name, Board map) {
        super(name);
        this.map = map;
    }

    /**
     * Konstruktor - beállítja a pumpa nevét, a csöveket.
     *
     * @param name  - A pumpa neve
     * @param pipes - A pumpára csatlakoztatott csövek
     */
    public Pump(String name, Board map, ArrayList<Pipe> pipes) {
        this(name, map);

        this.pipes = pipes;
        for (Pipe pipe : pipes) {
            pipe.attached(this);
        }
    }

    /**
     * Konstruktor - beállítja a pumpa nevét, a csöveket, a működési állapotot, a befogadott és továbbtolt víz mennyiségét, az output és input csövet.
     *
     * @param name          - A pumpa neve
     * @param working       - A pumpa működési állapota
     * @param pushedWater   - A továbbtolt víz mennyisége
     * @param acceptedWater - A befogadott víz mennyisége
     */
    public Pump(String name, Board map, boolean working, int pushedWater, int acceptedWater) {
        this(name, map);
        this.working = working;
        this.pushedWater = pushedWater;
        this.acceptedWater = acceptedWater;
    }

    /**
     * Szomszédok közül kiválasztja az input csövet
     *
     * @param number - A kiválasztott cső sorszáma
     * @return boolean - igaz, ha sikerült a kiválasztás
     */
    @Override
    public boolean setInput(int number) {
        int index = number - 1;
        // If the index is out of bounds --> return false
        if (index >= pipes.size() || index < 0) return false;

        // If selected  pipe is already input or output --> return false
        if (this.input == pipes.get(index) || this.output == pipes.get(index)) return false;

        // Set input
        input = pipes.get(index);
        return true;
    }

    /**
     * Szomszédok közül kiválasztja az output csövet
     *
     * @param number - A kiválasztott cső sorszáma
     * @return boolean - igaz, ha sikerült a kiválasztás
     */
    @Override
    public boolean setOutput(int number) {
        int index = number - 1;

        // If the index is out of bounds --> return false
        if (index >= pipes.size() || index < 0) return false;

        // If selected  pipe is already input or output --> return false
        if (this.input == pipes.get(index) || this.output == pipes.get(index)) return false;

        // Set output
        output = pipes.get(index);
        return true;
    }

    /**
     * Megjavítja a pumpát
     *
     * @return boolean - igaz, ha sikerült a javítás
     */
    @Override
    public boolean fix() {
        // If the pump is already working --> return false
        if (working) return false;

        // Set working to true and a new roundsUntilBreakdown value
        this.working = true;
        this.roundsUntilBreakdown = Settings.pump_roundsUntilBreakdown();

        // Return true
        return true;
    }

    /**
     * Ha a pumpa működik, továbbítja a vizet az output csövön
     *
     * @return boolean - igaz, ha sikerült a víz továbbítása
     */
    public boolean pushWater() {
        // If already stepped --> return false
        if (stepped) return false;

        // Set stepped to true
        stepped = true;

        // If the pump is not working --> return false
        if (!working) return false;

        // Try to push water to output pipe --> return true if successful
        if (output != null && output.flowWater(this)) {
            acceptedWater++;
            pushedWater++;
            return true;
        }

        // Try to accept water to tank ->> return true if successful
        int water_in_tank = acceptedWater - pushedWater;
        if (water_in_tank < Settings.pump_tankSize()) {
            acceptedWater++;
            return true;
        }

        // Otherwise return false
        return false;
    }

    /**
     * Ha a pumpa működik, akkor vízet kap az input csövön, és azt tovább pumpálja az output csövön
     *
     * @param iamPushing - A cső, amelyik a víztovábbítást kezdeményezte
     * @return boolean - igaz, ha sikerült a víz továbbítása vagy befogadása
     */
    @Override
    public boolean pushWater(Pipe iamPushing) {
        // If water not come from input pipe --> return false
        if (iamPushing != input) return false;

        // Try to push water to output pipe --> return true if successful
        return this.pushWater();
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    /**
     * Lekéri a pumpa által továbbított víz mennyiségét
     *
     * @return int - a továbbított víz mennyisége
     */
    @Override
    public int getPushedWater() {
        return pushedWater;
    }

    /**
     * Lekéri a pumpa által elfogadott víz mennyiségét
     *
     * @return int - az elfogadott víz mennyisége
     */
    @Override
    public int getAcceptedWater() {
        return acceptedWater;
    }

    /**
     * Egy karakter felvesz egy csövet a pumpánál, a paraméterben megadott számnak megfeleően
     *
     * @param number - a cső sorszáma, amit felvesz a karakter
     * @param c      - a karakter, aki a csövet veszi fel
     */
    public boolean pickupPipe(int number, Character c) {
        int index = number - 1;

        // If the index is out of bounds --> return false otherwise set pipe_to_pickup
        if (index >= pipes.size() || index < 0) return false;
        Pipe pipe_to_pickup = pipes.get(index);

        // Try to deattach pipe from pump
        boolean success_deattach = deattachPipe(pipe_to_pickup);

        // If deattach was successful --> try to add pipe to character
        if (success_deattach) {
            // Try to add pipe to character
            boolean success_recieve = c.recievePipe(pipe_to_pickup);

            // If character couldn't recieve pipe --> reattach pipe to pump
            if (!success_recieve) attachPipe(pipe_to_pickup);

            // Return success_recieve
            return success_recieve;
        }

        // Otherwise return false
        return false;
    }

    /**
     * Elveszi a paraméterként kapott karakter pickedPipe-ját, és hozzáadja a szomszédokhoz (oda-vissza)
     *
     * @param c - a karakter, aki a csövet csatlakoztatja
     * @return boolean - igaz, ha sikerült a csatlakoztatás
     */
    @Override
    public boolean attachPipe(Character c) {
        // Get pipe to attach from character
        Pipe toAttach = c.givePipeToAttach();

        // If pipe is null --> return false
        if (toAttach == null) return false;

        // Try to attach pipe to pump --> return true if successful
        return attachPipe(toAttach);
    }

    /**
     * Csatlakoztatja a paraméterként kapott csövet a pumpához
     *
     * @param p - a cső, amit csatlakoztatunk
     * @return boolean - igaz, ha sikerült a csatlakoztatás
     */
    public boolean attachPipe(Pipe p) {
        // If the pipe is already attached --> return false
        if (pipes.contains(p)) return false;

        // Try to tell pipe that it is attached
        boolean success = p.attached(this);

        // If pipe could attach --> add pipe to pipes, neighbours and this to pipe's neighbours
        if (success) {
            this.addNeighbour(p);
            pipes.add(p);
            p.addNeighbour(this);
        }

        // Return success
        return success;
    }

    /**
     * Eltávolítja a paraméterként kapott csövet a szomszédok közül
     *
     * @param p - a cső, amit lecsatlakoztatunk
     * @return boolean - igaz, ha sikerült a lecsatlakoztatás
     */
    @Override
    public boolean deattachPipe(Pipe p) {
        // If the pipe is not attached --> return false
        if (!pipes.contains(p)) return false;

        // Try to tell pipe that it is deattached
        boolean success = p.deattached(this);

        // If deattach was successful
        if (success) {
            // Remove pipe from pipes, neighbours and this from pipe's neighbours
            pipes.remove(p);
            this.removeNeighbour(p);
            p.removeNeighbour(this);

            // If pipe is input or output --> set input or output to null
            if (p.equals(input)) input = null;
            if (p.equals(output)) output = null;
        }

        // Return success
        return success;
    }

    /**
     * Szétválasztja a paraméterként kapott csövet, és a paraméterként kapott Attachable-ra csatlakoztatja a kettőt
     *
     * @param oldPipe - a cső, amit szétválasztunk
     * @param a       - az Attachable, amire csatlakoztatjuk a kettőt
     * @return boolean - igaz, ha sikerült a szétválasztás
     */
    public boolean splitPipe(Pipe oldPipe, Attachable a) {
        int success_counter = 0;
        Point pipeCenter;
        try {
            pipeCenter = map.calculatePipeCenter(oldPipe);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Try to deattach old pipe from the other attachable
        if (a.deattachPipe(oldPipe)) success_counter++;

        // Try to attach old pipe to this pump
        if (this.attachPipe(oldPipe)) success_counter++;

        // Create new pipe
        Pipe newPipe = new Pipe("pi" + (++this.pi_count) + "_" + this.name);

        // Try to attach new pipe to the other attachable
        if (a.attachPipe(newPipe)) success_counter++;

        // Try to attach new pipe to this pump
        if (this.attachPipe(newPipe)) success_counter++;

        // If all 4 steps were successful
        if (success_counter == 4) {
            // Add pump and new pipe to map
            try {
                map.addPumpOnPipe(this, pipeCenter);
                map.addPipe(newPipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return true
            return true;
        }

        // If not all 4 steps were successful --> redo everything
        a.attachPipe(oldPipe);
        this.deattachPipe(oldPipe);
        a.deattachPipe(newPipe);
        this.deattachPipe(newPipe);

        // Otherwise return false
        return false;

    }

    /**
     * Ha a pumpa működik akkor vízet nyom az output csőbe
     */
    @Override
    public void step() {
        // If not stepped, working and there is water in tank --> try to push water to output
        if (!stepped && working && acceptedWater - pushedWater > 0) {
            if (output == null) return;
            boolean result = output.flowWater(this);
            if (result) pushedWater++;
        }

        // Decrease rounds until breakdown, if it is 0 --> set working to false and reset rounds until breakdown
        if (--roundsUntilBreakdown <= 0) {
            working = false;
            roundsUntilBreakdown = Settings.pump_roundsUntilBreakdown();
        }

        // Reset stepped
        stepped = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pump pump = (Pump) o;
        return stepped == pump.stepped && working == pump.working && acceptedWater == pump.acceptedWater && pushedWater == pump.pushedWater && roundsUntilBreakdown == pump.roundsUntilBreakdown;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), input, output, stepped, working, acceptedWater, pushedWater, roundsUntilBreakdown, pipes);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Pump " + this.getName() + " ");
        for (int i = 0; i < pipes.size(); i++) {
            if (i == pipes.size() - 1) string.append(pipes.get(i).getName()).append(" ");
            else string.append(pipes.get(i).getName()).append(",");
        }
        if (pipes.size() == 0) string.append("- ");
        String outputName = output == null ? "-" : output.getName();
        String inputName = input == null ? "-" : input.getName();
        string.append(working).append(",").append(pushedWater).append(",").append(acceptedWater).append(",").append(outputName).append(",").append(inputName);
        return string.toString();

    }

    public boolean getWorking() {
        return working;
    }

    public void setInput(Pipe pipe) {
        this.input = pipe;
    }

    public void setOutput(Pipe pipe) {
        this.output = pipe;
    }

    public Pipe getInput() {
        return input;
    }

    public Pipe getOutput() {
        return output;
    }

    public int getWaterInPump() {
        return Math.max(acceptedWater - pushedWater, 0);
    }
}
