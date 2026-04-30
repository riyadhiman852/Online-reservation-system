package com.reservation.ui;

import com.reservation.storage.DataStore;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {
    public MainFrame(DataStore dataStore, String username) {
        setTitle("Online Reservation System");
        setSize(860, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Welcome, " + username + "  |  Railway Reservation Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Reservation Form", new ReservationPanel(dataStore));
        tabs.addTab("Cancellation Form", new CancellationPanel(dataStore));
        add(tabs, BorderLayout.CENTER);
    }
}
