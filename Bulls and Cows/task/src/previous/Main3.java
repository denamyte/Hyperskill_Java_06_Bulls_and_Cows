package previous;

import java.util.*;
import java.util.stream.Stream;

public class Main3 {

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
                code = generateSecret(LENGTH);
            }
            secret = code.toCharArray();
            secretSorted = Arrays.copyOf(secret, LENGTH);
            Arrays.sort(secretSorted);
        }

        public static String generateSecret(int count) {
            if (count-- > 10) {
                System.out.println("Error.");
            }
            Random rnd = new Random();
            List<Character> chars = new ArrayList<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0'));
            StringBuilder code = new StringBuilder("" + chars.remove(rnd.nextInt(9)));
            while (count > 0) {
                code.append(chars.remove(rnd.nextInt(count--)));
            }
            return code.toString();
        }

        public static String generateSecret2(int count) {
            if (count > 10) {
                return "[Error]";
            }
            ArrayList<String> chars = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
            Collections.shuffle(chars);
            int index = 1 | (int) (System.nanoTime() % 10);
            chars.add(index, "0");
            return String.join("", chars.subList(0, count));
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
        String s = BullsCows.generateSecret(new Scanner(System.in).nextInt());
        System.out.printf("The random secret number is %s.%n", s);
//        for (int i = 0; i < 10; i++) {
//            System.out.printf("The random secret number is %s.%n", BullsCows.generateSecret(10));
//        }
    }
}
