package cz.robotron.jdbc;

public class Sets {

	public static <T> boolean isIn(T t, T... ts) {
		for (T t2 : ts)
			if (t.equals(t2))
				return true;
		return false;
	}

}
