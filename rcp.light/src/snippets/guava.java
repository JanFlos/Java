package snippets;

import org.junit.Test;

public class guava {

    @Test
    public void splitterTest() {
        String sortedColumn = "name desc";
        
        if (sortedColumn.matches("\\w[\\w\\d]+(\\s+asc|\\s+desc)?"))
                System.out.println("kuna");
    }

}
