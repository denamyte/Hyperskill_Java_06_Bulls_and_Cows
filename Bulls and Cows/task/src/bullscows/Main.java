package bullscows;

import java.util.*;

public class Main {

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
        // Hey, look, I used System.nanoTime(), I did it right, huh? ... :)
        int index = 1 | (int) (System.nanoTime() % 10);
        chars.add(index, "0");
        return String.join("", chars.subList(0, count));
    }

    public static void main(String[] args) {
        String s = generateSecret2(new Scanner(System.in).nextInt());
        System.out.printf("The random secret number is %s.%n", s);
    }
}
