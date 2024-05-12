/**
 * Ezt a teszt struktúrát a ChatGPT támogatta.
 */

import Controller.Board;
import Controller.IO;
import Controller.Prototype;
import Controller.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class PrototypeTest {

    @BeforeAll
    static void init() {
        IO.writeLine("Initializing test...\n");
        Settings.isTesting = true;
    }

    @TestFactory
    List<DynamicTest> dynamicTests() throws FileNotFoundException {

        // Get all file names from input folder
        File input_folder = new File("./test/input");
        File output_folder = new File("./test/output");
        File[] input_files = input_folder.listFiles();

        // Validate folders and input_files
        if (!input_folder.isDirectory() || !output_folder.isDirectory() || input_files == null) {
            throw new FileNotFoundException("Input or output folder not found");
        }

        // Print founded files info
        IO.writeLine("Input folder: " + input_folder.getPath());
        IO.writeLine("Founded files in input folder: " + input_files.length + "\n");
        IO.writeLine("Output folder: " + output_folder.getPath());
        IO.writeLine("Founded files in output folder: " + Objects.requireNonNull(output_folder.listFiles()).length + "\n");

        // Create dynamic tests list
        List<DynamicTest> dynamicTests = new ArrayList<>();

        // Iterate through all input files
        for (File input_file : input_files) {

            // Check if file exists
            if (!input_file.isFile()) {
                IO.writeLine(input_file.getName() + " is not a file");
                continue;
            }

            // Found output file
            File output_file = new File(output_folder.getPath() + "/" + input_file.getName());

            // Check if output file exists
            if (!output_file.isFile()) {
                IO.writeLine(output_file.getName() + " is not a file");
                continue;
            }

            // Create dynamic test
            DynamicTest dynamicTest = dynamicTest("Testing file: " + input_file.getName(),
                    () -> {
                        // Print current test info
                        IO.writeLine("\n--------------------------- TESTING " + input_file.getName() + "---------------------------\n");
                        IO.writeLine("Input file: " + input_file.getPath());
                        IO.writeLine("Output file: " + output_file.getPath());

                        // Set file input for IO
                        IO.setFileInput(input_file.getPath());

                        // Create prototype
                        Prototype prototype = new Prototype();

                        // Try to init prototype (create map)
                        try {
                            prototype.init();
                        } catch (Exception e) {
                            IO.writeLine("Error in input file: " + input_file.getName());
                            throw e;
                        }

                        // Try to run prototype
                        try {
                            prototype.run();
                        } catch (Exception e) {
                            IO.writeLine("Error while running actions: " + input_file.getName());
                            throw e;
                        }

                        // Create expected map and read it from output file
                        Board expected_map = new Board();
                        try {
                            IO.setFileInput(output_file.getPath());
                            expected_map.createFromInput();
                        } catch (Exception e) {
                            IO.writeLine("Error in output file: " + input_file.getName());
                            throw e;
                        } finally {
                            IO.closeFiles();
                        }

                        // Compare maps
                        assertEquals(expected_map, prototype.map);
                    });

            // Add dynamic test to list
            dynamicTests.add(dynamicTest);
        }

        return dynamicTests;
    }
}
