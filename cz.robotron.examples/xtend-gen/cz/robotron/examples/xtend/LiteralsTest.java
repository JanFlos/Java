package cz.robotron.examples.xtend;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class LiteralsTest {
  private final List<String> myList1 = Collections.<String>unmodifiableList(Lists.<String>newArrayList("a", "b"));
  
  private final String[] myList2 = { "c", "d" };
  
  private final int[] myArray = new Function0<int[]>() {
    public int[] apply() {
      int[] _newIntArrayOfSize = new int[400];
      return _newIntArrayOfSize;
    }
  }.apply();
}
