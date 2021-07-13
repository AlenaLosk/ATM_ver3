import Card.Card;
import Card.MirDtCard;
import Card.UnionPayCtCard;
import Card.VisaDtCard;
import Interface.ATM;
import Interface.GUI_ATM;
import Interface.GUI_InternetShop;
import Interface.InternetShop;

import java.io.Closeable;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Card card1 = new UnionPayCtCard(5790_1567_3333_11L);
//        card1.printNumberAndClientToFile("1.txt");
        Card card6 = new VisaDtCard(true, true, true, "read3.txt");
        Card card9 = new MirDtCard(new BigDecimal(160000));
        ATM atm = new ATM();
        atm.insertCard(card6);
        GUI_ATM frame1 = new GUI_ATM(atm);
        int openCounter = 0;
        InternetShop internetShop = new InternetShop();
        internetShop.involvedCardInPaymentProcess(card9);
        GUI_InternetShop frame2 = new GUI_InternetShop(internetShop);
        while (openCounter == 0) {
            if (GUI_ATM.isWindowClosed) {
                frame2.setVisible(true);
                openCounter++;
            }
        }
    }
}
