package Card;

import java.math.BigDecimal;

import static Card.TypeOfCard.DEBIT;

public class VisaDtCard extends Card {

    public VisaDtCard() {
        super();
    }

    public VisaDtCard(long number) {
        super(number);
    }

    public VisaDtCard(boolean loadClientFromFile, boolean loadNumberFromFile, String fileName) {
        super(loadClientFromFile, loadNumberFromFile, fileName);
    }

    public VisaDtCard(boolean loadClientFromFile, boolean loadNumberFromFile, boolean loadBalanceFromFile, String fileName) {
        super(loadClientFromFile, loadNumberFromFile, loadBalanceFromFile, fileName);
    }

    public VisaDtCard(BigDecimal balance) {
        super(balance);
    }

    @Override
    void setPartOfPan_1Symbol() {
        super.partOfPan_1Symbol = 4;
    }

    @Override
    void setTypeOfCard() {
        super.typeOfCard = DEBIT;
    }

    @Override
    //устанавливает случайные номер в 16 символов
    public void setNumber() {
        StringBuilder str = new StringBuilder(String.valueOf(partOfPan_1Symbol));
        // все числа карты генерятся, кроме 1й - платежная система, 2-4 - код банка
        setPartOfPan_1Symbol();
        str.append(super.partOfPan_1Symbol).append(getBankOfCard().getPartOfPan()).append((long) (Math.random() * 1_000_000_000_000L));
        try {
            super.number = Long.parseLong(String.valueOf(str));
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }
    }

    @Override
    protected boolean isFormatValid(long number) {
        byte countsOfDigits = Card.getCountsOfDigits(number);
        //пусть карты типа Виза поддерживают только 16-ти значный номер карты
        return countsOfDigits == 16;
    }

    @Override
    public String toString() {
        return "Visa дебетовая #" + getNumberString(super.number) + " " + super.getBankOfCard().getName() + "банка";
    }
}
