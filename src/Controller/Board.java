package Controller;

import Model.Character;
import Model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

/**
 * A pályát reprezentáló osztály
 * A pályán található összes objektumot tárolja
 * Getter / setter metódusokkal rendelkezik
 */
public class Board {

    public TreeMap<String, Point> coordinates = new TreeMap<>();

    /**
     * A pályán található mechanikus karakterek listája
     */
    public TreeMap<String, MechanicCharacter> mechanicCharacters = new TreeMap<>();
    /**
     * A pályán található saboteur karakterek listája
     */
    public TreeMap<String, SaboteurCharacter> saboteurCharacters = new TreeMap<>();
    /**
     * A pályán található csövek listája
     */
    public TreeMap<String, Pipe> pipes = new TreeMap<>();
    /**
     * A pályán található vízforrások listája
     */
    public TreeMap<String, WaterSource> waterSources = new TreeMap<>();
    /**
     * A pályán található pumpák listája
     */
    public TreeMap<String, Pump> pumps = new TreeMap<>();
    /**
     * A pályán található ciszternák listája
     */
    public TreeMap<String, Cistern> cisterns = new TreeMap<>();

    /**
     * Visszaadja az adott pályaelem koordinátáját, a neve alapján
     */
    public Point location(String s) {
        return coordinates.get(s);
    }

    /**
     * Visszaadja a pályán található Field-et a neve alapján
     *
     * @param fieldName - A keresett Field neve
     * @return - A keresett Field
     */
    public Field getField(String fieldName) {
        Field field = pumps.get(fieldName);
        if (field == null) field = cisterns.get(fieldName);
        if (field == null) field = pipes.get(fieldName);
        return field;
    }

    public ArrayList<Field> getFields() {
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(pumps.values());
        fields.addAll(cisterns.values());
        fields.addAll(pipes.values());
        return fields;
    }

    /**
     * Összerendezi a MechanicCharacter-eket és a SaboteurCharacter-eket és visszaadja a teljes Characters listát
     *
     * @return - A teljes Characters lista
     */
    public ArrayList<Character> getCharacters() {
        ArrayList<Character> characters = new ArrayList<>();
        characters.addAll(mechanicCharacters.values());
        characters.addAll(saboteurCharacters.values());
        return characters;
    }

    public Character getCharacter(int index) {
        return getCharacters().get(index);
    }

    /**
     * Visszaadja a pályán található random Field-et
     *
     * @param includePipes ha false a csöveket nem veszi figyelembe
     * @return - A random Field
     */
    private Field getRandomField(boolean includePipes) {
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(pumps.values());
        fields.addAll(cisterns.values());
        if (includePipes) fields.addAll(pipes.values());
        Random rand = new Random();
        return fields.get(rand.nextInt(fields.size()));
    }

    /**
     * Visszaadja a pályán található Attachable-t a neve alapján
     *
     * @param attachableName - A keresett Attachable neve
     * @return - A keresett Attachable
     */
    public Attachable getAttachable(String attachableName) {
        Attachable attachable = pumps.get(attachableName);
        if (attachable == null) attachable = cisterns.get(attachableName);
        if (attachable == null) attachable = waterSources.get(attachableName);
        return attachable;
    }

    /**
     * Hozzáad egy csövet a csövek listájához
     *
     * @param pipe - A hozzáadandó cső
     */
    public void addPipe(Pipe pipe) {
        pipes.put(pipe.getName(), pipe);
    }

    /**
     * Hozzáad egy pumpát a pumpák listájához
     *
     * @param pump - A hozzáadandó pumpa
     */
    public void addPump(Pump pump) {
        pumps.put(pump.getName(), pump);
    }

    /**
     * Hozzáad egy pumpát a pumpák listájához és a megadott cső közepére helyezi a koordinátorait
     *
     * @param pump     - A hozzáadandó pumpa
     * @param position - A cső középpontja amin a pumpa lesz
     */
    public void addPumpOnPipe(Pump pump, Point position) {
        // Add pump to list
        addPump(pump);
        // Add pump coordinate to center of pipe
        coordinates.put(pump.getName(), position);
    }

