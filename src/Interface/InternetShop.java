package Interface;

import Account.*;
import Bank.*;
import Card.*;

import java.math.*;
import java.util.*;

public class InternetShop {
    private Card currentCard = null;

    public Card getCurrentCard() {
        return currentCard;
    }

    public void involvedCardInPaymentProcess(Card card) {
        if (currentCard == null) {
            this.currentCard = card;
        }
    }

    public void debarCardInPaymentProcess() {
        this.currentCard = null;
    }

    public boolean checkCvv(int cvv) {
        return currentCard.getCvv() == cvv;
    }

    public boolean checkCardValidThru(Date date) {
        return currentCard.getCardValidThru().equals(date);
    }

    public boolean checkMonthOfCardValidThru(int month) {
        return currentCard.getCardValidThru().getMonth() == month;
    }

    public boolean checkYearOfCardValidThru(int year) {
        return currentCard.getCardValidThru().getYear() + 1900 == year;
    }

    public boolean checkSms(int userSms, int bankSms) {
        return userSms == bankSms;
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

    public void withdrawFromCard(BigDecimal amount) {
        if (amount != null && findCurrentAccount(currentCard).withdrawOfAccount(amount) != null) {
            findCurrentAccount(currentCard).setBalanceOfAccount(findCurrentAccount(currentCard).withdrawOfAccount(amount));
        }
    }
}
