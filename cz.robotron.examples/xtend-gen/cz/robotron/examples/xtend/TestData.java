package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.Address;
import cz.robotron.examples.xtend.Person;
import cz.robotron.examples.xtend.PersonGroup;
import cz.robotron.examples.xtend.PersonList;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class TestData {
  public static PersonGroup getRoot() {
    PersonGroup _personGroup = new PersonGroup();
    final Procedure1<PersonGroup> _function = new Procedure1<PersonGroup>() {
      public void apply(final PersonGroup it) {
        PersonList _personList = new PersonList();
        final Procedure1<PersonList> _function = new Procedure1<PersonList>() {
          public void apply(final PersonList it) {
            it.setName("Berechnungen");
            List<Person> _personData = TestData.getPersonData();
            it.setPersons(_personData);
          }
        };
        PersonList _doubleArrow = ObjectExtensions.<PersonList>operator_doubleArrow(_personList, _function);
        PersonList _personList_1 = new PersonList();
        final Procedure1<PersonList> _function_1 = new Procedure1<PersonList>() {
          public void apply(final PersonList it) {
            it.setName("Second");
            List<Person> _personData = TestData.getPersonData();
            it.setPersons(_personData);
          }
        };
        PersonList _doubleArrow_1 = ObjectExtensions.<PersonList>operator_doubleArrow(_personList_1, _function_1);
        ArrayList<PersonList> _newArrayList = CollectionLiterals.<PersonList>newArrayList(_doubleArrow, _doubleArrow_1);
        it.setLists(_newArrayList);
      }
    };
    return ObjectExtensions.<PersonGroup>operator_doubleArrow(_personGroup, _function);
  }
  
  public static List<Person> getPersonData() {
    ArrayList<Person> list = CollectionLiterals.<Person>newArrayList();
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
    Person _doubleArrow = ObjectExtensions.<Person>operator_doubleArrow(_person, _function);
    list.add(_doubleArrow);
    Person _person_1 = new Person();
    final Procedure1<Person> _function_1 = new Procedure1<Person>() {
      public void apply(final Person it) {
        it.setFirstName("Enrico");
        it.setLastName("Bernardetti");
        Address _address = new Address();
        final Procedure1<Address> _function = new Procedure1<Address>() {
          public void apply(final Address it) {
            it.setStreet("12 Asmolovova");
            it.setCity("Litomerice");
          }
        };
        Address _doubleArrow = ObjectExtensions.<Address>operator_doubleArrow(_address, _function);
        it.setAddress(_doubleArrow);
      }
    };
    Person _doubleArrow_1 = ObjectExtensions.<Person>operator_doubleArrow(_person_1, _function_1);
    list.add(_doubleArrow_1);
    Person _person_2 = new Person();
    final Procedure1<Person> _function_2 = new Procedure1<Person>() {
      public void apply(final Person it) {
        it.setFirstName("Steffen");
        it.setLastName("Effinge");
        Address _address = new Address();
        final Procedure1<Address> _function = new Procedure1<Address>() {
          public void apply(final Address it) {
            it.setStreet("78 Heidelbergerstraße");
            it.setCity("Dresden");
          }
        };
        Address _doubleArrow = ObjectExtensions.<Address>operator_doubleArrow(_address, _function);
        it.setAddress(_doubleArrow);
      }
    };
    Person _doubleArrow_2 = ObjectExtensions.<Person>operator_doubleArrow(_person_2, _function_2);
    list.add(_doubleArrow_2);
    Person _person_3 = new Person();
    final Procedure1<Person> _function_3 = new Procedure1<Person>() {
      public void apply(final Person it) {
        it.setFirstName("Karl");
        it.setLastName("Valenstein");
        Address _address = new Address();
        final Procedure1<Address> _function = new Procedure1<Address>() {
          public void apply(final Address it) {
            it.setStreet("17 Schloß");
            it.setCity("Frydlant");
          }
        };
        Address _doubleArrow = ObjectExtensions.<Address>operator_doubleArrow(_address, _function);
        it.setAddress(_doubleArrow);
      }
    };
    Person _doubleArrow_3 = ObjectExtensions.<Person>operator_doubleArrow(_person_3, _function_3);
    list.add(_doubleArrow_3);
    return list;
  }
}
