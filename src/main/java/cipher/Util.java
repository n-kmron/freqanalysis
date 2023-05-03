package cipher;

import java.util.HashMap;
import java.util.Map;

public class Util {

    private static final HashMap<Character, Double> STANDARD_FREQ = new HashMap<>();

    static {
        STANDARD_FREQ.put('a', 0.08167);
        STANDARD_FREQ.put('b', 0.01492);
        STANDARD_FREQ.put('c', 0.02782);
        STANDARD_FREQ.put('d', 0.04253);
        STANDARD_FREQ.put('e', 0.12702);
        STANDARD_FREQ.put('f', 0.02228);
        STANDARD_FREQ.put('g', 0.02015);
        STANDARD_FREQ.put('h', 0.06094);
        STANDARD_FREQ.put('i', 0.06966);
        STANDARD_FREQ.put('j', 0.00153);
        STANDARD_FREQ.put('k', 0.00772);
        STANDARD_FREQ.put('l', 0.04025);
        STANDARD_FREQ.put('m', 0.02406);
        STANDARD_FREQ.put('n', 0.06749);
        STANDARD_FREQ.put('o', 0.07507);
        STANDARD_FREQ.put('p', 0.01929);
        STANDARD_FREQ.put('q', 0.00095);
        STANDARD_FREQ.put('r', 0.05987);
        STANDARD_FREQ.put('s', 0.06327);
        STANDARD_FREQ.put('t', 0.09056);
        STANDARD_FREQ.put('u', 0.02758);
        STANDARD_FREQ.put('v', 0.00978);
        STANDARD_FREQ.put('w', 0.02360);
        STANDARD_FREQ.put('x', 0.00150);
        STANDARD_FREQ.put('y', 0.01974);
        STANDARD_FREQ.put('z', 0.00074);
    }

    /**
     * Returns the frequencies of each character in a string.
     * <p>
     * The returned map is in the format key->value where key is a character and value is the amount of times it appears
     * in {@code text}.
     *
     * @param text the text to get the character frequencies from
     * @return a map of all characters and their frequencies
     */ // FIXME : shouldn't it be occurrences ?
    public static Map<Character, Integer> getFreq(String text) {
        Map<Character, Integer> frequency = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (frequency.containsKey(c))
                frequency.put(c, frequency.get(c) + 1);
            else
                frequency.put(c, 1);
        }

        return frequency;
    }

    /**
     * Performs a Chi-Square test between a given string and the standard english character frequencies.
     * <p>
     * This method will return a sentinel value of {@code Double.MAX_VALUE} if the given text contains
     * characters that aren't standard lowercase English letters.
     *
     * @param text the string to perform the chi square test on
     * @return the chi square resulting from the comparison between text and the standard english character frequencies
     */
    public static double chiSquare(String text) {
        var freq = getFreq(text);
        double ret = 0;
        for (var c : freq.entrySet()) {
            double ci = c.getValue() * 1. / text.length();
            Double ei = STANDARD_FREQ.get(c.getKey());

            // if the char couldn't be found in STANDARD_FREQ, either the plain text
            // wasn't preprocessed, or (if ciphered with Vernam) the key is incorrect as it doesn't
            // cipher back to [a-z] characters
            if (ei == null) return Double.MAX_VALUE;

            ret += (ci - ei) * (ci - ei) / ei;
        }

        return ret;
    }
}
