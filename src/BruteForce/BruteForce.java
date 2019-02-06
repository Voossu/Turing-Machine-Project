package BruteForce;

import java.util.stream.IntStream;

public class BruteForce {

    public static String[] go(int wordsLength, String alphabet) {
        String[] temp;
        if (alphabet.length()==0 || wordsLength==0) {
            temp = new String[1];
            temp[0] = "";
            return temp;
        }
        temp = new String[(int)Math.pow(alphabet.length(), wordsLength)];
        int[] counterMassive = IntStream.range(0, wordsLength).map(i -> 0).toArray();

        boolean fl = true;
        int i = 0;
        do {
            temp[i] = "";
            for (int j : counterMassive) temp[i] += alphabet.charAt(j);
            int j = counterMassive.length - 1;
            counterMassive[j]++;
            while (fl && counterMassive[j] > alphabet.length() - 1) {
                counterMassive[j] = 0;
                j--;
                if (j == -1) fl = false;
                else counterMassive[j]++;
            }
            i++;
        } while (fl);
        return temp;
    }

}
