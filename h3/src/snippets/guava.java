package snippets;

import org.junit.Test;
import com.google.common.base.Splitter;

public class guava {

    @Test
    public void splitterTest() {
        Iterable<String> kuna = Splitter.on('=').omitEmptyStrings().trimResults().split("ID = :TSU_ID");
        //String dd = "TSU_ID = :ID".replaceAll("\\:\\w[\\w\\d]+", "?");
        //        System.out.println(dd);
        for (String token : kuna) {
            System.out.println(token);

        }
    }

}
