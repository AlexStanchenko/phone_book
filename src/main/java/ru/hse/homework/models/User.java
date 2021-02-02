package ru.hse.homework.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;
import java.util.Objects;

@JsonAutoDetect
public class User {


    /**
     * Поле - Имя
     */
    private String name;
    /**
     * Поле - Фамилия
     */
    private String surname;
    /**
     * Поле - Отчество
     */
    private String patronymic;
    /**
     * Поле - Адрес
     */
    private Address address;
    /**
     * Поле - Список номеров
     */
    private List<String> phoneNumbers;
    /**
     * Поле - Дата рождения
     */
    private String dateBirth;
    /**
     * Поле - Электронная почта
     */
    private String emailAddress;


    public User(String name, String surname, String patronymic, Address address, List<String> phoneNumbers, String dateBirth, String emailAddress) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
        this.dateBirth = dateBirth;
        this.emailAddress = emailAddress;
    }

    public User() {

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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    /**
     * Переопределнный equals сравнивающий объекты данного класса
     * по полям {@link #name,#surname,#patronymic}
     *
     * @param o - сравниваемый объект
     * @return true, если у объектов совподают поля {@link #name,#surname,#patronymic}, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name.toLowerCase(), user.name.toLowerCase()) &&
                Objects.equals(surname.toLowerCase(), user.surname.toLowerCase()) &&
                Objects.equals(patronymic.toLowerCase(), user.patronymic.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic, address, phoneNumbers, dateBirth, emailAddress);
    }

    /**
     * Преобразование объекта в строку с коротким описанием
     *
     * @return строковое представление объекта
     */
    public String shortInfo() {
        return String.format("%s %s %s(%s) - %s",
                name, surname, patronymic, dateBirth, String.join(",", phoneNumbers));


    }


    /**
     * Преобразование объекта в строку с полным описанием
     *
     * @return строковое представление объекта
     */
    public String longInfo() {
        return shortInfo() + String.format("\naddress:%s\nemail:%s",
                address, emailAddress);

    }

}
