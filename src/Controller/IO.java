package Controller;

import Model.Nameable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Class for handling input and output.
 */
public class IO {
    public static Scanner input = new Scanner(System.in);
    public static PrintStream output = System.out;
    public static Scanner fileInput;
    public static PrintStream fileOutput;
    public static boolean isFileInput = false;
    public static boolean isFileOutput = false;

    ////////////////////////////////////////////
    // Basic methods for reading and writing  //
    ////////////////////////////////////////////

    public static String readLine() {
        if (isFileInput) {
            // Check if file input has next line
            if (!fileInput.hasNextLine()) {
                isFileInput = false;
                fileInput.close();
                return null;
            }
            // Read line from file
            return fileInput.nextLine();
        } else {
            return input.nextLine();
        }
    }

    public static String readLine(String message) {
        write(message);
        return readLine();
    }

    public static void setFileInput(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        // Check if file exists
        if (!file.exists()) throw new FileNotFoundException("File not found: " + fileName);

        // Set file input
        IO.fileInput = new Scanner(file);
        isFileInput = true;
    }

    public static void setFileOutput(String fileName) throws FileNotFoundException {
        IO.fileOutput = new PrintStream(fileName);
        isFileOutput = true;
    }

    public static void write(Object x) {
        String s = String.valueOf(x);
        if (isFileOutput) {
            fileOutput.print(s);
        } else {
            output.print(s);
        }
    }

    public static void writeLine(Object x) {
        write(x);
        write("\n");
    }

    public static void closeFiles() {
        if (isFileInput) {
            fileInput.close();
        }
        if (isFileOutput) {
            fileOutput.close();
        }
    }

    ////////////////////////////////
    // Read / Write game commands //
    ////////////////////////////////

    public static void writeAction(boolean success, Nameable what, String action) {
        if (success) {
            writeLine(what.getName() + " sikeresen " + getActionDesc(action.charAt(0)));
        } else {
            writeLine(what.getName() + " NEM sikeresen " + getActionDesc(action.charAt(0)));
        }
    }

    public static void writeActionError(String action, String error) {
        writeLine("Hiba történt a(z) " + action + " során: " + error);
    }

    public static String readInit() {
        return readLine();
    }

    public static ArrayList<String> readAllInit() {
        ArrayList<String> list = new ArrayList<>();
        String line = readInit();
        while (line != null && !line.equals("---") && !line.equals("-") && !line.equals("—") && !line.equals("")) {
            line = line.trim().replaceAll("\\s+", " ");
            list.add(line);
            line = readLine();
        }
        return list;
    }

    public static String readCommand() {
        return readLine();
    }

    public static String readCommand(String message) {
        return readLine(message);
    }

    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isBoolean(String strBool) {
        return strBool.equals("true") || strBool.equals("false");
    }

    private static String getActionDesc(char action) {
        return switch (action) {
            case 'm' -> "mozgott";
            case 'i' -> "input csövet állított";
            case 'o' -> "output csövet állított";
            case 'h' -> "csövet lyukasztott";
            case 'f' -> "javított";
            case 'p' -> "csövet vett fel";
            case 'n' -> "új csövet vett fel";
            case 'b' -> "új pumpát vett fel";
            case 'a' -> "csövet kötött rá";
            case 'q' -> "pumpát rakott a csőre";
            case 's' -> "csúszóssá tette a csövet";
            case 't' -> "ragadóssá tette a csövet";
            case 'x' -> "passzolt";
            default -> "(hibás parancsot adott meg!)";
        };
    }
}
