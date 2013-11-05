package cz.robotron.examples.xtend

import org.eclipse.swt.widgets.Text
import static org.eclipse.swt.SWT.*
import org.eclipse.swt.events.KeyListener

class SWTAndLambda {

	def void main() {
		val text = new Text(null, NONE)
		val text2 = new Text(null, NONE)
		
		text.addListener(MouseEnter) [ text.setText("KeyPressed")]
		text.addListener(KeyUp) [ text2.setText('''Border With «text.background.toString»''')]
	}

}
