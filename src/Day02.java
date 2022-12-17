import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02 {

    public static void main(String[] args) {
        // The score for a single round is the score for the shape you selected (1 for Rock, 2 for Paper, and 3 for Scissors)
        // plus the score for the outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you won)
        int score = 0;
        try (Scanner s = new Scanner(new FileInputStream("Day02_IN.txt"))) {
            while (s.hasNextLine()) {
                char[] line = s.nextLine().toCharArray();

                int player = line[0] - 65;
                int target = line[2] - 88;

                int me;
                if (target == 0) {
                    // LOOSE
                    System.out.println("I shall loose");
                    me = (player - 1) % 3;
                } else if (target == 1) {
                    // DRAW
                    System.out.println("I shall draw");
                    me = player;
                } else {
                    // WIN
                    System.out.println("I shall win");
                    me = (player + 1) % 3;
                }
                me = target;

                System.out.printf("Player:%d vs Me:%d%n", player + 1, me + 1);
                System.out.println("Score +" + (me + 1));
                score += (me + 1);
                if (player == me) {
                    // DRAW
                    System.out.println("DRAW! score +3");
                    score += 3;
                } else if (isMyWin(player, me)) {
                    // WIN
                    System.out.println("WIN! score +6");
                    score += 6;
                } else {
                    // LOOSE
                    System.out.println("I LOOSE! score +0");
                }

                System.out.printf("New score: %d%n---------------------------%n", score);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isMyWin(int player, int me) {
        return me != player
                && ((player == 0 && me == 1)
                || (player == 1 && me == 2)
                || (player == 2 && me == 0));
    }
}
