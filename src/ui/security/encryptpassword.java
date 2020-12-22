package ui.security;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class encryptpassword {
    public static String encryptpassword(String pass) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] b = md.digest();
            for (byte b1 : b) {
                sb.append(Integer.toHexString(b1 & 0Xff).toString());
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();

    }
}

