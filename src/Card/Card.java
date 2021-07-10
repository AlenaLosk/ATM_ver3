package Card;

import Account.Account;
import Bank.Alfa;
import Bank.Bank;
import Bank.Sber;
import Client.Client;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public abstract class Card {
    protected long number;
    protected Bank bank;
    protected Client cardHolder;
    protected int partOfPan_1Symbol;
    protected TypeOfCard typeOfCard;
    private int pinCode;
    private int cvv;
    private Date cardValidThru;

    public Card() {
        setBankOfCard();
        setNumber();
        cardHolder = new Client();
        setTypeOfCard();
        setPinCode(9999);
        setCvv(111);
        setCardValidThru(new GregorianCalendar(2022, 11, 31).getTime());
        Account cardAccount = new Account(this.bank, this.cardHolder, this.typeOfCard);
        cardHolder.clientAccounts.add(cardAccount);
        checkBankAndAddBankData(cardAccount);
    }

    public Card(long number) {
        setBankOfCard();
        if (isFormatValid(number)) {
            this.number = number;
        } else {
            setNumber();
        }
        cardHolder = new Client();
        setTypeOfCard();
        setPinCode(9999);
        setCvv(111);
        setCardValidThru(new GregorianCalendar(2022, 11, 31).getTime());
        Account cardAccount = new Account(this.bank, this.cardHolder, this.typeOfCard);
        cardHolder.clientAccounts.add(cardAccount);
        checkBankAndAddBankData(cardAccount);
    }

    public Card(boolean loadClientFromFile, boolean loadNumberFromFile, String fileName) {
        setBankOfCard();
        if (loadNumberFromFile) {
            this.number = Card.readNumberFromFile(fileName);
        } else {
            setNumber();
        }
        if (loadClientFromFile) {
            this.cardHolder = Card.readClientFromFile(fileName);
        } else {
            this.cardHolder = new Client();
        }
        setTypeOfCard();
        setPinCode(9999);
        setCvv(111);
        setCardValidThru(new GregorianCalendar(2022, 11, 31).getTime());
        Account cardAccount = new Account(this.bank, this.cardHolder, this.typeOfCard, new BigDecimal(10000));
        cardHolder.clientAccounts.add(cardAccount);
        checkBankAndAddBankData(cardAccount);
    }

    public Card(boolean loadClientFromFile, boolean loadNumberFromFile, boolean loadBalanceFromFile, String fileName) {
        setBankOfCard();
        if (loadNumberFromFile) {
            this.number = Card.readNumberFromFile(fileName);
        } else {
            setNumber();
        }
        if (loadClientFromFile) {
            this.cardHolder = Card.readClientFromFile(fileName);
        } else {
            this.cardHolder = new Client();
        }
        setTypeOfCard();
        setPinCode(9999);
        setCvv(111);
        setCardValidThru(new GregorianCalendar(2022, 11, 31).getTime());
        BigDecimal balance = new BigDecimal(0);
        if (loadBalanceFromFile) {
            balance = Card.readBalanceFromFile(fileName);
        } else {
            balance = new BigDecimal(10000);
        }
        Account cardAccount = new Account(this.bank, this.cardHolder, this.typeOfCard, balance);
        cardHolder.clientAccounts.add(cardAccount);
        checkBankAndAddBankData(cardAccount);
    }

    public Card(BigDecimal balance) {
        setBankOfCard();
        setNumber();
        cardHolder = new Client();
        setTypeOfCard();
        setPinCode(9999);
        setCvv(111);
        setCardValidThru(new GregorianCalendar(2022, 11, 31).getTime());
        Account cardAccount = new Account(this.bank, this.cardHolder, this.typeOfCard, balance);
        cardHolder.clientAccounts.add(cardAccount);
        checkBankAndAddBankData(cardAccount);
    }

    public static long readNumberFromFile(String fileName) {
        long number = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String tempNumber = reader.readLine();
            number = Long.parseLong(String.valueOf(tempNumber));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return number;
        }
    }

    public static Client readClientFromFile(String fileName) {
        Client client = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            String Person = reader.readLine();
            String[] list = Person.split(" ");
            String gender = list[0];
            String firstName = list[1];
            String middleName = list[2];
            String surname = list[3];
            client = new Client(gender, firstName, middleName, surname);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return client;
        }
    }

    public static BigDecimal readBalanceFromFile(String fileName) {
        BigDecimal balance = new BigDecimal(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();
            reader.readLine();
            balance = new BigDecimal(Integer.parseInt(String.valueOf(reader.readLine())));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return balance;
        }
    }

    public static byte getCountsOfDigits(long number) { // подсчет числа символов в номере карты
        return (number == 0) ? 1 : (byte) Math.ceil(Math.log10(Math.abs(number) + 0.5));
    }

    public static byte getCountsOfDigits(BigInteger number) { // подсчет числа символов в номере карты
        return (byte) (new String(number.toByteArray())).length();
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getCardValidThru() {
        return cardValidThru;
    }

    public void setCardValidThru(Date date) {
        this.cardValidThru = date;
    }

    public abstract void setNumber(); // метод произвольного выбора номера, но длиной в количество символом, предопределенное типом карты

    public void printNumberAndClientToFile(String fileName) {
        long number = getNumber();
        String gender = getCardHolder().getGender();
        String firstName = getCardHolder().getFirstName();
        String middleName = getCardHolder().getMiddleName();
        String surname = getCardHolder().getSurname();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(number + "\n" + gender + " " + firstName + " " + middleName + " " + surname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getNumber() {
        return number;
    }

    abstract void setPartOfPan_1Symbol();

    public int getPartOfPan_1Symbol() {
        return partOfPan_1Symbol;
    }

    abstract void setTypeOfCard();

    public TypeOfCard getTypeOfCard() {
        return typeOfCard;
    }

    public Client getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(Client cardHolder) {
        this.cardHolder = cardHolder;
    }

    protected abstract boolean isFormatValid(long number); // проверка соответствует ли число символов номера типу карты

    public void setBankOfCard() {
        int index = new Random().nextInt(2); // новому счету случайно присваивается банк
        if (index == 0) {
            this.bank = Alfa.getInstance();
        } else {
            this.bank = Sber.getInstance();
        }
    }

    public Bank getBankOfCard() {
        return this.bank;
    }

    public String getNumberString(long number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat fmt = new DecimalFormat("####,####,####,####", symbols);
        return fmt.format(number);
    }

    public void checkBankAndAddBankData(Account cardAccount) {
        if (this.bank == Alfa.getInstance()) {
            Alfa.clients.add(cardHolder);
            Alfa.cardsAndAccounts.put(this, cardAccount);
            Alfa.accountsAndClients.put(cardAccount, cardHolder);
            Alfa.clientsByAccounts.put(cardHolder, cardHolder.clientAccounts);
        } else if (this.bank == Sber.getInstance()) { //на случай если список банков расширится и станет более 2х
            Sber.clients.add(cardHolder);
            Sber.cardsAndAccounts.put(this, cardAccount);
            Sber.accountsAndClients.put(cardAccount, cardHolder);
            Sber.clientsByAccounts.put(cardHolder, cardHolder.clientAccounts);
        }
    }

}
