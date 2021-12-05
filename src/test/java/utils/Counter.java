package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Counter {

    private static String path = "src/test/resources/";

    private String fileName;

    public Counter(String fileName) {
        this.fileName = fileName;
    }

    public int readCounterValue () throws IOException {
        java.nio.file.Path filePath = Paths.get(path + fileName);
        File file = new File(path+fileName);
        if (file.length() == 0 ) {
            return 0;
        }
        else {
            return Integer.parseInt(Files.readAllLines(filePath).get(0));
        }
    }

    public void writeIncrement () throws IOException {
        int increment = readCounterValue()+1;
            File statText = new File(path + fileName);
            PrintWriter writer = new PrintWriter(statText);
            writer.println(increment);
            writer.close();
    }

}
