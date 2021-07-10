package Card;

import java.math.BigDecimal;

import static Card.TypeOfCard.CREDIT;

public class UnionPayCtCard extends Card {

    public UnionPayCtCard() {
        super();
    }

    public UnionPayCtCard(long number) {
        super(number);
    }

    public UnionPayCtCard(boolean loadClientFromFile, boolean loadNumberFromFile, String fileName) {
        super(loadClientFromFile, loadNumberFromFile, fileName);
    }

    public UnionPayCtCard(boolean loadClientFromFile, boolean loadNumberFromFile, boolean loadBalanceFromFile, String fileName) {
        super(loadClientFromFile, loadNumberFromFile, loadBalanceFromFile, fileName);
    }

    public UnionPayCtCard(BigDecimal balance) {
        super(balance);
    }

    @Override
    void setPartOfPan_1Symbol() {
        super.partOfPan_1Symbol = 6;
    }

    @Override
    void setTypeOfCard() {
        super.typeOfCard = CREDIT;
    }

    @Override
    //устанавливает случайные номер в 14 символов
    public void setNumber() {
        StringBuilder str = new StringBuilder(String.valueOf(partOfPan_1Symbol));
        // все числа карты генерятся, кроме 1й - платежная система, 2-4 - код банка
        setPartOfPan_1Symbol();
        str.append(super.partOfPan_1Symbol).append(getBankOfCard().getPartOfPan()).append((long) (Math.random() * 10_000_000_000L));
        try {
            super.number = Long.parseLong(String.valueOf(str));
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
        }
    }

    @Override
    protected boolean isFormatValid(long number) {
        byte countsOfDigits = Card.getCountsOfDigits(number);
        //пусть карты типа ЮнионПэй поддерживают только 14-ти значный номер карты
        return countsOfDigits == 14;
    }

    @Override
    public String toString() {
        return "UnionPay кредитная #" + getNumberString(super.number) + " " + super.getBankOfCard().getName() + "банка";
    }

    @Override
    public String getNumberString(long number) {
        String n = Long.toString(number);
        n = n.substring(0, 4) + " " + n.substring(4, 8) + " " + n.substring(8, 12) + " "
                + n.substring(12, n.length());
        return n;
    }
}
