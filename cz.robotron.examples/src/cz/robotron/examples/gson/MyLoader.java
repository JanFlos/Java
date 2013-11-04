package cz.robotron.examples.gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import com.google.gson.Gson;

public class MyLoader {

    public MyNode[] load(String string) throws URISyntaxException, IOException {
        Gson gson = new Gson();
        InputStream s = this.getClass().getResourceAsStream(string);
        //String next = new Scanner(s).useDelimiter("\\Z").next();

        MyNode[] fromJson = gson.fromJson(new InputStreamReader(s), MyNode[].class);
        s.close();
        return fromJson;
    }

}
