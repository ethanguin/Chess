package model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class RandomNum {
    private static Collection<Integer> usedNumbers = new HashSet<>();

    public static int newNum() {
        int num;
        Random random = new Random();
        do {
            num = random.nextInt(1111, 9999);
        } while (usedNumbers.contains(num));
        usedNumbers.add(num);
        return num;
    }

}
