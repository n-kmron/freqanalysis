# Lab 1 - Frequency Analysis

A utility program written in Java to cipher and decipher text files using the Caesar and Vernam ciphers.  
This program also deciphers text files without the key provided using frequency analysis.

## Building

To build the project, you'll have to run the makefile by using `make` at the root of the repository to compile.
You will find the resulting jar in the target folder.

You will need to have maven installed to build.

## Usage

Command :

```
java -jar target/freqanalysis-1.0.jar
    encrypt|decrypt     -> the operation
    -c Caesar|Vernam    -> the type of cipher
    -f path/to/input    -> the path to the input file
    [-k key]            -> the key, optional when decrypting
    -o path/to/output   -> the path to the output file
```

Please note that the path to the jar file `target/freqanalysis-1.0.jar` is relative to the root
folder after running the Makefile.

Sample files are provided in the `data/` folder, but you can use your own. Beware that deciphering without the key
(frequency analysis attacks) may not work on non-English text.

### Encryption

To encrypt, you will need to provide a cipher and its key. Its nature depends on the type of cipher used. For Caesar,
the key is an integer. For Vernam, the key is... anything you want.

For example, ciphering the provided "Alice in the Wonderland" text using the Caesar cipher with key 3 :

`java -jar target/freqanalysis-1.0.jar encrypt -c Caesar -f data/aliceinwonderland.txt -k 3 -o data/result.txt`

### Decryption

To decrypt, you will need to provide the cipher used to encrypt the file. You may also provide the key if it's known.
If the key isn't provided, it will be deduced by the program using frequency analysis.

For example, deciphering the file resulting from the previous command (parts between [brackets] are optional) :

`java -jar target/freqanalysis-1.0.jar decrypt -c Caesar -f data/result.txt [-k 3] -o data/decipher.txt`

## Remarks

* Frequency analysis attacks comparing letter frequencies to the English standard, each input file is pre-processed
  to only include lowercase English letters. As such, spaces, new lines, accents and other characters disappear.
* As mentioned, frequency analysis compares to letter frequencies in standard **English**. As such, a frequency analysis
  attack on non-English text will be unreliable.
* As letter frequencies match closer to standard English as the input gets larger, frequency analysis attacks are very
  unreliable on short texts.
* For Vernam, frequency analysis attacks get more unreliable as the key's length gets closer to the input text's, and
  are impossible if it exceeds it.
* For Vernam, frequency analysis attacks are especially unreliable on repetitive keys, like `aaaaaaab`.

## Authors

* 58089 - Ayoub Moufidi
* 58008 - Cameron Noupoue
