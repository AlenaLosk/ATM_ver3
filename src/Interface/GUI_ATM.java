package Interface;

import Card.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.text.SimpleDateFormat;

public class GUI_ATM extends JFrame {
    public boolean isWindowClosed = false;
    boolean isPinCodeCorrect = false;
    private ATM atm;
    //Настройки блока 1
    JPanel block1 = new JPanel();
    JLabel message1 = new JLabel("Введите пин-код карты", JLabel.RIGHT);
    JPasswordField pinCodeField = new JPasswordField("****", 6);
    //Настройки блока 2
    JPanel block2 = new JPanel();
    JLabel message2_1 = new JLabel("Введите сумму пополнения", JLabel.RIGHT);
    JTextField amountField2_1 = new JTextField("", 15);
    JButton deposit = new JButton("Пополнить");
    JLabel message2_2 = new JLabel("Введите сумму снятия", JLabel.RIGHT);
    JTextField amountField2_2 = new JTextField("", 15);
    JButton withdraw = new JButton("Снять");
    JLabel message2_3 = new JLabel("Здесь будет отображен баланс", JLabel.RIGHT);
    JTextField amountField2_3 = new JTextField("", 15);
    JButton balance = new JButton("Показать баланс");
    JLabel message2_4_1 = new JLabel("", JLabel.RIGHT);
    JLabel message2_4_2 = new JLabel("", JLabel.RIGHT);
    JButton removeCard = new JButton("Забрать карту");

