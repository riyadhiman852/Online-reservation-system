package com.reservation;

import com.reservation.storage.DataStore;
import com.reservation.ui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DataStore dataStore = new DataStore();
        SwingUtilities.invokeLater(() -> new LoginFrame(dataStore).setVisible(true));
    }
}
