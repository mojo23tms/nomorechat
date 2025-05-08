package client.gui.src;

import client.src.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame{

    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;
    private JButton exitButton;

    public ChatClientGUI() {
        super("nomorechat");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color backGroundColor = new Color(240, 240, 240);
        Color buttonColor = new Color(75, 75, 75);
        Color textColor = new Color(50, 50, 50);
        Font textFont = new Font("Montserrat", Font.BOLD, 14);
        Font buttonFont = new Font("Montserrat", Font.BOLD, 12);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(backGroundColor);
        messageArea.setFont(textFont);
        messageArea.setForeground(textColor);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        String name = JOptionPane.showInputDialog(this,
                "Enter your name: ", "Name Entry",
                JOptionPane.PLAIN_MESSAGE);;
        while (name.equalsIgnoreCase("andrey") || name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Нука нахуй отсюда, " + name, "Ты че сука",
                    JOptionPane.ERROR_MESSAGE);
            name = JOptionPane.showInputDialog(this,
                    "Enter your name: ", "Name Entry",
                    JOptionPane.PLAIN_MESSAGE);
        }
        this.setTitle("nomorechat - " + name);
        final String lambdaName = name;

        textField = new JTextField();
        textField.addActionListener(event -> {
            String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + lambdaName + ": " + textField.getText();
            client.sendMessage(message);
            textField.setText("");
        });
        textField.setFont(textFont);
        textField.setForeground(textColor);
        add(textField, BorderLayout.SOUTH);
        exitButton = new JButton("no more chat");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(backGroundColor);
        exitButton.addActionListener(e -> {
            String departureMessage = lambdaName + " has left the chat.";
            client.sendMessage(departureMessage);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            System.exit(0);
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error connecting to the server", "Connection error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append((message + "\n")));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }

}
