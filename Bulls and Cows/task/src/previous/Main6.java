package previous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main6 {

    static class BullsCows {

        Scanner scanner = new Scanner(System.in);
        private final SecretCode code;
        private int moveNumber;
        private final Counter bulls = new Counter("bull");
        private final Counter cows = new Counter("cow");

        public BullsCows() {
            System.out.println("Input the length of the secret code:");
            int size = Integer.parseInt(scanner.nextLine());
            if (size > 36) {
                System.out.println("[Error]");
                System.exit(-1);
            }
            System.out.println("Input the number of possible symbols in the code:");
            int usedCharsSize = Integer.parseInt(scanner.nextLine());
            code = new SecretCode(size, usedCharsSize);
            System.out.println(code.getInfoString());
        }

        public void play() {
            while (!isFinished()) {
                moveNumber++;
                System.out.println(currentStats());
                System.out.printf("Turn %d:%n", moveNumber);
                matchCodeAndAnswer(scanner.nextLine());
            }
            System.out.println(currentStats());
            System.out.println("Congratulations! You guessed the secret code.");
        }

        private String currentStats() {
            return moveNumber == 1
                    ? "Okay, let's start a game!"
                    : "Grade: " + bulls + Counter.getConnective(bulls, cows) + cows;
        }

        private void matchCodeAndAnswer(String answer) {
            Stream.of(bulls, cows).forEach(Counter::clear);
            for (int i = 0; i < code.size; i++) {
                char key = answer.charAt(i);
                if (key == code.secret[i]) {
                    bulls.inc();
                } else if (Arrays.binarySearch(code.secretSorted, key) > -1) {
                    cows.inc();
                }
            }
        }

        public boolean isFinished() {
            return bulls.count == code.size;
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
        static String getConnective(Counter c1, Counter c2) {
            return c1.count > 0 && c2.count > 0 ? " and "
                    : c1.count == 0 && c2.count == 0 ? "None"
                    : "";
        }
    }

    static class SecretCode {
        static String chars = "0123456789abcdefghijklmnopqrstuvwxyz";

        public final int size;
        public final int usedCharsSize;
        private char[] secret;  // The original secret, for search of the exact position of chars
        private char[] secretSorted;  // The sorted secret, for easy search of chars regardless of their position

        public SecretCode(int size, int usedCharsSize) {
            this.size = size;
            this.usedCharsSize = usedCharsSize;
            generate();
        }

        public final void generate() {
            ArrayList<String> usedChars = new ArrayList<>(
                    Arrays.asList(chars.substring(0, usedCharsSize).split("")));
            Collections.shuffle(usedChars);
            secret = String.join("", usedChars.subList(0, size)).toCharArray();
            secretSorted = Arrays.copyOf(secret, size);
            Arrays.sort(secretSorted);
        }

        public String getInfoString() {
            String digitPart = usedCharsSize == 1 ? "1" :
                    "0-" + (usedCharsSize <= 10 ? chars.charAt(usedCharsSize - 1) : '9');
            String letterPart = usedCharsSize < 10 ? "" :
                    usedCharsSize == 11 ? ", a" : ", a-" + chars.charAt(usedCharsSize - 1);
            return String.format("The secret is prepared: %s (%s%s).",
                                 "*".repeat(size), digitPart, letterPart);
        }
    }

    public static void main(String[] args) {
        new BullsCows().play();
    }
}