    /**
     * Létrehoz egy mechanikus karaktert a megadott névvel és hozzáadja a pályához egy random mezőre
     *
     * @param characterName - A karakter neve
     */
    public void addMechanicCharacter(String characterName) {
        // Create new mechanic character
        MechanicCharacter mechanicCharacter = new MechanicCharacter(characterName);

        // Add to list
        mechanicCharacters.put(characterName, mechanicCharacter);

        // Place mechanic character on a random field
        Field field = getRandomField(false);
        field.accept(mechanicCharacter);
    }

    /**
     * Létrehoz egy saboteur karaktert a megadott névvel és hozzáadja a pályához egy random mezőre
     *
     * @param characterName - A karakter neve
     */
    public void addSaboteurCharacter(String characterName) {
        // Create new saboteur character
        SaboteurCharacter saboteurCharacter = new SaboteurCharacter(characterName);

        // Add to list
        saboteurCharacters.put(characterName, saboteurCharacter);

        // Place saboteur character on a random field
        Field field = getRandomField(false);
        field.accept(saboteurCharacter);
    }

    /**
     * Létrehozza a pályát a bemeneti adatok alapján
     */
    public void createFromInput() {
        // Read all init lines from input
        ArrayList<String> lines = IO.readAllInit();

        // Create objects with names and states (states that are not other objects)
        for (String line : lines) {
            String[] parts = line.split(" ");
            String type = parts[0];
            String name = parts[1];
            String[] states = new String[0];
            if (parts.length > 3) {
                states = parts[3].split(",");
            }

            switch (type) {
                case "WaterSource" -> {
                    if (states.length == 1) {
                        // Check if states are valid types
                        if (!IO.isInteger(states[0]))
                            throw new IllegalArgumentException("WaterSource pushedWater must be an integer: " + line);
                        // Parse states
                        int pushedWater = Integer.parseInt(states[0]);
                        // Create water source
                        waterSources.put(name, new WaterSource(name, pushedWater));
                        coordinates.put(name, new Point(20, 50));
                    } else if (states.length == 0) {
                        waterSources.put(name, new WaterSource(name));
                        coordinates.put(name, new Point(20, 50));
                    } else {
                        throw new IllegalArgumentException("WaterSource must have 0 or 1 state: " + line);
                    }
                }
                case "Pump" -> {
                    if (states.length == 5) {
                        // Check if states are valid types
                        if (!IO.isBoolean(states[0]) || !IO.isInteger(states[1]) || !IO.isInteger(states[2]))
                            throw new IllegalArgumentException("Pump working must be a boolean, pushedWater and acceptedWater must be an integer: " + line);
                        // Parse states
                        boolean working = Boolean.parseBoolean(states[0]);
                        int pushedWater = Integer.parseInt(states[1]);
                        int acceptedWater = Integer.parseInt(states[2]);
                        // Create pump
                        pumps.put(name, new Pump(name, this, working, pushedWater, acceptedWater));
                        coordinates.put(name, new Point(300, 10));
                    } else if (states.length == 0) {
                        pumps.put(name, new Pump(name, this));
                        coordinates.put(name, new Point(200, 10));
                    } else {
                        throw new IllegalArgumentException("Pump must have 0 or 5 states: " + line);
                    }
                }
                case "Pipe" -> {
                    if (states.length == 3) {
                        // Check if states are valid types
                        if (!IO.isBoolean(states[0]) || !IO.isBoolean(states[1]) || !IO.isBoolean(states[2]))
                            throw new IllegalArgumentException("Pipe holey, slippery and sticky must be a boolean: " + line);
                        // Parse states
                        boolean holey = Boolean.parseBoolean(states[0]);
                        int slippery = Boolean.parseBoolean(states[1]) ? Settings.pipe_slippery() : 0;
                        int sticky = Boolean.parseBoolean(states[2]) ? Settings.pipe_sticky() : 0;
                        // Create pipe
                        pipes.put(name, new Pipe(name, holey, slippery, sticky));
                    } else if (states.length == 0) {
                        pipes.put(name, new Pipe(name));
                    } else {
                        throw new IllegalArgumentException("Pipe must have 0 or 3 states: " + line);
                    }
                }
                case "Cistern" -> {
                    if (states.length == 3) {
                        // Check if states are valid types
                        if (!IO.isInteger(states[0]))
                            throw new IllegalArgumentException("Cistern waterAccepted must be an integer: " + line);
                        // Parse states
                        int waterAccepted = Integer.parseInt(states[0]);
                        String newPipeName = states[1];
                        String newPumpName = states[2];

                        // Create cistern
                        cisterns.put(name, new Cistern(name, this, waterAccepted, newPipeName, newPumpName));
                        coordinates.put(name, new Point(300, 50));
                    } else if (states.length == 0) {
                        cisterns.put(name, new Cistern(name, this));
                        coordinates.put(name, new Point(100, 50));
                    } else {
                        throw new IllegalArgumentException("Cistern must have 0 or 3 states: " + line);
                    }
                }
                case "MechanicCharacter" -> mechanicCharacters.put(name, new MechanicCharacter(name));
                case "SaboteurCharacter" -> saboteurCharacters.put(name, new SaboteurCharacter(name));
            }
        }

        // Set objects connections and states
        for (String line : lines) {
            String[] parts = line.split(" ");
            String type = parts[0];
            String name = parts[1];

            switch (type) {
                case "WaterSource" -> {
                    // Attach pipes
                    if (parts.length >= 3) {
                        WaterSource waterSource = waterSources.get(name);
                        String[] pipes_names = parts[2].split(",");
                        for (String pipe_name : pipes_names) {
                            if (pipe_name.equals("null") || pipe_name.equals("-")) continue;
                            waterSource.attachPipe(pipes.get(pipe_name));
                        }
                    }
                }
                case "Pump" -> {
                    // Find pump from list
                    Pump pump = pumps.get(name);
                    // Attach pipes
                    if (parts.length >= 3) {
                        String[] pipes_names = parts[2].split(",");
                        for (String pipe_name : pipes_names) {
                            if (pipe_name.equals("null") || pipe_name.equals("-")) continue;
                            pump.attachPipe(pipes.get(pipe_name));
                        }
                    }
                    // Set states (output pipe, input pipe)
                    if (parts.length >= 4) {
                        String[] states = parts[3].split(",");
                        String outputPipeName = states[3];
                        String inputPipeName = states[4];
                        ArrayList<Pipe> pipes = pump.getPipes();
                        for (int i = 0; i < pipes.size(); i++) {
                            if (pipes.get(i).getName().equals(outputPipeName)) {
                                pump.setOutput(i + 1);
                            }
                            if (pipes.get(i).getName().equals(inputPipeName)) {
                                pump.setInput(i + 1);
                            }
                        }
                    }
                }
                case "Pipe" -> {
                    // Find pipe from list
                    Pipe pipe = pipes.get(name);
                    // Attach pumps
                    if (parts.length >= 3) {
                        String[] attachable_names = parts[2].split(",");
                        if (attachable_names.length > 2)
                            throw new RuntimeException("Pipe can't be attached to more than 2 attachable");
                        for (String attachable_name : attachable_names) {
                            if (attachable_name.equals("null") || attachable_name.equals("-")) continue;
                            // Find attachable from list
                            Attachable found_attachable = this.getAttachable(attachable_name);
                            if (found_attachable == null)
                                throw new RuntimeException("Attachable not found: " + attachable_name);
                            pipe.attached(found_attachable);
                        }
                    }
                }
                case "Cistern" -> {
                    // Attach pipes
                    if (parts.length >= 3) {
                        Cistern cistern = cisterns.get(name);
                        String[] pipes_names = parts[2].split(",");
                        for (String pipe_name : pipes_names) {
                            if (pipe_name.equals("null") || pipe_name.equals("-")) continue;
                            cistern.attachPipe(pipes.get(pipe_name));
                        }
                    }
                }
                case "MechanicCharacter" -> {
                    MechanicCharacter mechanicCharacter = mechanicCharacters.get(name);
                    // Place mechanic character on field
                    if (parts.length >= 3) {
                        String fieldName = parts[2];
                        if (fieldName.equals("null") || fieldName.equals("-")) continue;
                        Field field = this.getField(fieldName);
                        field.accept(mechanicCharacter);
                    }
                    // Set states (picked pipe, picked pump)
                    if (parts.length >= 4) {
                        String[] states = parts[3].split(",");
                        String pickedPipeName = states[0];
                        String pickedPumpName = states[1];
                        if (!pickedPipeName.equals("null") && !pickedPipeName.equals("-")) {
                            Pipe pickedPipe = pipes.get(pickedPipeName);
                            mechanicCharacter.receivePipe(pickedPipe);
                        }
                        if (!pickedPumpName.equals("null") && !pickedPumpName.equals("-")) {
                            Pump pickedPump = pumps.get(pickedPumpName) == null ? new Pump(pickedPumpName, this) : pumps.get(pickedPumpName);
                            mechanicCharacter.receivePump(pickedPump);
                        }
                    }
                }
                case "SaboteurCharacter" -> {
                    SaboteurCharacter saboteurCharacter = saboteurCharacters.get(name);
                    // Place saboteur character on field
                    if (parts.length >= 3) {
                        String fieldName = parts[2];
                        if (fieldName.equals("null") || fieldName.equals("-")) continue;
                        Field field = this.getField(fieldName);
                        field.accept(saboteurCharacter);
                    }
                    // Set states (picked pipe)
                    if (parts.length >= 4) {
                        String[] states = parts[3].split(",");
                        String pickedPipeName = states[0];
                        if (!pickedPipeName.equals("null") && !pickedPipeName.equals("-")) {
                            Pipe pickedPipe = pipes.get(pickedPipeName);
                            saboteurCharacter.receivePipe(pickedPipe);
                        }
                    }
                }
            }

        }

    }

