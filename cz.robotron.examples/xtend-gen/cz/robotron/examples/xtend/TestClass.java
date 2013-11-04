package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.ITestClassPersistence;
import cz.robotron.examples.xtend.TestClassPersistenceImpl;
import java.util.Collections;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class TestClass {
  @Extension
  private ITestClassPersistence tcp = new Function0<ITestClassPersistence>() {
    public ITestClassPersistence apply() {
      TestClassPersistenceImpl _testClassPersistenceImpl = new TestClassPersistenceImpl();
      return _testClassPersistenceImpl;
    }
  }.apply();
  
  public static void main(final String[] args) {
    TestClass.extensionCall();
  }
  
  public static String myMethod(final Object obj) {
    String _string = obj.toString();
    String _println = InputOutput.<String>println(_string);
    return _println;
  }
  
  public static String extensionCall() {
    String _xblockexpression = null;
    {
      Object _object = new Object();
      final Object obj = _object;
      String _myMethod = TestClass.myMethod(obj);
      _xblockexpression = (_myMethod);
    }
    return _xblockexpression;
  }
  
  public void extensionImports() {
    TestClass _testClass = new TestClass();
    Collections.<TestClass>singletonList(_testClass);
  }
  
  public void extensionProvider() {
    this.tcp.save(this);
  }
}
