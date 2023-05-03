# Frequential Analysis

## Introduction

Frequency analysis project. This project implements a [Vigenère
cipher](https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher) with the 
possibility
of ciphering and deciphering text files.

A frequency analysis attack is also implemented, based on a guess of 
keyword
length. In short, it breaks down ciphered text into chunks based on the 
keyword
length. The length providing the highest [index of
coincidence](https://en.wikipedia.org/wiki/Index_of_coincidence) becomes 
the
candidate length to find the keyword. Then, each letter of the alphabet is
compared against a ciphered chunk of text with a measure of proximity 
using
letters' frequencies. This step can be seen as a repeatedly trying to find 
the
answer to a Caesar cipher, for each chunk. Then, all the best letter 
guesses are
concatenated to give the keyword.

Note that this method is not foolproof for two reasons: it's weak against 
short
texts which tend to deviate from expected letter frequencies and long 
keywords
make the checking process prohibitive as well.

N.B: A Vigenère cipher with one letter is a [Caesar
cipher](https://en.wikipedia.org/wiki/Caesar_cipher).

See [the high-level overview](#High-level-overview) for a quick rundown.

## How to run our project

<p>TODO : add a makefile</p>
<p>To use the project, you'll have to run the makefile by using 'make' at 
the root of the repository to compile and have a .jar</p>

## Encryption 
<p><code>java -jar target/freqanalysis-1.0-SNAPSHOT.jar encrypt -c Caesar 
-f data/aliceinwonderland.txt -k 3 -o data/result.txt</code> (Obsvioulsy, 
you can replace the option by the type, the key, the input and the output 
files you want</p>

## Decryption 
<p><code>java -jar target/freqanalysis-1.0-SNAPSHOT.jar decrypt -c Caesar 
-f data/result.txt -o data/decipher.txt</code> (Obsvioulsy, you can 
replace the option by the type, the input and the output files you 
want</p>

## Execution
<p>The execution of the program will notice that the program was executed 
correctly</p> 

## Authors
* Cameron Noupoue
* Ayoub Moufidi

## Credits

Project devised and created during my studies at the École Supérieure 
d'Informatique (ESI), Brussels.
