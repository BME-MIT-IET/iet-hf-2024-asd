package Controller;

import View.View;

public class Controller {
    private final Board board;
    private final View view;


    public Controller(View view) {
        this.view = view;
        board = new Board();
        if (Settings.generate_random_map) this.board.createRandomMap();
        else this.board.createDefaultMap();
    }


    public void startMenu() {
        MenuController menuController = new MenuController(view.getMenuView(), board);
        view.showMenu();
        menuController.run(this::startGame);
    }

    public void startGame() {
        GameController gameController = new GameController(view.getBoardView(), board);
        view.showBoard();
        gameController.run();
    }

}
