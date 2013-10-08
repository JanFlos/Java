package snippets;


import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class guava {

    @SuppressWarnings("unchecked")
    @Test
    public void splitterTest() {

        JsonObject kuna = new JsonObject();
        kuna.addProperty("sub", 111);
        
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "foo");
        obj.addProperty("num", new Integer(100));
        obj.addProperty("balance", new Double(1000.21));
        obj.add("kuna", kuna);
        obj.addProperty("is_vip", new Boolean(true));
        obj.addProperty("nickname", (String) null);
        //System.out.print(obj.toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(obj));

        //JsonObject p = (JsonObject) JsonParser. parse(obj.toString());
        //Object a = p.get("balance");
        /*
        JSONArray list = new JSONArray();
        list.put("foo");
        list.put(new Integer(100));
        list.put(new Double(1000.21));
        list.put(new Boolean(true));
        list.put("");
        System.out.print(list);
        */
        /*
        Map<String, Integer> a = Maps.newHashMap();
        a.put("kuna", 125);
        System.out.println(JsonObject.valueToString(a));

        List list = Lists.newLinkedList();
        list.add("String");
        list.add(111);
        list.add(null);
        System.out.println(JsonObject.valueToString(list));
        */

        //        JSONTokener u = new JSONTokener(list.toString());
        //      u.
    }

}
