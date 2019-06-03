package com.rootnetsec.cryptofile;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jetbrains.exodus.util.HexUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.security.MessageDigest;

/**
 * Checks whether the password is in the HaveIBeenPwned database.
 */
public final class PasswordService {
    private static final Logger LOGGER = Logger.getLogger(PasswordService.class.getName()); /**Logger used to monitor connection problems*/
    private static String PWNED_API = "https://api.pwnedpasswords.com/range/"; /**The api URL*/

    /** Method that checks if a hash prefix of the password is in the database.
     * @param password The password to check.
     * @return  Method returns true if given password is found.
     */
    public static Boolean searchHaveIBeenPwnedDatabase(String password) {
        Boolean result = false;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestedPassword = md.digest(password.getBytes());
            String hash = HexUtil.byteArrayToString(digestedPassword).toUpperCase();
            String hashPrefix = hash.substring(0, 5);
            String hashSuffix = hash.substring(5);
            
            URL url = new URL(PWNED_API + hashPrefix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.startsWith(hashSuffix)) {
                    result = true;
                }
            }
            rd.close();
            return result;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return result;
        }
    }

}