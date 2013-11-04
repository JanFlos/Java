package cz.robotron.examples.xtend;

import cz.robotron.examples.xtend.Address;
import cz.robotron.xtend.annotations.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Observable
@SuppressWarnings("all")
public class Person {
  private String firstName;
  
  private String lastName;
  
  private Address address;
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(final String firstName) {
    String _oldValue = this.firstName;
    this.firstName = firstName;
    _propertyChangeSupport.firePropertyChange("firstName", _oldValue, firstName);
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(final String lastName) {
    String _oldValue = this.lastName;
    this.lastName = lastName;
    _propertyChangeSupport.firePropertyChange("lastName", _oldValue, lastName);
  }
  
  public Address getAddress() {
    return this.address;
  }
  
  public void setAddress(final Address address) {
    Address _oldValue = this.address;
    this.address = address;
    _propertyChangeSupport.firePropertyChange("address", _oldValue, address);
  }
  
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.removePropertyChangeListener(listener);
  }
}
