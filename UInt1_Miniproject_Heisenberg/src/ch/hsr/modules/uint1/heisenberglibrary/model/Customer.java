/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.hsr.modules.uint1.heisenberglibrary.model;

/**
 * 
 * @author mstolze
 * @author msyfrig
 */
public class Customer extends AbstractObservable {

    protected String name;
    protected String surname;
    protected String street;
    protected String city;
    protected int    zip;

    public Customer(String aName, String aSurname) {
        name = aName;
        surname = aSurname;
    }

    public void setAdress(String aStreet, int aZip, String aCity) {
        Customer oldValue = null;
        try {
            oldValue = (Customer) clone();
        }
        catch (CloneNotSupportedException aCloneableException) {
            // this will never happen since we are cloneable
            aCloneableException.printStackTrace();
        }

        street = aStreet;
        zip = aZip;
        city = aCity;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.ADDRESS, oldValue, this));
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        String oldValue = name;
        name = aName;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.NAME, oldValue, name));
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String aSurname) {
        String oldValue = surname;
        surname = aSurname;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.SURNAME, oldValue, surname));
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String aStreet) {
        String oldValue = street;
        street = aStreet;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.STREET, oldValue, street));
    }

    public String getCity() {
        return city;
    }

    public void setCity(String aCity) {
        String oldValue = city;
        city = aCity;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.CITY, oldValue, city));
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int aZip) {
        int oldValue = zip;
        zip = aZip;
        doNotify(new ObservableModelChangeEvent(
                ModelChangeTypeEnums.Customer.ZIP, Integer.valueOf(oldValue),
                Integer.valueOf(zip)));
    }

    @Override
    public String toString() {
        return name + " " + surname + " , " + street + " , " + zip + " " + city;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((street == null) ? 0 : street.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        result = prime * result + zip;
        return result;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject == null) {
            return false;
        }
        if (getClass() != anObject.getClass()) {
            return false;
        }
        Customer other = (Customer) anObject;
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (street == null) {
            if (other.street != null) {
                return false;
            }
        } else if (!street.equals(other.street)) {
            return false;
        }
        if (surname == null) {
            if (other.surname != null) {
                return false;
            }
        } else if (!surname.equals(other.surname)) {
            return false;
        }
        if (zip != other.zip) {
            return false;
        }
        return true;
    }
}
