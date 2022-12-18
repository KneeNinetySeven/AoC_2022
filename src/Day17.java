import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day17 {

    public static final int CHAMBER_WIDTH = 7;
    public static final int SIM_LENGTH = 20;
    private static List<char[][]> rocks;

    public static void main(String[] args) {
        rocks = readRocks("Day17_rocks.txt");

        List<char[]> cave = new ArrayList<>();
        int anchorX = 2;
        int anchorY = 0;
        char[][] currentRock = spawnRock(cave);
        print(cave, currentRock, anchorX, anchorY);
        int rockCount = 0;
        while (rockCount < SIM_LENGTH) {
            //simulate
            if (anchorY < cave.size() - 1 && !isLineInUseBy(cave.get(cave.size() - currentRock.length - anchorY - 1), '#')) {
                anchorY++;
            } else {
                freeze(anchorX, anchorY, currentRock, cave);
                currentRock = spawnRock(cave);
                anchorY = 0;
                anchorX = 2;
                rockCount++;
            }
        }
        print(cave, currentRock, anchorX, anchorY);
    }

    private static int rockIndex = 0;

    private static char[][] spawnRock(List<char[]> cave) {
        char[][] rock = rocks.get(rockIndex);
        rockIndex = rockIndex + 1 > rocks.size() - 1 ? 0 : rockIndex + 1;

        int stackHeight = 0;
        while (stackHeight < cave.size() - 1 && isLineInUseBy(cave.get(stackHeight), '#')) {
            stackHeight++;
        }
        int spawnHeight = stackHeight + 3;

        if ((spawnHeight + rock.length) > cave.size()) {
            int requiredExtension = (spawnHeight + rock.length) - cave.size();
            extend(cave, requiredExtension);
        }

        return rock;
    }

    private static void freeze(int anchorX, int anchorY, char[][] currentRock, List<char[]> cave) {
        for (int y = 0; y < currentRock.length; y++) {
            for (int x = 0; x < currentRock[y].length; x++) {
                setInCave(cave, y + anchorY, x + anchorX, currentRock[y][x]);
            }
        }
    }

    private static void setInCave(List<char[]> cave, int line, int index, char character) {
        if (character != '.') cave.get(cave.size() - 1 - line)[index] = '#';
    }

    private static void extend(List<char[]> cave, int count) {
        for (int i = 0; i < count; i++) {
            char[] newLine = new char[CHAMBER_WIDTH];
            Arrays.fill(newLine, '.');
            cave.add(newLine);
        }
    }

    private static boolean isLineInUseBy(char[] line, char usedBy) {
        for (char c : line) {
            if (c == usedBy) return true;
        }
        return false;
    }

    private static List<char[][]> readRocks(String fileName) {
        List<char[][]> rocks = new ArrayList<>();
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            String line;
            List<char[]> thisRock = new ArrayList<>();
            while (s.hasNextLine()) {
                line = s.nextLine();
                if (!line.isEmpty()) {
                    thisRock.add(line.toCharArray());
                } else {
                    rocks.add(thisRock.toArray(new char[][]{}));
                    thisRock.clear();
                }
            }
            rocks.add(thisRock.toArray(new char[][]{}));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return rocks;
    }

    private static void print(List<char[]> cave, char[][] currentRock, int anchorX, int anchorY) {
        if (cave.size() == 0) System.out.println("NONE");
        for (int y = 0; y < cave.size(); y++) {
            System.out.print("|");
            char[] chars = cave.get(cave.size() - 1 - y);
            for (int x = 0; x < chars.length; x++) {
                if (
                        anchorY <= y
                                && y < currentRock.length + anchorY
                                && anchorX <= x
                                && x < currentRock[y - anchorY].length + anchorX
                ) {
                    System.out.print(currentRock[y - anchorY][x - anchorX] == '#' ? '@' : chars[x]);
                } else {
                    System.out.print(chars[x]);
                }
            }
            System.out.println("|");
        }
        System.out.print("+");
        for (int i = 0; i < CHAMBER_WIDTH; i++) {
            System.out.print("-");
        }
        System.out.println("+\n");
    }

}
