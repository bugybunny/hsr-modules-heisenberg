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

    private String name;
    private String surname;
    private String street;
    private String city;
    private int    zip;

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

}
