import Controller.Controller;
import View.View;

public class Game implements Runnable {

    Controller controller;
    View view;

    public Game() {
        view = new View();
        controller = new Controller(view);
    }

    @Override
    public void run() {
        controller.startMenu();
        //controller.startGame();
    }
}
