package Bank;

import Account.*;
import Card.*;
import Client.*;

import java.util.*;

public class Sber extends Bank {
    static Sber instance;

    public static Sber getInstance() {
        if (instance == null) {
            instance = new Sber();
        }
        return instance;
    }

    private Sber() {
        super.bik = "044525225";
        super.name = "Сбер";
        super.partOfPan_From2To4Symbols = 276;
    }

    @Override
    public int sendSms() {
        return (int) (Math.random() * 10_000);
    }

    public static HashSet<Client> clients = new HashSet<>(); //уникальное множество клиентов Сбера
    public static HashMap<Card, Account> cardsAndAccounts = new HashMap<>(); //к одному счету может быть привязано несколько карт, каждая имеет уникальный номер
    public static HashMap<Account, Client> accountsAndClients = new HashMap<>(); //у одного клиента может быть несколько счетов, каждый счет уникален
    public static HashMap<Client, HashSet<Account>> clientsByAccounts = new HashMap<>();

}
