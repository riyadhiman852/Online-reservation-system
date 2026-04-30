package com.reservation.ui;

import com.reservation.model.Reservation;
import com.reservation.storage.DataStore;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CancellationPanel extends JPanel {
    private final DataStore dataStore;
    private final JTextField pnrField = new JTextField(18);
    private final JTextArea detailsArea = new JTextArea(12, 40);
    private Reservation currentReservation;

    public CancellationPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildLayout();
    }

    private void buildLayout() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.add(new JLabel("Enter PNR Number:"));
        topPanel.add(pnrField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(event -> searchReservation());
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JButton cancelButton = new JButton("OK");
        cancelButton.addActionListener(event -> cancelReservation());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void searchReservation() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PNR number.");
            return;
        }

        Optional<Reservation> reservation = dataStore.findReservationByPnr(pnr);
        if (reservation.isPresent()) {
            currentReservation = reservation.get();
            detailsArea.setText(formatReservation(currentReservation));
        } else {
            currentReservation = null;
            detailsArea.setText("");
            JOptionPane.showMessageDialog(this, "No reservation found for this PNR.");
        }
    }

    private void cancelReservation() {
        if (currentReservation == null) {
            JOptionPane.showMessageDialog(this, "Search for a reservation before cancelling.");
            return;
        }

        if (currentReservation.isCancelled()) {
            JOptionPane.showMessageDialog(this, "This ticket is already cancelled.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to cancel ticket for PNR " + currentReservation.getPnr() + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean cancelled = dataStore.cancelReservation(currentReservation.getPnr());
            if (cancelled) {
                currentReservation.setCancelled(true);
                detailsArea.setText(formatReservation(currentReservation));
                JOptionPane.showMessageDialog(this, "Ticket cancelled successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation could not be completed.");
            }
        }
    }

    private String formatReservation(Reservation reservation) {
        return "PNR Number: " + reservation.getPnr() + "\n"
                + "Passenger Name: " + reservation.getPassengerName() + "\n"
                + "Age: " + reservation.getAge() + "\n"
                + "Gender: " + reservation.getGender() + "\n"
                + "Train Number: " + reservation.getTrainNumber() + "\n"
                + "Train Name: " + reservation.getTrainName() + "\n"
                + "Class Type: " + reservation.getClassType() + "\n"
                + "Journey Date: " + reservation.getJourneyDate() + "\n"
                + "From Place: " + reservation.getFromPlace() + "\n"
                + "Destination: " + reservation.getDestination() + "\n"
                + "Status: " + (reservation.isCancelled() ? "Cancelled" : "Confirmed");
    }
}
