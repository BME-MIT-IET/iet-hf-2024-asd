package View;

import Controller.Board;
import Controller.Settings;
import Model.Character;
import Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class BoardView extends JPanel {
    private Board board;
    private boolean gameOver = false;
    private String winner = "";

    /**
     * A szerelő csapat és a szabotőr csapat pontjai
     */
    private Integer mechpoints = 0;
    private Integer sabpoints = 0;
    /**
     * A következő játékos nevét tartja számon
     */
    private String nextplayer = "";
    private int round = 0;
    private String message = "Choose action by pressing an input key!";
    private TreeMap<String, String> actionsMap = new TreeMap<>();
    private String selectedAction = null;
    private final ArrayList<Image> characterImages = new ArrayList<>();
    private Image gameOverImage = null;

    private final int circleRadius = Settings.board_circles_radius();

    public BoardView(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Settings.board_color);
        this.setLayout(null);
        this.setVisible(true);
        setFocusable(true);
    }

    public void init(Board board, TreeMap<String, String> actions, String nextCharacter, String message) {
        this.board = board;
        this.nextplayer = nextCharacter;
        this.actionsMap = actions;
        this.message = message;

        // Load images
        this.loadImages();

        // Repaint
        this.repaint();
    }

    private void loadImages() {
        // Get how many files in assets/Dino_pics folder
        File folder = new File("assets/Dino_pics");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return;

        // Load character images
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    Image image = ImageIO.read(file);
                    characterImages.add(image);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Load game over image
        try {
            gameOverImage = ImageIO.read(new File("assets/game_over.png"));
        } catch (Exception e) {
            System.out.println("Error loading game over image!");
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If Game over is true, draw game over screen
        if (gameOver) {
            drawGameOver(g);
            return;
        }

        // If board is not initialized, do not draw anything
        if (board == null) return;

        // Draw info / instructions
        drawInstructions(g);

        // Draw pipes
        try {
            drawPipes(g);
        } catch (Exception e) {
            //a karkterkoordinata dobta
            throw new RuntimeException(e);
        }

        // Draw fields
        try {
            drawAttachables(g);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Draw characters
        try {
            drawCharacters(g);
        } catch (Exception e) {
            //a karkterkoordinata dobta
            throw new RuntimeException(e);
        }

    }

    public void update(Board board, int mechpoints, int sabpoints, String nextCharacter, int round, String message) {
        this.board = board;
        this.mechpoints = mechpoints;
        this.sabpoints = sabpoints;
        this.nextplayer = nextCharacter;
        this.round = round;
        this.message = message;
        this.repaint();
    }

    public void updateMessage(String message) {
        this.message = message;
        this.repaint();
    }

    public void updateActions(TreeMap<String, String> actions, String selectedAction) {
        this.actionsMap = actions;
        this.selectedAction = selectedAction;
        this.repaint();
    }

    private void drawInstructions(Graphics g) {
        // Set default font
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setFont(g.getFont().deriveFont(Font.BOLD));


        // Get text height
        int textHeight = g.getFontMetrics(g.getFont()).getHeight();

        // Draw next player
        g.drawString("Next: " + nextplayer, 6, textHeight + 6);

        // Draw points
        String pointsText = "Points: ";
        int pointsTextWidth = g.getFontMetrics(g.getFont()).stringWidth(pointsText);
        g.drawString("Points: ", 6, textHeight * 2 + 6);

        String mechPointsText = String.valueOf(mechpoints);
        int mechPointsTextWidth = g.getFontMetrics(g.getFont()).stringWidth(mechPointsText);
        g.setColor(Settings.mechanic_color);
        g.drawString(mechPointsText, 6 + pointsTextWidth, textHeight * 2 + 6);

        String sepatorText = " / ";
        int sepatorTextWidth = g.getFontMetrics(g.getFont()).stringWidth(sepatorText);
        g.setColor(Color.black);
        g.drawString(sepatorText, 6 + pointsTextWidth + mechPointsTextWidth, textHeight * 2 + 6);

        String sabPointsText = String.valueOf(sabpoints);
        g.setColor(Settings.saboteur_color);
        g.drawString(sabPointsText, 6 + pointsTextWidth + mechPointsTextWidth + sepatorTextWidth, textHeight * 2 + 6);

        // Draw rounds left
        int roundLeft = Settings.game_rounds() - round;
        String roundLeftText = "Rounds left: " + roundLeft;
        g.setColor(Color.black);
        g.drawString(roundLeftText, 6, textHeight * 3 + 6);

        // Draw instructions in the bottom left corner
        g.setColor(Color.black);
        g.drawString(this.message, 6, this.getHeight() - 10);
        int instructionTextHeight = g.getFontMetrics(g.getFont()).getHeight();

        // Draw actions in the bottom right corner
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        int actionTextHeight = g.getFontMetrics(g.getFont()).getHeight();
        int i = 0;
        int actionCount = actionsMap.size();
        for (Map.Entry<String, String> entry : actionsMap.entrySet()) {
            if (selectedAction != null && selectedAction.equals(entry.getKey())) {
                g.setColor(Color.blue);
                g.setFont(g.getFont().deriveFont(Font.BOLD));
            } else {
                g.setColor(Color.black);
                g.setFont(g.getFont().deriveFont(Font.PLAIN));
            }
            g.drawString(entry.getKey() + " - " + entry.getValue(), 6, this.getHeight() - actionCount * actionTextHeight + i * actionTextHeight - instructionTextHeight);
            i++;
        }

    }

    private void drawPipes(Graphics g) throws Exception {
        for (Map.Entry<String, Pipe> pipe : board.pipes.entrySet()) {
            ArrayList<Point> points = board.calculatePipeEnds(pipe.getValue());

            Point p1 = points.get(0);
            Point p2 = points.get(1);

            // sima cso szine
            g.setColor(Settings.pipe_color);

            // csuszos vagy ragados szine
            if (pipe.getValue().getSticky() > 0) g.setColor(Settings.sticky_pipe_color);
            if (pipe.getValue().getSlippery() > 0) g.setColor(Settings.slippery_pipe_color);

            //https://www.codejava.net/java-se/graphics/drawing-lines-examples-with-graphics2d
            // lyukas cso -> szaggatott vonal
            if (pipe.getValue().getHoley()) {
                float[] dashingPattern = {10f, 10f};
                Stroke stroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 0.0f);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(stroke);
                g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
                stroke = new BasicStroke(2f);
                g2d.setStroke(stroke);
            }
            //nem lyukas
            else {
                Graphics2D g2 = (Graphics2D) g;
                int pipesCountInTheSameField = 0;
                // If more than one pipe is on the same field, draw with wider line
                for (Pipe otherPipe : board.pipes.values()) {
                    if (
                            !pipe.getValue().equals(otherPipe) && // Not the same pipe
                                    pipe.getValue().getAttachables().size() == 2 && otherPipe.getAttachables().size() == 2 && // Both pipes are connected to two fields
                                    board.calculatePipeCenter(otherPipe).equals(board.calculatePipeCenter(pipe.getValue())) // Connected to the same field
                    ) {
                        pipesCountInTheSameField++;
                    }
                }
                g2.setStroke(new BasicStroke(2 + pipesCountInTheSameField * 2));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private void drawAttachables(Graphics g) throws Exception {

        // Draw cisterns
        for (Map.Entry<String, Cistern> c : board.cisterns.entrySet()) {
            Point p1 = board.getFieldCoordinate(c.getValue());
            if (p1 != null) drawCircle(g, p1, circleRadius, Settings.cistern_color);
            drawNewItems(c.getValue(), g, p1);
        }

        // Draw pumps
        for (Map.Entry<String, Pump> p : board.pumps.entrySet()) {
            Point pump_pos = board.getFieldCoordinate(p.getValue());
            if (pump_pos == null) continue;

            drawCircle(g, pump_pos, circleRadius, Settings.pump_color);
            if (!p.getValue().getWorking()) {
                // Calculate string font size
                int fontSize = circleRadius / 2;
                // Calculate string position
                Point string_pos = new Point(pump_pos.x - fontSize / 3, pump_pos.y + fontSize / 3);
                // Draw string
                g.setColor(Color.BLACK);
                g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
                g.drawString("X", string_pos.x, string_pos.y);
            }


            // Draw status bar to show how much water is in the pump
            int waterInPump = p.getValue().getWaterInPump();
            Point status_bar_pos = new Point(pump_pos.x, pump_pos.y + circleRadius / 3);
            int status_bar_width = (int) (circleRadius * 0.5);
            int status_bar_height = status_bar_width / 5;
            int water_percent = (int) (((double) waterInPump / (double) Settings.pump_tankSize()) * 100.0);
            this.drawStatusBar(g, status_bar_pos, status_bar_width, status_bar_height, water_percent, Settings.water_source_color);

            if (p.getValue().getInput() != null)
                drawPumpInputOutput(p.getValue(), p.getValue().getInput(), g, true);
            if (p.getValue().getOutput() != null)
                drawPumpInputOutput(p.getValue(), p.getValue().getOutput(), g, false);
        }

        // Draw water sources
        for (Map.Entry<String, WaterSource> w : board.waterSources.entrySet()) {
            Point p1 = board.location(w.getValue().getName());
            if (p1 != null) drawCircle(g, p1, circleRadius, Settings.water_source_color);
        }
    }

    private void drawPumpInputOutput(Pump p, Pipe pipe, Graphics g, boolean in) throws Exception {

        Point pipeCenter = board.calculatePipeCenter(pipe);
        Point pumpCenter = board.getFieldCoordinate(p);
        Point direction = new Point(pumpCenter.x - pipeCenter.x, pumpCenter.y - pipeCenter.y);
        double directionLength = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        double directionX = (direction.x / directionLength);
        double directionY = (direction.y / directionLength);
        Point center = new Point((int) (pumpCenter.x - directionX * 50), (int) (pumpCenter.y - directionY * 50));

        for (Attachable a : pipe.getAttachables()) {
            if (in && !a.equals(p)) {
                drawTriangle(center, direction, g, Settings.board_circles_radius() / 4, Settings.pipe_color);
            } else if (!a.equals(p)) {
                // Reverse direction
                direction.x *= -1;
                direction.y *= -1;
                drawTriangle(center, direction, g, Settings.board_circles_radius() / 4, Settings.pipe_color);
            }
        }
    }

    private void drawTriangle(Point center, Point direction, Graphics g, int size, Color color) {

        // Set color
        g.setColor(color);

        // Normalize direction
        double length = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        double x = direction.x / length;
        double y = direction.y / length;

        Polygon triangle = new Polygon();

        Point a = new Point((int) (center.x + x * size), (int) (center.y + y * size));
        Point b = new Point((int) (center.x + y * size), (int) (center.y - x * size));
        Point c = new Point((int) (center.x - y * size), (int) (center.y + x * size));

        triangle.addPoint(a.x, a.y);
        triangle.addPoint(b.x, b.y);
        triangle.addPoint(c.x, c.y);
        g.fillPolygon(triangle);
    }

    private void drawStatusBar(Graphics g, Point center, int width, int height, int filledPercent, Color color) {

        // Draw border
        g.setColor(color);
        g.drawRect(center.x - width / 2, center.y - height / 2, width, height);

        // Draw filled part
        g.setColor(color);
        g.fillRect(center.x - width / 2, center.y - height / 2, width * filledPercent / 100, height);
    }

    private void drawNewItems(Cistern c, Graphics g, Point p) {
        // Draw new pipe
        if (c.getNewPipe() != null) {
            g.setColor(Settings.pipe_color);
            Point newPipePosStart = new Point(p.x, p.y);
            Point newPipePosEnd = new Point(p.x, p.y + circleRadius);
            g.drawLine(newPipePosStart.x, newPipePosStart.y, newPipePosEnd.x, newPipePosEnd.y);
        }
        // Draw new pump
        if (c.getNewPump() != null) {
            Point newPumpPos = new Point(p.x + circleRadius / 4, p.y - circleRadius / 4);
            drawCircle(g, newPumpPos, circleRadius / 2, Settings.pump_color);
        }
    }

    private void drawCircle(Graphics g, Point center, int radius, Color c) {
        g.setColor(c);
        // Calculate top left corner position
        Point p = new Point(center.x - radius / 2, center.y - radius / 2);
        g.fillOval(p.x, p.y, radius, radius);
    }

    private void drawCharacters(Graphics g) throws Exception {
        int i = 1;

        for (Map.Entry<String, MechanicCharacter> w : board.mechanicCharacters.entrySet()) {
            int imageIndex = i % this.characterImages.size();
            drawCharacter(g, w.getValue(), this.characterImages.get(imageIndex));
            drawPickedPump(w.getValue(), g);
            i++;
        }
        for (Map.Entry<String, SaboteurCharacter> w : board.saboteurCharacters.entrySet()) {
            int imageIndex = i % this.characterImages.size();
            drawCharacter(g, w.getValue(), this.characterImages.get(imageIndex));
            i++;
        }
    }

    private void drawPickedPump(MechanicCharacter w, Graphics g) throws Exception {
        if (w.getPickedPump() != null) {
            Point p = board.getCharacterCoordinate(w);
            p.x -= 2;
            p.y -= 2;
            drawCircle(g, p, circleRadius / 2, Settings.pump_color);
        }
    }

    private void drawCharacter(Graphics g, Character character, Image image) throws Exception {
        // Get character position
        Point pos = board.getCharacterCoordinate(character);

        // Calculate character size
        float ratio = (float) image.getHeight(null) / (float) image.getWidth(null);
        int dino_height = Settings.board_character_height();
        int dino_width = (int) (dino_height / ratio);

        // Calculate draw position
        Point drawPos = new Point(pos.x - dino_width / 2, pos.y - dino_height);

        // If more than one character is on the same field, draw them next to each other
        int charactersInThisField = character.getField().getCharacters().size();
        if (charactersInThisField > 1) {
            int index = character.getField().getCharacters().indexOf(character);
            drawPos.x += index * dino_width / 2 - charactersInThisField / 2 * dino_width / 4;
        }

        // If character is stuck, draw lower and smaller
        if (character.getStuck() > 0) {
            drawPos.y += dino_height / 2;
            dino_height /= 1.5;
            dino_width /= 1.5;
        }

        // Draw character
        g.drawImage(image, drawPos.x, drawPos.y, dino_width, dino_height, null);


        // CHARACTER NAME
        // Set color and font (bold and colored if it's the next player)
        if (Objects.equals(character.getName(), this.nextplayer)) {
            // Check if character is mechanic or saboteur
            if (character instanceof MechanicCharacter) {
                g.setColor(Settings.mechanic_color);
            } else {
                g.setColor(Settings.saboteur_color);
            }
            g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
            g.setFont(g.getFont().deriveFont(Font.BOLD));
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        }

        // Calculate text width
        int textWidth = g.getFontMetrics().stringWidth(character.getName());

        // Draw name above character
        Point namePos = new Point(drawPos.x + (dino_width / 2) - (textWidth / 2), drawPos.y - 2);
        g.drawString(character.getName(), namePos.x, namePos.y);

    }

    public void showGameOver(String winner) {
        this.gameOver = true;
        this.winner = winner;
    }

    private void drawGameOver(Graphics g) {

        // Draw background
        if (this.gameOverImage != null) {
            g.drawImage(gameOverImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }


        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        g.drawString("GAME OVER!", this.getWidth() / 2 - 180, 50);

        // Draw winner name
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Winner: " + this.winner, 10, this.getHeight() / 2 + 150);

        // Draw mechanic and saboteur points
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Mechanic points: " + this.mechpoints, 10, this.getHeight() / 2 + 180);
        g.drawString("Saboteur points: " + this.sabpoints, 10, this.getHeight() / 2 + 210);

        // Draw close message
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Press enter to close the window", 10, this.getHeight() / 2 + 250);

    }
}
