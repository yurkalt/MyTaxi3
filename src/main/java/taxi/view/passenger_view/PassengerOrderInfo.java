package taxi.view.passenger_view;

import taxi.exception.DriverOrderActionException;
import taxi.exception.OrderNotFoundException;
import taxi.exception.WrongStatusOrderException;
import taxi.model.Order;
import taxi.model.User;
import taxi.model.ClientAccessToken;
import taxi.service.UserService;
import taxi.view.MessageAdd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassengerOrderInfo extends JFrame {

    private final UserService userService;
    private final Order myOrder;

    private JLabel mainLabel;
    private JLabel nullLabel;
    private JLabel idOrderLabel;
    private JTextField idOrderText;
    private JLabel statusLabel;
    private JTextField statusText;
    private JLabel fromLabel;
    private JTextField fromText;
    private JLabel toLabel;
    private JTextField toText;
    private JLabel distanceLabel;
    private JTextField distanceText;
    private JLabel priceLabel;
    private JTextField priceText;
    private JLabel userLabel;
    private JTextField userText;
    private JLabel driverLabel;
    private JTextField driverText;
    private JLabel carLabel;
    private JTextField carText;
    private JLabel messageLabel;
    private JTextField messageText;

    private JPanel buttonPanel1;
    private JPanel buttonPanel2;
    private JPanel buttonPanel3;
    private JPanel buttonPanel4;
    private JPanel buttonPanel5;
    private JPanel buttonPanel6;

    private JButton phoneDriverButton;
    private JButton showMapButton;
    private JButton cancelOrderButton;
    private JButton closeOrderButton;
    private JButton changeMessageButton;
    private JButton returnButton;

    public PassengerOrderInfo(UserService userService, Order myOrder) {

        this.userService = userService;
        this.myOrder = myOrder;

        setTitle("Main");
        setSize(600, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(14, 2);
        setLayout(gridLayout);

        mainLabel = new JLabel("ORDER INFO");
        nullLabel = new JLabel("");

        idOrderLabel = new JLabel("ORDER ID:");
        idOrderText = new JTextField();
        idOrderText.setEditable(false);
        idOrderText.setText(myOrder.getId() + "");

        statusLabel = new JLabel("ORDER STATUS:");
        statusText = new JTextField(myOrder.getOrderStatus() + "");
        statusText.setEditable(false);

        fromLabel = new JLabel("FROM:");
        fromText = new JTextField(myOrder.getFrom().getCity() + " " +
                                    myOrder.getFrom().getStreet() + " " +
                                        myOrder.getFrom().getHouseNum());
        fromText.setEditable(false);

        toLabel = new JLabel("TO:");
        toText = new JTextField(myOrder.getTo().getCity() + " " +
                                    myOrder.getTo().getStreet() + " " +
                                        myOrder.getTo().getHouseNum());
        toText.setEditable(false);

        distanceLabel = new JLabel("DISTANCE, km:");
        distanceText = new JTextField(myOrder.getDistance() + "");
        distanceText.setEditable(false);

        priceLabel = new JLabel("PRICE, uah:");
        priceText = new JTextField(myOrder.getPrice() + "");
        priceText.setEditable(false);

        userLabel = new JLabel("USER INFO:");
        userText = new JTextField(myOrder.getPassenger().getName() + ", " + myOrder.getPassenger().getPhone());
        userText.setEditable(false);

        driverLabel = new JLabel("DRIVER INFO:");
        driverText = new JTextField();
        driverText.setEditable(false);
        if (myOrder.getDriver() != null) {
            driverText.setText(myOrder.getDriver().getName() + ", " + myOrder.getDriver().getPhone());
        }

        carLabel = new JLabel("CAR INFO:");
        carText = new JTextField();
        carText.setEditable(false);
        if (myOrder.getDriver() != null) {
            User orderDriver = myOrder.getDriver();
            carText.setText(orderDriver.getCar().getModel() + ", " + orderDriver.getCar().getNumber());
        }

        messageLabel = new JLabel("MESSAGE:");
        messageText = new JTextField(myOrder.getMessage());
        messageText.setEditable(false);

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        phoneDriverButton = new JButton("Make call");
        phoneDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myOrder.getDriver() != null) {
                    JOptionPane.showMessageDialog(getParent(), "Now you call driver " +
                            myOrder.getDriver().getName() + " " + myOrder.getDriver().getPhone());
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Your order without a driver yet. Please wait");
                }
            }
        });
        buttonPanel1.add(phoneDriverButton);

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        showMapButton = new JButton("Show map");
        showMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getParent(), "You need use Java Fix :)");
            }
        });
        buttonPanel2.add(showMapButton);

        buttonPanel3 = new JPanel(new GridLayout(1,1));
        cancelOrderButton = new JButton("Cancel order");
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JOptionPane.showMessageDialog(getParent(), "Order id " + myOrder.getId() + " will be cancelled");
                    Order cancelledOrder = userService.cancelOrder(myOrder.getId());
                    //todo rate driver
                    dispose();
                    new PassengerOrderInfo(userService, cancelledOrder);

                } catch (OrderNotFoundException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Order not found in data base");
                }

            }
        });
        buttonPanel3.add(cancelOrderButton);

        buttonPanel4 = new JPanel(new GridLayout(1,1));
        closeOrderButton = new JButton("Order done");
        closeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JOptionPane.showMessageDialog(getParent(), "Order id " + myOrder.getId() + " will be closed");
                    Order closedOrder = userService.closeOrder(ClientAccessToken.getAccessToken(), myOrder.getId());
                    //todo rate driver
                    dispose();
                    new PassengerOrderInfo(userService, closedOrder);

                } catch (OrderNotFoundException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Order not found in data base");
                } catch (WrongStatusOrderException e1) {
                    JOptionPane.showMessageDialog(getParent(), "Order id " + myOrder.getId() +
                            " must have status IN_PROGRESS for close");
                } catch (DriverOrderActionException e1) {
                    JOptionPane.showMessageDialog(getParent(), "This order is not your order");
                }

            }
        });
        buttonPanel4.add(closeOrderButton);

        buttonPanel5 = new JPanel(new GridLayout(1,1));
        changeMessageButton = new JButton("Add message");
        changeMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                new MessageAdd(userService, myOrder);
            }
        });
        buttonPanel5.add(changeMessageButton);

        buttonPanel6 = new JPanel(new GridLayout(1,1));
        returnButton = new JButton("Return to menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PassengerMenu(userService);
            }
        });
        buttonPanel6.add(returnButton);

        getContentPane().add(mainLabel);
        getContentPane().add(nullLabel);

        getContentPane().add(idOrderLabel);
        getContentPane().add(idOrderText);

        getContentPane().add(statusLabel);
        getContentPane().add(statusText);

        getContentPane().add(fromLabel);
        getContentPane().add(fromText);

        getContentPane().add(toLabel);
        getContentPane().add(toText);

        getContentPane().add(distanceLabel);
        getContentPane().add(distanceText);

        getContentPane().add(priceLabel);
        getContentPane().add(priceText);

        getContentPane().add(userLabel);
        getContentPane().add(userText);

        getContentPane().add(driverLabel);
        getContentPane().add(driverText);

        getContentPane().add(carLabel);
        getContentPane().add(carText);

        getContentPane().add(messageLabel);
        getContentPane().add(messageText);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);
        getContentPane().add(buttonPanel3);
        getContentPane().add(buttonPanel4);
        getContentPane().add(buttonPanel5);
        getContentPane().add(buttonPanel6);
    }
}


