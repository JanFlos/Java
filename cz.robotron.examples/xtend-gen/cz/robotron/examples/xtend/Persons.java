package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.Person;
import cz.robotron.xtend.annotations.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@Observable
@SuppressWarnings("all")
public class Persons {
  private List<Person> childs;
  
  public List<Person> getChilds() {
    return this.childs;
  }
  
  public void setChilds(final List<Person> childs) {
    List<Person> _oldValue = this.childs;
    this.childs = childs;
    _propertyChangeSupport.firePropertyChange("childs", _oldValue, childs);
  }
  
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.removePropertyChangeListener(listener);
  }
}
