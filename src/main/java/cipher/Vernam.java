package cipher;

public class Vernam {
    /**
     * Ciphers a plain text using the Vernam cipher.
     * <p>
     * The Vernam ciphers performs a bitwise XOR between the input text and the key.
     * The ith character of the ciphered text is te result of the bitwise XOR between the ith character from the
     * plain text and the ith character from the key.
     * If the key's length is inferior to the plain text's it will be concatenated to itself.
     * <p>
     * N.B. : The text is assumed to be preprocessed by {@code proc.Preprocess}.
     *
     * @param text the text to cipher
     * @param key  the key used for ciphering
     * @return the ciphered string
     */
    public static String cipher(String text, char[] key) {
        if (key.length == 0) throw new IllegalArgumentException("Cannot cipher with an empty key");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i);
            // the next operation (^) is a xor. We apply it char per char to avoid one more cast (from byte)
            c = key[i % key.length] ^ c;
            sb.appendCodePoint(c);
        }
        return sb.toString();
    }

    /**
     * Deciphers a text ciphered with the Vernam cipher, with a given key.
     * <p>
     * Deciphering Vernam is just... ciphering it again, provided the key is correct.
     *
     * @param text the text to decipher
     * @param key  the key used to cipher
     * @return the plain text
     */
    public static String decipher(String text, char[] key) {
        return cipher(text, key);
    }

    /**
     * Deciphers a text ciphered with the Vernam cipher, without the key.
     * <p>
     * This method computes the coincidence index for each possible key length. The higher the CI is,
     * the higher the chance that the key length is the actual length of the key.
     * <p>
     * When the key length is known, we can just do a simple frequency analysis to determine the key's byte at
     * that position.
     * <p>
     * N.B. : This method makes several assumptions :
     * <ul>
     *     <li>The text is assumed to be preprocessed by {@code proc.Preprocess}.
     *     Frequency analysis would be impossible otherwise.</li>
     *     <li>The text is supposed to be considerably larger than the key.</li>
     *     <li>The key itself is varied. By that, a key like "infoinfo" is good, because it can be reduced to "info",
     *     which is varied in its characters. A key like "aaaaab" is not, as it cannot be reduced yet is still
     *     repetitive.</li>
     * </ul>
     *
     * @param text the text to decipher
     * @return the plain text (hopefully)
     */
    public static String decipher(String text) {
        // we first try to determine the key's length
        int lenMax = getBestKeyLength(text);
        System.out.println("[VERNAM] Key length determined to be " + lenMax);

        // if it couldn't be determined, it's no use going further
        if (lenMax == 0) return "Undecipherable.";

        // next we need to guess which chars were used to form the key
        char[] keyParts = new char[lenMax];

        System.out.println("[VERNAM] Building key...");

        // so for each char of the key
        for (int i = 0; i < lenMax; i++) {

            // we get the corresponding column
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < text.length(); j += lenMax) {
                sb.append(text.charAt(j));
            }

            String column = sb.toString();

            // given that column, we guess what's the best key part possible
            keyParts[i] = getBestKeyPart(column);
            System.out.print(keyParts[i]);
        }

        System.out.println("\n[VERNAM] Applying key...");
        return decipher(text, keyParts);
    }

    /**
     * Determines the key used to cipher a column using Vernam.
     * <p>
     * This method ciphers the column with each character possible, and performs a chi-square test.
     * The best possible key is the key that resulted in the minimal chi-square value.
     * <p>
     * N.B. : this method assumes that the given column (a subsequence made of 0, k, 2k, ... characters,
     * where k is the length of the key) is correct, meaning that it is mono-alphabetically ciphered.
     *
     * @param column the column which key needs to be determined
     * @return the character used to cipher the column
     */
    private static char getBestKeyPart(String column) {
        char candidate = 0;

        // and we perform a chi square test for each possible character that key part could have been
        for (long c = 0; c <= Character.MAX_VALUE; c++) { // that's a very long loop
            String tried = decipher(column, new char[]{(char) c});
            double chiSq = Util.chiSquare(tried);
            if (chiSq < Double.MAX_VALUE) {
                candidate = (char) c;
                return candidate;
                /*
                    if we get here, it means we found a key that would NOT lead to ANY non-printable characters
                    with a large enough text, we can assume that the first correct ChiSq is the best one.
                    We can make that assumption as this deciphering method doesn't even work
                    for smaller texts.
                 */
            }
        }

        return candidate;
    }

    /**
     * Determines what is the correct Vernam key length given a ciphered string.
     * <p>
     * This method will try every single possible key length and compute the index of coincidence
     * for a column made with that key length. A column is a subsequence made of 0, k, 2k, ... characters,
     * where k is the length of the key.
     * <p>
     * The best key length is the first length that approximates close enough to the standard English index of
     * coincidence.
     * <p>
     * N.B. : if such a key length couldn't be found, most likely because the key is too large compared to the text,
     * it will return 0.
     *
     * @param text the ciphered string which key length needs to be determined
     * @return the best approximation of the key's length, or 0 if it couldn't be determined
     */
    private static int getBestKeyLength(String text) {
        final double IDEAL_CI = 0.065;
        final double EPSILON = 0.005;

        for (int i = 1; i < text.length(); i++) { // for each possible key length

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < text.length(); j += i) {
                sb.append(text.charAt(j)); // we construct the column
            }

            String toWork = sb.toString();
            var freq = Util.getFreq(toWork); // we get its character counts

            int sum = 0;
            for (var cnt : freq.values()) {
                sum += cnt * (cnt - 1);
            }

            double ci = 1.0 * sum / (toWork.length() * (toWork.length() - 1)); // and we get the CI for this column
            if (Math.abs(ci - IDEAL_CI) < EPSILON) { // if it's close enough
                return i; // we found it
            }
            /*
            Getting the largest CI will work, but as we try higher and higher keys, the subsequence used
            to compute the CI gets smaller and smaller, which leads to CIs being possibly bigger.
            And doing a frequency analysis on absurdly high key lengths DOES work, but it will be done
            on very small subsequences, and frequency analysis works better on larger texts.

            On the other hand, if we pick the first one that seems to be close enough, we get multiple advantages :
                1. early return -> more efficient
                2. usually closer to the real key length
                3. as the key is smaller, the frequency analysis will lead to better results
            But it has some trade-offs :
                1. we have to choose a good epsilon
                2. it may lead to wrong results
                    example : key = "aaaaab" would have a high chance of returning 1 as key length
             */
        }

        return 0;
    }
}
