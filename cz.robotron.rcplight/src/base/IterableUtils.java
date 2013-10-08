/**
 * 
 */
package base;

import java.util.List;
import com.google.common.collect.Lists;

/**
 * @author Jan Flos
 *
 */
public class IterableUtils {

    public static String join(Iterable<String> list, String separator, String suffix) {
		String result = "";
		if (list != null) {
			for (String item : list) {
				result += separator + item;
				if (suffix != null)
					result += suffix;
			}
			return result.substring(separator.length());
		}
		return null;
	}

	public static String join(String text, String separator, int length) {
		String result = "";
		for (int i = 0; i < length; i++) {
			result += separator + text;
		}
		return result.substring(separator.length());
	}

    public static String join(Iterable<String> list, String separator) {
		return join(list, separator, null);
	}

    /**
     * @param tableColumns
     * @param writableColumns
     * @return
     */
    public static List<String> difference(Iterable<String> tableColumns, List<String> writableColumns) {
        List<String> result = null;
        
        if (tableColumns != null && writableColumns != null) {
            result = Lists.newArrayList();

            for (String column : tableColumns) {
                if (!writableColumns.contains(column))
                    result.add(column);

            }
        
        } else if (tableColumns != null) {

            result = Lists.newArrayList(tableColumns);
        }
            

        return result;
    }

}