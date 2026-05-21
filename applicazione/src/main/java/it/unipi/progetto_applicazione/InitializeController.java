package it.unipi.progetto_applicazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class InitializeController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Hyperlink homeLink;

    @FXML
    private Button btnInitialize;

    @FXML
    private void backToHome() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void initializePressed() {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Are you sure?");
        alert.setHeaderText("Warning: you are going to initialize the entire database, you'll lose all homebrew data.");
        alert.setContentText("Are you sure you want to continue?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonYes) {

            progressBar.setManaged(true);
            progressLabel.setManaged(true);
            progressBar.setVisible(true);
            progressLabel.setVisible(true);
            progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);

            homeLink.setManaged(false);
            btnInitialize.setManaged(false);
            homeLink.setVisible(false);
            btnInitialize.setVisible(false);
            new Thread() {
                @Override
                public void run() {
                    try {
                        
                        String responseText = initializeDatabase();

                        Platform.runLater(() -> {

                            progressBar.setManaged(false);
                            progressLabel.setManaged(false);
                            progressBar.setVisible(false);
                            progressLabel.setVisible(false);

                            homeLink.setManaged(true);
                            btnInitialize.setManaged(true);
                            homeLink.setVisible(true);
                            btnInitialize.setVisible(true);

                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Result");
                            alert.setHeaderText(null);
                            alert.setContentText(responseText);
                            alert.showAndWait();
                        });

                    } catch (IOException ioe) {
                        Platform.runLater(() -> {
                            progressBar.setManaged(false);
                            progressLabel.setManaged(false);
                            progressBar.setVisible(false);
                            progressLabel.setVisible(false);

                            homeLink.setManaged(true);
                            btnInitialize.setManaged(true);
                            homeLink.setVisible(true);
                            btnInitialize.setVisible(true);

                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Connection error");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("There was a problem during the connection with the service.");
                            errorAlert.showAndWait();
                        });
                    }
                }
            }.start();
        }
    }

    public String initializeDatabase() throws IOException {

        URL url = new URL("http://localhost:8080/db/initialize");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        StringBuilder content;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        con.disconnect();
        
        return content.toString();
    }
}
