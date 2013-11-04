package cz.robotron.examples.xtend;

import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class EverythingIsExpression {
  public void test() {
    String _xtrycatchfinallyexpression = null;
    try {
      _xtrycatchfinallyexpression = null;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
        _xtrycatchfinallyexpression = "data";
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    final String j = _xtrycatchfinallyexpression;
  }
}
