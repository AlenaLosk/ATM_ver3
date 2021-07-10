package Account;

import Bank.Alfa;
import Bank.Bank;
import Bank.Sber;
import Card.Card;
import Card.TypeOfCard;
import Client.Client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Random;

public class Account {
    protected final BigDecimal creditLimit;
    protected BigInteger number;
    protected BigDecimal balance;
    protected Bank bank;
    protected Client client;
    protected int goalDay; //число в месяце, до которого необходимо регулярно гасить задолженность (иначе она станет просроченной)

    public Account() {
        setNumberOfAccount();
        setBankOfAccount();
        setBalanceOfAccount(new BigDecimal(0));
        this.client = new Client();
        this.creditLimit = new BigDecimal(0);
    }

    public Account(Bank bank, Client client, TypeOfCard typeOfCard) {
        setNumberOfAccount();
        this.bank = bank;
        this.client = client;
        if (typeOfCard == TypeOfCard.CREDIT) {
            //установим рандомный кредитный лимит в пределах [10_000; 110_000), округлив его до тысяч
            this.creditLimit = (new BigDecimal((int) (Math.random() * 100_000 + 10_000))).setScale(-3, BigDecimal.ROUND_DOWN);
            setGoalDay();
            setBalanceOfAccount(this.creditLimit);
        } else {
            this.creditLimit = new BigDecimal(0);
            setBalanceOfAccount(new BigDecimal(0));
        }
    }

    public Account(Bank bank, Client client, TypeOfCard typeOfCard, BigDecimal balance) {
        setNumberOfAccount();
        this.bank = bank;
        this.client = client;
        if (typeOfCard == TypeOfCard.CREDIT) {
            //установим рандомный кредитный лимит в пределах [10_000; 110_000), округлив его до тысяч
            this.creditLimit = (new BigDecimal((int) (Math.random() * 100_000 + 10_000))).setScale(-3, BigDecimal.ROUND_DOWN);
            setGoalDay();
            setBalanceOfAccount(this.creditLimit.add(balance));
        } else {
            this.creditLimit = new BigDecimal(0);
            setBalanceOfAccount(balance);
        }
    }

    public void setNumberOfAccount() { //генерация 20тизначного номера счета
        do {
            this.number = new BigInteger(64, new SecureRandom());
        } while (this.number.toString().length() != 20);
    }

    protected boolean isFormatValid(BigInteger number) {
        byte countsOfDigits = Card.getCountsOfDigits(number);
        return countsOfDigits == 16;
    }

    //устанавливает число, которое ежемесячно становится датой гашения
    public void setGoalDay() {
        this.goalDay = (int) (Math.random() * 28 + 1);
    }

    public int getGoalDay() {
        return this.goalDay;
    }

    public void setGoalDay(int day) {
        this.goalDay = day;
    }

    //возвращает дату погашения текущей задолженности
    public Date getDateOfRepayment() {
        int currentYear = new Date().getYear();
        int currentMonth = new Date().getMonth() + 1;
        int currentDay;
        if (goalDay != 0) {
            currentDay = goalDay;
        } else {
            currentDay = 21;
        }
        Date dateOfRepayment = new Date(currentYear, currentMonth, currentDay);
        return dateOfRepayment;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public BigInteger getNumberOfAccount() {
        return number;
    }

    public Bank getBankOfAccount() {
        return bank;
    }

    public Client getClientOfAccount() {
        return client;
    }

    public void setBankOfAccount() {
        int index = new Random().nextInt(2); // новому счету случайно присваивается банк
        if (index == 0) {
            this.bank = Alfa.getInstance();
        } else {
            this.bank = Sber.getInstance();
        }
    }

    public BigDecimal getBalanceOfAccount() {
        return balance;
    }

    public void setBalanceOfAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal depositOfAccount(BigDecimal amount) {
        return balance.add(amount);
    }

    public BigDecimal withdrawOfAccount(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            return balance.subtract(amount);
        }
        return null;
    }

    public BigDecimal getAmountOfDebt() {
        if (balance.compareTo(creditLimit) >= 0) {
            return new BigDecimal(0);
        } else {
            return creditLimit.subtract(balance);
        }
    }

    @Override
    public String toString() {
        return "Счет #" + getNumberString(number) + " " + bank.getName() + "банка. " +
                "\nОткрыт на имя " + this.client;
    }

    public String getNumberString(BigInteger number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat fmt = new DecimalFormat("####,####,####,####,####", symbols);
        return fmt.format(number);
    }
}
