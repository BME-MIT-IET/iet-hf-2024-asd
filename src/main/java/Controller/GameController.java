package Controller;

import Model.Cistern;
import Model.Pipe;
import Model.Pump;
import View.BoardView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameController {
    /**
     * Board view to show board of the game
     */
    private final BoardView view;
    /**
     * Board model to store game data
     */
    private final Board board;
    /**
     * Actions map to store actions for each key
     */
    private final HashMap<Character, Action> actions = new HashMap<>();
    /**
     * If true, the selected action waits for click
     */
    private boolean isWaitingForClick = false;
    /**
     * The last key pressed
     */
    private Character lastKeyPressed = null;
    /**
     * The index of the next character to make action (1. mechanic, if larger than mechanic size, saboteur index)
     */
    private int nextCharacterIndex = 0;

    /**
     * The current round
     */
    private int round = 0;

    private int mc_points = 0;
    private int sc_points = 0;

    private static final String ACTION = "Select action";

    /**
     * Interface for action run with coordinates
     */
    public interface coordinatesActionFunc {
        boolean op(int x, int y) throws Exception;
    }

    /**
     * Interface for action run without coordinates
     */
    public interface actionFunc {
        boolean op();
    }

    /**
     * Action class to store action parameters and function to run it
     */
    static class Action {
        public boolean isMechanicAction;
        public boolean isSaboteurAction;
        public boolean isNeedClick;
        public String message;
        public actionFunc actionFunc;
        public coordinatesActionFunc coordinatesFunc;

        public Action(boolean isNeedClick, boolean isMechanicAction, boolean isSaboteurAction, String message) {
            this.isMechanicAction = isMechanicAction;
            this.isSaboteurAction = isSaboteurAction;
            this.isNeedClick = isNeedClick;
            this.message = message;
        }

        public Action(boolean isNeedClick, boolean isMechanicAction, boolean isSaboteurAction, String message, coordinatesActionFunc action) {
            this(isNeedClick, isMechanicAction, isSaboteurAction, message);
            this.coordinatesFunc = action;
        }

        public Action(boolean isNeedClick, boolean isMechanicAction, boolean isSaboteurAction, String message, actionFunc action) {
            this(isNeedClick, isMechanicAction, isSaboteurAction, message);
            this.actionFunc = action;
        }
    }

    /**
     * Constructor
     *
     * @param view  Board view
     * @param board Board model
     */
    public GameController(BoardView view, Board board) {
        this.view = view;
        this.board = board;
        this.init();
    }

    /**
     * Init game controller
     */
    private void init() {
        // Place characters random
        board.placeCharactersRandom();

        initActions();
        initView();
    }

    /**
     * Init game actions with keys and functions
     */
    private void initActions() {
        // Skip
        actions.put('x', new Action(false, true, true, "skip", () -> true));

        // Move
        actions.put('m', new Action(true, true, true, "move", (x, y) -> board.getCharacter(nextCharacterIndex).move(board.findClosestField(x, y))));

        // Set pump input
        actions.put('i', new Action(true, true, true, "input", (x, y) -> {
            Pipe closestPipe = board.findClosestPipe(x, y);
            Model.Character character = board.getCharacter(nextCharacterIndex);

            Pump characterStandingPump = board.pumps.get(character.getField().getName());
            if (characterStandingPump == null) return false;
            int indexOfClosestPipe = characterStandingPump.getPipes().indexOf(closestPipe);

            return character.setPumpInput(indexOfClosestPipe + 1);
        }));

        // Set pump output
        actions.put('o', new Action(true, true, true, "output", (x, y) -> {
            Pipe closestPipe = board.findClosestPipe(x, y);
            Model.Character character = board.getCharacter(nextCharacterIndex);

            Pump characterStandingPump = board.pumps.get(character.getField().getName());
            if (characterStandingPump == null) return false;
            int indexOfClosestPipe = characterStandingPump.getPipes().indexOf(closestPipe);
            return character.setPumpOut(indexOfClosestPipe + 1);
        }));

        // Break pipe (make it holey)
        actions.put('h', new Action(false, true, true, "holey pipe", () -> board.getCharacter(nextCharacterIndex).breakPipe()));

        // Fix pipe/pump (only mechanic can fix)
        actions.put('f', new Action(false, true, false, "fix", () -> board.getCharacter(nextCharacterIndex).fix()));

        // Pick up pipe
        actions.put('p', new Action(true, true, true, "pick up pipe", (x, y) -> {
            Model.Character character = board.getCharacter(nextCharacterIndex);
            // On which pipe did the user click?
            Pipe closestPipe = board.findClosestPipe(x, y);
            // On which pump is the character standing?
            Pump characterStandingPump = board.pumps.get(character.getField().getName());
            int indexOfClosestPipe;
            if (characterStandingPump != null) {
                indexOfClosestPipe = characterStandingPump.getPipes().indexOf(closestPipe);
            } else {
                Cistern characterStandingCistern = board.cisterns.get(character.getField().getName());
                indexOfClosestPipe = characterStandingCistern.getPipes().indexOf(closestPipe);
            }

            return character.pickupPipe_c(indexOfClosestPipe + 1);
        }));

        // Pick up new pipe from cistern (only mechanic can pick up)
        actions.put('n', new Action(false, true, false, "pick up new pipe", () -> board.getCharacter(nextCharacterIndex).pickupNewPipe_mc()));

        // Pick up pump from cistern (only mechanic can)
        actions.put('b', new Action(false, true, false, "pick up pump", () -> board.getCharacter(nextCharacterIndex).pickupPump()));

        // Attach pipe to pump/cistern
        actions.put('a', new Action(false, true, true, "attach pipe", () -> board.getCharacter(nextCharacterIndex).attachPipeToPump()));

        // Place pump on pipe (only mechanic can)
        actions.put('q', new Action(false, true, false, "place pump", () -> board.getCharacter(nextCharacterIndex).placePumpToPipe()));

        // Make pipe slippery (only saboteur can)
        actions.put('s', new Action(false, false, true, "slippery", () -> board.getCharacter(nextCharacterIndex).makeSlippery()));

        // Make pipe sticky
        actions.put('t', new Action(false, true, true, "sticky", () -> board.getCharacters().get(nextCharacterIndex).makeSticky()));
    }

    private void initView() {
        // Set focus to view
        view.requestFocus();

        // Init view with board and possible actions
        view.init(this.board, getPossibleActions(), this.getWhoseNext(), ACTION);

    }

    /**
     * Set next character to make action
     */
    private void nextCharacter() {
        nextCharacterIndex++;
        // Check if all characters made action
        if (nextCharacterIndex >= board.mechanicCharacters.size() + board.saboteurCharacters.size()) {
            nextRound();
        }
        // Update view actions for next character
        view.updateActions(getPossibleActions(), null);
    }

    private TreeMap<String, String> getPossibleActions() {
        TreeMap<String, String> possibleActions = new TreeMap<>();
        boolean isMechanic = nextCharacterIndex < board.mechanicCharacters.size();
        for (Map.Entry<Character, Action> entry : actions.entrySet()) {
            if (isMechanic && entry.getValue().isMechanicAction || !isMechanic && entry.getValue().isSaboteurAction) {
                possibleActions.put(entry.getKey().toString(), entry.getValue().message);
            }
        }
        return possibleActions;
    }

    private void nextRound() {
        // Step board
        stepBoard();
        // Calculate mc and sc points
        calculatePoints();
        // Increment round counter
        round++;
        // Reset next character index
        nextCharacterIndex = 0;

        // Check if game is over
        if (isGameOver()) {
            // Calculate who is the winner
            String winner = "Mechanic";
            if (mc_points < sc_points) {
                winner = "Saboteur";
            } else if (mc_points == sc_points) {
                winner = "Draw";
            }
            // TODO: show game over screen
            view.showGameOver(winner);
        }
    }

    private boolean isGameOver() {
        // Check if round limit is reached
        if (round >= Settings.game_rounds() && Settings.game_rounds() != -1) {
            return true;
        }
        // Check if points limit is reached and return true if it is
        return (mc_points >= Settings.points_to_win() || sc_points >= Settings.points_to_win()) && Settings.points_to_win() != -1;
    }

    /**
     * Add key listener to view
     */
    private void addKeyListener() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });
    }

    private void handleKeyPressed(KeyEvent e) {
        if (isGameOver()) {
            handleGameOverKeyPress(e);
            return;
        }

        lastKeyPressed = e.getKeyChar();
        Action action = actions.get(lastKeyPressed);

        if (action == null || !isValidAction(action)) {
            return;
        }

        if (!action.isNeedClick) {
            processActionWithoutClick(action);
        } else {
            waitForClick();
        }
    }

    private void handleGameOverKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.exit(0);
        }
    }

    private boolean isValidAction(Action action) {
        if (nextCharacterIndex < 0 ||
                nextCharacterIndex >= board.mechanicCharacters.size() + board.saboteurCharacters.size() ||
                (nextCharacterIndex < board.mechanicCharacters.size() && !action.isMechanicAction) ||
                (nextCharacterIndex >= board.saboteurCharacters.size() && !action.isSaboteurAction)) {
            return false;
        }
        return true;
    }

    private void processActionWithoutClick(Action action) {
        isWaitingForClick = false;
        boolean isActionSuccess = action.actionFunc.op();
        if (isActionSuccess) {
            actionRunnedSuccessfully();
        } else {
            actionRunFailed();
        }
    }

    private void waitForClick() {
        isWaitingForClick = true;
        view.updateMessage("Waiting for click...");
        view.updateActions(getPossibleActions(), String.valueOf(lastKeyPressed));
    }


    /**
     * Add mouse listener to view
     */
    private void addClickListeners() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (isWaitingForClick) {
                    // Get click coordinates
                    int x = evt.getX();
                    int y = evt.getY();

                    // Find action by key
                    Action action = actions.get(lastKeyPressed);
                    if (action == null) return;

                    // Run action (try)
                    boolean isActionSuccess = false;
                    try {
                        isActionSuccess = action.coordinatesFunc.op(x, y);
                    } catch (Exception e) {
                        System.out.println("Action failed: " + e.getMessage());
                    }

                    // If success --> stop waiting for click, next character, update view
                    if (isActionSuccess) {
                        actionRunnedSuccessfully();
                    } else {
                        actionRunFailed();
                    }
                }
            }
        });
    }

    private void actionRunnedSuccessfully() {
        isWaitingForClick = false;
        nextCharacter();
        view.update(board, mc_points, sc_points, getWhoseNext(), this.round, ACTION);
    }

    private void actionRunFailed() {
        isWaitingForClick = false;
        view.update(board, mc_points, sc_points, getWhoseNext(), this.round, ACTION);
        view.updateActions(getPossibleActions(), null);
    }

    /**
     * Run game
     */
    public void run() {
        // Set focus to view
        view.requestFocus();

        // Add key listener to view
        addKeyListener();

        // Ad click listener to view
        addClickListeners();
    }

    private void stepBoard() {
        // 1. all water sources step
        this.board.waterSources.forEach((name, waterSource) -> waterSource.step());

        // 2. all pumps step
        this.board.pumps.forEach((name, pump) -> pump.step());

        // 3. all pipes step
        this.board.pipes.forEach((name, pipe) -> pipe.step());

        // 4. all cisterns step
        this.board.cisterns.forEach((name, cistern) -> cistern.step());

        // 5. all mechanic characters step
        this.board.mechanicCharacters.forEach((name, mechanicCharacter) -> mechanicCharacter.step());

        // 6. all saboteur characters step
        this.board.saboteurCharacters.forEach((name, saboteurCharacter) -> saboteurCharacter.step());
    }

    private String getWhoseNext() {
        Model.Character next = board.getCharacters().get(this.nextCharacterIndex);
        if (next == null) return "";
        return next.getName();
    }

    private void calculatePoints() {
        // Calculate water in pumps
        AtomicInteger water_in_pumps = new AtomicInteger();
        this.board.pumps.forEach((name, pump) -> water_in_pumps.addAndGet(pump.getAcceptedWater() - pump.getPushedWater()));

        // Calculate pushed water from water sources
        AtomicInteger pushed_water_from_watersource = new AtomicInteger();
        this.board.waterSources.forEach((name, waterSource) -> pushed_water_from_watersource.addAndGet(waterSource.getPushedWater()));

        // Calculate water in cisterns
        AtomicInteger water_in_cisterns = new AtomicInteger();
        this.board.cisterns.forEach((name, cistern) -> water_in_cisterns.addAndGet(cistern.getAcceptedWater()));

        // Calculate points
        this.mc_points = water_in_cisterns.get();
        this.sc_points = pushed_water_from_watersource.get() - water_in_pumps.get() - water_in_cisterns.get();

    }

}
