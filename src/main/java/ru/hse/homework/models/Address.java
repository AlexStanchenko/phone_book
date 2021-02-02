package ru.hse.homework.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Address {

    /**
     * Поле - Город
     */
    private String city;
    /**
     * Поле - Улица
     */
    private String street;
    /**
     * Поле - Дом
     */
    private String building;

    public Address(String city, String street, String building) {
        this.city = city;
        this.street = street;
        this.building = building;
    }

    public Address() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", city, street, building);
    }
}
