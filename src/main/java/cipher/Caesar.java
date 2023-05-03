package cipher;

public class Caesar {
    /**
     * Ciphers a normalized lowercase plain text using the Caesar cipher.
     * <p>
     * The Caesar cipher is a simple substitution cipher that shifts each letter by a certain amount in the alphabet.
     * For instance, with a shift of 3, A becomes D, Z becomes C, ...
     * Thus, the shift is the key of the cipher.
     * <p>
     * N.B. : The text is assumed to be preprocessed by {@code proc.Preprocess}.
     *
     * @param text the text to cipher
     * @param key  the key used for ciphering
     * @return the ciphered string
     */
    public static String cipher(String text, int key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            /* we take c, we shift the character by key amount, then we subtract 'a' to get the
             * index in the alphabet. We then get its modulo with 26 to loop back around if we exceeded the alphabet.
             * We then add back 'a' to get a character.
             *
             * The default Java % operator worked differently than intended. If the shift was negative, letters could be
             * shifted before the alphabet. Normally, the modulo would catch that. For example, "b" ciphered with -3
             * should become "b" -> 1 - 3 = -2 % 26 = 24. But Java's % operator returns -2, as -2 is the remainder of
             * -2 / 26. Math.floorMod() has the intended behaviour.
             *
             * Source : https://stackoverflow.com/a/31140209
             */
            c = (char) (Math.floorMod((int) c + key - 'a', 26) + 'a');
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * Deciphers a text ciphered with the Caesar cipher using a provided key.
     * <p>
     * Deciphering Caesar is just shifting every letter back by the provided key.
     *
     * @param text the text to decipher
     * @param key  the key used to cipher
     * @return the plain text
     */
    public static String decipher(String text, int key) {
        return cipher(text, -key);
    }

    /**
     * Deciphers a text ciphered using the Caesar cipher.
     * <p>
     * This method uses frequency analysis to compare the ciphered text to telegraphic English character frequencies,
     * and from that derives the key used to cipher the text.
     * <p>
     * N.B. : frequency analysis is more reliable on longer text. As the input text grows larger, the character
     * frequencies look more and more like the standard. Using this method may lead to WRONG results.
     *
     * @param text the text to decipher
     * @return the plain text
     */
    public static String decipher(String text) {
        double chiSq = Double.MAX_VALUE;
        int bestOffset = 0;

        for (int i = 0; i < 26; i++) { //NOTE : isn't this just brute-forcing ??
            String text2 = cipher(text, i);
            double chiSqSave = Util.chiSquare(text2);
            if (chiSqSave < chiSq) {
                chiSq = chiSqSave;
                bestOffset = i;
            }
        }
        System.out.println("[CAESAR] Detected key " + bestOffset);
        System.out.println("[CAESAR] Applying key");
        return cipher(text, bestOffset);
    }
}
