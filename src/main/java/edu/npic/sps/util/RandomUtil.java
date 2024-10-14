package edu.npic.sps.util;

import java.util.Random;

public class RandomUtil {
    private String generateSecurityCode() {
        Random random = new Random();
        int securityCode = 100000 + random.nextInt(900000);  // Generates a random number between 100000 and 999999
        return String.valueOf(securityCode);
    }
}
