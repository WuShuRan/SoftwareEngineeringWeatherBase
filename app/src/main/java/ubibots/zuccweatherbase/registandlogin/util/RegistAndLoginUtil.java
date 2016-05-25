package ubibots.zuccweatherbase.registandlogin.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ubibots.zuccweatherbase.registandlogin.control.UserManager;
import ubibots.zuccweatherbase.registandlogin.intrfc.IUserManager;

public class RegistAndLoginUtil {
    public static IUserManager userManager = new UserManager();

    public static String MD5Encrypt(String initial){
        String ret = null;
        try {
            byte[] bytesOfMessage = initial.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            ret = printHexBinary(thedigest);
            System.out.println(ret);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String printHexBinary(byte[] initial){
        StringBuilder hexValue = new StringBuilder();
        for (byte aThedigest : initial) {
            int val = ((int) aThedigest) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
