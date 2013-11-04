package cz.robotron.examples.xtend;

import cz.robotron.xtend.annotations.Observable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Observable
@SuppressWarnings("all")
public class Address {
  private String street;
  
  private String city;
  
  public String getStreet() {
    return this.street;
  }
  
  public void setStreet(final String street) {
    String _oldValue = this.street;
    this.street = street;
    _propertyChangeSupport.firePropertyChange("street", _oldValue, street);
  }
  
  public String getCity() {
    return this.city;
  }
  
  public void setCity(final String city) {
    String _oldValue = this.city;
    this.city = city;
    _propertyChangeSupport.firePropertyChange("city", _oldValue, city);
  }
  
  private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.addPropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    this._propertyChangeSupport.removePropertyChangeListener(listener);
  }
}
