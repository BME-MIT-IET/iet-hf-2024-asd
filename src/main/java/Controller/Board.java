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

    Random random = new Random();

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
        return fields.get(random.nextInt(fields.size()));
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
        ArrayList<String> lines = IO.readAllInit();

        // Create objects with names and states (states that are not other objects)
        for (String line : lines) {
            String[] parts = line.split(" ");
            String type = parts[0];
            String name = parts[1];
            String[] states = (parts.length > 3) ? parts[3].split(",") : new String[0];

            createObject(type, name, states);
        }

        // Set objects connections and states
        for (String line : lines) {
            String[] parts = line.split(" ");
            String type = parts[0];
            String name = parts[1];

            setConnectionsAndStates(type, name, parts);
        }
    }

    private void createObject(String type, String name, String[] states) {
        switch (type) {
            case "WaterSource" -> createWaterSource(name, states);
            case "Pump" -> createPump(name, states);
            case "Pipe" -> createPipe(name, states);
            case "Cistern" -> createCistern(name, states);
            case "MechanicCharacter" -> mechanicCharacters.put(name, new MechanicCharacter(name));
            case "SaboteurCharacter" -> saboteurCharacters.put(name, new SaboteurCharacter(name));
            default -> System.out.println("Unexpected type: " + type);
        }
    }

    private void createWaterSource(String name, String[] states) {
        if (states.length == 1) {
            if (!IO.isInteger(states[0])) throw new IllegalArgumentException("WaterSource pushedWater must be an integer: " + name);
            int pushedWater = Integer.parseInt(states[0]);
            waterSources.put(name, new WaterSource(name, pushedWater));
        } else if (states.length == 0) {
            waterSources.put(name, new WaterSource(name));
        } else {
            throw new IllegalArgumentException("WaterSource must have 0 or 1 state: " + name);
        }
        coordinates.put(name, new Point(20, 50));
    }

    private void createPump(String name, String[] states) {
        if (states.length == 5) {
            if (!IO.isBoolean(states[0]) || !IO.isInteger(states[1]) || !IO.isInteger(states[2]))
                throw new IllegalArgumentException("Pump working must be a boolean, pushedWater and acceptedWater must be an integer: " + name);
            boolean working = Boolean.parseBoolean(states[0]);
            int pushedWater = Integer.parseInt(states[1]);
            int acceptedWater = Integer.parseInt(states[2]);
            pumps.put(name, new Pump(name, this, working, pushedWater, acceptedWater));
            coordinates.put(name, new Point(300, 10));
        } else if (states.length == 0) {
            pumps.put(name, new Pump(name, this));
            coordinates.put(name, new Point(200, 10));
        } else {
            throw new IllegalArgumentException("Pump must have 0 or 5 states: " + name);
        }
    }

    private void createPipe(String name, String[] states) {
        if (states.length == 3) {
            if (!IO.isBoolean(states[0]) || !IO.isBoolean(states[1]) || !IO.isBoolean(states[2]))
                throw new IllegalArgumentException("Pipe holey, slippery and sticky must be a boolean: " + name);
            boolean holey = Boolean.parseBoolean(states[0]);
            int slippery = Boolean.parseBoolean(states[1]) ? Settings.pipe_slippery() : 0;
            int sticky = Boolean.parseBoolean(states[2]) ? Settings.pipe_sticky() : 0;
            pipes.put(name, new Pipe(name, holey, slippery, sticky));
        } else if (states.length == 0) {
            pipes.put(name, new Pipe(name));
        } else {
            throw new IllegalArgumentException("Pipe must have 0 or 3 states: " + name);
        }
    }

    private void createCistern(String name, String[] states) {
        if (states.length == 3) {
            if (!IO.isInteger(states[0])) throw new IllegalArgumentException("Cistern waterAccepted must be an integer: " + name);
            int waterAccepted = Integer.parseInt(states[0]);
            String newPipeName = states[1];
            String newPumpName = states[2];
            cisterns.put(name, new Cistern(name, this, waterAccepted, newPipeName, newPumpName));
            coordinates.put(name, new Point(300, 50));
        } else if (states.length == 0) {
            cisterns.put(name, new Cistern(name, this));
            coordinates.put(name, new Point(100, 50));
        } else {
            throw new IllegalArgumentException("Cistern must have 0 or 3 states: " + name);
        }
    }

    private void setConnectionsAndStates(String type, String name, String[] parts) {
        switch (type) {
            case "WaterSource" -> setWaterSourceConnections(name, parts);
            case "Pump" -> setPumpConnectionsAndStates(name, parts);
            case "Pipe" -> setPipeConnections(name, parts);
            case "Cistern" -> setCisternConnections(name, parts);
            case "MechanicCharacter" -> setMechanicCharacterStates(name, parts);
            case "SaboteurCharacter" -> setSaboteurCharacterStates(name, parts);
            default -> System.out.println("Unexpected type: " + type);
        }
    }

    private void setWaterSourceConnections(String name, String[] parts) {
        if (parts.length >= 3) {
            WaterSource waterSource = waterSources.get(name);
            String[] pipeNames = parts[2].split(",");
            for (String pipeName : pipeNames) {
                if (!pipeName.equals("null") && !pipeName.equals("-")) {
                    waterSource.attachPipe(pipes.get(pipeName));
                }
            }
        }
    }

    private void setPumpConnectionsAndStates(String name, String[] parts) {
        Pump pump = pumps.get(name);
        if (parts.length >= 3) {
            setPumpConnections(pump, parts[2]);
        }
        if (parts.length >= 4) {
            setPumpStates(pump, parts[3]);
        }
    }

    private void setPumpConnections(Pump pump, String pipeNamesStr) {
        String[] pipeNames = pipeNamesStr.split(",");
        for (String pipeName : pipeNames) {
            if (!pipeName.equals("null") && !pipeName.equals("-")) {
                pump.attachPipe(pipes.get(pipeName));
            }
        }
    }

    private void setPumpStates(Pump pump, String statesStr) {
        String[] states = statesStr.split(",");
        if (states.length >= 5) {
            String outputPipeName = states[3];
            String inputPipeName = states[4];
            ArrayList<Pipe> pumpPipes = pump.getPipes();
            for (int i = 0; i < pumpPipes.size(); i++) {
                if (pumpPipes.get(i).getName().equals(outputPipeName)) {
                    pump.setOutput(i + 1);
                }
                if (pumpPipes.get(i).getName().equals(inputPipeName)) {
                    pump.setInput(i + 1);
                }
            }
        }
    }


    private void setPipeConnections(String name, String[] parts) {
        Pipe pipe = pipes.get(name);
        if (parts.length >= 3) {
            String[] attachableNames = parts[2].split(",");
            if (attachableNames.length > 2) throw new RuntimeException("Pipe can't be attached to more than 2 attachables");
            for (String attachableName : attachableNames) {
                if (!attachableName.equals("null") && !attachableName.equals("-")) {
                    Attachable attachable = getAttachable(attachableName);
                    if (attachable == null) throw new RuntimeException("Attachable not found: " + attachableName);
                    pipe.attached(attachable);
                }
            }
        }
    }

    private void setCisternConnections(String name, String[] parts) {
        if (parts.length >= 3) {
            Cistern cistern = cisterns.get(name);
            String[] pipeNames = parts[2].split(",");
            for (String pipeName : pipeNames) {
                if (!pipeName.equals("null") && !pipeName.equals("-")) {
                    cistern.attachPipe(pipes.get(pipeName));
                }
            }
        }
    }

    private void setMechanicCharacterStates(String name, String[] parts) {
        MechanicCharacter mechanicCharacter = mechanicCharacters.get(name);
        if (parts.length >= 3) {
            String fieldName = parts[2];
            if (!fieldName.equals("null") && !fieldName.equals("-")) {
                Field field = getField(fieldName);
                field.accept(mechanicCharacter);
            }
        }
        if (parts.length >= 4) {
            String[] states = parts[3].split(",");
            String pickedPipeName = states[0];
            String pickedPumpName = states[1];
            if (!pickedPipeName.equals("null") && !pickedPipeName.equals("-")) {
                Pipe pickedPipe = pipes.get(pickedPipeName);
                mechanicCharacter.receivePipe(pickedPipe);
            }
            if (!pickedPumpName.equals("null") && !pickedPumpName.equals("-")) {
                Pump pickedPump = pumps.getOrDefault(pickedPumpName, new Pump(pickedPumpName, this));
                mechanicCharacter.receivePump(pickedPump);
            }
        }
    }

    private void setSaboteurCharacterStates(String name, String[] parts) {
        SaboteurCharacter saboteurCharacter = saboteurCharacters.get(name);
        if (parts.length >= 3) {
            String fieldName = parts[2];
            if (!fieldName.equals("null") && !fieldName.equals("-")) {
                Field field = getField(fieldName);
                field.accept(saboteurCharacter);
            }
        }
        if (parts.length >= 4) {
            String[] states = parts[3].split(",");
            String pickedPipeName = states[0];
            if (!pickedPipeName.equals("null") && !pickedPipeName.equals("-")) {
                Pipe pickedPipe = pipes.get(pickedPipeName);
                saboteurCharacter.receivePipe(pickedPipe);
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
        coordinates.put("ci1", new Point(Settings.WINDOW_WIDTH - Settings.board_circles_radius(), 200));

        // Pumps
        pumps.put("pu1", new Pump("pu1", null));
        coordinates.put("pu1", new Point(Settings.WINDOW_WIDTH / 2, Settings.WINDOW_HEIGHT / 6));
        pumps.put("pu2", new Pump("pu2", null));
        coordinates.put("pu2", new Point(Settings.WINDOW_WIDTH / 3, Settings.WINDOW_HEIGHT / 2));
        pumps.put("pu3", new Pump("pu3", null));
        coordinates.put("pu3", new Point(Settings.WINDOW_WIDTH / 2, (int) (Settings.WINDOW_HEIGHT / 1.5)));

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
        int bias = Settings.WINDOW_HEIGHT / 15;
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
        clearFields();

        int columnWidth = (Settings.WINDOW_WIDTH - 50) / 4;

        createWaterSources();
        int pumpsCountColumnTwo = createPumpsInColumn(columnWidth, 1);
        int pumpsCountColumnThree = createPumpsInColumn(columnWidth, 2);
        createCisterns(columnWidth);

        connectWaterSourcesToPumps(pumpsCountColumnTwo);
        connectPumpsToPumps(pumpsCountColumnTwo, pumpsCountColumnThree);
        connectPumpsToCisterns(pumpsCountColumnThree);

        ensureAllWaterSourcesConnected(pumpsCountColumnTwo);
        ensureAllPumpsConnected(pumpsCountColumnTwo, pumpsCountColumnThree);
        ensureAllCisternsConnected(pumpsCountColumnThree);
    }

    private void createWaterSources() {
        int waterSourcesCount = random.nextInt(2) + 1;
        int columnOneY = Settings.WINDOW_HEIGHT / waterSourcesCount;

        for (int i = 0; i < waterSourcesCount; i++) {
            String wsKey = "ws" + (i + 1);
            waterSources.put(wsKey, new WaterSource(wsKey, 0));
            Point pos = new Point(60, (int) ((i + 0.5) * columnOneY));
            coordinates.put(wsKey, pos);
        }
    }

    private int createPumpsInColumn(int columnWidth, int columnIndex) {
        int pumpsCount = random.nextInt(3) + 1;
        int columnY = Settings.WINDOW_HEIGHT / pumpsCount;

        for (int i = 0; i < pumpsCount; i++) {
            String pumpKey = "pu" + (i + 1 + (columnIndex == 2 ? 3 : 0));
            pumps.put(pumpKey, new Pump(pumpKey, null));
            Point pos = randomCoordinateNear(new Point(columnWidth * columnIndex + 60, (int) ((i + 0.5) * columnY)));
            coordinates.put(pumpKey, pos);
        }

        return pumpsCount;
    }

    private void createCisterns(int columnWidth) {
        int cisternsCount = random.nextInt(2) + 1;
        int columnFourY = Settings.WINDOW_HEIGHT / cisternsCount;

        for (int i = 0; i < cisternsCount; i++) {
            String cisternKey = "ci" + (i + 1);
            cisterns.put(cisternKey, new Cistern(cisternKey, this));
            Point pos = new Point(columnWidth * 3 + 60, (int) ((i + 0.5) * columnFourY));
            coordinates.put(cisternKey, pos);
        }
    }

    private void connectWaterSourcesToPumps(int pumpsCountColumnTwo) {
        for (int i = 0; i < waterSources.size(); i++) {
            for (int j = 0; j < pumpsCountColumnTwo; j++) {
                if (random.nextBoolean() || i == j) {
                    connectPipe("ws" + (i + 1), "pu" + (j + 1));
                }
            }
        }
    }

    private void connectPumpsToPumps(int pumpsCountColumnTwo, int pumpsCountColumnThree) {
        for (int i = 0; i < pumpsCountColumnTwo; i++) {
            for (int j = 0; j < pumpsCountColumnThree; j++) {
                if (random.nextBoolean() || i == j) {
                    connectPipe("pu" + (i + 1), "pu" + (j + 1 + pumpsCountColumnTwo));
                }
            }
        }
    }

    private void connectPumpsToCisterns(int pumpsCountColumnThree) {
        for (int i = 0; i < pumpsCountColumnThree; i++) {
            for (int j = 0; j < cisterns.size(); j++) {
                if (random.nextBoolean() || i == j) {
                    connectPipe("pu" + (i + 1 + 3), "ci" + (j + 1));
                }
            }
        }
    }

    private void ensureAllWaterSourcesConnected(int pumpsCountColumnTwo) {
        for (WaterSource waterSource : waterSources.values()) {
            if (waterSource.getPipes().isEmpty()) {
                connectPipe(waterSource.getName(), "pu" + (random.nextInt(pumpsCountColumnTwo) + 1));
            }
        }
    }

    private void ensureAllPumpsConnected(int pumpsCountColumnTwo, int pumpsCountColumnThree) {
        for (Pump pump : pumps.values()) {
            if (pump.getPipes().isEmpty()) {
                connectPipe(pump.getName(), "pu" + (random.nextInt(pumpsCountColumnThree) + 1 + pumpsCountColumnTwo));
            }
        }
    }

    private void ensureAllCisternsConnected(int pumpsCountColumnThree) {
        for (Cistern cistern : cisterns.values()) {
            if (cistern.getPipes().isEmpty()) {
                connectPipe("pu" + (random.nextInt(pumpsCountColumnThree) + 1 + 3), cistern.getName());
            }
        }
    }

    private void connectPipe(String from, String to) {
        String pipeKey = "pi" + (pipes.size() + 1);
        Pipe pipe = new Pipe(pipeKey);
        pipes.put(pipeKey, pipe);
        if (waterSources.containsKey(from)) {
            waterSources.get(from).attachPipe(pipe);
        } else if (pumps.containsKey(from)) {
            pumps.get(from).attachPipe(pipe);
            pumps.get(from).setOutput(pipe);
        }
        if (pumps.containsKey(to)) {
            pumps.get(to).attachPipe(pipe);
            pumps.get(to).setInput(pipe);
        } else if (cisterns.containsKey(to)) {
            cisterns.get(to).attachPipe(pipe);
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
        ArrayList<Attachable> attachables = pipe.getAttachables();

        if (isAttachedToTwoAttachables(attachables)) {
            return calculateMiddlePoint(
                    coordinates.get(attachables.get(0).getName()),
                    coordinates.get(attachables.get(1).getName())
            );
        }

        Character firstCharacter = findCharacterWithPipe(pipe);

        if (isAttachedToOneAttachable(attachables) && firstCharacter != null) {
            return calculateMiddlePoint(
                    coordinates.get(attachables.get(0).getName()),
                    getFieldCoordinate(firstCharacter.getField())
            );
        }

        Character secondCharacter = findCharacterWithPipeExcluding(pipe, firstCharacter);

        if (firstCharacter != null && secondCharacter != null) {
            return calculateMiddlePoint(
                    getFieldCoordinate(firstCharacter.getField()),
                    getFieldCoordinate(secondCharacter.getField())
            );
        }

        throw new Exception("Pipe is not attached to 2 attachables or 1 attachable and 1 character or 2 characters!");
    }

    private boolean isAttachedToTwoAttachables(ArrayList<Attachable> attachables) {
        return attachables.size() == 2 && attachables.get(0) != null && attachables.get(1) != null;
    }

    private boolean isAttachedToOneAttachable(ArrayList<Attachable> attachables) {
        return attachables.size() == 1 && attachables.get(0) != null;
    }

    private Point calculateMiddlePoint(Point p1, Point p2) throws Exception {
        if (p1 == null || p2 == null) {
            throw new Exception("One of the coordinates is null but it shouldn't be!");
        }
        return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    private Character findCharacterWithPipe(Pipe pipe) {
        for (Character character : getCharacters()) {
            if (character.getPickedPipe() == pipe) {
                return character;
            }
        }
        return null;
    }

    private Character findCharacterWithPipeExcluding(Pipe pipe, Character excludeCharacter) {
        for (Character character : getCharacters()) {
            if (character.getPickedPipe() == pipe && character != excludeCharacter) {
                return character;
            }
        }
        return null;
    }


    public ArrayList<Point> calculatePipeEnds(Pipe pipe) throws Exception {
        ArrayList<Point> points = new ArrayList<>();

        ArrayList<Attachable> attachables = pipe.getAttachables();

        if (isSimplePipe(attachables)) {
            points.add(location(attachables.get(0).getName()));
            points.add(location(attachables.get(1).getName()));
        } else if (isOneEndAttached(attachables)) {
            points.add(location(attachables.get(0).getName()));
            points.add(findOtherEndForOneAttachment(pipe));
        } else if (isBothEndsPicked(pipe)) {
            points.addAll(findEndsForPickedPipe(pipe));
        } else {
            throw new Exception("Pipe is not attached to 2 attachables or 1 attachable and 1 character or 2 characters!");
        }

        return points;
    }

    private boolean isSimplePipe(ArrayList<Attachable> attachables) {
        return attachables.size() == 2 && attachables.get(0) != null && attachables.get(1) != null;
    }

    private boolean isOneEndAttached(ArrayList<Attachable> attachables) {
        return attachables.size() == 1 && attachables.get(0) != null;
    }

    private boolean isBothEndsPicked(Pipe pipe) {
        return pipe.getAttachables().isEmpty();
    }

    private Point findOtherEndForOneAttachment(Pipe pipe) throws Exception {
        for (Character character : getCharacters()) {
            Pipe pickedPipe = character.getPickedPipe();
            if (pickedPipe != null && pickedPipe.equals(pipe)) {
                Point characterCoord = getCharacterCoordinate(character);
                return new Point(characterCoord.x, (int) (characterCoord.y - Settings.board_character_height() / 1.5));
            }
        }
        throw new Exception("Other end of the pipe is not attached!");
    }

    private ArrayList<Point> findEndsForPickedPipe(Pipe pipe) throws Exception {
        ArrayList<Point> points = new ArrayList<>();
        boolean p1_found = false;

        for (Character character : getCharacters()) {
            Pipe pickedPipe = character.getPickedPipe();
            if (pickedPipe != null && pickedPipe.equals(pipe)) {
                if (!p1_found) {
                    points.add(getCharacterCoordinate(character));
                    p1_found = true;
                } else {
                    points.add(getCharacterCoordinate(character));
                    return points;
                }
            }
        }

        if (points.size() < 2) {
            throw new Exception("Both ends of the pipe are not picked by characters!");
        }

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
