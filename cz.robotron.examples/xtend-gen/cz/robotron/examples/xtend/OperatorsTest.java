package cz.robotron.examples.xtend;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import cz.robotron.examples.xtend.Address;
import cz.robotron.examples.xtend.Person;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class OperatorsTest {
  public void test() {
    final String s1 = "kuna";
    final String s2 = "kuna";
    boolean _equals = Objects.equal(s1, s2);
    if (_equals) {
      InputOutput.<String>println("Strings are equal");
    }
  }
  
  public void withOperator() {
    Person _person = new Person();
    final Procedure1<Person> _function = new Procedure1<Person>() {
      public void apply(final Person it) {
        it.setFirstName("Homer");
        it.setLastName("Simpson");
        Address _address = new Address();
        final Procedure1<Address> _function = new Procedure1<Address>() {
          public void apply(final Address it) {
            it.setStreet("742 Evergreen Terrace");
            it.setCity("SpringField");
          }
        };
        Address _doubleArrow = ObjectExtensions.<Address>operator_doubleArrow(_address, _function);
        it.setAddress(_doubleArrow);
      }
    };
    final Person person = ObjectExtensions.<Person>operator_doubleArrow(_person, _function);
  }
  
  public void rangeOperator() {
    final List<String> list = Collections.<String>unmodifiableList(Lists.<String>newArrayList("a", "b"));
    int _size = list.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      final String element = list.get((i).intValue());
    }
  }
  
  public void pairOperator() {
    final Pair<String,Integer> nameAndAge = Pair.<String, Integer>of("Homer", Integer.valueOf(42));
  }
  
  public void localVariables() {
    String variable = "Homer";
  }
  
  public void settingProperties() {
    Person _person = new Person();
    Person person = _person;
    person.setFirstName("Alibaba");
  }
  
  public void blockExpression() {
    {
      int i = 12;
      int j = 45;
    }
  }
  
  public void nullOperators() {
    Person _person = new Person();
    Person person = _person;
    if (person!=null) {
      person.setFirstName("Oleg");
    }
  }
  
  public void testLambdaExpression() {
    final Runnable _function = new Runnable() {
      public void run() {
        InputOutput.<String>println("Hello I\'m executed!");
      }
    };
    final Runnable runnable = _function;
    final Comparator<String> _function_1 = new Comparator<String>() {
      public int compare(final String a, final String b) {
        int _length = a.length();
        int _length_1 = b.length();
        int _minus = (_length - _length_1);
        return _minus;
      }
    };
    Collections.<String>sort(Collections.<String>unmodifiableList(Lists.<String>newArrayList("A", "B")), _function_1);
  }
}
