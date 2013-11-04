package cz.robotron.examples.xtend

// 2. Extension imports
import static extension java.util.Collections.*
import java.io.File
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.SWT
import org.eclipse.swt.events.KeyAdapter
import org.eclipse.swt.events.KeyListener
import org.eclipse.swt.events.KeyEvent
import org.eclipse.swt.events.SelectionEvent
import javax.swing.JTextField
import java.awt.event.ActionEvent
import org.eclipse.swt.events.SelectionListener
import java.util.Collections

// 1. Local Extension Methods
class TestClass {

	// 3. Extension provider
	extension ITestClassPersistence tcp = new TestClassPersistenceImpl();

	def static void main(String[] args) {
		extensionCall();
	}

	def static myMethod(Object obj) {
		println(obj.toString);
	}

	def static extensionCall() {
		val obj = new Object;
		obj.myMethod;
	}

	def void extensionImports() {
		new TestClass().singletonList();
	}

	def void extensionProvider() {
		this.save();
	}
}

class TestClassPersistenceImpl implements ITestClassPersistence {

	override save(TestClass e) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

}

class EverythingIsExpression {

	def void test() {
		val j = try {
		} catch (Exception e) {
			'data'
		}
	}
}

// 4. Annotation Declaration
annotation MyAnnotation {
	String[] value
	boolean isTricky = false
	int[] lotteryNumbers = #[42, 137]
}

class LiteralsTest {
	val myList1 = #['a', 'b']
	val String[] myList2 = #['c', 'd']
	val int[] myArray = newIntArrayOfSize(400)
}

class OperatorsTest {

	def void test() {
		val s1 = "kuna"
		val s2 = "kuna"
		if(s1 == s2) println("Strings are equal")
	}

	def void withOperator() {
		val person = new Person => [
			firstName = 'Homer'
			lastName = 'Simpson'
			address = new Address => [
				street = '742 Evergreen Terrace'
				city = 'SpringField'
			]
		]
	}

	def void rangeOperator() {
		val list = #["a", "b"]

		for (i : 0 ..< list.size) {
			val element = list.get(i)
		}
	}

	def void pairOperator() {
		val nameAndAge = 'Homer' -> 42
	}

	def void localVariables() {
		var variable = 'Homer'
	}

	def void settingProperties() {
		var person = new Person();
		person.firstName = "Alibaba";
	}

	def void blockExpression() {
		{
			var i = 12
			{
				var j = 45
			}
		}
	}

	def void nullOperators() {
		var person = new Person;
		person?.setFirstName("Oleg")
	}

	def void testLambdaExpression() {
		val Runnable runnable = [ |
			println("Hello I'm executed!")
		]

		Collections.sort(#["A","B"]) [ a, b |
			a.length - b.length
		]
	}

}
