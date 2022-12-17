import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Day07 {

    public static final String FILE_NAME = "Day07_IN.txt";

    public static void main(String[] args) {

        Node rootNode = new Node(null, "", 0);
        Node currentNode = rootNode;

        try (Scanner s = new Scanner(new FileReader(FILE_NAME))) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("$ ")) {
                    String cmd = line.replace("$ ", "");
                    if (cmd.startsWith("cd")) {
                        String dirName = cmd.split(" ")[1];

                        if (dirName.equals("/")) {
                            currentNode = rootNode;

                        } else if (dirName.equals("..")) {
                            currentNode = currentNode.getParent();

                        } else {
                            Optional<Node> nextNodeOptional = currentNode.getChildren()
                                    .stream()
                                    .filter(n -> n.getName().equals(dirName))
                                    .findFirst();
                            if (nextNodeOptional.isPresent()) {
                                currentNode = nextNodeOptional.get();
                            } else {
                                Node nextNode = new Node(currentNode, dirName, 0);
                                currentNode.getChildren().add(nextNode);
                                currentNode = nextNode;
                            }
                        }
                    }
                } else {
                    String[] splitLine = line.split(" ");
                    if (splitLine[0].equals("dir")) {
                        currentNode.getChildren().add(new Node(currentNode, splitLine[1], 0));
                    } else {
                        currentNode.getChildren().add(new Node(currentNode, splitLine[1], Integer.parseInt(splitLine[0])));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        printRecursively(rootNode, 0);

        int sum = searchSmallerThanRecursively(rootNode, 100000).stream().mapToInt(Node::getSize).sum();
        System.out.println(sum);

        int freeSpace = 70000000 - rootNode.getSize();
        int reqSpace = 30000000 - freeSpace;

        Node node = searchSuitableRecursively(rootNode, reqSpace);
        if (node != null) System.out.println(node.getSize());
    }

    /**
     * To begin, find all of the directories with a total size of at most 100000,
     * then calculate the sum of their total sizes. In the example above, these
     * directories are a and e; the sum of their total sizes is 95437 (94853 + 584).
     * (As in this example, this process can count files more than once!)
     *
     * @param node
     * @param maxSize
     * @return
     */
    private static List<Node> searchSmallerThanRecursively(Node node, int maxSize) {
        List<Node> smallerChildren = new ArrayList<>();
        for (Node child : node.getChildren()) {
            if (!child.getChildren().isEmpty()) {
                if (child.getSize() <= maxSize) {
                    smallerChildren.add(child);
                }
                smallerChildren.addAll(searchSmallerThanRecursively(child, maxSize));
            }
        }
        return smallerChildren;
    }

    /**
     * The total disk space available to the filesystem is 70000000. To run the update,
     * you need unused space of at least 30000000. You need to find a directory you can
     * delete that will free up enough space to run the update.
     *
     * @param node
     * @param targetSize
     */
    private static Node searchSuitableRecursively(Node node, int targetSize) {
        Node bestOption = null;
        if (node.getSize() > targetSize && !node.getChildren().isEmpty()) {
            System.out.println(node.getName() + ": " + node.getSize());
            bestOption = node;
            for (Node child : node.getChildren()) {
                Node bestSubOption = searchSuitableRecursively(child, targetSize);
                if (bestSubOption != null && bestSubOption.getSize() < bestOption.getSize()) bestOption = bestSubOption;
            }
        }
        return bestOption;
    }

    private static void printRecursively(Node node, int depth) {
        if (!node.getChildren().isEmpty()) {
            System.out.printf("%s/ (%d)%n", padLeft(node.getName(), " ", depth * 2), node.getSize());
        } else {
            System.out.printf("%s (%d)%n", padLeft("+ " + node.getName(), " ", depth * 2), node.getSize());
        }
        node.getChildren().forEach(n -> printRecursively(n, depth + 1));
    }

    private static String padLeft(String string, String character, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(character);
        }
        sb.append(string);
        return sb.toString();
    }

    public static class Node {

        public final Node parent;
        public final List<Node> children = new ArrayList<>();
        public final int size;
        private final String name;

        public Node(Node parent, String name, int size) {
            this.parent = parent;
            this.name = name;
            this.size = size;
        }

        public Node getParent() {
            return parent;
        }

        public List<Node> getChildren() {
            return children;
        }

        public int getSize() {
            return (children.size() > 0) ? children.stream().mapToInt(Node::getSize).sum() : size;
        }

        public String getName() {
            return name;
        }
    }


}
