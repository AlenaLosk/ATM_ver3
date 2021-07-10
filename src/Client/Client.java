package Client;

import Account.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class Client {
    private String gender;
    private String[] genders = {"мужчина", "женщина"};
    private String firstName;
    private String[] firstFemaleNames = {"Татьяна", "Ольга", "Елена", "Зоя", "Екатерина", "Марина", "Анастасия", "Наталья", "Ирина", "Елизавета"};
    private String[] firstMaleNames = {"Алексей", "Павел", "Николай", "Петр", "Иван", "Максим", "Александр", "Андрей", "Кирилл", "Дмитрий"};
    private String middleName;
    private String[] middleFemaleNames = {"Алексеевна", "Павловна", "Николаевна", "Петровна", "Ивановна", "Максимовна", "Александровна", "Андреевна", "Кирилловна", "Дмитриевна"};
    private String[] middleMaleNames = {"Алексеевич", "Павлович", "Николаевич", "Петрович", "Иванович", "Максимович", "Александрович", "Андреевич", "Кириллович", "Дмитриевич"};
    private String surname;
    private String[] femaleSurnames = {"Алексеева", "Павлова", "Николаева", "Петрова", "Иванова", "Гудкова", "Александрова", "Андреева", "Тихомирова", "Дмитриева", "Волкова", "Зайцева"};
    private String[] maleSurnames = {"Алексеев", "Павлов", "Гордеев", "Петров", "Семенов", "Гудков", "Александров", "Андреев", "Тихомиров", "Дмитриев", "Волков", "Зайцев", "Кузнецов"};
    private final int lowerYearBorder = 1940;//возраст, старше которого клиенты не обращаются в банк для выпуска карт
    private final int lowerAgeBorder = 14;//возраст, с которого разрешен выпуск карт
    private Date dateOfBirthday;
    private String typeOfDocument;
    private String[] typeOfDocuments = {"паспорт", "загранпаспорт", "водительское удостоверение"};
    private long numberOfDocument;
    public HashSet<Account> clientAccounts;

    public Client() {
        setRandomGender();
        setRandomName();
        setRandomMiddleName();
        setRandomSurname();
        setRandomDateOfBirthday();
        setRandomDocument();
        setRandomNumberOfDocument();
        clientAccounts = new HashSet<Account>();
    }

    public Client(String gender, String firstName, String middleName, String surname) {
        this.gender = gender;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        setRandomDateOfBirthday();
        setRandomDocument();
        setRandomNumberOfDocument();
        clientAccounts = new HashSet<Account>();
    }

    private void setRandomGender() {
        int index = new Random().nextInt(genders.length);
        this.gender = genders[index];
    }

    private void setRandomName() {
        int index = 0;
        if (gender.equals(genders[0])) {
            index = new Random().nextInt(firstMaleNames.length);
            this.firstName = firstMaleNames[index];
        } else {
            index = new Random().nextInt(firstFemaleNames.length);
            this.firstName = firstFemaleNames[index];
        }
    }

    private void setRandomMiddleName() {
        int index = 0;
        if (gender.equals(genders[0])) {
            index = new Random().nextInt(middleMaleNames.length);
            this.middleName = middleMaleNames[index];
        } else {
            index = new Random().nextInt(middleFemaleNames.length);
            this.middleName = middleFemaleNames[index];
        }
    }

    private void setRandomSurname() {
        int index = 0;
        if (gender.equals(genders[0])) {
            index = new Random().nextInt(maleSurnames.length);
            this.surname = maleSurnames[index];
        } else {
            index = new Random().nextInt(femaleSurnames.length);
            this.surname = femaleSurnames[index];
        }
    }

    private void setRandomDocument() {
        int index = new Random().nextInt(typeOfDocuments.length);
        this.typeOfDocument = typeOfDocuments[index];
    }

    private void setRandomNumberOfDocument() {
        this.numberOfDocument = (long) (Math.random() * 10_000_000_000L);
    }

    private void setRandomDateOfBirthday() {
        int year = (int) (Math.random() * (1900 + new Date().getYear() - lowerYearBorder - lowerAgeBorder) + lowerYearBorder);
        int month = new Random().nextInt(12);
        int day = 1;
        if (month == 3 || month == 5 || month == 8 || month == 10) {
            day = new Random().nextInt(30) + 1;
        } else if (month == 1) {
            day = new Random().nextInt(28) + 1;
        } else {
            day = new Random().nextInt(31) + 1;
        }
        this.dateOfBirthday = new GregorianCalendar(year, month, day).getTime();
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return surname + " " + firstName + " " + middleName + ",\nдата рождения: " +
                (new SimpleDateFormat("dd.MM.YYYY")).format(dateOfBirthday) + ", документ: " + typeOfDocument + " " +
                numberOfDocument;
    }
}

