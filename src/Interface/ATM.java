package Interface;

import Account.*;
import Bank.*;
import Card.*;

import java.math.*;
import java.util.*;

public class ATM {
    private Card currentCard = null;
    private Bank bank;
    protected double rateOfCommissionForDeposit = 0.05;
    protected double rateOfCommissionForWithdrawing = 0.1;

    public ATM() {
        int index = new Random().nextInt(2);
        if (index == 0) {
            this.bank = Alfa.getInstance();
        } else {
            this.bank = Sber.getInstance();
        }
    }

    public Bank getBank() {
        return bank;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void insertCard(Card card) {
        if (currentCard == null) {
            this.currentCard = card;
        }
    }

    public void injectCard() {
        this.currentCard = null;
    }

    public boolean checkPinCode(int pinCode) {
        if (currentCard.getPinCode() == pinCode) {
            return true;
        }
        return false;
    }

    public Account findCurrentAccount(Card card) {
        if (card.getBankOfCard() == Alfa.getInstance()) {
            if (Alfa.cardsAndAccounts.containsKey(card)) {
                return Alfa.cardsAndAccounts.get(card);
            }
        } else if (card.getBankOfCard() == Sber.getInstance()) {
            if (Sber.cardsAndAccounts.containsKey(card)) {
                return Sber.cardsAndAccounts.get(card);
            }
        }
        return null;
    }

    public BigDecimal showBalanceOnHomeBankCard() {
        if (findCurrentAccount(currentCard) != null) {
            return findCurrentAccount(currentCard).getBalanceOfAccount();
        } else {
            return null;
        }
    }

    public BigDecimal showBalanceOnOutsideBankCard() {
        BigDecimal result = new BigDecimal(0);
        if (findCurrentAccount(currentCard) != null) {
            result = findCurrentAccount(currentCard).getBalanceOfAccount().subtract(new BigDecimal(100));
        }
        if (result.compareTo(BigDecimal.valueOf(0)) == -1) {
            result = BigDecimal.valueOf(-1);
        } else {
            findCurrentAccount(currentCard).setBalanceOfAccount(result);
            result = findCurrentAccount(currentCard).getBalanceOfAccount();
        }
        return result;
    }

    public BigDecimal getCommission(BigDecimal amount, double rateOfCommission) {
        BigDecimal commission = (amount.multiply(new BigDecimal(rateOfCommission))).setScale(0, BigDecimal.ROUND_DOWN);
        return commission;
    }

    public void depositOnHomeBankCard(BigDecimal amount) {
        findCurrentAccount(currentCard).setBalanceOfAccount(findCurrentAccount(currentCard).depositOfAccount(amount));
        BigDecimal commission = new BigDecimal(0); // если решено будет ввести комиссию на пополнение из собственных банкоматов
    }

    public void depositOnOutsideBankCard(BigDecimal amount) {
        findCurrentAccount(currentCard).setBalanceOfAccount(findCurrentAccount(currentCard).depositOfAccount(amount));
        BigDecimal commission = getCommission(amount, rateOfCommissionForDeposit);
        findCurrentAccount(currentCard).setBalanceOfAccount(findCurrentAccount(currentCard).withdrawOfAccount(commission));
    }

    public void withdrawFromHomeBankCard(BigDecimal amount) {
        BigDecimal commission = new BigDecimal(0); // если решено будет ввести комиссию на снятие из собственных банкоматов
        if (findCurrentAccount(currentCard).withdrawOfAccount(amount) != null) {
            findCurrentAccount(currentCard).setBalanceOfAccount(findCurrentAccount(currentCard).withdrawOfAccount(amount));
        }
    }

    public void withdrawFromOutsideBankCard(BigDecimal amount) {
        BigDecimal commission = getCommission(amount, rateOfCommissionForWithdrawing);
        BigDecimal newBalance = findCurrentAccount(currentCard).withdrawOfAccount(amount.add(commission));
        if (newBalance != null) {
            findCurrentAccount(currentCard).setBalanceOfAccount(newBalance);
        }
    }

    public BigDecimal getDebtOfReturn(Card card) {
        if (currentCard.getTypeOfCard() == TypeOfCard.CREDIT) {
            return findCurrentAccount(currentCard).getAmountOfDebt();
        } else {
            return null;
        }
    }

    public Date getDateOfReturn(Card card) {
        Date dateOfReturn = null;
        if (currentCard.getTypeOfCard() == TypeOfCard.CREDIT && getDebtOfReturn(card).compareTo(new BigDecimal(0)) > 0) {
            dateOfReturn = findCurrentAccount(currentCard).getDateOfRepayment();
        }
        return dateOfReturn;
    }
}
