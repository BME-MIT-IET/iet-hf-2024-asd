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
            boolean success = runCommand(command.trim().replaceAll("\\s+", " "));

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

    public boolean runCommand(String line) {
        String[] parts = line.split(" ");
        String command = parts[0];
        boolean success = false;

        switch (command) {
            case "m" -> success = move(parts);
            case "i" -> success = setPumpInput(parts);
            case "o" -> success = setPumpOutput(parts);
            case "h" -> success = breakPipe();
            case "f" -> success = fix();
            case "p" -> success = pickupPipe(parts);
            case "n" -> success = pickupNewPipeFromCistern();
            case "b" -> success = pickupPumpFromCistern();
            case "a" -> success = attachPipeToPump();
            case "q" -> success = placePumpOnPipe();
            case "s" -> success = makePipeSlippery();
            case "t" -> success = makePipeSticky();
            case "x" -> success = true; // Pass turn
            default -> System.out.println("No action like this");
        }

        return success;
    }

    private boolean move(String[] parts) {
        if (parts.length < 2 || map.getField(parts[1]) == null) {
            IO.writeActionError("m", "Invalid parameter");
            return false;
        }

        if (mc_next != null) {
            return mc_next.move(map.getField(parts[1]));
        } else if (sc_next != null) {
            return sc_next.move(map.getField(parts[1]));
        }

        return false;
    }

    private boolean setPumpInput(String[] parts) {
        if (parts.length < 2 || !IO.isInteger(parts[1])) {
            IO.writeActionError("i", "Invalid parameter");
            return false;
        }

        if (mc_next != null) {
            return mc_next.setPumpInput(Integer.parseInt(parts[1]));
        } else if (sc_next != null) {
            return sc_next.setPumpInput(Integer.parseInt(parts[1]));
        }

        return false;
    }

    private boolean setPumpOutput(String[] parts) {
        if (parts.length < 2 || !IO.isInteger(parts[1])) {
            IO.writeActionError("o", "Invalid parameter");
            return false;
        }

        if (mc_next != null) {
            return mc_next.setPumpOut(Integer.parseInt(parts[1]));
        } else if (sc_next != null) {
            return sc_next.setPumpOut(Integer.parseInt(parts[1]));
        }

        return false;
    }

    private boolean breakPipe() {
        if (mc_next != null) {
            return mc_next.breakPipe();
        } else if (sc_next != null) {
            return sc_next.breakPipe();
        }

        return false;
    }

    private boolean fix() {
        if (mc_next != null) {
            return mc_next.fix();
        }

        return false;
    }

    private boolean pickupPipe(String[] parts) {
        if (parts.length < 2 || !IO.isInteger(parts[1])) {
            IO.writeActionError("p", "Invalid parameter");
            return false;
        }

        boolean success = false;
        if (mc_next != null) {
            success = mc_next.pickupPipe_c(Integer.parseInt(parts[1]));
        }
        if (sc_next != null) {
            success = sc_next.pickupPipe_c(Integer.parseInt(parts[1]));
        }
        return success;
    }

    private boolean pickupNewPipeFromCistern() {
        if (mc_next != null) {
            return mc_next.pickupNewPipe_mc();
        }

        return false;
    }

    private boolean pickupPumpFromCistern() {
        if (mc_next != null) {
            return mc_next.pickupPump();
        }

        return false;
    }

    private boolean attachPipeToPump() {
        boolean success = false;
        if (mc_next != null) {
            success = mc_next.attachPipeToPump();
        }
        if (sc_next != null) {
            success = sc_next.attachPipeToPump();
        }
        return success;
    }

    private boolean placePumpOnPipe() {
        if (mc_next != null) {
            return mc_next.placePumpToPipe();
        }

        return false;
    }

    private boolean makePipeSlippery() {
        if (sc_next != null) {
            return sc_next.makeSlippery();
        }

        return false;
    }

    private boolean makePipeSticky() {
        boolean success = false;
        if (mc_next != null) {
            success = mc_next.makeSticky();
        }
        if (sc_next != null) {
            success = sc_next.makeSticky();
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


