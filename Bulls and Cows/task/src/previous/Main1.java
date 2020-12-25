package previous;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main1 {

    static class BullsCows {

        private static final int LENGTH = 4;
        private int moveNumber;
        private String answer;  // Current answer
        private char[] secret;  // The original secret, for search of the exact position of chars
        private char[] secretSorted;  // The sorted secret, for easy search of chars regardless of their position

        private MammalCounter bulls = new MammalCounter("bull", 0);
        private MammalCounter cows = new MammalCounter("cow", 0);
        StringBuilder sb = new StringBuilder();

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
            compare(answer.toCharArray());
            System.out.println(renderLog());
        }

        /** The heart of everything, comparison of the secret and the answer */
        private void compare(char[] answerChars) {
            bulls = bulls.clear();
            cows = cows.clear();
            // We need a copy because we may change it later
            char[] secretSortedTemp = Arrays.copyOf(secretSorted, LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                char subj = answerChars[i];
                if (subj == secret[i]) {
                    search(secretSortedTemp, subj, () -> bulls = bulls.inc());
                } else {
                    search(secretSortedTemp, subj, () -> cows = cows.inc());
                }
            }
        }

        private void search(char[] sorted, char ch, Runnable action) {
            int index = Arrays.binarySearch(sorted, ch);
            if (index > -1) {
                // If it appears in the future stages that repeating characters is prohibited,
                // there will be no need to replace characters and sort array here
                sorted[index] = ' ';
                Arrays.sort(sorted);
                action.run();
            }
        }

        public String renderLog() {
            sb.setLength(0);
            sb.append("Turn ").append(moveNumber).append(". Answer:\n")
                    .append(answer)
                    .append("\nGrade: ")
                    .append(bulls)
                    .append(MammalCounter.getAndOrNoneOrEmpty(bulls, cows))
                    .append(cows)
                    .append('.');
            if (bulls.count == LENGTH) {
                sb.append("\nCongrats! The secret code is ").append(secret).append(".");
            } else {
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    /** The counter class for bulls and cows. Immutable. The name is just for fun :) */
    static class MammalCounter {

        final String name;
        final int count;

        MammalCounter(String name, int count) {
            this.name = name;
            this.count = count;
        }

        MammalCounter inc() {
            return new MammalCounter(name, count + 1);
        }

        MammalCounter clear() {
            return new MammalCounter(name, 0);
        }

        @Override
        public String toString() {
            return count > 0 ? String.format("%d %s%s", count, name, count > 1 ? "s" : "") : "";
        }

        /** Some special tricks for logs. */
        static String getAndOrNoneOrEmpty(MammalCounter m1, MammalCounter m2) {
            return m1.count > 0 && m2.count > 0 ? " and "
                    : m1.count == 0 && m2.count == 0 ? "None"
                    : "";
        }
    }

    static List<String> moves = List.of(
            "1234", "5678", "9012", "9087", "1087", "9205", "9305"
//            "1234", "9876"
    );

    public static void main(String[] args) {
        BullsCows game = new BullsCows();
        game.prepareSecretCode("9305");
//        game.prepareSecretCode("9876");
        System.out.println("The secret code is prepared: ****.\n");
        moves.forEach(game::nextMove);
    }
}