    /**
     * Létrehoz egy alapértelmezett pályát.
     */
    public void createDefaultMap() {
        // Water sources
        waterSources.put("ws1", new WaterSource("ws1", 0));
        coordinates.put("ws1", new Point(Settings.board_circles_radius(), 200));

        // Cisterns
        cisterns.put("ci1", new Cistern("ci1", this));
        coordinates.put("ci1", new Point(Settings.window_width - Settings.board_circles_radius(), 200));

        // Pumps
        pumps.put("pu1", new Pump("pu1", null));
        coordinates.put("pu1", new Point(Settings.window_width / 2, Settings.window_height / 6));
        pumps.put("pu2", new Pump("pu2", null));
        coordinates.put("pu2", new Point(Settings.window_width / 3, Settings.window_height / 2));
        pumps.put("pu3", new Pump("pu3", null));
        coordinates.put("pu3", new Point(Settings.window_width / 2, (int) (Settings.window_height / 1.5)));

        // Pipes
        pipes.put("pi1", new Pipe("pi1"));
        pipes.put("pi2", new Pipe("pi2"));
        pipes.put("pi3", new Pipe("pi3"));
        pipes.put("pi4", new Pipe("pi4"));
        pipes.put("pi5", new Pipe("pi5"));
        pipes.put("pi6", new Pipe("pi6"));

        // Connect water source with pumps
        waterSources.get("ws1").attachPipe(pipes.get("pi1"));
        pumps.get("pu1").attachPipe(pipes.get("pi1"));
        waterSources.get("ws1").attachPipe(pipes.get("pi2"));
        pumps.get("pu2").attachPipe(pipes.get("pi2"));

        // Connect pump with cistern
        pumps.get("pu1").attachPipe(pipes.get("pi3"));
        cisterns.get("ci1").attachPipe(pipes.get("pi3"));
        pumps.get("pu3").attachPipe(pipes.get("pi6"));
        cisterns.get("ci1").attachPipe(pipes.get("pi6"));

        // Connect 2 pumps
        pumps.get("pu1").attachPipe(pipes.get("pi4"));
        pumps.get("pu2").attachPipe(pipes.get("pi4"));
        pumps.get("pu2").attachPipe(pipes.get("pi5"));
        pumps.get("pu3").attachPipe(pipes.get("pi5"));

        // Set pumps input and output
        pumps.get("pu1").setInput(1);
        pumps.get("pu1").setOutput(2);
        pumps.get("pu2").setInput(1);
    }

