package bullscows;

public class Main {

    static class BullsCows {

        private int turnNumber;
        private String answer;

        private final Mammal bulls = new Mammal("BULL");  // ;)
        private final Mammal cows = new Mammal("COW");
        StringBuilder sb = new StringBuilder();

        public String renderLog() {
            sb.setLength(0);
            return sb.append("Turn ").append(turnNumber).append(" Answer:%n")
                    .append(answer)
                    .append("\nGrade: ")
                    .append(bulls)
                    .append(Mammal.getAndOrNone(bulls, cows))
                    .append(cows)
                    .append('.')
                    .toString();
        }
    }

    static class Mammal {
        final String name;
        int count;

        Mammal(String name) {
            this.name = name;
        }

        String getEnding() {
            return count > 1 ? "s" : "";
        }

        @Override
        public String toString() {
            return count > 0 ? String.format("%d %s%s", count, name, getEnding()) : "";
        }

        static String getAndOrNone(Mammal m1, Mammal m2) {
            return m1.count > 0 && m2.count > 0 ? " and " : "None";
        }
    }

    public static void main(String[] args) {

    }
}
