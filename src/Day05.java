import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05 {

    private static List<Stack<String>> stacks;

    public static void main(String[] args) {
        String configuration;
        String movements;

        try (Scanner s = new Scanner(new FileReader("Day05_IN.txt"))) {
            String line = "NIL";

            StringBuilder confBuilder = new StringBuilder();
            while (s.hasNextLine() && !(line = s.nextLine()).isEmpty()) {
                confBuilder.append(line).append("\n");
            }
            configuration = confBuilder.toString();

            StringBuilder movementBuilder = new StringBuilder();
            while (s.hasNextLine()) {
                line = s.nextLine();
                movementBuilder.append(line).append("\n");
            }
            movements = movementBuilder.toString();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        stacks = parseConfiguration(configuration);

        List<int[]> tasks = new ArrayList<>();
        for (String movement : movements.split("\n")) {
            Matcher matcher = Pattern.compile("[0-9]+").matcher(movement);
            List<String> matchResults = matcher.results().map(MatchResult::group).toList();

            int targets = Integer.parseInt(matchResults.get(0));
            int from = Integer.parseInt(matchResults.get(1));
            int to = Integer.parseInt(matchResults.get(2));
            int[] task = {targets, from, to};
            tasks.add(task);
        }
        System.out.println();

        printStatusVertical(stacks);
        for (int[] task : tasks) {
            moveInOrder(task[0], task[1], task[2]);
        }

        System.out.println("Uppermost crates: " + stacks.stream().map(Stack::peek).reduce(String::concat).orElse("?!"));

    }

    private static void move(int count, int from, int to) {
        System.out.printf("%d =[%dx]=> %d%n", from, count, to);
        for (int i = 0; i < count; i++) {
            String item = stacks.get(from - 1).pop();
            stacks.get(to - 1).push(item);
        }
        printStatusVertical(stacks);
    }

    private static void moveInOrder(int count, int from, int to) {
        System.out.printf("🏗️ %d =[%dx]=> %d%n", from, count, to);
        List<String> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(stacks.get(from - 1).pop());
        }
        for (int i = items.size() - 1; i >= 0; i--) {
            stacks.get(to - 1).push(items.get(i));
        }

        printStatusVertical(stacks);
    }

    private static void printStatusHorizontal(List<Stack<String>> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            System.out.print(i + 1 + " ");
            for (String item : stacks.get(i)) {
                System.out.printf("[%s]", item);
            }
            System.out.println();
        }
        System.out.println();

    }

    private static void printStatusVertical(List<Stack<String>> stacks) {
        int maxLength = stacks.stream().mapToInt(Collection::size).max().orElse(0);
        StringBuilder sb = new StringBuilder();
        for (int i = maxLength - 1; i >= 0; i--) {
            for (Stack<String> stack : stacks) {
                if (stack.size() > i) {
                    sb.append(String.format("[%s] ", stack.get(i)));
                } else {
                    sb.append("    ");
                }
            }
            sb.append("\n");
        }
        for (int i = 0; i < stacks.size(); i++) {
            sb.append(String.format(" %d  ", i + 1));
        }
        sb.append("\n");
        System.out.println(sb);

    }

    private static List<Stack<String>> parseConfiguration(String configuration) {
        List<Stack<String>> stacks = new ArrayList<>();

        String[] configLines = configuration.split("\n");
        for (int i = configLines.length - 1; i >= 0; i--) {
            String row = configLines[i];
            if (Pattern.matches("((\\s)*(\\[[A-Z]\\]))+", row)) {
                int offset = 0;
                int index = 0;
                while (offset < row.length()) {
                    String item = row.substring(offset + 1, offset + 2);
                    if (stacks.size() < index + 1) stacks.add(new Stack<>());
                    if (!item.replaceAll(" ", "").isEmpty()) stacks.get(index).push(item);

                    index++;
                    offset += 4;
                }
            }
        }
        return stacks;
    }


}