    public Point randomCoordinateNear(Point near) {
        Random random = new Random();
        int bias = Settings.window_height / 15;
        int x = near.x + random.nextInt(bias * 2) - bias;
        int y = near.y + random.nextInt(bias * 2) - bias;
        return new Point(x, y);
    }

    public void clearFields() {
        waterSources.clear();
        cisterns.clear();
        pumps.clear();
        pipes.clear();
        coordinates.clear();
    }

    public void createRandomMap() {
        // Clear fields
        clearFields();

        Random random = new Random();
        int columnWidth = (Settings.window_width - 50) / 4;

        // First column - Water sources
        int waterSourcesCount = random.nextInt(2) + 1;
        int columnOneY = Settings.window_height / waterSourcesCount;

        for (int i = 0; i < waterSourcesCount; i++) {
            String wsKey = "ws" + (i + 1);
            waterSources.put(wsKey, new WaterSource(wsKey, 0));
            Point pos = new Point(60, (int) ((i + 0.5) * columnOneY));
            coordinates.put(wsKey, pos);
        }

        // Second column - Pumps
        int pumpsCountColumnTwo = random.nextInt(3) + 1;
        int columnTwoY = Settings.window_height / pumpsCountColumnTwo;

        for (int i = 0; i < pumpsCountColumnTwo; i++) {
            String pumpKey = "pu" + (i + 1);
            pumps.put(pumpKey, new Pump(pumpKey, null));
            Point pos = randomCoordinateNear(new Point(columnWidth + 60, (int) ((i + 0.5) * columnTwoY)));
            coordinates.put(pumpKey, pos);
        }

        // Third column - Pumps
        int pumpsCountColumnThree = random.nextInt(3) + 1;
        int columnThreeY = Settings.window_height / pumpsCountColumnThree;

        for (int i = 0; i < pumpsCountColumnThree; i++) {
            String pumpKey = "pu" + (i + 1 + pumpsCountColumnTwo);
            pumps.put(pumpKey, new Pump(pumpKey, null));
            Point pos = randomCoordinateNear(new Point(columnWidth * 2 + 60, (int) ((i + 0.5) * columnThreeY)));
            coordinates.put(pumpKey, pos);
        }

        // Fourth column - Cisterns
        int cisternsCount = random.nextInt(2) + 1;
        int columnFourY = Settings.window_height / cisternsCount;

        for (int i = 0; i < cisternsCount; i++) {
            String cisternKey = "ci" + (i + 1);
            cisterns.put(cisternKey, new Cistern(cisternKey, this));
            Point pos = new Point(columnWidth * 3 + 60, (int) ((i + 0.5) * columnFourY));
            coordinates.put(cisternKey, pos);
        }

        // Connect watersources with second column pumps
        for (int i = 0; i < waterSourcesCount; i++) {
            for (int j = 0; j < pumpsCountColumnTwo; j++) {
                if (random.nextBoolean() || i == j) {
                    String wsKey = "ws" + (i + 1);
                    String pumpKey = "pu" + (j + 1);
                    String pipeKey = "pi" + (i * pumpsCountColumnTwo + j + 1);
                    pipes.put(pipeKey, new Pipe(pipeKey));
                    waterSources.get(wsKey).attachPipe(pipes.get(pipeKey));
                    pumps.get(pumpKey).attachPipe(pipes.get(pipeKey));
                    pumps.get(pumpKey).setInput(pipes.get(pipeKey));
                }
            }
        }

        // Connect second column pumps with third column pumps
        for (int i = 0; i < pumpsCountColumnTwo; i++) {
            for (int j = 0; j < pumpsCountColumnThree; j++) {
                if (random.nextBoolean() || i == j) {
                    String pumpKey1 = "pu" + (i + 1);
                    String pumpKey2 = "pu" + (j + 1 + pumpsCountColumnTwo);
                    String pipeKey = "pi" + (i * pumpsCountColumnThree + j + 1 + pumpsCountColumnTwo * waterSourcesCount);
                    pipes.put(pipeKey, new Pipe(pipeKey));
                    pumps.get(pumpKey1).attachPipe(pipes.get(pipeKey));
                    pumps.get(pumpKey2).attachPipe(pipes.get(pipeKey));
                    pumps.get(pumpKey1).setOutput(pipes.get(pipeKey));
                    pumps.get(pumpKey2).setInput(pipes.get(pipeKey));
                }
            }
        }

        // Connect third column pumps with fourth column cisterns
        for (int i = 0; i < pumpsCountColumnThree; i++) {
            for (int j = 0; j < cisternsCount; j++) {
                if (random.nextBoolean() || i == j) {
                    String pumpKey = "pu" + (i + 1 + pumpsCountColumnTwo);
                    String cisternKey = "ci" + (j + 1);
                    String pipeKey = "pi" + (i * cisternsCount + j + 1 + pumpsCountColumnTwo * waterSourcesCount + pumpsCountColumnThree * pumpsCountColumnTwo);
                    pipes.put(pipeKey, new Pipe(pipeKey));
                    pumps.get(pumpKey).attachPipe(pipes.get(pipeKey));
                    cisterns.get(cisternKey).attachPipe(pipes.get(pipeKey));
                    pumps.get(pumpKey).setOutput(pipes.get(pipeKey));
                }
            }
        }

        // Check if all water sources has at least one connection
        for (WaterSource waterSource : waterSources.values()) {
            if (waterSource.getPipes().size() == 0) {
                // Connect to random pump
                String pumpKey = "pu" + (random.nextInt(pumpsCountColumnTwo) + 1);
                String pipeKey = "pi" + (pipes.size() + 1);
                pipes.put(pipeKey, new Pipe(pipeKey));
                waterSource.attachPipe(pipes.get(pipeKey));
                pumps.get(pumpKey).attachPipe(pipes.get(pipeKey));
            }
        }


        // Check if all pumps are connected
        for (Pump pump : pumps.values()) {
            if (pump.getPipes().size() == 0) {
                // Connect to random pump
                String pumpKey = "pu" + (random.nextInt(pumpsCountColumnThree) + 1 + pumpsCountColumnTwo);
                String pipeKey = "pi" + (pipes.size() + 1);
                pipes.put(pipeKey, new Pipe(pipeKey));
                pump.attachPipe(pipes.get(pipeKey));
                pumps.get(pumpKey).attachPipe(pipes.get(pipeKey));
            }
        }

        // Check if all cisterns has at least one connection
        for (Cistern cistern : cisterns.values()) {
            if (cistern.getPipes().size() == 0) {
                // Connect to random pump
                String pumpKey = "pu" + (random.nextInt(pumpsCountColumnThree) + 1 + pumpsCountColumnTwo);
                String pipeKey = "pi" + (pipes.size() + 1);
                pipes.put(pipeKey, new Pipe(pipeKey));
                cistern.attachPipe(pipes.get(pipeKey));
                pumps.get(pumpKey).attachPipe(pipes.get(pipeKey));
            }
        }

    }

