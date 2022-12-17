import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day17 {

    public static final int CHAMBER_WIDTH = 7;
    public static final int SIM_LENGTH = 10;
    public static void main(String[] args) {
        List<char[][]> rocks = readRocks("Day17_rocks.txt");

        List<char[]> cave = new ArrayList<>();
        spawnRock(cave, rocks.get(0));
    }

    private static void spawnRock(List<char[]> cave, char[][] rock) {
        int height = 0;
        
    }

    private static List<char[][]> readRocks(String fileName) {
        List<String> rocks = new ArrayList<>();
        try(Scanner s = new Scanner(new FileReader(fileName))) {

            String line;
            StringBuilder rockBuilder = new StringBuilder();
            while(s.hasNextLine()) {
                line = s.nextLine();
                if(!line.isEmpty()) {
                    rockBuilder.append(line).append("\n");
                } else {
                    rocks.add(rockBuilder.toString());
                    rockBuilder = new StringBuilder();
                }
            }
            rocks.add(rockBuilder.toString());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return rocks.toArray(new String[0]);
    }

}
