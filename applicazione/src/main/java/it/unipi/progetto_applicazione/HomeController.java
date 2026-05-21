package it.unipi.progetto_applicazione;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void initializeDatabase() throws IOException {
        App.setRoot("initialize");
    }

    @FXML
    private void addNewData() throws IOException {
        App.setRoot("add");

    }

    @FXML
    private void database() throws IOException {
        App.setRoot("database");

    }
}
