package cz.robotron.rf.tests;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import com.google.common.collect.Lists;

public class MiscTests {

    public List<String> extractTokens(String text) {

        int position = 0;
        List<String> result = Lists.newArrayList();

        Pattern p = Pattern.compile("\\:(\\w[\\w\\d]+)");
        Matcher m = p.matcher(text);

        while (m.find()) {
            result.add(m.group(1));

        }
        return result;
    }

    @Test
    public void test() {
        System.out.println(extractTokens("select * from TTS_DETAIL where tts_id = :id and o = :ooo").toString());
    }

}
