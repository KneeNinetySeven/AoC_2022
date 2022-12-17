import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Day01 {

    public static void main(String[] args) {

        StringBuilder sb = new StringBuilder();
        try(FileReader fr = new FileReader("Day01_IN.txt")) {
            int r;
            while((r = fr.read()) != -1){
                sb.append((char) r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> calList = new ArrayList<>();
        int currentInventory = 0;
        for (String line : sb.toString().split("\n")) {
            System.out.println(line);
            if(Objects.equals(line, "")) {
                System.out.println("New Inventory:");
                calList.add(currentInventory);
                currentInventory = 0;
            } else {
                System.out.print("Add " + currentInventory + " + " + line + " = ");
                currentInventory += Integer.parseInt(line);
                System.out.println(currentInventory);
            }
        }

        System.out.print("\n\n** Most calories carried: ");
        Integer max = Collections.max(calList);
        System.out.println(max);

        // ---------- PART 2 ----------------------------------------------------------
        System.out.println("** Top 3 inventories (excluding first): ");
        Stream<Integer> limit = calList.stream().sorted(Comparator.reverseOrder()).limit(3);
        int sum = limit.mapToInt(v -> v).sum();
        System.out.println("Summing up to " + sum);

    }

}
