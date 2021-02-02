package ru.hse.homework.collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hse.homework.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonAutoDetect
public class PhoneBook {
    /**
     * Константа - Путь к файлу по умолчанию
     */
    private final static String DEFAULT_PATH_TO_FILE = "default.txt";

    /**
     * Поле - Список контактов телефонного справочника
     */
    private List<User> phoneBook = new ArrayList<>();
    /**
     * Поле - Путь к файлу
     */
    private final String pathToFile;

    /**
     * Создает объект с путем к файлу по умолчанию
     *
     * @see #PhoneBook(String)
     */
    public PhoneBook() {
        this.pathToFile = DEFAULT_PATH_TO_FILE;
    }

    /**
     * Создает объект с путем к файлу заданным пользователем
     *
     * @param pathToFile - путь к файлу
     * @see #PhoneBook()
     */
    public PhoneBook(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    /**
     * Загружает телефонный справочник из памяти устройства
     *
     * @throws IOException если файл отсутствует или был поврежден
     */
    public void loadBook() throws IOException {
        FileReader reader = new FileReader(pathToFile);
        ObjectMapper mapper = new ObjectMapper();
        phoneBook = mapper.readValue(reader, PhoneBook.class).getPhoneBook();
    }

    /**
     * Сохраняет телефонный справочник в память устройства
     */
    public void saveBook() {
        try {
            FileWriter writer = new FileWriter(pathToFile);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, this);
            //При сохранение произошла ошибка
        } catch (IOException ignored) {
        }
    }

    /**
     * Осуществляет поиск подходящих контактов по входной Фамилии
     *
     * @param surname - фамилия
     * @return Список подходящих контактов
     */
    public List<User> searchBySurname(String surname) {
        return phoneBook.stream()
                .filter(x -> x.getSurname().toLowerCase().startsWith(surname.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Осуществляет поиск подходящих контактов по входному номеру телефона
     *
     * @param phoneNumber - номер телефона
     * @return Список подходящих контактов
     */
    public List<User> searchByPhoneNumber(String phoneNumber) {
        return phoneBook.stream()
                .filter(x -> x.getPhoneNumbers().stream().anyMatch(y -> y.startsWith(phoneNumber)))
                .collect(Collectors.toList());
    }

    /**
     * Осуществляет поиск подходящих контактов по дате рождения
     *
     * @param dateBirth - дата рождения
     * @return Список подходящих контактов
     */
    public List<User> searchByDateBirth(String dateBirth) {
        return phoneBook.stream()
                .filter(x -> x.getDateBirth().startsWith(dateBirth))
                .collect(Collectors.toList());
    }


    /**
     * Добавляет входной контакт в телефонный справочник
     *
     * @param user входной контакт
     * @return false если контакт содержит ФИО или номер телефона имеющийся в справочнике, иначе true
     */
    public boolean addUser(User user) {
        // Проверка что пользователя с такими же ФИО нет в книге
        if (phoneBook.contains(user)) {
            return false;
        }

        // Проверка что номер телефона не числится в книге
        List<String> AllPhoneNumbers = phoneBook.stream()
                .flatMap(x -> x.getPhoneNumbers().stream())
                .collect(Collectors.toList());
        for (String phoneNumber : user.getPhoneNumbers()) {
            if (AllPhoneNumbers.contains(phoneNumber)) {
                return false;
            }
        }

        phoneBook.add(user);
        return true;
    }


    /**
     * Удаляет указанный контакт из телефонного справочника
     *
     * @param user - контакт
     * @return true, если контакт был удален, false если его нет в справочнике
     */
    public boolean removeUser(User user) {

        if (!phoneBook.contains(user)) {
            return false;
        }


        phoneBook.remove(user);
        return true;
    }

    /**
     * Возвращает список контактов телефонного справочника
     *
     * @return конаткты
     */
    public List<User> getPhoneBook() {
        return phoneBook;
    }

    /**
     * Возвращает список контактов подходящих под удаление
     *
     * @param surname - фамилия, по которой осуществляется удаление
     * @return список контактов
     */
    public List<User> getUsersDelete(String surname) {
        return phoneBook.stream()
                .filter(x -> x.getSurname().toLowerCase().equals(surname.toLowerCase()))
                .collect(Collectors.toList());
    }

}
