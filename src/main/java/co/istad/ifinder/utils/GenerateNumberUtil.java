package co.istad.ifinder.utils;

import java.util.Random;

public class GenerateNumberUtil {

    public static String generateCodeNumber(){

        Random random = new Random();

        int number = random.nextInt(999999);

        return String.format("%06d", number);
    }
}
