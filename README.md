# Files encryptor
![Logo](https://dr2csg.dm.files.1drv.com/y4mlQ8YuFHSP5bYoGmWuuyqtplTYhkyiQeNZe-SGgGHvI1P_15pD7UOwMLtbYcCfpnyyU0Xy_BbdLBfADeiirfes1C65gTfptIq16LSjuV17MtduOl4CVRXQ9HZu91qo0wkK5wevq6_LhbBctVgeATmaWmbE9_DVbL8E5xGwuqkEbQFnyqBqUuGQSKd_poyC9suUX_xJQBaMsLtGX0kI497Yg?width=1115&height=445&cropmode=none)
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
