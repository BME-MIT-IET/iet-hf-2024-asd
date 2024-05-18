package Controller;

import Model.MechanicCharacter;
import Model.SaboteurCharacter;
import View.MenuView;

import javax.swing.*;

public class MenuController {

    private final MenuView view;
    private final Board board;

    public MenuController(MenuView view, Board board) {
        this.view = view;
        this.board = board;
    }

    public void run(Runnable startGame) {

        view.getStartButton().addActionListener(e -> {
            for (int i = 0; i < view.mechanicTextField.size(); i++) {
                String name = view.mechanicTextField.get(i).getText();
                if (name.equals("")) name = "mc" + (i + 1);
                MechanicCharacter mc = new MechanicCharacter(name);
                board.mechanicCharacters.put(mc.getName(), mc);
            }
            for (int i = 0; i < view.saboteurTextField.size(); i++) {
                String name = view.saboteurTextField.get(i).getText();
                if (name.equals("")) name = "sc" + (i + 1);
                SaboteurCharacter sc = new SaboteurCharacter(name);
                board.saboteurCharacters.put(sc.getName(), sc);
            }
            startGame.run();
        });

        view.getMechanicButton().addActionListener(e -> {
            String hanyadik = String.valueOf(view.mechanicLabel.size() + 1);
            view.mechanicLabel.add(new JLabel(hanyadik + ". Szerelő játékos neve"));
            view.mechanicTextField.add(new JTextField(20));
            JPanel panel = new JPanel();
            panel.add(view.mechanicLabel.getLast());
            panel.add(view.mechanicTextField.getLast());
            view.playersPanel.add(panel);
            view.Update();
        });

        view.getSaboteurButton().addActionListener(e -> {
            String hanyadik = String.valueOf(view.saboteurLabel.size() + 1);
            view.saboteurLabel.add(new JLabel(hanyadik + ". Szabotőr játékos neve"));
            view.saboteurTextField.add(new JTextField(20));
            JPanel panel = new JPanel();
            panel.add(view.saboteurLabel.getLast());
            panel.add(view.saboteurTextField.getLast());
            view.playersPanel.add(panel);
            view.Update();
        });
    }
}
