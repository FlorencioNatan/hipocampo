package example.com.hipocampo.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import example.com.hipocampo.model.Password;

/**
 * Created by florencio on 22/03/17.
 */

public class ImportSeahorseFile {

    private InputStream is;
    private String fileName;

    public  ImportSeahorseFile(InputStream file){
        this.is = file;
    }

    private String readFile() throws FileNotFoundException {
        try {
            int ch;
            String str = new String();
            while ((ch = is.read())!= -1) {
                str += (char) ch;
            }
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void readHeader(String[] header) {
        if (header[0].equals("[keyring]")) {
            for (int i = 1; i < header.length; i++) {
                String[] pair = header[i].split("=");
                if (pair[0].equals("display-name")) {
                    fileName = pair[1];
                }
            }
        }
    }

    private void readKey(String[] body, HashMap<String, HashMap<String, String>> keysMap){
        String id = body[0].replace("[", "").replace("]", "");
        for (int j = 1; j < body.length; j++) {
            String[] pair = body[j].split("=");
            if(pair[0].equals("display-name")){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("display-name", pair[1]);
                keysMap.put(id, map);
            } else if(pair[0].equals("secret")){
                keysMap.get(id).put("secret", pair[1]);
            }
        }
    }

    private void readAttribute(String[] body, HashMap<String, HashMap<String, String>> keysMap){
        String[] attrTitle = body[0].split(":");
        String id = attrTitle[0].replace("[", "");
        String name = "";
        String value = "";
        for (int j = 1; j < body.length; j++) {
            String[] pair = body[j].split("=");
            if(pair[0].equals("name")){
                name = pair[1];
                keysMap.get(id).put(name, value);
            } else if (pair[0].equals("value")) {
                value = pair[1];
                keysMap.get(id).put(name, value);
            }
        }
    }

    private HashMap<String, HashMap<String, String>> processFile(){
        String str = null;
        try {
            str = readFile();
            String[] items = str.split("\n\n");
            String[] header = items[0].split("\n");
            HashMap<String, HashMap<String, String>> keysMap = new HashMap();
            readHeader(header);
            for (int i = 1; i < items.length; i++) {
                String[] body = items[i].split("\n");
                Pattern p = Pattern.compile("\\[\\d+\\]");
                Matcher m = p.matcher(body[0]);
                if (m.matches()) {
                    readKey(body, keysMap);
                } else {
                    p = Pattern.compile("\\[\\d+:attribute\\d+\\]");
                    m = p.matcher(body[0]);
                    if (m.matches()) {
                        readAttribute(body, keysMap);
                    }
                }
            }
            return keysMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Password> importFile(){
        HashMap<String, HashMap<String, String>> keysMap = processFile();
        List<Password> passwordList = new ArrayList();

        for (HashMap.Entry<String, HashMap<String, String>> entry : keysMap.entrySet()) {
            Password pas = new Password();
            HashMap<String, String> value = entry.getValue();
            if (value.get("display-name") != null){
                pas.setDescription(value.get("display-name"));
            }
            if (value.get("secret") != null){
                pas.setPassword(value.get("secret"));
            }
            if (value.get("xdg:schema") != null){
                pas.setObservation(value.get("xdg:schema"));
            }
            passwordList.add(pas);
        }
        return passwordList;
    }

    public String getFileName() {
        return fileName;
    }

}
