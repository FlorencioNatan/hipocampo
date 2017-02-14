package example.com.hipocampo.util;

import com.google.gson.Gson;

import java.util.List;

import example.com.hipocampo.model.Password;

/**
 * Created by florencio on 14/02/17.
 */

public class PasswordSingleton {

    private static PasswordSingleton instance = new PasswordSingleton();
    private List<Password> passwords;

    public static PasswordSingleton getInstance() {
        return instance;
    }

    public void setPasswordList(List<Password> list){
        passwords = list;
    }

    public List<Password> getPasswordList(){
        return passwords;
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
}
