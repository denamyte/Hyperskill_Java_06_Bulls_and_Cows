package previous;

import java.util.*;
import java.util.stream.Stream;

public class Main4 {

    static class BullsCows {

        Scanner scanner = new Scanner(System.in);
        private final int length;
        private int moveNumber;
        private char[] secret;  // The original secret, for search of the exact position of chars
        private char[] secretSorted;  // The sorted secret, for easy search of chars regardless of their position

        private final Counter bulls = new Counter("bull");
        private final Counter cows = new Counter("cow");

        public BullsCows() {
            System.out.println("Please, enter the secret code's length:");
            length = Integer.parseInt(scanner.nextLine());
            if (length > 10) {
                System.out.println("[Error]");
                System.exit(-1);
            }
            prepareSecretCode();
        }

        public void prepareSecretCode() {
            secret = generateSecret(length);
            secretSorted = Arrays.copyOf(secret, length);
            Arrays.sort(secretSorted);
        }

        public static char[] generateSecret(int count) {
            ArrayList<String> digits = new ArrayList<>(Arrays.asList("123456789".split("")));
            Collections.shuffle(digits);
            int index = 1 | (int) (System.nanoTime() % 10);  // Generating a random number in range [1..10)
            digits.add(index, "0");
            return String.join("", digits.subList(0, count)).toCharArray();
        }

        public void play() {
            while (!isFinished()) {
                moveNumber++;
                System.out.println(currentStats());
                System.out.printf("Turn %d:%n", moveNumber);
                compare(scanner.nextLine());
            }
            System.out.println(currentStats());
            System.out.println("Congratulations! You guessed the secret code.");
        }

        private String currentStats() {
            return moveNumber == 1
                    ? "Okay, let's start a game!"
                    : "Grade: " + bulls + Counter.getAndOrNoneOrEmpty(bulls, cows) + cows;
        }

        private void compare(String answer) {
            Stream.of(bulls, cows).forEach(Counter::clear);
            for (int i = 0; i < length; i++) {
                char key = answer.charAt(i);
                if (key == secret[i]) {
                    bulls.inc();
                } else if (Arrays.binarySearch(secretSorted, key) > -1) {
                    cows.inc();
                }
            }
        }

        public boolean isFinished() {
            return bulls.count == length;
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
            return count > 0 ? String.format("%d %s%s", count, name, count > 1 ? "s" : "") : "";
        }

        /** Some special tricks for the log. */
        static String getAndOrNoneOrEmpty(Counter m1, Counter m2) {
            return m1.count > 0 && m2.count > 0 ? " and "
                    : m1.count == 0 && m2.count == 0 ? "None"
                    : "";
        }
    }

    public static void main(String[] args) {
        new BullsCows().play();
    }
}