    /**
     * Find the closest field to the given coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the closest field
     */
    public Field findClosestField(int x, int y) throws Exception {
        Field closestField = null;
        double closestDistance = Double.MAX_VALUE;
        ArrayList<Field> fields = getFields();
        // loop through all fields
        for (Field field : fields) {
            Point fieldCoordinate = getFieldCoordinate(field);
            if (fieldCoordinate == null) throw new Exception("Could not calculate field coordinate");
            // Calculate distance
            double distance = Math.sqrt(Math.pow(fieldCoordinate.x - x, 2) + Math.pow(fieldCoordinate.y - y, 2));
            // If distance is smaller than the closest distance
            if (distance < closestDistance) {
                // Set closest distance
                closestDistance = distance;
                // Set closest field
                closestField = field;
            }
        }
        return closestField;
    }

    /**
     * Finde the closest pipe to the given coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return the closest pipe
     */
    public Pipe findClosestPipe(int x, int y) throws Exception {
        Pipe closestPipe = null;
        double closestDistance = Double.MAX_VALUE;

        // loop through all pipes
        for (Pipe pipe : pipes.values()) {
            // Calculate the pipe middle coordinate
            Point pipeCoordinate;
            pipeCoordinate = calculatePipeCenter(pipe);
            if (pipeCoordinate == null) throw new Exception("Could not calculate pipe coordinate");

            // Calculate distance
            double distance = Math.sqrt(Math.pow(pipeCoordinate.x - x, 2) + Math.pow(pipeCoordinate.y - y, 2));
            // If distance is smaller than the closest distance
            if (distance < closestDistance) {
                // Set closest distance
                closestDistance = distance;
                // Set closest pipe
                closestPipe = pipe;
            }
        }

        return closestPipe;
    }

