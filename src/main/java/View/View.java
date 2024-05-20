package View;

import Controller.Settings;

import javax.swing.*;

public class View extends JFrame {

    private final MenuView menuView;
    private final BoardView boardView;


    public View() {
        String TITLE = "Sivatagi Dínó";
        this.setTitle(TITLE);
        int WIDTH = Settings.WINDOW_WIDTH;
        int HEIGHT = Settings.WINDOW_HEIGHT;
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon LOGO = new ImageIcon("./assets/dino_icon.png");
        this.setIconImage(LOGO.getImage());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        this.menuView = new MenuView(WIDTH, HEIGHT);
        this.boardView = new BoardView(WIDTH, HEIGHT);
    }

    public MenuView getMenuView() {
        return this.menuView;
    }

    public BoardView getBoardView() {
        return this.boardView;
    }

    private void clear() {
        this.getContentPane().removeAll();
        this.getContentPane().repaint();
    }

    public void showMenu() {
        this.clear();
        this.getContentPane().add(menuView);
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }

    public void showBoard() {
        this.clear();
        this.getContentPane().add(boardView);
        this.revalidate();
        this.repaint();
        this.requestFocus();
    }
}
