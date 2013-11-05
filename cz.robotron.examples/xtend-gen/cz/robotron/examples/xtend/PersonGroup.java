package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.PersonList;
import cz.robotron.xtend.annotations.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@Observable
@SuppressWarnings("all")
public class PersonGroup {
  private List<PersonList> lists;
  
  public List<PersonList> getLists() {
    return this.lists;
  }
  
  public void setLists(final List<PersonList> lists) {
    List<PersonList> _oldValue = this.lists;
    this.lists = lists;
    _propertyChangeSupport.firePropertyChange("lists", _oldValue, lists);
  }
  
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.removePropertyChangeListener(listener);
  }
}
