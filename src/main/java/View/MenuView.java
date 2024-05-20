package View;

import Controller.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class MenuView extends JPanel {

    // Buttons panel
    private JPanel buttonsPanel;
    private JButton startButton;
    private JButton addMechanicButton;
    private JButton addSaboteurButton;

    // Players panel
    public JPanel playersPanel;

    public LinkedList<JLabel> mechanicLabel;
    public LinkedList<JLabel> saboteurLabel;

    public LinkedList<JTextField> mechanicTextField;
    public LinkedList<JTextField> saboteurTextField;

    private Image menuBackgroundImage = null;

    @Override
    protected void paintComponent(Graphics g) {
        try {
            menuBackgroundImage = ImageIO.read(new File("assets/menu_background2.png"));
        } catch (Exception e) {
            System.out.println("Error loading menu background image!");
        }

        if (this.menuBackgroundImage != null) {
            g.drawImage(menuBackgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
    public MenuView(int width, int height) {

        this.setPreferredSize(new Dimension(width, height));

        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);

        setButtons();
        this.add(buttonsPanel, BorderLayout.SOUTH);


        setPlayersPanel();
        this.add(playersPanel, BorderLayout.CENTER);

        // Title
        JLabel titleLabel = new JLabel("SIVATAGI DÍNÓ", SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT / 3));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel, BorderLayout.NORTH);

        this.setVisible(true);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getMechanicButton() {
        return addMechanicButton;
    }

    public JButton getSaboteurButton() {
        return addSaboteurButton;
    }

    public void Update() {
        playersPanel.removeAll();
        drawPlayersPanel();
        this.add(playersPanel);
        this.revalidate();
        this.repaint();

    }

    public void setButtons() {
        buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);

        startButton = new JButton("Játék indítása");
        startButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        addMechanicButton = new JButton("Szerelő hozzáadása");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addSaboteurButton = new JButton("Szabotőr hozzáadása");
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttonsPanel.add(addMechanicButton);
        buttonsPanel.add(addSaboteurButton);
        buttonsPanel.add(startButton);

    }

    public void setPlayersPanel() {
        mechanicLabel = new LinkedList<>();
        saboteurLabel = new LinkedList<>();
        mechanicTextField = new LinkedList<>();
        saboteurTextField = new LinkedList<>();

        playersPanel = new JPanel();
        playersPanel.setMaximumSize(new Dimension(100, 100));
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.PAGE_AXIS));

        // Set label color to white
        UIManager.put("Label.foreground", Color.WHITE);

        mechanicLabel.add(new JLabel("1. Szerelő játékos neve"));
        mechanicLabel.add(new JLabel("2. Szerelő játékos neve"));

        saboteurLabel.add(new JLabel("1. Szabotőr játékos neve"));
        saboteurLabel.add(new JLabel("2. Szabotőr játékos neve"));

        int textInputWidth = 20;
        mechanicTextField.add(new JTextField(textInputWidth));
        mechanicTextField.add(new JTextField(textInputWidth));

        saboteurTextField.add(new JTextField(textInputWidth));
        saboteurTextField.add(new JTextField(textInputWidth));

        playersPanel.setOpaque(false);

        drawPlayersPanel();
    }

    public void drawPlayersPanel() {

        for (int i = 0; i < mechanicLabel.size(); i++) {
            JPanel panel = new JPanel();
            panel.add(mechanicLabel.get(i));
            panel.add(mechanicTextField.get(i));
            panel.setOpaque(false);
            playersPanel.add(panel);
        }
        for (int i = 0; i < saboteurLabel.size(); i++) {
            JPanel panel = new JPanel();
            panel.add(saboteurLabel.get(i));
            panel.add(saboteurTextField.get(i));
            panel.setOpaque(false);
            playersPanel.add(panel);
        }
    }


}