    /**
     * Calculate the coordinate of the given pipe
     *
     * @param pipe the given pipe
     * @return the coordinate of the pipe
     * @throws Exception if could not calculate the coordinate of the pipe
     */
    public Point calculatePipeCenter(Pipe pipe) throws Exception {
        Point pipeCoordinate = new Point();
        // If pipe is attached to 2 attachables
        if (pipe.getAttachables().size() == 2 && pipe.getAttachables().get(0) != null && pipe.getAttachables().get(1) != null) {
            // Calculate the middle point of the 2 attachables
            Point attachable1Coordinate = coordinates.get(pipe.getAttachables().get(0).getName());
            Point attachable2Coordinate = coordinates.get(pipe.getAttachables().get(1).getName());
            if (attachable1Coordinate == null || attachable2Coordinate == null)
                throw new Exception("One of the attachable coordinates is null but it shouldn't be!");
            pipeCoordinate.x = (attachable1Coordinate.x + attachable2Coordinate.x) / 2;
            pipeCoordinate.y = (attachable1Coordinate.y + attachable2Coordinate.y) / 2;
            return pipeCoordinate;
        }

        // Find the character who holds the other end of the pipe
        Character founded_character1 = null;
        ArrayList<Character> characters = getCharacters();
        for (Character character : characters) {
            if (character.getPickedPipe() == pipe) {
                founded_character1 = character;
                break;
            }
        }

        // If pipe is attached to 1 attachable and the other end is in 1 character hands
        if (pipe.getAttachables().size() == 1 && pipe.getAttachables().get(0) != null && founded_character1 != null) {
            // Calculate the middle point of the attachable and the character
            Point attachableCoordinate = coordinates.get(pipe.getAttachables().get(0).getName());
            Point characterCoordinate = getFieldCoordinate(founded_character1.getField());
            if (attachableCoordinate == null || characterCoordinate == null)
                throw new Exception("Attachable coordinate or the character coordinate is null!");

            // Calculate the middle point of the pipe
            pipeCoordinate.x = (attachableCoordinate.x + characterCoordinate.x) / 2;
            pipeCoordinate.y = (attachableCoordinate.y + characterCoordinate.y) / 2;
            return pipeCoordinate;
        }

        // Find the second character who holds the other end of the pipe
        Character founded_character2 = null;
        for (Character character : characters) {
            if (character.getPickedPipe() == pipe && character != founded_character1) {
                founded_character2 = character;
                break;
            }
        }

        // If pipe is in 2 characters hands
        if (founded_character1 != null && founded_character2 != null) {
            // Calculate the middle point of the 2 characters
            Point character1Coordinate = getFieldCoordinate(founded_character1.getField());
            Point character2Coordinate = getFieldCoordinate(founded_character2.getField());
            if (character1Coordinate == null || character2Coordinate == null)
                throw new Exception("One of the character coordinates is null but it shouldn't be!");
            pipeCoordinate.x = (character1Coordinate.x + character2Coordinate.x) / 2;
            pipeCoordinate.y = (character1Coordinate.y + character2Coordinate.y) / 2;
            return pipeCoordinate;
        }

        // Throw exception if pipe is not attached to 2 attachables or 1 attachable and 1 character or 2 characters
        throw new Exception("Pipe is not attached to 2 attachables or 1 attachable and 1 character or 2 characters!");
    }

