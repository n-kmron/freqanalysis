import cipher.Caesar;
import cipher.Vernam;
import proc.Preprocess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("encrypt|decrypt -c Caesar|Vernam -f path/to/plaintext [-k key] -o path/to/output");
            System.exit(1);
        }

        if (args[0].equals("encrypt")) {
            if (args.length != 9 || !args[1].equalsIgnoreCase("-c")
                    || !args[3].equalsIgnoreCase("-f")
                    || !args[5].equalsIgnoreCase("-k")
                    || !args[7].equalsIgnoreCase("-o")
            ) {
                System.out.println("""
                        encrypt
                            -c Caesar|Vernam        -> the type of cipher
                            -f path/to/plaintext    -> the path to the text to cipher
                            -k key                  -> the key used to cipher : an integer for Caesar, a string for Vernam
                            -o path/to/output       -> the path where the ciphered text will be stored""");
                System.exit(1);
            }
            encrypt(args[2], args[4], args[6], args[8]);
        } else if (args[0].equals("decrypt")) {
            if (args.length < 7
                    || !args[1].equalsIgnoreCase("-c")
                    || !args[3].equalsIgnoreCase("-f")
                    || (!args[5].equalsIgnoreCase("-k") && !args[5].equalsIgnoreCase("-o"))
                    || (args[5].equalsIgnoreCase("-k") && !args[7].equalsIgnoreCase("-o"))) {
                System.out.println("""
                        decrypt
                            -c Caesar|Vernam        -> the type of cipher
                            -f path/to/plaintext    -> the path to the text to decipher
                            [-k key]                -> the key used to decipher : an integer for Caesar, a string for Vernam
                                                    -> if not provided, the key will be cracked autonomously
                            -o path/to/output       -> the path where the deciphered text will be stored""");
                System.exit(1);
            }

            String key = args[5].equalsIgnoreCase("-k") ? args[6] : "";
            String output = key.equals("") ? args[6] : args[8];


            decrypt(args[2], args[4], key, output);
        } else {
            System.out.println("encrypt|decrypt -c Caesar|Vernam -f path/to/plaintext [-k key] -o path/to/output");
            System.exit(1);
        }
    }

    /**
     * Ciphers an input file using the requested cipher and writes the result to a file
     * <p>
     * N.B. All arguments are case-insensitive.
     *
     * @param type   the cipher to be used : either Caesar or Vernam
     * @param input  the path to the input file
     * @param key    they used to cipher
     * @param output the path to the output file
     */
    private static void encrypt(String type, String input, String key, String output) {

        if (!(type.equalsIgnoreCase("Caesar") || type.equalsIgnoreCase("Vernam"))) {
            System.out.println("""
                    encrypt
                        -c Caesar|Vernam        -> the type of cipher
                        -f path/to/plaintext    -> the path to the text to cipher
                        -k key                  -> the key used to cipher : an integer for Caesar, a string for Vernam
                        -o path/to/output       -> the path where the ciphered text will be stored""");
            System.exit(1);
        }


        try {
            String text = Files.readString(Paths.get(input));
            text = Preprocess.sanitizeToAlpha(text);

            Path outputPath = Paths.get(output);

            System.out.println(input + " as the input file");
            System.out.println(key + " as the key");

            String cipheredText = "";

            if (type.equalsIgnoreCase("Caesar")) {
                System.out.println("Ciphering with Caesar");

                cipheredText = Caesar.cipher(text, Integer.parseInt(key));

            } else if (type.equalsIgnoreCase("Vernam")) {
                System.out.println("Ciphering with Vernam");
                cipheredText = Vernam.cipher(text, key.toCharArray());
            }

            Files.writeString(outputPath, cipheredText);
            System.out.println("The ciphered text is now saved at : " + output);
        } catch (NumberFormatException e) {
            System.err.println("Caesar key must be a numeral!");
        } catch (IOException e) {
            System.err.println("Couldn't access file : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred : " + e.getMessage());
        }
    }


    /**
     * Deciphers a ciphered file and outputs the result to another.
     * <p>
     * N.B. All arguments are case-insensitive.
     *
     * @param type   the type of cipher used
     * @param input  the path to the input file
     * @param key    the key used to cipher, or "" if not provided
     * @param output the path to the output file
     */
    private static void decrypt(String type, String input, String key, String output) {

        if (!(type.equalsIgnoreCase("Caesar") || type.equalsIgnoreCase("Vernam"))) {
            System.out.println("""
                    decrypt
                        -c Caesar|Vernam        -> the type of cipher
                        -f path/to/plaintext    -> the path to the text to decipher
                        [-k key]                -> the key used to decipher : an integer for Caesar, a string for Vernam
                                                -> if not provided, the key will be cracked autonomously
                        -o path/to/output       -> the path where the deciphered text will be stored""");
            System.exit(1);
        }

        try {
            String text = new String(Files.readAllBytes(Paths.get(input)));

            Path outputPath = Paths.get(output);

            System.out.println(input + " as the input file");
            System.out.println(key.equals("") ? "With key deduced" : "With provided key : " + key);

            String decipheredText = "";

            if (type.equalsIgnoreCase("Caesar")) {
                System.out.println("Deciphering Caesar");

                decipheredText = key.equals("") ? Caesar.decipher(text) : Caesar.decipher(text, Integer.parseInt(key));

            } else if (type.equalsIgnoreCase("Vernam")) {
                System.out.println("Deciphering Vernam");
                decipheredText = key.equals("") ? Vernam.decipher(text) : Vernam.decipher(text, key.toCharArray());
            }

            Files.writeString(outputPath, decipheredText);
            System.out.println("The deciphered text is now saved at : " + output);
        } catch (NumberFormatException e) {
            System.err.println("Caesar key must be a numeral!");
        } catch (IOException e) {
            System.err.println("Couldn't access file : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred : " + e.getMessage());
        }

    }


}
