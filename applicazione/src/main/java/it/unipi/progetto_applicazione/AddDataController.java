package it.unipi.progetto_applicazione;

import it.unipi.progetto_applicazione.javabeans.GameData;
import it.unipi.progetto_applicazione.javabeans.Spell;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import com.google.gson.Gson;
import it.unipi.progetto_applicazione.javabeans.EquipmentItem;
import it.unipi.progetto_applicazione.javabeans.MagicItem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddDataController {

    private static final long MAX_IMAGE_SIZE = 16777215L; // max size of a MEDIUMBLOB

    @FXML
    private ComboBox<String> dataBox;

    //START - Spell info
    @FXML
    private VBox spellBox;
    @FXML
    private TextField spellName;
    @FXML
    private TextField spellRange;
    @FXML
    private ComboBox<String> spellLevel;
    @FXML
    private TextArea spellDesc;
    @FXML
    private TextArea spellHiLev;
    @FXML
    private TextArea spellMaterial;
    @FXML
    private VBox spellComponents;
    @FXML
    private CheckBox spellComponentS;
    @FXML
    private CheckBox spellComponentM;
    @FXML
    private CheckBox spellComponentV;
    @FXML
    private RadioButton spellRitual;
    @FXML
    private RadioButton spellConcentration;
    @FXML
    private TextField spellDuration;
    @FXML
    private TextField spellCastingTime;
    @FXML
    private TextField spellAttackType;
    @FXML
    private TextField spellSchool;
    @FXML
    private TextField spellAoeType;
    @FXML
    private TextField spellAoeRange;
    @FXML
    private Button spellButton;
    //END - Spell info
    //
    //START - Magic item info
    @FXML
    private VBox magicItemBox;
    @FXML
    private TextField magicItemName;
    @FXML
    private TextArea magicItemDesc;
    @FXML
    private ComboBox magicItemRarity;
    @FXML
    private Button magicItemImgBtn;
    @FXML
    private ImageView magicItemImg;
    @FXML
    private Button magicItemButton;

    private String selectedImageBlob = null;
    //END - Magic item info
    //
    //START - Equipment item info
    @FXML
    private VBox equipmentItemBox;
    @FXML
    private TextField equipmentItemName;
    @FXML
    private TextArea equipmentItemDesc;
    @FXML
    private TextField equipmentItemPrice;
    @FXML
    private ComboBox equipmentItemCurrency;
    @FXML
    private TextField equipmentItemWeight;
    @FXML
    private Button equipmentItemButton;
    //END - Eqipment item info

    @FXML
    public void initialize() {

        dataBox.getItems().addAll("Spell", "Magic item", "Equipment item");

        spellLevel.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9");
        spellLevel.setValue("1");

        magicItemRarity.getItems().addAll("Common", "Uncommon", "Rare", "Very Rare", "Legendary", "Artifact");
        magicItemRarity.setValue("Common");

        equipmentItemCurrency.getItems().addAll("cp", "sp", "ep", "gp", "pp");
        equipmentItemCurrency.setValue("gp");

    }

    @FXML
    public void selectVbox() {
        resetFields();
        switch (dataBox.getValue()) {
            case "Spell": {

                spellBox.setManaged(true);
                spellBox.setVisible(true);
                magicItemBox.setManaged(false);
                magicItemBox.setVisible(false);
                equipmentItemBox.setManaged(false);
                equipmentItemBox.setVisible(false);
                break;
            }

            case "Magic item": {

                spellBox.setManaged(false);
                spellBox.setVisible(false);
                magicItemBox.setManaged(true);
                magicItemBox.setVisible(true);
                equipmentItemBox.setManaged(false);
                equipmentItemBox.setVisible(false);
                break;
            }

            case "Equipment item": {

                spellBox.setManaged(false);
                spellBox.setVisible(false);
                magicItemBox.setManaged(false);
                magicItemBox.setVisible(false);
                equipmentItemBox.setManaged(true);
                equipmentItemBox.setVisible(true);
                break;
            }

            default:
        }

    }

    @FXML
    private void backToHome() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void checkSpell() {

        //REQUIRED: name, one component, description, range, duration, casting time
        spellButton.setDisable(true);

        spellName.setStyle("");
        spellDesc.setStyle("");
        spellComponents.setStyle("");
        spellRange.setStyle("");
        spellDuration.setStyle("");
        spellCastingTime.setStyle("");
        spellAoeType.setStyle("");
        spellAoeRange.setStyle("");

        if (spellName.getText().equals("") || spellDesc.getText().equals("") || (!spellComponentS.isSelected() && !spellComponentM.isSelected() && !spellComponentV.isSelected()) || spellRange.getText().equals("")
                || spellDuration.getText().equals("")
                || spellCastingTime.getText().equals("")
                || (spellAoeType.getText().equals("") ^ spellAoeRange.getText().equals(""))) {

            if (spellName.getText().equals("")) {
                spellName.setStyle("-fx-border-color: red;");
            }

            if (spellDesc.getText().equals("")) {
                spellDesc.setStyle("-fx-border-color: red;");
            }

            if (!spellComponentS.isSelected() && !spellComponentM.isSelected() && !spellComponentV.isSelected()) {
                spellComponents.setStyle("-fx-border-color: red;");
            }
            if (spellRange.getText().equals("")) {
                spellRange.setStyle("-fx-border-color: red;");
            }

            if (spellDuration.getText().equals("")) {
                spellDuration.setStyle("-fx-border-color: red;");
            }

            if (spellCastingTime.getText().equals("")) {
                spellCastingTime.setStyle("-fx-border-color: red;");
            }

            if (spellAoeType.getText().equals("") && !spellAoeRange.getText().equals("")) {
                spellAoeType.setStyle("-fx-border-color: red;");
            }

            if (!spellAoeType.getText().equals("") && spellAoeRange.getText().equals("")) {
                spellAoeRange.setStyle("-fx-border-color: red;");
            }

            spellButton.setDisable(false);
        } else {
            String index = spellName.getText().toLowerCase();
            index = index.replaceAll("[']+", "");//remove the apostrophe
            index = index.replaceAll("[^a-z0-9]+", "-");//replace spaces with -
            index = index.replaceAll("^-|-$", "");//remove - at the start and end of the string

            List<String> components = new ArrayList<>();
            if (spellComponentS.isSelected()) {
                components.add("S");
            }
            if (spellComponentM.isSelected()) {
                components.add("M");
            }
            if (spellComponentV.isSelected()) {
                components.add("V");
            }

            Integer level = Integer.valueOf(spellLevel.getValue());
            Boolean ritual = spellRitual.isSelected();
            Boolean concentration = spellConcentration.isSelected();

            List<String> description = new ArrayList<>();
            String[] descRows = spellDesc.getText().split("\\R");
            for (String s : descRows) {
                if (!s.equals("")) {
                    description.add(s);
                }
            }

            List<String> higherLevel = new ArrayList<>();
            String[] hiLelRows = spellHiLev.getText().split("\\R");
            for (String s : hiLelRows) {
                if (!s.equals("")) {
                    higherLevel.add(s);
                }
            }

            Spell spell = new Spell(
                    index,
                    spellName.getText(),
                    level,
                    spellRange.getText(),
                    components,
                    spellMaterial.getText().equals("") ? null : spellMaterial.getText(), //optional field
                    ritual,
                    concentration,
                    spellDuration.getText(),
                    spellCastingTime.getText(),
                    spellAttackType.getText().equals("") ? null : spellAttackType.getText(), //optional field
                    description,
                    higherLevel,
                    spellSchool.getText().equals("") ? null : spellSchool.getText(), //optional fied
                    (spellAoeType.getText().equals("") || spellAoeRange.getText().equals("")) ? null : (spellAoeType.getText() + "," + spellAoeRange.getText()) //both set or both null
            );

            addData(spell);

        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = createFileChooser();
        File selectedFile = fileChooser.showOpenDialog(magicItemImg.getScene().getWindow());
        if (selectedFile != null) {

            if (selectedFile.length() > MAX_IMAGE_SIZE) {

                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Result");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Image to big, the limit is 16MB.");
                    errorAlert.showAndWait();
                });

                return;
            }

            try {
                Image image = new Image(selectedFile.toURI().toURL().toExternalForm());
                magicItemImg.setImage(image);
                selectedImageBlob = Base64.getEncoder().encodeToString(Files.readAllBytes(selectedFile.toPath()));

            } catch (IOException e) {
                System.err.println("Error during file read -> " + e.getMessage());
            }
        }
    }

    @FXML
    private void deleteImg() {
        selectedImageBlob = null;
        magicItemImg.setImage(null);
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image for your Magic Item");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Supported extensions:", "*.png", "*.jpg", "*.jpeg"));
        return fileChooser;
    }

    @FXML
    private void checkMagicItem() {

        magicItemButton.setDisable(true);
        magicItemName.setStyle("");
        magicItemDesc.setStyle("");

        //REQUIRED: name and description
        if (magicItemName.getText().equals("") || magicItemDesc.getText().equals("")) {
            if (magicItemName.getText().equals("")) {
                magicItemName.setStyle("-fx-border-color: red;");
            }
            if (magicItemDesc.getText().equals("")) {
                magicItemDesc.setStyle("-fx-border-color: red;");
            }
            magicItemButton.setDisable(false);

        } else {
            String index = magicItemName.getText().toLowerCase();
            index = index.replaceAll("[']+", "");//remove the apostrophe
            index = index.replaceAll("[^a-z0-9]+", "-");//replace spaces with -
            index = index.replaceAll("^-|-$", "");//remove - at the start and end of the string

            List<String> description = new ArrayList<>();
            String[] descRows = magicItemDesc.getText().split("\\R");
            for (String s : descRows) {
                if (!s.equals("")) {
                    description.add(s);
                }
            }

            MagicItem magicItem = new MagicItem(
                    index,
                    magicItemName.getText(),
                    description,
                    selectedImageBlob,
                    magicItemRarity.getValue().toString()
            );

            addData(magicItem);
        }
    }

    @FXML
    private void checkEquipmentItem() {

        equipmentItemButton.setDisable(true);

        equipmentItemName.setStyle("");
        equipmentItemDesc.setStyle("");
        equipmentItemPrice.setStyle("");

        //REQUIRED: name, description and price
        if (equipmentItemName.getText().equals("") || equipmentItemDesc.getText().equals("") || equipmentItemPrice.getText().equals("")) {
            if (equipmentItemName.getText().equals("")) {
                equipmentItemName.setStyle("-fx-border-color: red;");
            }
            if (equipmentItemDesc.getText().equals("")) {
                equipmentItemDesc.setStyle("-fx-border-color: red;");
            }
            if (equipmentItemPrice.getText().equals("")) {
                equipmentItemPrice.setStyle("-fx-border-color: red;");
            }

            equipmentItemButton.setDisable(false);

        } else {
            String index = equipmentItemName.getText().toLowerCase();
            index = index.replaceAll("[']+", "");//remove the apostrophe
            index = index.replaceAll("[^a-z0-9]+", "-");//replace spaces with -
            index = index.replaceAll("^-|-$", "");//remove - at the start and end of the string

            List<String> description = new ArrayList<>();
            String[] descRows = equipmentItemDesc.getText().split("\\R");
            for (String s : descRows) {
                if (!s.equals("")) {
                    description.add(s);
                }
            }

            EquipmentItem equipmentItem = new EquipmentItem(
                    index,
                    equipmentItemName.getText(),
                    equipmentItemPrice.getText(),
                    equipmentItemCurrency.getValue().toString(),
                    equipmentItemWeight.getText().equals("") ? null : equipmentItemWeight.getText(),
                    description
            );

            addData(equipmentItem);
        }
    }

    public void addData(GameData data) {
        new Thread(() -> {
            try {
                String[] response = sendDataToDatabase(data);
                int responseCode = Integer.parseInt(response[0]);
                String responseMsg = response[1];

                Platform.runLater(() -> {
                    if (responseCode == 201) {
                        Platform.runLater(() -> {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Result");
                            alert.setHeaderText(null);
                            alert.setContentText(responseMsg);
                            alert.showAndWait();

                        });
                    } else {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Result");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText(responseMsg);
                            errorAlert.showAndWait();
                            resetFields();
                        });
                    }
                });
            } catch (IOException ioe) {
                System.out.println("Connection failed -> " + ioe.getMessage());
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Result");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Connection with the service failed.");
                    errorAlert.showAndWait();

                });
            } finally {
                Platform.runLater(() -> {
                    spellButton.setDisable(false);
                    magicItemButton.setDisable(false);
                    equipmentItemButton.setDisable(false);

                });
            }
        }).start();
    }

    public String[] sendDataToDatabase(GameData data) throws IOException {
        String[] result = new String[2];
        HttpURLConnection con = null;
        try {
            Gson gson = new Gson();
            String dataToJson = gson.toJson(data);

            String dataPath = (data instanceof Spell) ? "spells" : ((data instanceof MagicItem) ? "magic_items" : "equipment_items");

            URL url = new URL("http://localhost:8080/db/" + dataPath + "/add");

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = dataToJson.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            StringBuilder content;
            if (responseCode == 201) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                }
            } else {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
                    String inputLine;
                    content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                }
            }
            result[0] = Integer.toString(responseCode);
            result[1] = content.toString();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }

    private void resetFields() {
        spellName.setText("");
        spellRange.setText("");
        spellLevel.setValue("1");
        spellDesc.setText("");
        spellHiLev.setText("");
        spellMaterial.setText("");
        spellComponentS.setSelected(false);
        spellComponentM.setSelected(false);
        spellComponentV.setSelected(false);
        spellRitual.setSelected(false);
        spellConcentration.setSelected(false);
        spellDuration.setText("");
        spellCastingTime.setText("");
        spellAttackType.setText("");
        spellSchool.setText("");
        spellAoeType.setText("");
        spellAoeRange.setText("");

        magicItemName.setText("");
        magicItemDesc.setText("");
        magicItemRarity.setValue("Common");
        magicItemImg.setImage(null);
        selectedImageBlob = null;

        equipmentItemName.setText("");
        equipmentItemDesc.setText("");
        equipmentItemPrice.setText("");
        equipmentItemCurrency.setValue("gp");
        equipmentItemWeight.setText("");
    }
}
