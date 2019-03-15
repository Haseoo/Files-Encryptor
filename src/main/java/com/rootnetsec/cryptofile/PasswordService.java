package com.rootnetsec.cryptofile;

import java.io.*;
import java.net.*;
import jetbrains.exodus.util.HexUtil;

import java.security.MessageDigest;

public final class PasswordService {
    private static String PWNED_API = "https://api.pwnedpasswords.com/range/";

    public static Boolean searchHaveIBeenPwnedDatabase(String password) {
        Boolean result = false;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestedPassword = md.digest(password.getBytes());
            String hash = HexUtil.byteArrayToString(digestedPassword).toUpperCase();
            String prefixHash = hash.substring(0, 5);
            String suffixHash = hash.substring(5);
            
            URL url = new URL(PWNED_API + prefixHash);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.startsWith(suffixHash)) {
                    result = true;
                }
            }
            rd.close();
            return result;

        } catch (Exception e) {
            System.out.println(e);
            return result;
        }
    }

}