    public ArrayList<Point> calculatePipeEnds(Pipe pipe) throws Exception {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);

        // Get attachables
        ArrayList<Attachable> attachables = pipe.getAttachables();

        //ha sima cso
        if (attachables.size() == 2 && attachables.get(0) != null && attachables.get(1) != null) {
            p1 = location(attachables.get(0).getName());
            p2 = location(attachables.get(1).getName());
        }
        //ha fel van veve az egyik vege
        else if (attachables.size() == 1 && attachables.get(0) != null) {
            p1 = location(attachables.get(0).getName());
            for (Character character : getCharacters()) {
                Pipe pickedPipe = character.getPickedPipe();
                if (pickedPipe != null && pickedPipe.equals(pipe)) {
                    p2 = getCharacterCoordinate(character);
                    p2 = new Point(p2.x, (int) (p2.y - Settings.board_character_height() / 1.5));
                }
            }
        }
        //ha mind2 vege fel van veve
        else if (pipe.getAttachables().size() == 0) {
            boolean p1_found = false;
            for (Character character : getCharacters()) {
                Pipe pickedPipe = character.getPickedPipe();
                if (pickedPipe == null) continue;
                if (pickedPipe.equals(pipe)) {
                    if (!p1_found) {
                        p1 = getCharacterCoordinate(character);
                        p1_found = true;
                    } else {
                        p2 = getCharacterCoordinate(character);
                    }
                }
            }
        } else {
            throw new Exception("Pipe is not attached to 2 attachables or 1 attachable and 1 character or 2 characters!");
        }

