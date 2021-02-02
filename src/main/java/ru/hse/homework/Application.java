package ru.hse.homework;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.hse.homework.collection.PhoneBook;
import ru.hse.homework.models.Address;
import ru.hse.homework.models.User;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Application {

    private static final int CODE_RETURN_FROM_APP = 1;
    private static final int CODE_COMPLETE_ADD_PHONE_NUMBER = 1;
    private static final int CODE_ADD_ADDITIONAL_PHONE_NUMBER = 2;
    private static final int START_MENU_LEFT_BOUND = 1;
    private static final int START_MENU_RIGHT_BOUND = 8;
    private static final int ADD_PHONE_LEFT_BOUND = 1;
    private static final int ADD_PHONE_RIGHT_BOUND = 2;
    private static final String CODE_CREATE_NEW_BOOK = "да";
    private final static Scanner scanner = new Scanner(System.in);

    private static final Logger rootLogger = LogManager.getRootLogger();
    private static final Logger bookLogger = LogManager.getLogger(PhoneBook.class);

    public static void main(String[] args) {

        PhoneBook book = new PhoneBook();

        try {
            book.loadBook();
            bookLogger.info("load book");
            // Файл отсутствует или поврежден
        } catch (IOException e) {
            bookLogger.error("error message: " + e.getMessage());
            bookLogger.fatal("fatal error message: " + e.getMessage());

            String userAnswer = getInputString(
                    "При загрузке книги произошла ошибка:" +
                            "\"Загрузочный файл не был найден или был поврежден\"\n" +
                            "Хотите продолжить работу с новым телефонным справочником?\n" +
                            "Введите \"Да\", иначе любое другое слово:");
            rootLogger.info("RootLogger: input code");
            rootLogger.info("code:" + userAnswer);


            if (!userAnswer.toLowerCase().equals(CODE_CREATE_NEW_BOOK)) {
                rootLogger.info("RootLogger in end app");
                return;
            }
        }


        int menuOperationId;
        do {
            rootLogger.info("RootLogger in start menu");

            menuOperationId = getInputNumber(
                    "1.Выход из программы\n" +
                            "2.Вывод всех контактов\n" +
                            "3.Вывод всех контактов по городам\n" +
                            "4.Добавить контакт\n" +
                            "5.Удалить контакт\n" +
                            "6.Поиск по Фамилии\n" +
                            "7.Поиск по номеру телефона\n" +
                            "8.Поиск по дате рождения\n" +
                            "Ввод:", START_MENU_LEFT_BOUND, START_MENU_RIGHT_BOUND);
            rootLogger.info("RootLogger: input code menu");
            rootLogger.info("code:" + menuOperationId);

            switch (menuOperationId) {

                case 1:
                    rootLogger.info("RootLogger in \"Выход из программы\" section");
                    book.saveBook();
                    bookLogger.info("book is saved");
                    break;
                case 2:
                    rootLogger.info("RootLogger in \"Вывод всех контактов\" section");
                    String message = book.getPhoneBook().stream()
                            .map(User::longInfo)
                            .collect(Collectors.joining("\n\n"));
                    System.out.format("Ваша телефонная книга:\n%s\n", message);
                    rootLogger.info("print all phone book");
                    break;
                case 3:
                    rootLogger.info("RootLogger in \"Вывод всех контактов по городам\" section");
                    System.out.println("Телефонная книга сгруппированная по городам:");
                    Map<String, List<User>> usersByCity = book.getPhoneBook().stream().collect(
                            Collectors.groupingBy(x -> x.getAddress().getCity().toLowerCase()));
                    bookLogger.info("grouping phone book by cities");

                    for (Map.Entry<String, List<User>> item : usersByCity.entrySet()) {
                        String keyCity = item.getKey();
                        keyCity = keyCity.substring(0, 1).toUpperCase() + keyCity.substring(1).toLowerCase();
                        message = item.getValue().stream().map(User::shortInfo).collect(Collectors.joining("\n"));
                        System.out.format("%s\n%s\n", keyCity, message);
                        System.out.println();
                    }
                    bookLogger.info("print phone book by cities");
                    break;
                case 4:
                    rootLogger.info("RootLogger in \"Добавить контакт\" section");
                    User user = inputUser();
                    rootLogger.info("RootLogger: input user");

                    if (book.addUser(user)) {
                        System.out.println("Контакт успешно был добавлен!");
                        bookLogger.info("Successful add new user");

                    } else {
                        System.out.println("Контакт не был добавлен!\n" +
                                "Контакт с такими ФИО или номером телефона уже числится в книге");
                        bookLogger.error("error message:" + "\"Контакт с такими ФИО или номером телефона уже числится в книге\"");
                    }
                    break;
                case 5:
                    rootLogger.info("RootLogger in \"Удалить контакт\" section");
                    String input = getInputString("Введите Фамилию контакта, который хотите удалить:");
                    rootLogger.info("RootLogger: Input surname");
                    rootLogger.info("surname:" + input);

                    // Контакты подходящие под удаление или вовсе их нет
                    List<User> usersDelete = book.getUsersDelete(input);
                    bookLogger.info("get users for delete");

                    User deleteUser = null;
                    // Если контакт один то он и будет удаляемым контактом
                    if (usersDelete.size() == 1) {
                        deleteUser = usersDelete.get(0);
                        bookLogger.info("install user for delete");
                    }

                    // Если подходящих контактов несколько, просим пользователя уточнить кого удалить
                    if (usersDelete.size() > 1) {
                        AtomicInteger numberUser = new AtomicInteger();
                        message = usersDelete.stream()
                                .map(x -> String.format("%d.%s", numberUser.addAndGet(1), x.shortInfo()))
                                .collect(Collectors.joining("\n"));
                        System.out.format("Кого вы хотите удалить?:\n%s\n", message);
                        int indexDeleteUser = getInputNumber("Введите цифру:", 1, numberUser.get()) - 1;
                        rootLogger.info("RootLogger: chose user for delete");
                        deleteUser = usersDelete.get(indexDeleteUser);
                        bookLogger.info("install user for delete");
                    }

                    if (deleteUser == null) {
                        System.out.println("Контакт с такой фамилией не был найден!");
                        bookLogger.error("error message:\"Контакт с такой фамилией не был найден!\"");
                    } else {
                        book.removeUser(deleteUser);
                        System.out.println(deleteUser.shortInfo() + " был удален!");
                        bookLogger.info("Successful delete user");
                    }
                    break;
                case 6:
                    rootLogger.info("RootLogger in \"Поиск по фамилии\" section");
                    input = getInputString("Введите фамилию(или начало фамилии):");
                    rootLogger.info("RootLogger: input surname");
                    rootLogger.info("surname:" + input);

                    message = book.searchBySurname(input).stream()
                            .map(User::shortInfo)
                            .collect(Collectors.joining("\n"));
                    bookLogger.info("get users by surname");
                    System.out.format("Возможно вы искали:\n%s\n", message);
                    rootLogger.info("RootLogger: print all phone book");
                    break;
                case 7:
                    rootLogger.info("RootLogger in \"Поиск по номеру телефона\" section");
                    input = getInputString("Введите номер телефона(или начало телефона):");
                    rootLogger.info("RootLogger: input phone number");
                    rootLogger.info("phone number:" + input);

                    message = book.searchByPhoneNumber(input).stream()
                            .map(User::shortInfo)
                            .collect(Collectors.joining("\n"));
                    bookLogger.info("get users by phone number");
                    System.out.format("Возможно вы искали:\n%s\n", message);
                    rootLogger.info("RootLogger: print phone book");
                    break;
                case 8:
                    rootLogger.info("RootLogger in \"Поиск по дате рождения\" section");
                    input = getInputString("Введите дату рождения(или начало даты в формате dd.mm.yyyy):");
                    rootLogger.info("RootLogger: input date birth");
                    rootLogger.info("date birth:" + input);
                    message = book.searchByDateBirth(input).stream()
                            .map(User::shortInfo)
                            .collect(Collectors.joining("\n"));
                    bookLogger.info("get users by date birth");
                    System.out.format("Возможно вы искали:\n%s\n", message);
                    rootLogger.info("RootLogger: print phone book");
                    break;
            }
            System.out.println("\n");
        } while (menuOperationId != CODE_RETURN_FROM_APP);
        rootLogger.info("RootLogger in end app");
    }

    /**
     * Возвращает введенную строку пользователем
     *
     * @param message - сообщение для пользователя
     * @return строка
     */
    private static String getInputString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Возвращает экземпляр класса {@link User}, заполненный пользователем
     *
     * @return экземпляр класса {@link User}
     */
    private static User inputUser() {
        System.out.println("Заполните информацию о контакте");
        String name = getInputString("Имя:");
        rootLogger.info("RootLogger: input name");
        rootLogger.info("name:" + name);

        String surname = getInputString("Фамилия:");
        rootLogger.info("RootLogger: input surname");
        rootLogger.info("surname:" + surname);

        String patronymic = getInputString("Отчество:");
        rootLogger.info("RootLogger: input patronymic");
        rootLogger.info("patronymic:" + patronymic);

        List<String> phoneNumbers = new ArrayList<>();
        String phoneNumber = getInputString("Номер телефона(их может быть несколько):");
        phoneNumbers.add(phoneNumber);
        rootLogger.info("RootLogger: input phone number");
        rootLogger.info("phone number:" + phoneNumber);


        int addPhoneOperationId;
        do {
            addPhoneOperationId = getInputNumber(
                    "1.Продолжить\n" +
                            "2.Добавить еще один телефон\n" +
                            "Ввод:", ADD_PHONE_LEFT_BOUND, ADD_PHONE_RIGHT_BOUND);

            rootLogger.info("RootLogger: input code add phone operation");
            rootLogger.info("code:" + addPhoneOperationId);
            if (addPhoneOperationId == CODE_ADD_ADDITIONAL_PHONE_NUMBER) {
                phoneNumber = getInputString("Дополнительный номер телефона:");
                phoneNumbers.add(phoneNumber);
                rootLogger.info("RootLogger: input additional phone number");
                rootLogger.info("additional phone number:" + phoneNumber);

            }

        } while (addPhoneOperationId != CODE_COMPLETE_ADD_PHONE_NUMBER);
        rootLogger.info("RootLogger: exit from input additional phone number");


        String dateBirth = getInputString("Дата рождения(dd.mm.yyyy):");
        rootLogger.info("RootLogger: input date birth");
        rootLogger.info("date birth:" + dateBirth);

        String email = getInputString("Электронная почта:");
        rootLogger.info("RootLogger: input email");
        rootLogger.info("email:" + email);

        System.out.println("Заполните информацию об адресе проживания");

        String city = getInputString("Город:");
        rootLogger.info("RootLogger: input city");
        rootLogger.info("city:" + city);

        String street = getInputString("Улица:");
        rootLogger.info("RootLogger: input street");
        rootLogger.info("street:" + street);

        String building = getInputString("Дом:");
        rootLogger.info("RootLogger: input building");
        rootLogger.info("building:" + building);

        Address address = new Address(city, street, building);

        return new User(name, surname, patronymic, address, phoneNumbers, dateBirth, email);
    }


    /**
     * Возвращает число, введенное пользователем
     *
     * @param message    - сообщение для пользователя
     * @param leftBound  - левая граница для ввода
     * @param rightBound - правая граница для ввода
     * @return введенное число
     */
    private static int getInputNumber(String message, int leftBound, int rightBound) {
        int number;
        System.out.print(message);
        while (true) {
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();

                if (number >= leftBound && number <= rightBound) {
                    scanner.nextLine();
                    return number;
                }
            }

            rootLogger.error("error message:\"Некорректный ввод!\"");
            System.out.print("Некорректный ввод!\nПопробуйте еще раз:");
            scanner.nextLine();
        }
    }
}
