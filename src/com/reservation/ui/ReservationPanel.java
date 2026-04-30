package com.reservation.ui;

import com.reservation.model.Reservation;
import com.reservation.storage.DataStore;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ReservationPanel extends JPanel {
    private final DataStore dataStore;
    private final JTextField passengerNameField = new JTextField(18);
    private final JTextField ageField = new JTextField(18);
    private final JComboBox<String> genderBox = new JComboBox<>(new String[] {"Male", "Female", "Other"});
    private final JComboBox<String> trainNumberBox;
    private final JTextField trainNameField = new JTextField(18);
    private final JComboBox<String> classBox = new JComboBox<>(new String[] {"Sleeper", "AC 3 Tier", "AC 2 Tier", "First Class"});
    private final JTextField journeyDateField = new JTextField(18);
    private final JTextField fromPlaceField = new JTextField(18);
    private final JTextField destinationField = new JTextField(18);

    public ReservationPanel(DataStore dataStore) {
        this.dataStore = dataStore;
        this.trainNumberBox = new JComboBox<>(dataStore.getTrainCatalog().keySet().toArray(new String[0]));
        this.trainNameField.setEditable(false);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        trainNumberBox.addActionListener(event -> updateTrainName());
        updateTrainName();
        buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField("Passenger Name:", passengerNameField, gbc, 0);
        addField("Age:", ageField, gbc, 1);
        addField("Gender:", genderBox, gbc, 2);
        addField("Train Number:", trainNumberBox, gbc, 3);
        addField("Train Name:", trainNameField, gbc, 4);
        addField("Class Type:", classBox, gbc, 5);
        addField("Journey Date (YYYY-MM-DD):", journeyDateField, gbc, 6);
        addField("From Place:", fromPlaceField, gbc, 7);
        addField("Destination:", destinationField, gbc, 8);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(event -> saveReservation());
        gbc.gridx = 1;
        gbc.gridy = 9;
        add(insertButton, gbc);
    }

    private void addField(String label, java.awt.Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);

        gbc.gridx = 1;
        add(component, gbc);
    }

    private void updateTrainName() {
        String trainNumber = (String) trainNumberBox.getSelectedItem();
        if (trainNumber != null) {
            Map<String, String> catalog = dataStore.getTrainCatalog();
            trainNameField.setText(catalog.getOrDefault(trainNumber, ""));
        }
    }

    private void saveReservation() {
        try {
            String passengerName = passengerNameField.getText().trim();
            String ageText = ageField.getText().trim();
            String journeyDate = journeyDateField.getText().trim();
            String fromPlace = fromPlaceField.getText().trim();
            String destination = destinationField.getText().trim();

            if (passengerName.isEmpty() || ageText.isEmpty() || journeyDate.isEmpty()
                    || fromPlace.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all reservation details.");
                return;
            }

            int age = Integer.parseInt(ageText);
            String trainNumber = (String) trainNumberBox.getSelectedItem();
            String trainName = trainNameField.getText();
            String classType = (String) classBox.getSelectedItem();
            String gender = (String) genderBox.getSelectedItem();
            String pnr = dataStore.createPnr();

            Reservation reservation = new Reservation(
                    pnr,
                    passengerName,
                    age,
                    gender,
                    trainNumber,
                    trainName,
                    classType,
                    journeyDate,
                    fromPlace,
                    destination,
                    false);

            dataStore.saveReservation(reservation);
            JOptionPane.showMessageDialog(this, "Reservation saved successfully.\nGenerated PNR: " + pnr);
            clearForm();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Could not save reservation: " + exception.getMessage());
        }
    }

    private void clearForm() {
        passengerNameField.setText("");
        ageField.setText("");
        journeyDateField.setText("");
        fromPlaceField.setText("");
        destinationField.setText("");
        genderBox.setSelectedIndex(0);
        classBox.setSelectedIndex(0);
        trainNumberBox.setSelectedIndex(0);
        updateTrainName();
    }
}
