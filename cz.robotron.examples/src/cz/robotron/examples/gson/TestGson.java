package cz.robotron.examples.gson;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestGson {

    public static void main(String[] args) throws IOException, URISyntaxException {

        
        getTestNodes();

        //String content = njew Scanner(file).next();
        //System.out.println(content);


    }

    public static MyNode[] getTestNodes() throws URISyntaxException, IOException {
        /*
        MyNode[] myNodes = new MyNode[1];
        MyNode myNode2 = myNodes[0] = new MyNode("Kuna");

        myNode2.childNodes = new MyNode[2];
        myNode2.childNodes[0] = new MyNode("Child1");
        myNode2.childNodes[1] = new MyNode("Child2");
        */

        return new MyLoader().load("tree.json");

    }

}

