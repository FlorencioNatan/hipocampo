package example.com.hipocampo.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by florencio on 11/02/17.
 */

public class FileManager {

    private File file;

    public FileManager(Context context, String filename){
        this.file = new File(context.getFilesDir(), filename);
    }

    public String readFile(){
        byte[] cipherContent = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
            int ch, i = 0;
            while((ch = fis.read()) != -1) {
                cipherContent[i++] = (byte) ch;
            }
            fis.close();

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] key = messageDigest.digest(PasswordSingleton.getInstance()
                    .getMasterPassword().getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key, 0, 16, "AES");
            Cipher aesCipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] plainText = aesCipher.doFinal(cipherContent);
            String decipher = new String(plainText, "UTF-8");
            if (decipher.split("\n")[0].equals("Password File"))
                return  decipher.substring(14);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void writeFile(String data){
        try {
            data = "Password File\n" + data;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] key = messageDigest.digest(PasswordSingleton.getInstance()
                    .getMasterPassword().getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(key, 0, 16, "AES");
            Cipher aesCipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] cipherText = aesCipher.doFinal(data.getBytes("UTF-8"));
            FileOutputStream fos = new FileOutputStream(file.getPath());
            fos.write(cipherText);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(){
        file.delete();
    }
}
