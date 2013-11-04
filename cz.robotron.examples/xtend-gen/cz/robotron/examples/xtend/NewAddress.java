package cz.robotron.examples.xtend;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class NewAddress {
  private final String _street;
  
  public String getStreet() {
    return this._street;
  }
  
  private final String _zip;
  
  public String getZip() {
    return this._zip;
  }
  
  private final String _city;
  
  public String getCity() {
    return this._city;
  }
  
  public NewAddress(final String street, final String zip, final String city) {
    super();
    this._street = street;
    this._zip = zip;
    this._city = city;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_street== null) ? 0 : _street.hashCode());
    result = prime * result + ((_zip== null) ? 0 : _zip.hashCode());
    result = prime * result + ((_city== null) ? 0 : _city.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NewAddress other = (NewAddress) obj;
    if (_street == null) {
      if (other._street != null)
        return false;
    } else if (!_street.equals(other._street))
      return false;
    if (_zip == null) {
      if (other._zip != null)
        return false;
    } else if (!_zip.equals(other._zip))
      return false;
    if (_city == null) {
      if (other._city != null)
        return false;
    } else if (!_city.equals(other._city))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
