package ch.hsr.modules.uint1.heisenberglibrary.model;

public class Customer {

    private String name, surname, street, city;
    private int    zip;

    public Customer(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setAdress(String street, int zip, String city) {
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return name + " " + surname + " , " + street + " , " + zip + " " + city;
    }

}
