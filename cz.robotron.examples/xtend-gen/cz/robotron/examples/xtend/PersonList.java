package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.Person;
import cz.robotron.xtend.annotations.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@Observable
@SuppressWarnings("all")
public class PersonList {
  private String name;
  
  private List<Person> persons;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(final String name) {
    String _oldValue = this.name;
    this.name = name;
    _propertyChangeSupport.firePropertyChange("name", _oldValue, name);
  }
  
  public List<Person> getPersons() {
    return this.persons;
  }
  
  public void setPersons(final List<Person> persons) {
    List<Person> _oldValue = this.persons;
    this.persons = persons;
    _propertyChangeSupport.firePropertyChange("persons", _oldValue, persons);
  }
  
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.removePropertyChangeListener(listener);
  }
}