    public GUI_ATM(ATM atm) {
        super("Интерфейс банкомата " + atm.getBank().getName() + " банка. Внутри находится карта " + atm.getCurrentCard());
        this.atm = atm;
        setSize(800, 400); // установить размер
        setLocationRelativeTo(null); // фрейм по крайней левой точке будет центрирован на экране
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridLayout mainLayout = new GridLayout(2, 1, 10, 10);
        setLayout(mainLayout);

        JFrame thisFrame = this;

        thisFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                isWindowClosed = true;
            }
        });

        FlowLayout layout1 = new FlowLayout(FlowLayout.CENTER, 10, 50);
        block1.setLayout(layout1);
        block1.add(message1);
        block1.add(pinCodeField);
        add(block1);
        pinCodeField.addActionListener(e -> {
            try {
                isPinCodeCorrect = atm.checkPinCode(Integer.parseInt(pinCodeField.getText()));
            } catch (Exception c) {
                //Игнорируем ошибку
            }
            if (!isPinCodeCorrect) {
                JOptionPane.showMessageDialog(thisFrame, "Введен некорректный пин-код!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                message1.setText("Вы ввели корректный пин-код");
                pinCodeField.setEditable(false);
                //JOptionPane.showMessageDialog(thisFrame, "Вы ввели корректный пин-код", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        GridLayout layout2 = new GridLayout(4, 3, 10, 10);
        block2.setLayout(layout2);
        block2.add(message2_1);
        block2.add(amountField2_1);
        block2.add(deposit);
        deposit.addActionListener(e -> {
            BigDecimal amount = new BigDecimal(0);
            if (atm.getCurrentCard() == null) {
                JOptionPane.showMessageDialog(thisFrame, "Сначала вставьте карту в банкомат!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!isPinCodeCorrect) {
                JOptionPane.showMessageDialog(thisFrame, "Пока не введен корректный пин-код,\nвы не можете пополнить карту",
                        "Уведомление", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    amount = new BigDecimal(Integer.parseInt(amountField2_1.getText()));
                    if (!atm.getBank().equals(atm.getCurrentCard().getBankOfCard())) {
                        BigDecimal commission = atm.getCommission(amount, atm.rateOfCommissionForDeposit);
                        atm.depositOnOutsideBankCard(amount);
                        JOptionPane.showMessageDialog(thisFrame, "Баланс карты пополнен на " + amount.subtract(commission) +
                                        ". За вычетом комиссии за пополнение - " + commission, "Уведомление",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        atm.depositOnHomeBankCard(amount);
                        JOptionPane.showMessageDialog(thisFrame, "Баланс карты пополнен на " + amount, "Уведомление",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception d) {
                    JOptionPane.showMessageDialog(thisFrame, "Неверный формат суммы пополнения!",
                            "Уведомление", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        block2.add(message2_2);
        block2.add(amountField2_2);
        block2.add(withdraw);
        withdraw.addActionListener(e -> {
            BigDecimal amount = new BigDecimal(0);
            if (atm.getCurrentCard() == null) {
                JOptionPane.showMessageDialog(thisFrame, "Сначала вставьте карту в банкомат!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!isPinCodeCorrect) {
                JOptionPane.showMessageDialog(thisFrame, "Пока не введен корректный пин-код,\nвы не можете снять деньги с карты",
                        "Уведомление", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    amount = new BigDecimal(Integer.parseInt(amountField2_2.getText()));
                    BigDecimal commission = atm.getCommission(amount, atm.rateOfCommissionForWithdrawing);
                    if (!atm.getBank().equals(atm.getCurrentCard().getBankOfCard())) {
                        if (atm.findCurrentAccount(atm.getCurrentCard()).withdrawOfAccount(amount.add(commission)) == null) {
                            JOptionPane.showMessageDialog(thisFrame, "Нехватка средств на карте, требумая к списанию сумма в " +
                                            amount + " и комиссия за снятие в " + commission + " (суммарно - " +
                                            amount.add(commission) + ") превышают баланс", "Уведомление",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            atm.withdrawFromOutsideBankCard(amount);
                            JOptionPane.showMessageDialog(thisFrame, "C баланса карты списано " + amount.add(commission) +
                                            ". В том числе комиссия за снятие составила " + commission, "Уведомление",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        if (atm.findCurrentAccount(atm.getCurrentCard()).withdrawOfAccount(amount) == null) {
                            JOptionPane.showMessageDialog(thisFrame, "Нехватка средств на карте, требумая к списанию сумма в "
                                            + amount + " превышает баланс", "Уведомление",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            atm.withdrawFromHomeBankCard(amount);
                            JOptionPane.showMessageDialog(thisFrame, "C баланса карты списано " + amount, "Уведомление",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(thisFrame, "Неверный формат суммы списания!",
                            "Уведомление", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        block2.add(message2_3);
        amountField2_3.setEditable(false);
        block2.add(amountField2_3);
        block2.add(balance);
        balance.addActionListener(e -> {
            if (atm.getCurrentCard() == null) {
                JOptionPane.showMessageDialog(thisFrame, "Сначала вставьте карту в банкомат!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!isPinCodeCorrect) {
                JOptionPane.showMessageDialog(thisFrame, "Пока не введен корректный пин-код,\nвы не можете снять деньги с карты",
                        "Уведомление", JOptionPane.WARNING_MESSAGE);
            } else {
                BigDecimal newBalance = new BigDecimal(0);
                if (!(atm.getBank()).equals(atm.getCurrentCard().getBankOfCard())) {
                    newBalance = atm.showBalanceOnOutsideBankCard();
                    if (newBalance.compareTo(BigDecimal.valueOf(-1)) == 0) {
                        JOptionPane.showMessageDialog(thisFrame, "Комиссия за запрос баланса составляет 100.\n" +
                                        "На карте недостаточно средств для оказания данной услуги.", "Уведомление",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(thisFrame, "Комиссия за запрос баланса - 100.\n" +
                                        "Итоговый баланс карты - " + newBalance.setScale(0, BigDecimal.ROUND_HALF_UP) +
                                        getDebtAndDateOfReturn(atm.getCurrentCard()), "Уведомление",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    newBalance = atm.showBalanceOnHomeBankCard();
                    JOptionPane.showMessageDialog(thisFrame, "Итоговый баланс карты - " +
                                    newBalance.setScale(0, BigDecimal.ROUND_HALF_UP) +
                                    getDebtAndDateOfReturn(atm.getCurrentCard()), "Уведомление",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                String text = newBalance.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
                amountField2_3.setText(text);
            }
        });

        block2.add(message2_4_1);
        block2.add(message2_4_2);
        block2.add(removeCard);
        add(block2);
        removeCard.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        removeCard.addActionListener(e -> {
            JOptionPane.showMessageDialog(thisFrame, "Карта " + atm.getCurrentCard() + " извлечена", "Уведомление",
                    JOptionPane.INFORMATION_MESSAGE);
            atm.injectCard();
            thisFrame.setVisible(false);
            thisFrame.dispose();
        });
        //pack();//автонастройка размера фрейма
        setVisible(true);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            //Игнорировать ошибки
        }
    }

    public String getDebtAndDateOfReturn(Card card) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        BigDecimal amountOfDebt = atm.getDebtOfReturn(card);
        if (amountOfDebt == null) {
            return "";
        } else if (amountOfDebt.compareTo(new BigDecimal(0)) > 0) {
            return "\nСумма долга по кредитной карте - " + amountOfDebt.setScale(0, BigDecimal.ROUND_HALF_UP) + "\n" +
                    "Дата гашения задолженности: " + formatter.format(atm.getDateOfReturn(card));
        } else {
            return "\nСумма долга по кредитной карте - " + amountOfDebt.setScale(0, BigDecimal.ROUND_HALF_UP);
        }
    }
}
