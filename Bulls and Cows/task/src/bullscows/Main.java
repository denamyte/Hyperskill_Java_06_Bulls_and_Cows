package bullscows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        new BullsCows().play();
    }
}

class BullsCows {

    public static final String CODE_LENGTH_PROMPT = "Input the length of the secret code:";
    public static final String CHAR_NUMBER_PROMPT = "Input the number of possible symbols in the code:";
    Scanner scanner = new Scanner(System.in);
    private final SecretCode code;
    private int moveNumber;
    private final Counter bulls = new Counter("bull");
    private final Counter cows = new Counter("cow");

    public BullsCows() {
        int codeLength = inputNumber(CODE_LENGTH_PROMPT);
        ErrorsChecker.lteZero(codeLength);
        int usedCharsNumber = inputNumber(CHAR_NUMBER_PROMPT);
        ErrorsChecker.lteZero(usedCharsNumber);

        ErrorsChecker.tooManyUsedChars(usedCharsNumber);
        ErrorsChecker.notEnoughUsedChars(codeLength, usedCharsNumber);

        code = new SecretCode(codeLength, usedCharsNumber);
        System.out.println(code.getInfoString());
    }

    private int inputNumber(String prompt) {
        System.out.println(prompt);
        String str = scanner.nextLine();
        ErrorsChecker.invalidNumber(str);
        return Integer.parseInt(str);
    }

    public void play() {
        while (!finished()) {
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
        for (int i = 0; i < code.codeLength; i++) {
            char key = answer.charAt(i);
            if (key == code.secret[i]) {
                bulls.inc();
            } else if (Arrays.binarySearch(code.secretSorted, key) > -1) {
                cows.inc();
            }
        }
    }

    public boolean finished() {
        return bulls.count == code.codeLength;
    }
}

/** The counter class for bulls and cows. */
class Counter {

    public final String name;
    public int count;

    Counter(String name) {
        this.name = name;
    }

    public void inc() {
        count++;
    }

    public void clear() {
        count = 0;
    }

    @Override
    public String toString() {
        return count > 0 ? String.format("%d %s%s", count, name, count > 1 ? "s" : "") : "";
    }

    public static String getConnective(Counter c1, Counter c2) {
        return c1.count > 0 && c2.count > 0 ? " and "
                : c1.count == 0 && c2.count == 0 ? "None"
                : "";
    }
}

class SecretCode {
    static String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
    static final int MAX_USED_CHARS_NUMBER = 36;

    public final int codeLength;
    public final int usedCharsNumber;
    public char[] secret;  // The original secret, for search of the exact position of chars
    public char[] secretSorted;  // The sorted secret, for easy search of chars regardless of their position

    public SecretCode(int codeLength, int usedCharsNumber) {
        this.codeLength = codeLength;
        this.usedCharsNumber = usedCharsNumber;
        generate();
    }

    public final void generate() {
        ArrayList<String> usedChars = new ArrayList<>(
                Arrays.asList(chars.substring(0, usedCharsNumber).split("")));
        Collections.shuffle(usedChars);
        secret = String.join("", usedChars.subList(0, codeLength)).toCharArray();
        secretSorted = Arrays.copyOf(secret, codeLength);
        Arrays.sort(secretSorted);
    }

    public String getInfoString() {
        return String.format("The secret is prepared: %s %s.", "*".repeat(codeLength), getUsedCharsString());
    }

    public String getUsedCharsString() {
        String digitPart = usedCharsNumber == 1 ? "1" :
                "0-" + (usedCharsNumber <= 10 ? chars.charAt(usedCharsNumber - 1) : '9');
        String letterPart = usedCharsNumber < 10 ? "" :
                usedCharsNumber == 11 ? ", a" : ", a-" + chars.charAt(usedCharsNumber - 1);
        return String.format("(%s%s)", digitPart, letterPart);
    }
}

class ErrorsChecker {
    private static final Supplier<String> LTE_ZERO_SUPPLIER = () ->
            "Error: the number cannot be less than or equal to 0";
    private static final Supplier<String> MAX_USED_SUPPLIER = () ->
            "Error: maximum number of possible symbols in the code is 36 (0-9, a-z).";
    private static final String INVALID_NUMBER_MSG = "Error: \"%s\" isn't a valid number.";
    private static final String NOT_ENOUGH_USED_CHARS_MSG =
            "Error: it's not possible to generate a code with a length of %d with %d unique symbols.";

    public static void lteZero(int value) {
        commonCheck(value <= 0, LTE_ZERO_SUPPLIER);
    }

    public static void notEnoughUsedChars(int codeLength, int usedCharsNumber) {
        commonCheck(codeLength > usedCharsNumber,
                    () -> String.format(NOT_ENOUGH_USED_CHARS_MSG, codeLength, usedCharsNumber));
    }

    public static void invalidNumber(String str) {
        commonCheck(!str.matches("-?\\d+"), () -> String.format(INVALID_NUMBER_MSG, str));
    }

    public static void tooManyUsedChars(int charsNumber) {
        commonCheck(SecretCode.MAX_USED_CHARS_NUMBER < charsNumber, MAX_USED_SUPPLIER);
    }

    private static void commonCheck(boolean isError, Supplier<String> msgSupplier) {
        if (isError) {
            System.out.println(msgSupplier.get());
            System.exit(0);
        }
    }
}
