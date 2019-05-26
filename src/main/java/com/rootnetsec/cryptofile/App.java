package com.rootnetsec.cryptofile;

import java.util.Arrays;

enum WorkingMode {
    ENCRYPTION,
    DECYRPTION
}

class ArgsParser {
    public static final String encryptionFormat = "-e";
    public static final String decryptionFormat = "-d";

    private String srcFilePath,
                   destFilePath,
                   password;
    
    private WorkingMode mode;

    public String getSrcFilePath() {
        return srcFilePath;
    }

    public String getDestFilePath() {
        return destFilePath;
    }

    public String getPassword() {
        return password;
    }

    public WorkingMode getMode() {
        return mode;
    }

    ArgsParser(String[] args) throws AgrsParseException {
        if (args.length != 4) {
            throw new AgrsParseException(getArgsInfo());
        }

        if (!(args[0].equals(encryptionFormat) || args[0].equals(decryptionFormat))) {
            throw new AgrsParseException(getArgsInfo());
        }

        mode = ((args[0].equals(encryptionFormat)) ? WorkingMode.ENCRYPTION : WorkingMode.DECYRPTION);

        srcFilePath = args[1];
        destFilePath = args[2];
        password = args[3];
        
    }
    public static String getArgsInfo() {
        return ("Arguments format\n-e [source file path] [destination file path] [password] for encryption\n"
                + "-d [source file path] [destination file path] [password] for decryption\n ");
    }
}

public class App {

    public static void main(String[] args) {
        try {
        ArgsParser parser = new ArgsParser(args);
        
        /*
        if (parser.getMode() == WorkingMode.ENCRYPTION)
            AesCipher.encryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());
        else 
            AesCipher.decryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());
        

        if (parser.getMode() == WorkingMode.ENCRYPTION)
            SerpentCipher.encryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());
        else 
            SerpentCipher.decryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());
        */

        if (parser.getMode() == WorkingMode.ENCRYPTION)
            TwofishCipher.encryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());
        else 
            TwofishCipher.decryptFile(parser.getSrcFilePath(), parser.getDestFilePath(), parser.getPassword());

        System.out.println("Success!");

        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
        
        //System.out.println(new String(plaintext));

        //Boolean find = PasswordService.searchHaveIBeenPwnedDatabase("qwerty");
        //if (find == true) {
        //    System.out.println("Password found in HaveIBeenPwned database!");
        //} else {
        //    System.out.println("You're safe! For now...");
        //}


    }


}