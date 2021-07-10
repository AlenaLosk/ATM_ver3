package Interface;

import Card.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.*;
import java.text.SimpleDateFormat;

public class GUI_InternetShop extends JFrame {
    boolean isCvvCorrect = false;
    boolean isMonthOfCardValidThruCorrect = false;
    boolean isYearOfCardValidThruCorrect = false;
    private InternetShop internetShop;
    //Настройки блока 1
    JPanel block1 = new JPanel();
    JLabel message1_1 = new JLabel("Введите cvv карты", JLabel.RIGHT);
    JPasswordField CvvField = new JPasswordField("***", 6);
    JLabel message1_2 = new JLabel("Выберите месяц", JLabel.RIGHT);
    String[] arrayOfMonths = new String[] {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь",
            "октябрь", "ноябрь", "декабрь"};
    JComboBox<String> months = new JComboBox<>(arrayOfMonths);
    JLabel message1_3 = new JLabel("Выберите год", JLabel.RIGHT);
    JComboBox years = new JComboBox();
    //Настройки блока 2
    JPanel block2 = new JPanel();
    JTextField amountField2_1 = new JTextField("Введите сумму оплаты", 22);
    JButton pay = new JButton("Оплатить");
    JLabel message2_1 = new JLabel("", JLabel.RIGHT);
    JButton stopPay = new JButton("Забрать карту");

    public GUI_InternetShop(InternetShop internetShop) {
        super("Платежная сессия интернет-магазина с картой " + internetShop.getCurrentCard());
        this.internetShop = internetShop;
        setSize(600, 600); // установить размер
        setLocation(50, 50); // крайняя левая точка экрана (0, 0), фрейм расположен по координатам правее и ниже
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GridLayout mainLayout = new GridLayout(2, 1, 10, 10);
        setLayout(mainLayout);

        JFrame thisFrame = this;

        GridLayout layout1 = new GridLayout(3, 2, 10, 10);
        block1.setLayout(layout1);
        block1.add(message1_1);
        block1.add(CvvField);
        CvvField.addActionListener(e -> {
            try {
                isCvvCorrect = internetShop.checkCvv(Integer.parseInt(CvvField.getText()));
            } catch (Exception b) {
                //Игнорируем ошибку
            }
            if (!isCvvCorrect) {
                JOptionPane.showMessageDialog(thisFrame, "Введен некорректный cvv!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                message1_1.setText("Вы ввели корректный cvv");
                CvvField.setEditable(false);
            }
        });

        block1.add(message1_2);
        block1.add(months);
        months.addActionListener(e -> {
            int userMonth = months.getSelectedIndex();
            isMonthOfCardValidThruCorrect = internetShop.checkMonthOfCardValidThru(userMonth);
        });
        block1.add(message1_3);
        years.addItem("2021");
        years.addItem("2022");
        years.addItem("2023");
        years.addItem("2024");
        years.addItem("2025");
        block1.add(years);
        years.addActionListener(e -> {
            int userYear = years.getSelectedIndex();
            isYearOfCardValidThruCorrect = internetShop.checkYearOfCardValidThru(userYear + 2021);
            if (!(isYearOfCardValidThruCorrect && isMonthOfCardValidThruCorrect)) {
                JOptionPane.showMessageDialog(thisFrame, "Введена некорректная дата!", "Уведомление",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                message1_2.setText("Вы ввели корректную дату");
                message1_3.setText("Вы ввели корректную дату");
                months.setEditable(false);
                years.setEditable(false);
            }

        });

        add(block1);

        GridLayout layout2 = new GridLayout(2, 2, 10, 10);
        block2.setLayout(layout2);
        block2.add(amountField2_1);
        block2.add(pay);
        pay.addActionListener(e -> {
            int bankSms = authorisePayment(internetShop.getCurrentCard());
            if (internetShop.getCurrentCard() == null) {
                JOptionPane.showMessageDialog(thisFrame, "В интернет-магазине не задействовано ни одной карты!" +
                        " для совершения платежей", "Уведомление", JOptionPane.WARNING_MESSAGE);
            } else if (!(isYearOfCardValidThruCorrect && isMonthOfCardValidThruCorrect && isCvvCorrect)) {
                JOptionPane.showMessageDialog(thisFrame, "Пока не введены корректные cvv, месяц и год срока действия" +
                        "\nвы не можете совершить платеж!","Уведомление", JOptionPane.WARNING_MESSAGE);
            } else {
                int userVerOfSms = Integer.parseInt(JOptionPane.showInputDialog(thisFrame, "Введите одноразовый смс-код",
                        "Запрос", JOptionPane.OK_OPTION));
                if (userVerOfSms == bankSms) {
                    BigDecimal amount = new BigDecimal(0);
                    try {
                        amount = new BigDecimal(Integer.parseInt(amountField2_1.getText()));
                    } catch (Exception a) {
                        //Игнорируем ошибки
                        amount = null;
                    }
                    if (amount == null) {
                        JOptionPane.showMessageDialog(thisFrame, "Некорректный формат суммы списания",
                                "Уведомление", JOptionPane.WARNING_MESSAGE);
                    } else if (internetShop.findCurrentAccount(internetShop.getCurrentCard()).withdrawOfAccount(amount) == null) {
                        JOptionPane.showMessageDialog(thisFrame, "Нехватка средств на карте, требумая к списанию сумма в "
                                        + amount.setScale(0, BigDecimal.ROUND_HALF_UP) + " превышает баланс",
                                "Уведомление", JOptionPane.WARNING_MESSAGE);
                    } else {
                        internetShop.withdrawFromCard(amount);
                        JOptionPane.showMessageDialog(thisFrame, "C баланса карты списано " +
                                        amount.setScale(0, BigDecimal.ROUND_HALF_UP), "Уведомление",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(thisFrame, "Введен некорректный одноразовый смс-код!",
                            "Уведомление", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        block2.add(message2_1);
        block2.add(stopPay);
        stopPay.addActionListener(e -> {
            JOptionPane.showMessageDialog(thisFrame, "Работа с картой " + internetShop.getCurrentCard() +
                            " завершена", "Уведомление",
                    JOptionPane.INFORMATION_MESSAGE);
            internetShop.debarCardInPaymentProcess();
            thisFrame.setVisible(false);
            thisFrame.dispose();
        });
        add(block2);
        pack();//автонастройка размера фрейма

        setVisible(true);
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            //Игнорировать ошибки
        }
    }

    private int authorisePayment(Card card) {
        int bankSms = card.getBankOfCard().sendSms();
        System.out.println(card.getBankOfCard().getName() + "банк отправил смс для авторизации платежа - " + bankSms);
        return bankSms;
    }
}
