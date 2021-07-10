package Bank;

import Account.*;
import Card.*;
import Client.*;

import java.util.*;

public class Alfa extends Bank {
    static Alfa instance;

    public static Alfa getInstance() {
        if (instance == null) {
            instance = new Alfa();
        }
        return instance;
    }

    private Alfa() {
        super.bik = "044525593";
        super.name = "Альфа";
        super.partOfPan_From2To4Symbols = 234;
    }

    @Override
    public int sendSms() {
        return (int) (Math.random() * 10_000);
    }

    public static HashSet<Client> clients = new HashSet<>(); //уникальное множество клиентов Альфабанка
    public static HashMap<Card, Account> cardsAndAccounts = new HashMap<>(); //к одному счету может быть привязано несколько карт, каждая имеет уникальный номер
    public static HashMap<Account, Client> accountsAndClients = new HashMap<>(); //у одного клиента может быть несколько счетов, каждый счет уникален
    public static HashMap<Client, HashSet<Account>> clientsByAccounts = new HashMap<>();
}
