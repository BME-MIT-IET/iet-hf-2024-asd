package Controller;

import Model.MechanicCharacter;
import Model.SaboteurCharacter;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Prototype {

    public Board map = new Board();
    private int round = 1;
    private int mc_index = 0;
    private int sc_index = 0;
    private MechanicCharacter mc_next = null;
    private SaboteurCharacter sc_next = null;
    private int mc_points = 0;
    private int sc_points = 0;

    public void init() {
        // If testing, then read from input
        if (Settings.isTesting) {
            this.map.createFromInput();
        }

        // If not testing --> create default map, read characters from input and create them
        else {
            this.map.createDefaultMap();
            this.createCharacters();
        }
    }

    //Read the commands from input
    public void run() {

        // Calculate who is starting
        this.calculateWhoNext();

        // Run loop until game is over
        while (this.round <= Settings.game_rounds() || Settings.game_rounds() == -1) {
            // Print infos
            printRound();
            printMap();

            // Read command from input
            String command = IO.readCommand("Enter command: ");
            if (command == null || command.equals("---") || command.equals("-") || command.equals("")) break;

            // Run command
            boolean success = run_command(command.trim().replaceAll("\\s+", " "));

            // If command was not successful --> increase index
            if (success && mc_next != null) this.mc_index++;
            if (success && sc_next != null) this.sc_index++;

            // Print command was success
            printActionSuccess(success, command);

            // Calculate who is next --> if there is no next character, then step map
            boolean isNext = calculateWhoNext();
            if (!isNext) {
                stepMap();
                calculateWhoNext();
                calculatePoints();
                printPoints();
                this.round++;
            }
        }

        // Print game over
        printGameOver();
    }

    private void stepMap() {
        // 1. all water sources step
        this.map.waterSources.forEach((name, waterSource) -> waterSource.step());

        // 2. all pumps step
        this.map.pumps.forEach((name, pump) -> pump.step());

        // 3. all pipes step
        this.map.pipes.forEach((name, pipe) -> pipe.step());

        // 4. all cisterns step
        this.map.cisterns.forEach((name, cistern) -> cistern.step());

        // 5. all mechanic characters step
        this.map.mechanicCharacters.forEach((name, mechanicCharacter) -> mechanicCharacter.step());

        // 6. all saboteur characters step
        this.map.saboteurCharacters.forEach((name, saboteurCharacter) -> saboteurCharacter.step());
    }

    private String getWhoseNext() {
        if (mc_next != null) return mc_next.getName() + " (Mechanic)";
        else if (sc_next != null) return sc_next.getName() + " (Saboteur)";
        else throw new RuntimeException("Error: No more characters to step");
    }

    private boolean calculateWhoNext() {
        // Get who is next
        if (map.mechanicCharacters.size() > this.mc_index) {
            mc_next = this.getCharacter(map.mechanicCharacters, mc_index);
            sc_next = null;
        } else if (map.saboteurCharacters.size() > this.sc_index) {
            sc_next = this.getCharacter((map.saboteurCharacters), sc_index);
            mc_next = null;
        } else {
            // Reset indexes for next round
            this.mc_index = 0;
            this.mc_next = null;
            this.sc_index = 0;
            this.sc_next = null;
            return false;
        }
        return true;
    }

    public boolean run_command(String line) {

        boolean success = false;

        // Get command
        String[] parts = line.split(" ");
        String command = parts[0];

        // Run command for the next character
        switch (command) {
            // Move
            case "m" -> {
                // Check if parameter is valid
                if (parts.length < 2 || map.getField(parts[1]) == null) {
                    IO.writeActionError(command, "Invalid parameter");
                    break;
                }
                // If valid --> move
                if (mc_next != null) success = mc_next.move(map.getField(parts[1]));
                else if (sc_next != null) success = sc_next.move(map.getField(parts[1]));
            }
            // Set pump input
            case "i" -> {
                // Check if parameter is valid
                if (parts.length < 2 || !IO.isInteger(parts[1])) {
                    IO.writeActionError(command, "Invalid parameter");
                    break;
                }
                if (mc_next != null) success = mc_next.setPumpInput(Integer.parseInt(parts[1]));
                else if (sc_next != null) success = sc_next.setPumpInput(Integer.parseInt(parts[1]));
            }
            // Set pump output
            case "o" -> {
                // Check if parameter is valid
                if (parts.length < 2 || !IO.isInteger(parts[1])) {
                    IO.writeActionError(command, "Invalid parameter");
                    break;
                }
                if (mc_next != null) success = mc_next.setPumpOut(Integer.parseInt(parts[1]));
                else if (sc_next != null) success = sc_next.setPumpOut(Integer.parseInt(parts[1]));
            }
            // Break pipe (holey)
            case "h" -> {
                if (mc_next != null) success = mc_next.breakPipe();
                else if (sc_next != null) success = sc_next.breakPipe();
            }
            // Fix pipe/pump (only mechanic character)
            case "f" -> {
                if (mc_next != null) success = mc_next.fix();
            }
            // Pickup pipe
            case "p" -> {
                // Check if parameter is valid
                if (parts.length < 2 || !IO.isInteger(parts[1])) {
                    IO.writeActionError(command, "Invalid parameter");
                    break;
                }
                if (mc_next != null) success = mc_next.pickupPipe_c(Integer.parseInt(parts[1]));
                if (sc_next != null) success = sc_next.pickupPipe_c(Integer.parseInt(parts[1]));
            }
            // Pickup new pipe from cistern (only mechanic character)
            case "n" -> {
                if (mc_next != null) success = mc_next.pickupNewPipe_mc();
            }
            // Pickup pump from cistern (only mechanic character)
            case "b" -> {
                if (mc_next != null) success = mc_next.pickupPump();
            }
            // Attach pipe to pump/cistern
            case "a" -> {
                if (mc_next != null) success = mc_next.attachPipeToPump();
                if (sc_next != null) success = sc_next.attachPipeToPump();
            }
            // Place pump on pipe (only mechanic character)
            case "q" -> {
                if (mc_next != null) success = mc_next.placePumpToPipe();
            }
            // Make pipe slippery (only saboteur character)
            case "s" -> {
                if (sc_next != null) success = sc_next.makeSlippery();
            }
            // Make pipe sticky
            case "t" -> {
                if (mc_next != null) success = mc_next.makeSticky();
                if (sc_next != null) success = sc_next.makeSticky();
            }
            // Pass turn
            case "x" -> {
                if (mc_next != null) success = true;
                if (sc_next != null) success = true;
            }
        }

        return success;
    }

    private void createCharacters() {

        IO.writeLine("""

                ========== CREATING CHARACTERS ==========
                """);

        while (true) {
            // Print created characters
            IO.writeLine("Created mechanic characters: " + this.map.mechanicCharacters.size());
            IO.writeLine("Created saboteur characters: " + this.map.saboteurCharacters.size() + "\n");

            // Get character type
            String character_type = IO.readLine("Enter character type (m/s): ");
            while (!character_type.equals("m") && !character_type.equals("s")) {
                character_type = IO.readLine("Enter character type (m/s): ");
            }

            // Get character name
            String character_name = IO.readLine("Enter character name: ");

            // Create character
            if (character_type.equals("m")) {
                this.map.addMechanicCharacter(character_name);
            } else {
                this.map.addSaboteurCharacter(character_name);
            }

            // Check if there is least 2 mechanic characters and 2 saboteur character, if no --> continue
            if (this.map.mechanicCharacters.size() < 2 || this.map.saboteurCharacters.size() < 2) {
                IO.writeLine("\nYou need at least 2 mechanic characters and 2 saboteur characters.");
                continue;
            }

            // Check if you want to add another character
            String another_character = IO.readLine("\nAdd another character (y/n): ");
            while (!another_character.equals("y") && !another_character.equals("n")) {
                another_character = IO.readLine("Add another character (y/n): ");
            }

            // If not --> break
            if (another_character.equals("n")) break;

        }
    }

    private void calculatePoints() {
        // Calculate water in pumps
        AtomicInteger water_in_pumps = new AtomicInteger();
        this.map.pumps.forEach((name, pump) -> water_in_pumps.addAndGet(pump.getAcceptedWater() - pump.getPushedWater()));

        // Calculate pushed water from water sources
        AtomicInteger pushed_water_from_watersource = new AtomicInteger();
        this.map.waterSources.forEach((name, waterSource) -> pushed_water_from_watersource.addAndGet(waterSource.getPushedWater()));

        // Calculate water in cisterns
        AtomicInteger water_in_cisterns = new AtomicInteger();
        this.map.cisterns.forEach((name, cistern) -> water_in_cisterns.addAndGet(cistern.getAcceptedWater()));

        // Calculate points
        this.mc_points = water_in_cisterns.get();
        this.sc_points = pushed_water_from_watersource.get() - water_in_pumps.get() - water_in_cisterns.get();

    }

    private <T> T getCharacter(TreeMap<String, T> characters, int index) {
        Set<Map.Entry<String, T>> entrySet = characters.entrySet();
        Map.Entry<String, T>[] entryArray = entrySet.toArray(new Map.Entry[entrySet.size()]);
        return entryArray[index].getValue();
    }

    private void printPoints() {
        IO.writeLine("\n========== POINTS ==========");
        IO.writeLine("Mechanic: " + this.mc_points);
        IO.writeLine("Saboteur: " + this.sc_points);
    }

    private void printRound() {
        IO.write("\n\n");
        // Print round
        IO.writeLine("========== ROUND " + this.round + " ==========");
        // Print rounds left
        if (Settings.game_rounds() != -1) IO.writeLine("Rounds left: " + (Settings.game_rounds() - this.round));
        // Print next character
        IO.writeLine("Next character: " + this.getWhoseNext());
        IO.write("\n");
    }

    private void printMap() {
        IO.writeLine("========== MAP ==========");
        IO.writeLine(map);
    }

    private void printActionSuccess(boolean success, String command) {
        IO.writeLine("========== ACTION ==========");
        IO.writeLine("Command: " + command);
        IO.write("Result: ");
        if (mc_next != null) {
            IO.writeAction(success, mc_next, command);
        } else if (sc_next != null) {
            IO.writeAction(success, sc_next, command);
        }
    }

    private void printGameOver() {
        IO.writeLine("\n============ GAME OVER ============\n");
    }

}


