package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A skeleton osztály ami a teszteléshez szükséges
 */
public class Skeleton {

    // Static variables/methods for test runs
    /**
     * A metódusok hívásainak mélységét tárolja a skeleton osztálytól
     */
    private static int METHOD_DISTANCE = 0;
    /**
     * Ha igaz, akkor a skeleton osztály kiírja a metódusok hívásait
     */
    public static boolean PRINT_ON = true;

    /**
     * Bekér egy stringet a felhasználótól a message paraméterben megadott üzenettel
     *
     * @param message a bekéréshez megjelenítendő üzenet
     * @return a felhasználó által megadott string
     */
    public static String getInput(String message) {
        if (!PRINT_ON) return "nem";
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < Skeleton.METHOD_DISTANCE; i++) {
            System.out.print("\t");
        }
        System.out.print(message);
        return input.nextLine();
    }

    /**
     * Kiírja a metódus hívását
     *
     * @param classIdentifier a hívott osztály típusa
     * @param objectName      a hívott objektum neve
     * @param methodName      a hívott metódus neve
     * @param returnValue     a metódus visszatérési értéke
     */
    public static void print(String classIdentifier, String objectName, String methodName, String returnValue) {
        if (!PRINT_ON) {
            return;
        }
        for (int i = 0; i < Skeleton.METHOD_DISTANCE; i++) {
            System.out.print("\t");
        }
        System.out.println(classIdentifier + ": " + objectName + " -> " + methodName + (returnValue != null ? " return: " + returnValue : ""));
    }

    /**
     * Kiírja a metódus hívását az elején
     *
     * @param classIdentifier a hívott osztály típusa
     * @param objectName      a hívott objektum neve
     * @param methodName      a hívott metódus neve
     */
    public static void printStart(String classIdentifier, String objectName, String methodName) {
        METHOD_DISTANCE += 1;
        print(classIdentifier, objectName, methodName, null);
    }

    /**
     * Kiírja a metódus hívását a végén
     *
     * @param classIdentifier a hívott osztály típusa
     * @param objectName      a hívott objektum neve
     * @param methodName      a hívott metódus neve
     * @param returnValue     a metódus visszatérési értéke
     */
    public static void printEnd(String classIdentifier, String objectName, String methodName, String returnValue) {
        print(classIdentifier, objectName, methodName, returnValue);
        METHOD_DISTANCE -= 1;
    }

    /**
     * Input scanner ami a felhasználótól kér majd bemenetet
     */
    Scanner input = new Scanner(System.in);

    /**
     * A játékot reprezentáló objektum
     */
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private Pump pu1, pu2;
    private Cistern ci1;
    private WaterSource ws1;
    private MechanicCharacter mc1, mc2, mc3;
    private SaboteurCharacter sc1;

    /**
     * Egy tesztesetet reprezentáló osztály
     */
    static class TestCase {
        public String name;
        public Runnable runnable;

        public TestCase(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }
    }

    /**
     * A teszteseteket tartalmazó lista
     */
    private ArrayList<TestCase> testCases;

    /**
     * A játékot reprezentáló objektumok inicializálása
     */
    private void init() {
        Skeleton.PRINT_ON = false;
        initMap();
        initCharacters();
        initTestCases();
        Skeleton.PRINT_ON = true;
    }

    /**
     * A térképen lévő tárgyak inicializálása
     */
    private void initMap() {
        // Create map elements
        pipes = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Pipe pipe = new Pipe();
            pipe.setName("pi" + i);
            pipes.add(pipe);
        }
        pu1 = new Pump("", null, new ArrayList<>(Arrays.asList(pipes.get(0), pipes.get(1), pipes.get(2), pipes.get(3))));
        pu1.setName("pu1");
        pu2 = new Pump(null);
        pu2.setName("pu2");
        ci1 = new Cistern("ci1", null, new ArrayList<>(Arrays.asList(pipes.get(2), pipes.get(3))));
        ci1.setName("ci1");
        ws1 = new WaterSource("ws1", new ArrayList<>(Arrays.asList(pipes.get(0), pipes.get(1))));
        ws1.setName("ws1");
    }

    /**
     * A karakterek inicializálása
     */
    private void initCharacters() {
        // Create characters
        mc1 = new MechanicCharacter("", pu1, pipes.get(4), null);
        mc1.setName("mc1");
        mc2 = new MechanicCharacter("", ci1);
        mc2.setName("mc2");
        mc3 = new MechanicCharacter("", pipes.get(2), null, pu2);
        mc3.setName("mc3");
        sc1 = new SaboteurCharacter("", pipes.get(0));
        sc1.setName("sc1");

        // Place characters on map
        pu1.accept(mc1);
        ci1.accept(mc2);
        pipes.get(2).accept(mc3);
        pipes.get(0).accept(sc1);
    }

    /**
     * A tesztesetek inicializálása
     */
    private void initTestCases() {
        testCases = new ArrayList<>();
        testCases.add(new TestCase("Szerelő magához vesz egy új csövet a ciszternán állva.", this::mech_pick_up_pipe_from_cistern));
        testCases.add(new TestCase("Szerelő magához vesz egy új pumpát a ciszternán állva.", this::mech_pick_up_pump_from_cistern));
        testCases.add(new TestCase("Szerelő megjavítja az elromlott pumpát.", this::mech_fix_pump));
        testCases.add(new TestCase("Szerelő leteszi a nála lévő pumpát egy csőre.", this::mech_place_pump_on_pipe));
        testCases.add(new TestCase("Szerelő megjavítja a törött csövet.", this::mech_fix_pipe));
        testCases.add(new TestCase("Szabotőr eltöri a csövet.", this::sab_break_pipe));
        testCases.add(new TestCase("Karakter pumpáról csőre lép.", this::char_moves_from_pump_to_pipe));
        testCases.add(new TestCase("Karakter csőről pumpára lép.", this::char_moves_from_pipe_to_pump));
        testCases.add(new TestCase("Karakter ciszternáról csőre lép.", this::char_moves_from_cistern_to_pipe));

        // 10
        testCases.add(new TestCase("Karakter csőről ciszternára lép.", this::char_moves_from_pipe_to_cistern));
        testCases.add(new TestCase("Karakter átállítja a pumpa outputját.", this::char_set_pump_output));
        testCases.add(new TestCase("Karakter átállítja a pumpa inputját.", this::char_set_pump_input));
        testCases.add(new TestCase("Karakter hozzáköti a nála lévő csövet a pumpához.", this::char_connect_pipe_to_pump));
        testCases.add(new TestCase("Karakter felveszi egy új csövet a ciszternánál.", this::char_pick_up_new_pipe_from_cistern));
        testCases.add(new TestCase("Karakter felveszi egy cső végét egy pumpából.", this::char_pick_up_the_end_of_a_pipe_from_pump));
        testCases.add(new TestCase("Forrásból a bekötött csőbe elindul egy vízfolyam.", this::water_flows_from_source));
        testCases.add(new TestCase("A pumpa tartályából elindul egy vízfolyam", this::water_flows_from_pump_to_pipe));
        testCases.add(new TestCase("Új pumpa és cső létrehozása a ciszternában.", this::create_pump_and_pipe));

    }

    /**
     * A tesztesetek menüjének kiírása
     */
    private void printMenu() {
        System.out.println("""

                =====================
                ||  TEST CASE MENU  ||
                =====================""");
        for (int i = 0; i < testCases.size(); i++) {
            System.out.println((i + 1) + ": " + testCases.get(i).name);
        }
        System.out.println("""
                0: Kilépés
                =====================
                Kérem válasszon tesztesetet:\s""");
    }

    /**
     * Teszteset sorszáma alapján a teszteset futtatása loop-olva
     */
    public void runMenuLoop() {
        while (true) {
            // Init map
            init();
            // Print menu
            printMenu();

            try {
                // Get selected testcase number
                int selected = input.nextInt();
                // Exit
                if (selected == 0) {
                    return;
                }
                // Run selected testcase
                else if (selected >= 1 && selected <= testCases.size()) {
                    testCases.get(selected - 1).runnable.run();
                }
                // Invalid testcase number
                else {
                    System.out.println("\nNem létező teszteset! Kérlek adj meg egy létező teszteset számát! \n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nNem számot adtál meg! Kérlek adj meg számot!");
            }

            // Press enter to continue
            System.out.println("\nNyomj entert, hogy visszakerülj a menübe");
            input.nextLine();
            input.nextLine();
        }

    }

    /**
     * Tesztesetek
     */

    private void mech_pick_up_pipe_from_cistern() {
        mc2.pickupNewPipe_mc();
    }

    private void mech_pick_up_pump_from_cistern() {
        mc2.pickupPump();
    }

    private void mech_fix_pump() {
        mc1.fix();
    }

    private void mech_place_pump_on_pipe() {
        mc3.placePumpToPipe();
    }

    private void mech_fix_pipe() {
        mc3.fix();
    }

    private void sab_break_pipe() {
        sc1.breakPipe();
    }

    private void char_moves_from_pump_to_pipe() {
        mc1.move(pipes.get(3));
    }

    private void char_moves_from_pipe_to_pump() {
        mc3.move(pu1);
    }

    private void char_moves_from_cistern_to_pipe() {
        mc2.move(pipes.get(2));
    }

    private void char_moves_from_pipe_to_cistern() {
        mc3.move(ci1);
    }

    private void char_set_pump_output() {
        System.out.println("Kérem válasszon pumpa output cső sorszámot (1 - 4): ");
        int selected = input.nextInt();
        mc1.setPumpOut(selected - 1);
    }

    private void char_set_pump_input() {
        System.out.println("Kérem válasszon pumpa input cső sorszámot (1 - 4): ");
        int selected = input.nextInt();
        mc1.setPumpInput(selected - 1);
    }

    private void char_connect_pipe_to_pump() {
        // TODO: mc1-nél kell hogy legyen egy cső (szekvencián pi6)
        mc1.attachPipeToPump();
    }

    private void char_pick_up_new_pipe_from_cistern() {
        mc2.pickupNewPipe_mc();
    }

    private void char_pick_up_the_end_of_a_pipe_from_pump() {
        System.out.println("Kérem válasszon cső sorszámot: (1-4) ");
        int selected = input.nextInt();
        mc1.pickupPipe_c(selected - 1);
    }

    private void water_flows_from_source() {
        ws1.step();
    }

    private void water_flows_from_pump_to_pipe() {
        pu1.step();
    }

    private void create_pump_and_pipe() {
        ci1.step();
    }

}
