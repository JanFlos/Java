package cz.robotron.examples.xtend;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class SWTAndLambda {
  public void main() {
    Text _text = new Text(null, SWT.NONE);
    final Text text = _text;
    Text _text_1 = new Text(null, SWT.NONE);
    final Text text2 = _text_1;
    final Listener _function = new Listener() {
      public void handleEvent(final Event it) {
        text.setText("KeyPressed");
      }
    };
    text.addListener(SWT.MouseEnter, _function);
    final Listener _function_1 = new Listener() {
      public void handleEvent(final Event it) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Border With ");
        Color _background = text.getBackground();
        String _string = _background.toString();
        _builder.append(_string, "");
        text2.setText(_builder.toString());
      }
    };
    text.addListener(SWT.KeyUp, _function_1);
  }
}
