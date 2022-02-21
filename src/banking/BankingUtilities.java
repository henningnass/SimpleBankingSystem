package banking;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class BankingUtilities {

    public static String generateRandomSeries(int places) {
        Date date = new Date();
        long millis = date.getTime();
        Random random = new Random(millis);
        StringBuilder pin = new StringBuilder("");
        int num;

        for (int count = 1; count <= places; count++) {
            num = random.nextInt(10);
            pin.append(num);
        }
        return pin.toString();
    }


    // return -1 if a valid checksum could not be generated
    // series must contain 15 digits
    public static int getCheckSum(String series) {
        int[] digits = new int[15];
        int sum = 0;
        int checksum;
        String regex = "[0-9]{15}";
        if (series.matches(regex)) {
            for (int i = 1; i <= 15; i++) {
                digits[i - 1] = Integer.parseInt(series.substring(i - 1, i));
                if (i % 2 != 0) {
                    digits[i - 1] *= 2;
                    if (digits[i - 1] > 9) {
                        digits[i - 1] = digits[i - 1] - 9;
                    }
                }
                sum += digits[i - 1];
            }
            //System.out.println(Arrays.toString(digits));

            checksum = (sum % 10 == 0) ? 0 : (10 - (sum % 10));
            //System.out.println("Checksum: " + checksum);
            return checksum;
        } else {
            return -1;
        }
    }


}
