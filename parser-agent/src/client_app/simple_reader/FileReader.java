package client_app.simple_reader;

import client_app.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 4/28/14
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileReader implements Runnable {
    private final File file;

    public FileReader(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
            HashMap<String, String> matches = null;
            String line;
            while ((line = reader.readLine()) != null)  {
                for (String key: Core.conditions.keySet()){
                    Pattern p = Pattern.compile(Core.conditions.get(key)[0], Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        if (matches == null) {
                            matches = new HashMap<String, String>();
                        }
                        p = Pattern.compile(Core.conditions.get(key)[1], Pattern.CASE_INSENSITIVE);
                        m = p.matcher(line);
                        m.find();
                        matches.put(key, m.group(0));
                    }
                }
            }
            if (matches != null && matches.keySet().size() == Core.conditions.keySet().size()) {
                Core.res.put(file.hashCode(), matches);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
