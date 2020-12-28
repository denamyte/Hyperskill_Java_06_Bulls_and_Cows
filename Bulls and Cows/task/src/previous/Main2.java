package previous;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main2 {

    /** This is a completely solved puzzle (was solved on Stage 1),
     * improved and reduced to comply the Stage 2 objective. */
    static class BullsCows {

        private static final int LENGTH = 4;
        private int moveNumber;
        private String answer;  // Current answer
        private char[] secret;  // The original secret, for search of the exact position of chars
        private char[] secretSorted;  // The sorted secret, for easy search of chars regardless of their position

        private final Counter bulls = new Counter("bull");
        private final Counter cows = new Counter("cow");

        public void prepareSecretCode(String code) {  // the param is for debugging purposes
            if (code == null) {
                code = String.format("%04d", new Random().nextInt(10000));
            }
            secret = String.valueOf(code).toCharArray();
            secretSorted = Arrays.copyOf(secret, LENGTH);
            Arrays.sort(secretSorted);
        }

        private void nextMove(String answer) {
            if (answer.length() != LENGTH)
                throw new AssertionError();
            moveNumber++;
            this.answer = answer;
            compare();
            System.out.println(renderLog());
        }

        private void compare() {
            Stream.of(bulls, cows).forEach(Counter::clear);
            for (int i = 0; i < LENGTH; i++) {
                char key = answer.charAt(i);
                if (key == secret[i]) {
                    bulls.inc();
                } else if (Arrays.binarySearch(secretSorted, key) > -1) {
                    cows.inc();
                }
            }
        }

        public String renderLog() {
            StringBuilder sb = new StringBuilder();
//            sb.append("Turn ").append(moveNumber).append(". Answer:\n")
//                    .append(answer).append('\n');
            sb.append("Grade: ")
                    .append(bulls)
                    .append(Counter.getAndOrNoneOrEmpty(bulls, cows))
                    .append(cows)
                    .append('.');
//            if (bulls.count == LENGTH) {
//                sb.append("\nCongrats! The secret code is ").append(secret).append(".");
//            } else {
//                sb.append('\n');
//            }
            sb.append(" The secret code is ").append(secret).append('.');
            return sb.toString();
        }
    }

    /** The counter class for bulls and cows. */
    static class Counter {

        final String name;
        int count;

        Counter(String name) {
            this.name = name;
        }

        void inc() {
            count++;
        }

        void clear() {
            count = 0;
        }

        @Override
        public String toString() {
            return count > 0 ? String.format("%d %s%s", count, name, "(s)") : "";
        }

        /** Some special tricks for the log. */
        static String getAndOrNoneOrEmpty(Counter m1, Counter m2) {
            return m1.count > 0 && m2.count > 0 ? " and "
                    : m1.count == 0 && m2.count == 0 ? "None"
                    : "";
        }
    }

    public static void main(String[] args) {
        BullsCows puzzle = new BullsCows();
        puzzle.prepareSecretCode("9305");
        puzzle.nextMove(new Scanner(System.in).nextLine());
    }
}
