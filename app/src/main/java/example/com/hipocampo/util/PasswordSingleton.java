package example.com.hipocampo.util;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.hipocampo.model.Password;

/**
 * Created by florencio on 14/02/17.
 */

public class PasswordSingleton {

    private static PasswordSingleton instance = new PasswordSingleton();
    private List<Password> passwords;
    private Map<String, String> directories;
    private String masterPassword;
    private String folder = "";

    public static PasswordSingleton getInstance() {
        return instance;
    }

    public void setPasswordList(List<Password> list){
        passwords = list;
    }

    public List<Password> getPasswordList(){
        return passwords;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String master) {
        this.masterPassword = master;
    }

    @Override
    public String toString(){
        String ret = "";
        Gson gson = new Gson();
        for (Password pass: passwords) {
            ret += gson.toJson(pass) + "\n";
        }
        return ret;
    }

    public Map<String, String> getDirectories() {
        return directories;
    }

    public void newDirectories() {
        this.directories = new HashMap<>();
    }

    public void setCurrentFolder(String folder){
        this.folder = folder;
    }

    public String getCurrentFile(){
        return directories.get(folder);
    }
}
