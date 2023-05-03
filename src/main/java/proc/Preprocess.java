package proc;

import java.io.*;
import java.text.Normalizer;
import java.util.function.Consumer;

public class Preprocess {

    /**
     * Remove diacritics.
     * Only the diacritics are removed, not the whole character.
     * The character 'à' becomes 'a'.
     *
     * @param str string to process.
     * @return the string with diacritics removed.
     */
    public static String removeDiacritics(String str) {
        return Normalizer
                .normalize(str, Normalizer.Form.NFD)
                .replaceAll("[\u0300-\u036F]", "");
    }

    /**
     * Sanitize the given string to only lowercase alphabetic characters.
     * All characters are converted to lowercase.
     * Characters with diacritics are converted to the corresponding
     * alphabetic character.
     * All remaining non-alphabetic characters are removed.
     *
     * @param str string to sanitize.
     * @return a string made only of alphabetic [a-z] characters.
     */
    public static String sanitizeToAlpha(String str) {
        return Normalizer
                .normalize(str.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("[^a-z]", "");
    }

    /**
     * Read the given input file and write a sanitized version of it to output.
     * This is an example to use the {@link #sanitizeToAlpha(String)} method.
     * You might want to do something else.
     *
     * @param input       path to the file to read.
     * @param output      path to the file to write to.
     * @param ignoreLines if true, do not output newlines.
     * @throws IOException
     */
    public static void preprocessFile(String input, String output, boolean ignoreLines)
            throws IOException {
        try (FileReader ifr = new FileReader(input);
             BufferedReader ibr = new BufferedReader(ifr);
             PrintWriter opw = new PrintWriter(new File(output))) {
            Consumer<String> printer = ignoreLines ? opw::print : opw::println;
            ibr.lines().map(Preprocess::sanitizeToAlpha).forEachOrdered(printer);
        }
    }
}
