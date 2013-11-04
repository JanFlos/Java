package cz.robotron.examples.gson;

public class MyNode {
    public MyNode(String string) {
        this.name = string;
    }

    public String   name       = null;
    public MyNode[] childNodes = null;
    public boolean  checked    = false;

}
