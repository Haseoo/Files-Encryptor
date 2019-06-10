# Files encryptor
![Logo](https://raw.githubusercontent.com/Haseoo/Files-Encryptor/master/logo.png?token=AI2P2C4QEH5IFYVCC4BIEJ25A6DF4)
This program can encrypt (and decrypt) a file provided by the user using one of three encryption algorithms:
 - AES / CBC / PKCS5PADDING
 - SERPENT / CBC / PKCS7Padding
 - Twofish / CBC / PKCS7Padding
 
The program also checks whether the password provided by the user during encryption is not one of the passwords that leaked to the network (in which case it informs the user). The application has a graphical interface that makes it easier to use it.

## User manual

The program is run with the command:
-   `gradlew.bat run` dla Windows
-   `gradlew run` – dla Linux i MacOS
    
Using the 'jar' command, you can compile the program into a jar file for the current operating system and run the program from a .jar file, but in Windows there is a problem with running encryption algorithms from the .jar file.
