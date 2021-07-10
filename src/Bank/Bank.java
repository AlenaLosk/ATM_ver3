package Bank;

public abstract class Bank {
    protected String bik;
    protected String name;
    protected int partOfPan_From2To4Symbols;

    public String getBik() {
        return bik;
    }

    public String getName() {
        return name;
    }

    public int getPartOfPan() {
        return partOfPan_From2To4Symbols;
    }

    public abstract int sendSms();
}
