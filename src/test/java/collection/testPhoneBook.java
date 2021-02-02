package collection;

import org.junit.jupiter.api.*;
import ru.hse.homework.collection.PhoneBook;
import ru.hse.homework.models.Address;
import ru.hse.homework.models.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class testPhoneBook {

    private final static String TEST_PATH_TO_FILE = "testPhoneBook.txt";
    private static final PhoneBook book = new PhoneBook(TEST_PATH_TO_FILE);

    @BeforeAll
    public static void createBook() {
        User user1 = new User(
                "Александр",
                "Маянский",
                "Сергеевич",
                new Address("Москва", "Октябрьская", "55к2"),
                List.of("89035832488", "89778643081"),
                "17.01.2002",
                "mikunski@mail.ru");

        User user2 = new User(
                "Михаил",
                "Маянский",
                "Сергеевич",
                new Address("Москва", "Октябрьская", "55к2"),
                List.of("89255673433"),
                "27.10.2003",
                "mai123f.mi@mail.ru");

        User user3 = new User(
                "Дмитрий",
                "Волханов",
                "Владимирович",
                new Address("Санкт-Петербург", "Броницкая", "22"),
                List.of("89775620417"),
                "30.06.1999",
                "deminator3000@gmail.com");

        User user4 = new User(
                "Василий",
                "Хаинский",
                "Дмитриевич",
                new Address("Владимир", "Полянская", "44"),
                List.of("89030620417"),
                "17.06.1999",
                "hul123kkf@gmail.com");


        book.addUser(user1);
        book.addUser(user2);
        book.addUser(user3);
        book.addUser(user4);
        book.saveBook();
    }

    @BeforeEach
    public void updateBook() throws IOException {
        book.loadBook();
    }


    @Test
    @DisplayName("Загрузка справочника")
    public void testLoadBook() throws IOException {
        PhoneBook newBook = new PhoneBook(TEST_PATH_TO_FILE);

        newBook.loadBook();
        List<User> actual = newBook.getPhoneBook();
        List<User> expected = List.of(
                new User(
                        "Александр",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89035832488", "89778643081"),
                        "17.01.2002",
                        "mikunski@mail.ru"),

                new User(
                        "Михаил",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89255673433"),
                        "27.10.2003",
                        "mai123f.mi@mail.ru"),

                new User(
                        "Дмитрий",
                        "Волханов",
                        "Владимирович",
                        new Address("Санкт-Петербург", "Броницкая", "22"),
                        List.of("89775620417"),
                        "30.06.1999",
                        "deminator3000@gmail.com"),

                new User(
                        "Василий",
                        "Хаинский",
                        "Дмитриевич",
                        new Address("Владимир", "Полянская", "44"),
                        List.of("89030620417"),
                        "17.06.1999",
                        "hul123kkf@gmail.com"));

        Assertions.assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Сохранение справочника")
    public void testSaveBook() throws IOException {
        PhoneBook newBook = new PhoneBook("testSaveBook.txt");

        User user1 = new User(
                "Дмитрий",
                "Волханов",
                "Владимирович",
                new Address("Санкт-Петербург", "Броницкая", "22"),
                List.of("89775620417"),
                "30.06.1999",
                "deminator3000@gmail.com");

        User user2 = new User(
                "Василий",
                "Хаинский",
                "Дмитриевич",
                new Address("Владимир", "Полянская", "44"),
                List.of("89030620417"),
                "17.06.1999",
                "hul123kkf@gmail.com");


        newBook.addUser(user1);
        newBook.addUser(user2);
        newBook.saveBook();
        newBook.getPhoneBook().clear();
        newBook.loadBook();
        List<User> actual = newBook.getPhoneBook();
        List<User> expected = List.of(user1, user2);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Добавление контакта с имеющимися ФИО в справочнике")
    public void testAddUserRepeatFullName() {

        User testUser = new User(
                "ДМИтрий",
                "волханов",
                "Владимирович",
                new Address("Москва", "Чертановская", "31"),
                List.of("89053483477"),
                "30.06.2000",
                "deminaаааtor@mail.ru");

        Assertions.assertFalse(book.addUser(testUser));

    }

    @Test
    @DisplayName("Добавление контакта с имеющимися номерами телефонов в справочнике")
    public void testAddUserRepeatPhoneNumber() {

        User testUser = new User(
                "Александр",
                "Александров",
                "Александрович",
                new Address("Москва", "Чертановская", "31"),
                List.of("89035832488"),
                "30.06.2000",
                "deminaаааtor@mail.ru");

        Assertions.assertFalse(book.addUser(testUser));

    }

    @Test
    @DisplayName("Успешное добавление контакта")
    public void testAddUserSuccessful() {
        User testUser = new User(
                "Александр",
                "Александров",
                "Александрович",
                new Address("Москва", "Чертановская", "31"),
                List.of("89052399233"),
                "30.06.2000",
                "deminaаааtor@mail.ru");

        Assertions.assertTrue(book.addUser(testUser));

    }

    @Test
    @DisplayName("Удаление контакта, которого нет в српавочнике")
    public void testRemoveUserNotContain() {

        User testUser = new User(
                "Александр",
                "Александров",
                "Александрович",
                new Address("Москва", "Чертановская", "31"),
                List.of("89052399233"),
                "30.06.2000",
                "deminaаааtor@mail.ru");

        Assertions.assertFalse(book.removeUser(testUser));

    }

    @Test
    @DisplayName("Успешное удаление контакта")
    public void testRemoveUserSuccessful() {
        User testUser = new User(
                "Александр",
                "Маянский",
                "Сергеевич",
                new Address("Москва", "Октябрьская", "55к2"),
                List.of(""),
                "",
                "");

        Assertions.assertTrue(book.removeUser(testUser));

    }

    @Test
    @DisplayName("Поиск в справочнике по фамилии")
    public void testSearchBySurname() {
        List<User> actual = book.searchBySurname("МАЯ");
        List<User> expected = List.of(
                new User(
                        "Александр",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89035832488", "89778643081"),
                        "17.01.2002",
                        "mikunski@mail.ru"),

                new User(
                        "Михаил",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89255673433"),
                        "27.10.2003",
                        "mai123f.mi@mail.ru"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Поиск в справочнике по номеру телефона")
    public void testSearchByPhoneNumber() {
        List<User> actual = book.searchByPhoneNumber("8903");
        List<User> expected = List.of(
                new User(
                        "Александр",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89035832488", "89778643081"),
                        "17.01.2002",
                        "mikunski@mail.ru"),

                new User(
                        "Василий",
                        "Хаинский",
                        "Дмитриевич",
                        new Address("Владимир", "Полянская", "44"),
                        List.of("89030620417"),
                        "17.06.1999",
                        "hul123kkf@gmail.com"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Поиск в справочнике по дате рождения")
    public void testSearchByDateBirth() {
        List<User> actual = book.searchByDateBirth("17");
        List<User> expected = List.of(
                new User(
                        "Александр",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89035832488", "89778643081"),
                        "17.01.2002",
                        "mikunski@mail.ru"),

                new User(
                        "Василий",
                        "Хаинский",
                        "Дмитриевич",
                        new Address("Владимир", "Полянская", "44"),
                        List.of("89030620417"),
                        "17.06.1999",
                        "hul123kkf@gmail.com"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Возврат коллекции контактов, подходящих под удаление")
    public void testGetUsersDelete() {
        List<User> actual1 = book.getUsersDelete("мая");
        List<User> expected1 = Collections.emptyList();
        Assertions.assertEquals(expected1, actual1);


        List<User> actual2 = book.getUsersDelete("маянский");
        List<User> expected2 = List.of(
                new User(
                        "Александр",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89035832488", "89778643081"),
                        "17.01.2002",
                        "mikunski@mail.ru"),
                new User(
                        "Михаил",
                        "Маянский",
                        "Сергеевич",
                        new Address("Москва", "Октябрьская", "55к2"),
                        List.of("89255673433"),
                        "27.10.2003",
                        "mai123f.mi@mail.ru"));

        Assertions.assertEquals(expected2, actual2);
    }

}
