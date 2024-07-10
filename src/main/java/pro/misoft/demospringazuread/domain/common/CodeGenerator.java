package pro.misoft.demospringazuread.domain.common;

import java.security.SecureRandom;

public class CodeGenerator {

    public static String generateNumbers(int length) {
        return generate(length, true);
    }

    public static String generate(int length, boolean numbersOnly) {
        String strTable = numbersOnly ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        StringBuilder retStr = new StringBuilder();
        do {
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = new SecureRandom().nextDouble() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr.append(strTable.charAt(intR));
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr.toString();
    }

    private CodeGenerator() {
    }
}