package example.com.hipocampo.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by florencio on 11/02/17.
 */

public class FileManager {

    private File file;

    public FileManager(Context context, String filename){
        this.file = new File(context.getFilesDir(), filename);
    }

    public String readFile(){
        String fileContent = "";
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            int ch;
            while((ch = isr.read()) != -1) {
                fileContent += (char) ch;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  fileContent;
    }

    public void writeFile(String data){
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            fos.write(data.getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendData(String data){
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath(),true);
            data += "\n";
            fos.write(data.getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(){
        file.delete();
    }
}