        // Return the coordinates of the pipe ends
        ArrayList<Point> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        return points;

    }

    /**
     * Get the coordinate of the given field
     *
     * @param field the field
     * @return the coordinate of the field
     * @throws Exception if the field is not in the coordinates map or could not calculate the pipe coordinate
     */
    public Point getFieldCoordinate(Field field) throws Exception {
        // Check if field name first two characters are "pi" (pipe)
        if (field.getName().length() >= 2 && field.getName().startsWith("pi")) {
            return calculatePipeCenter((Pipe) field);
        }

        // Return the coordinate of the field from the coordinates map
        return coordinates.get(field.getName());
    }

    public Point getCharacterCoordinate(Character character) throws Exception {
        return (Point) getFieldCoordinate(character.getField()).clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.toString(), o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mechanicCharacters, saboteurCharacters, waterSources, pumps, pipes, cisterns);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        waterSources.forEach((name, waterSource) -> result.append(waterSource).append("\n"));
        cisterns.forEach((name, cistern) -> result.append(cistern).append("\n"));
        pumps.forEach((name, pump) -> result.append(pump).append("\n"));
        pipes.forEach((name, pipe) -> result.append(pipe).append("\n"));
        mechanicCharacters.forEach((name, mechanicCharacter) -> result.append(mechanicCharacter).append("\n"));
        saboteurCharacters.forEach((name, saboteurCharacter) -> result.append(saboteurCharacter).append("\n"));
        return result.toString();
    }

    public void placeCharactersRandom() {
        ArrayList<Character> characters = getCharacters();
        for (Character character : characters) {

            Field randomField = getRandomField(true);
            boolean isPlaced = character.move(randomField);

            // Place in not a pipe
            if (!isPlaced) {
                Field randomFieldNotPipe = getRandomField(false);
                character.move(randomFieldNotPipe);
            }
        }
    }
}
