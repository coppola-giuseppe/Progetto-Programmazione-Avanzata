package it.unipi.progetto_applicazione;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.progetto_applicazione.javabeans.EquipmentItem;
import it.unipi.progetto_applicazione.javabeans.Homebrew;
import it.unipi.progetto_applicazione.javabeans.MagicItem;
import it.unipi.progetto_applicazione.javabeans.Spell;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DatabaseController {

    @FXML
    private TabPane tables;

    @FXML
    private ProgressBar loadingTables;

    //START: Spell fields
    @FXML
    private ComboBox<String> spellFilterBox;
    @FXML
    private TextField spellFilterText;
    @FXML
    private Button spellFilterButton;
    @FXML
    private TableView<Spell> spellsTable;
    private ObservableList<Spell> olSpells;
    private FilteredList<Spell> filteredSpells;
    @FXML
    private TableColumn colSpellIndex;
    @FXML
    private TableColumn colSpellName;
    @FXML
    private TableColumn colSpellLevel;
    @FXML
    private TableColumn colSpellDesc;
    @FXML
    private TableColumn colSpellHigherLevel;
    @FXML
    private TableColumn colSpellComponents;
    @FXML
    private TableColumn colSpellMaterials;
    @FXML
    private TableColumn colSpellRange;
    @FXML
    private TableColumn colSpellAttackType;
    @FXML
    private TableColumn colSpellRitual;
    @FXML
    private TableColumn colSpellConcentration;
    @FXML
    private TableColumn colSpellCastingTime;
    @FXML
    private TableColumn colSpellSchool;
    @FXML
    private TableColumn colSpellAoe;
    @FXML
    private TableColumn colSpellHomebrew;
    @FXML
    private TableColumn colSpellDuration;
    //END: Spell fields
    //
    //START: Magic item fields
    @FXML
    private ComboBox<String> magicItemFilterBox;
    @FXML
    private TextField magicItemFilterText;
    @FXML
    private Button magicItemFilterButton;
    @FXML
    private TableView<MagicItem> magicItemsTable;
    private ObservableList<MagicItem> olMagicItems;
    private FilteredList<MagicItem> filteredMagicItems;
    @FXML
    private TableColumn colMagicItemIndex;
    @FXML
    private TableColumn colMagicItemName;
    @FXML
    private TableColumn colMagicItemRarity;
    @FXML
    private TableColumn colMagicItemDesc;
    @FXML
    private TableColumn colMagicItemImage;
    @FXML
    private TableColumn colMagicItemHomebrew;
    //END: Magic item fields
    //
    //START: Equipment item fields
    @FXML
    private ComboBox<String> equipmentItemFilterBox;
    @FXML
    private TextField equipmentItemFilterText;
    @FXML
    private Button equipmentItemFilterButton;
    @FXML
    private TableView<EquipmentItem> equipmentItemsTable;
    private ObservableList<EquipmentItem> olEquipmentItems;
    private FilteredList<EquipmentItem> filteredEquipmentItems;
    @FXML
    private TableColumn colEquipmentItemIndex;
    @FXML
    private TableColumn colEquipmentItemName;
    @FXML
    private TableColumn colEquipmentItemDesc;
    @FXML
    private TableColumn colEquipmentItemPrice;
    @FXML
    private TableColumn colEquipmentItemCurrency;
    @FXML
    private TableColumn colEquipmentItemWeight;
    @FXML
    private TableColumn colEquipmentItemHomebrew;
    //END: Eqipment item fields
    //
    //START: Homebrew fields
    @FXML
    private ComboBox<String> homebrewFilterBox;
    @FXML
    private TextField homebrewFilterText;
    @FXML
    private Button homebrewFilterButton;
    @FXML
    private TableView<Homebrew> homebrewTable;
    private ObservableList<Homebrew> olHomebrews;
    private FilteredList<Homebrew> filteredHomebrews;
    @FXML
    private TableColumn colHomebrewIndex;
    @FXML
    private TableColumn colHomebrewName;
    @FXML
    private TableColumn colHomebrewType;
    @FXML
    private TableColumn colHomebrewDesc;
    //END: Homebrew fields

    @FXML
    public void initialize() {

        loadingTables.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        loadingTables.setManaged(true);
        loadingTables.setVisible(true);
        tables.setVisible(false);
        tables.setManaged(false);

        //PLACEHOLDERS INITIALIZE
        Label placeholderSpell = new Label("No spells found in the database.");
        Label placeholderMagicItem = new Label("No magic items found in the database.");
        Label placeholderEquipmentItem = new Label("No equipment items found in the database.");
        Label placeholderHomebrew = new Label("No homebrew data found in the database.");

        placeholderSpell.setStyle(("-fx-font-size: 24px;"));
        placeholderMagicItem.setStyle(("-fx-font-size: 24px;"));
        placeholderEquipmentItem.setStyle(("-fx-font-size: 24px;"));
        placeholderHomebrew.setStyle(("-fx-font-size: 24px;"));

        spellsTable.setPlaceholder(placeholderSpell);
        magicItemsTable.setPlaceholder(placeholderMagicItem);
        equipmentItemsTable.setPlaceholder(placeholderEquipmentItem);
        homebrewTable.setPlaceholder(placeholderHomebrew);

        //SPELL COLUMNS INITIALIZE
        colSpellIndex.setCellValueFactory(new PropertyValueFactory<>("spellIndex"));
        colSpellName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpellLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colSpellDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSpellHigherLevel.setCellValueFactory(new PropertyValueFactory<>("higherLevel"));
        colSpellComponents.setCellValueFactory(new PropertyValueFactory<>("components"));
        colSpellMaterials.setCellValueFactory(new PropertyValueFactory<>("material"));
        colSpellRange.setCellValueFactory(new PropertyValueFactory<>("spellRange"));
        colSpellAttackType.setCellValueFactory(new PropertyValueFactory<>("attackType"));
        colSpellRitual.setCellValueFactory(new PropertyValueFactory<>("ritual"));
        colSpellConcentration.setCellValueFactory(new PropertyValueFactory<>("concentration"));
        colSpellCastingTime.setCellValueFactory(new PropertyValueFactory<>("castingTime"));
        colSpellSchool.setCellValueFactory(new PropertyValueFactory<>("school"));
        colSpellAoe.setCellValueFactory(new PropertyValueFactory<>("areaOfEffect"));
        colSpellHomebrew.setCellValueFactory(new PropertyValueFactory<>("homebrew"));
        colSpellDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        //MAGIC ITEMS COLUMNS INITIALIZE
        colMagicItemIndex.setCellValueFactory(new PropertyValueFactory<>("magicItemsIndex"));
        colMagicItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMagicItemRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        colMagicItemDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMagicItemHomebrew.setCellValueFactory(new PropertyValueFactory<>("homebrew"));
        colMagicItemImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        //EQUIPMENT ITEMS COLUMNS INITIALIZE
        colEquipmentItemIndex.setCellValueFactory(new PropertyValueFactory<>("equipmentItemsIndex"));
        colEquipmentItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEquipmentItemDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEquipmentItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colEquipmentItemCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        colEquipmentItemWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colEquipmentItemHomebrew.setCellValueFactory(new PropertyValueFactory<>("homebrew"));

        //HOMEBREW ITEMS INITIALIZE
        colHomebrewIndex.setCellValueFactory(new PropertyValueFactory<>("homebrewIndex"));
        colHomebrewName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHomebrewType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colHomebrewDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        //convert the String BASE64 in an image
        colMagicItemImage.setCellFactory(column -> {
            return new TableCell<MagicItem, String>() {

                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String imageString, boolean empty) {
                    super.updateItem(imageString, empty);

                    if (empty || imageString == null || imageString.isEmpty()) {
                        setGraphic(null);
                    } else {
                        try {

                            // String to byte[]
                            byte[] imageBytes = Base64.getDecoder().decode(imageString);

                            // byte[] to inputStream
                            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

                            Image image = new Image(bis);

                            imageView.setImage(image);
                            imageView.setFitHeight(150);
                            imageView.setPreserveRatio(true);

                            setGraphic(imageView);

                        } catch (IllegalArgumentException e) {
                            System.err.println("Error during String to image conversion.");
                            setGraphic(null);
                        }
                    }
                }
            };
        });

        //EQUIPMENT ITEMS COLUMNS INITIALIZE
        colEquipmentItemIndex.setCellValueFactory(new PropertyValueFactory<>("equipmentItemsIndex"));
        colEquipmentItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEquipmentItemDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEquipmentItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colEquipmentItemCurrency.setCellValueFactory(new PropertyValueFactory<>("currency"));
        colEquipmentItemWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colEquipmentItemHomebrew.setCellValueFactory(new PropertyValueFactory<>("homebrew"));
        //

        olSpells = FXCollections.observableArrayList();
        filteredSpells = new FilteredList<>(olSpells);
        spellsTable.setItems(filteredSpells);
        spellFilterBox.getItems().addAll("Index", "Name", "Level", "School", "Homebrew");
        spellFilterBox.setValue("Name");

        olMagicItems = FXCollections.observableArrayList();
        filteredMagicItems = new FilteredList<>(olMagicItems);
        magicItemsTable.setItems(filteredMagicItems);
        magicItemFilterBox.getItems().addAll("Index", "Name", "Rarity", "Homebrew");
        magicItemFilterBox.setValue("Name");

        olEquipmentItems = FXCollections.observableArrayList();
        filteredEquipmentItems = new FilteredList<>(olEquipmentItems);
        equipmentItemsTable.setItems(filteredEquipmentItems);
        equipmentItemFilterBox.getItems().addAll("Index", "Name", "Homebrew");
        equipmentItemFilterBox.setValue("Name");

        olHomebrews = FXCollections.observableArrayList();
        filteredHomebrews = new FilteredList<>(olHomebrews);
        homebrewTable.setItems(filteredHomebrews);
        homebrewFilterBox.getItems().addAll("Index", "Name", "Type");
        homebrewFilterBox.setValue("Name");

        //descriptions and other fields can be very long, so i catch the double click and create an alert with extended info
        spellsTable.setRowFactory(tv -> {
            TableRow<Spell> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Spell rowData = row.getItem();
                    showSpellDetails(rowData);
                }
            });
            return row;
        });

        magicItemsTable.setRowFactory(tv -> {
            TableRow<MagicItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    MagicItem rowData = row.getItem();
                    showMagicItemDetails(rowData);
                }
            });
            return row;
        });

        equipmentItemsTable.setRowFactory(tv -> {
            TableRow<EquipmentItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    EquipmentItem rowData = row.getItem();
                    showEquipmentItemDetails(rowData);
                }
            });
            return row;
        });

        homebrewTable.setRowFactory(tv -> {
            TableRow<Homebrew> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Homebrew rowData = row.getItem();
                    showHomebrewDetails(rowData);
                }
            });
            return row;
        });

        //task thah handle the "connection" with the service
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                long time = System.currentTimeMillis();
                Gson gson = new Gson();
                try {
                    URL url = new URL("http://localhost:8080/db/all");

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

                    //retrieved all data from the service, now i filter the items
                    List<Spell> spells = new ArrayList<>();
                    List<MagicItem> magicItems = new ArrayList<>();
                    List<EquipmentItem> equipmentItems = new ArrayList<>();
                    List<Homebrew> homebrews = new ArrayList<>();

                    JsonElement rootElement = gson.fromJson(content.toString(), JsonElement.class);

                    if (rootElement.isJsonArray()) {

                        JsonArray jsonArray = rootElement.getAsJsonArray();

                        for (JsonElement element : jsonArray) {

                            JsonObject data = element.getAsJsonObject();

                            if (data.has("type") && !data.get("type").isJsonNull()) {

                                String type = data.get("type").getAsString();

                                switch (type) {
                                    case "spell":
                                        Spell spell = gson.fromJson(data, Spell.class);
                                        spells.add(spell);
                                        if (spell.getHomebrew()) {
                                            Homebrew hb = new Homebrew(spell.getSpellIndex(), spell.getName(), "spell", spell.getDescription());
                                            homebrews.add(hb);
                                        }
                                        break;

                                    case "magic-item":
                                        MagicItem magicItem = gson.fromJson(data, MagicItem.class);
                                        magicItems.add(magicItem);

                                        if (magicItem.getHomebrew()) {
                                            Homebrew hb = new Homebrew(magicItem.getMagicItemsIndex(), magicItem.getName(), "magic item", magicItem.getDescription());
                                            homebrews.add(hb);
                                        }
                                        break;

                                    case "equipment-item":
                                        EquipmentItem equipmentItem = gson.fromJson(data, EquipmentItem.class);
                                        equipmentItems.add(equipmentItem);
                                        if (equipmentItem.getHomebrew()) {
                                            Homebrew hb = new Homebrew(equipmentItem.getEquipmentItemsIndex(), equipmentItem.getName(), "equipment item", equipmentItem.getDescription());
                                            homebrews.add(hb);
                                        }
                                        break;

                                    default:
                                        System.err.println("Unknown object type: " + type);
                                        break;
                                }
                            }
                        }
                    } else {
                        System.err.println("Format error: not a JSON array.");
                    }
                    //For debug
                    System.out.println("Total number of SPELLS found: " + spells.size());
                    System.out.println("Total number of MAGIC ITEMS found: " + magicItems.size());
                    System.out.println("Total number of EQUIPMENT ITEMS found: " + equipmentItems.size());

                    //Data added to the observable list
                    addSpellToTable(spells);
                    addMagicItemToTable(magicItems);
                    addEquipmentItemToTable(equipmentItems);
                    addHomebrewToTable(homebrews);

                    System.out.println("Fetch&insert duration: " + (System.currentTimeMillis() - time) + " milliseconds.");

                    Platform.runLater(() -> {
                        loadingTables.setManaged(false);
                        loadingTables.setVisible(false);

                        tables.setVisible(true);
                        tables.setManaged(true);
                    });

                } catch (IOException ioe) {
                    System.out.println("ERROR -> " + ioe.getMessage());

                    loadingTables.setManaged(false);
                    loadingTables.setVisible(false);

                    tables.setVisible(true);
                    tables.setManaged(true);

                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(AlertType.ERROR);
                        errorAlert.setTitle("Connection error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("There was a problem during the connection with the service.");
                        errorAlert.showAndWait();
                    });
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void addSpellToTable(List<Spell> spells) {
        olSpells.addAll(spells);
    }

    private void addMagicItemToTable(List<MagicItem> magicItems) {
        olMagicItems.addAll(magicItems);
    }

    private void addEquipmentItemToTable(List<EquipmentItem> equipmentItems) {
        olEquipmentItems.addAll(equipmentItems);
    }

    private void addHomebrewToTable(List<Homebrew> homebrews) {
        olHomebrews.addAll(homebrews);
    }

    @FXML
    private void backToHome() throws IOException {
        App.setRoot("home");
    }

    //show an alert with extended info of the spell
    private void showSpellDetails(Spell spell) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Spell Details");
        alert.setHeaderText(spell.getName() + " (Level " + spell.getLevel() + (spell.getSchool() == null ? "" : (" " + spell.getSchool())) + ")");

        //extendible container
        GridPane content = new GridPane();
        content.add(new Label("Extended info:"), 0, 0);
        String fullDescription = "";

        //format the extended info: with String.join() i can insert a new line between each row
        if (spell.getDescription() != null && !spell.getDescription().isEmpty()) {
            fullDescription = ">>>Description<<<\n";
            fullDescription += String.join("\n", spell.getDescription());
            fullDescription += "\n\n";
        }

        if (spell.getHigherLevel() != null && !spell.getHigherLevel().isEmpty()) {
            fullDescription += ">>>At Higher Levels<<< \n" + String.join("\n", spell.getHigherLevel());
            fullDescription += "\n\n";
        }

        if (spell.getMaterial() != null && !spell.getMaterial().isEmpty()) {
            fullDescription += " >>>Materials<<< \n" + String.join("\n", spell.getMaterial());
        }

        TextArea textArea = new TextArea(fullDescription);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        content.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(content);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    //show an alert with extended info of the magic item
    private void showMagicItemDetails(MagicItem magicItem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Magic Item Details");
        alert.setHeaderText(magicItem.getName() + " (" + magicItem.getRarity() + ")");

        GridPane content = new GridPane();
        content.add(new Label("Extended info:"), 0, 0);
        String fullDescription = "";

        if (magicItem.getDescription() != null && !magicItem.getDescription().isEmpty()) {
            fullDescription = ">>>Description<<<\n";
            fullDescription += String.join("\n", magicItem.getDescription());
        }

        TextArea textArea = new TextArea(fullDescription);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        content.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(content);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    //show an alert with extended info of the equipment item
    private void showEquipmentItemDetails(EquipmentItem equipmentItem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Magic Item Details");
        alert.setHeaderText(equipmentItem.getName());

        GridPane content = new GridPane();
        content.add(new Label("Extended info:"), 0, 0);
        String fullDescription = "";
        if (equipmentItem.getDescription() != null && !equipmentItem.getDescription().isEmpty()) {
            fullDescription = ">>>Description<<<\n";
            fullDescription += String.join("\n", equipmentItem.getDescription());
            fullDescription += "\n\n";
        }

        if (equipmentItem.getPrice() != null && !equipmentItem.getPrice().isEmpty()) {
            fullDescription += ">>>Price<<< \n" + String.join("\n", equipmentItem.getPrice());
            fullDescription += " " + equipmentItem.getCurrency();
            fullDescription += "\n\n";
        }

        if (equipmentItem.getWeight() != null && !equipmentItem.getWeight().isEmpty()) {
            fullDescription += ">>>Weight<<< \n" + String.join("\n", equipmentItem.getWeight());
        }

        TextArea textArea = new TextArea(fullDescription);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        content.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(content);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    //show an alert with extended info of the homebrew data
    private void showHomebrewDetails(Homebrew homebrew) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Homebrew Details");
        alert.setHeaderText(homebrew.getName() + "(" + homebrew.getType() + ")");

        GridPane content = new GridPane();
        content.add(new Label("Extended info:"), 0, 0);
        String fullDescription = "";
        if (homebrew.getDescription() != null && !homebrew.getDescription().isEmpty()) {
            fullDescription = ">>>Description<<<\n";
            fullDescription += String.join("\n", homebrew.getDescription());
            fullDescription += "\n\n";
        }

        fullDescription += "-->For more info go to the " + homebrew.getType() + " tab and search it.<--";

        TextArea textArea = new TextArea(fullDescription);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        content.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(content);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    //Functions that handles the search buttons
    @FXML
    private void spellFilterBy() {
        String filterBy = spellFilterBox.getValue();
        String filterText = spellFilterText.getText().toLowerCase().trim();

        filteredSpells.setPredicate(spell -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            switch (filterBy) {
                case "Index":
                    return spell.getSpellIndex().toLowerCase().contains(filterText);
                case "Name":
                    return spell.getName().toLowerCase().contains(filterText);
                case "Level":
                    return String.valueOf(spell.getLevel()).equals(filterText);
                case "School":
                    return spell.getSchool().toLowerCase().contains(filterText);
                case "Homebrew":
                    return String.valueOf(spell.getHomebrew()).toLowerCase().startsWith(filterText);
                default:
                    return true;
            }
        });
    }

    @FXML
    private void magicItemFilterBy() {
        String filterBy = magicItemFilterBox.getValue();
        String filterText = magicItemFilterText.getText().toLowerCase().trim();

        filteredMagicItems.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            switch (filterBy) {
                case "Index":
                    return item.getMagicItemsIndex().toLowerCase().contains(filterText);
                case "Name":
                    return item.getName().toLowerCase().contains(filterText);
                case "Rarity":
                    return item.getRarity().toLowerCase().contains(filterText);
                case "Homebrew":
                    return String.valueOf(item.getHomebrew()).toLowerCase().startsWith(filterText);
                default:
                    return true;
            }
        });
    }

    @FXML
    private void equipmentItemFilterBy() {
        String filterBy = equipmentItemFilterBox.getValue();
        String filterText = equipmentItemFilterText.getText().toLowerCase().trim();

        filteredEquipmentItems.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            switch (filterBy) {
                case "Index":
                    return item.getEquipmentItemsIndex().toLowerCase().contains(filterText);
                case "Name":
                    return item.getName().toLowerCase().contains(filterText);
                case "Homebrew":
                    return String.valueOf(item.getHomebrew()).toLowerCase().startsWith(filterText);
                default:
                    return true;
            }
        });
    }

    @FXML
    private void homebrewFilterBy() {
        String filterBy = homebrewFilterBox.getValue();
        String filterText = homebrewFilterText.getText().toLowerCase().trim();

        filteredHomebrews.setPredicate(item -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            switch (filterBy) {
                case "Index":
                    return item.getHomebrewIndex().toLowerCase().contains(filterText);
                case "Name":
                    return item.getName().toLowerCase().contains(filterText);
                case "Type":
                    return item.getType().toLowerCase().contains(filterText);
                default:
                    return true;
            }
        });
    }

    //Functions that handles the delete option with the ContextMenu
    @FXML
    public void removeSpell() {
        new Thread() {
            @Override
            public void run() {
                Spell selected = spellsTable.getSelectionModel().getSelectedItem();
                int responseCode = removeData("spells/delete", selected.getSpellIndex());

                switch (responseCode) {
                    case 200: {
                        olSpells.remove(selected);

                        if (selected.getHomebrew()) {
                            String index = selected.getSpellIndex();
                            for (Homebrew hb : olHomebrews) {
                                if (hb.getHomebrewIndex().equals(index)) {
                                    olHomebrews.remove(hb);
                                    break;
                                }
                            }
                        }

                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Operation result");
                            alert.setHeaderText(null);
                            alert.setContentText("Spell removed.");
                            alert.showAndWait();
                        });
                        break;
                    }
                    case 404: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Spell not in the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                    default: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Can't connect with the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                }
            }
        }.start();
    }

    @FXML
    public void removeMagicItem() {
        new Thread() {
            @Override
            public void run() {

                MagicItem selected = magicItemsTable.getSelectionModel().getSelectedItem();
                int responseCode = removeData("magic_items/delete", selected.getMagicItemsIndex());

                switch (responseCode) {
                    case 200: {
                        olMagicItems.remove(selected);

                        if (selected.getHomebrew()) {
                            String index = selected.getMagicItemsIndex();
                            for (Homebrew hb : olHomebrews) {
                                if (hb.getHomebrewIndex().equals(index)) {
                                    olHomebrews.remove(hb);
                                    break;
                                }
                            }
                        }

                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Operation result");
                            alert.setHeaderText(null);
                            alert.setContentText("Magic item removed.");
                            alert.showAndWait();
                        });
                        break;
                    }
                    case 404: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Magic item not in the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                    default: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Can't connect with the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                }

            }
        }.start();

    }

    @FXML
    public void removeEquipmentItem() {
        new Thread() {
            @Override
            public void run() {
                EquipmentItem selected = equipmentItemsTable.getSelectionModel().getSelectedItem();
                int responseCode = removeData("equipment_items/delete", selected.getEquipmentItemsIndex());

                switch (responseCode) {
                    case 200: {
                        olEquipmentItems.remove(selected);

                        if (selected.getHomebrew()) {
                            String index = selected.getEquipmentItemsIndex();
                            for (Homebrew hb : olHomebrews) {
                                if (hb.getHomebrewIndex().equals(index)) {
                                    olHomebrews.remove(hb);
                                    break;
                                }
                            }
                        }

                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Operation result");
                            alert.setHeaderText(null);
                            alert.setContentText("Equipment item removed.");
                            alert.showAndWait();
                        });
                        break;
                    }
                    case 404: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Equipment item not in the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                    default: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Can't connect with the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                }
            }
        }.start();
    }

    //removes the data from the homebrew observable list but also from the other list the data is in
    @FXML
    public void removeHomebrew() {
        new Thread() {
            @Override
            public void run() {
                Homebrew selected = homebrewTable.getSelectionModel().getSelectedItem();
                String index = selected.getHomebrewIndex();
                String type = selected.getType();

                String path = "";

                switch (type) {
                    case "spell": {
                        path = "spells/delete";
                        break;
                    }
                    case "magic item": {
                        path = "magic_items/delete";
                        break;
                    }
                    case "equipment item": {
                        path = "equipment_items/delete";
                        break;
                    }
                }

                int responseCode = removeData(path, index);

                switch (responseCode) {
                    case 200: {
                        olHomebrews.remove(selected);

                        switch (type) {
                            case "spell": {
                                Spell spell = null;
                                for (Spell s : olSpells) {
                                    if (s.getSpellIndex().equals(index)) {
                                        spell = s;
                                        break;
                                    }
                                }
                                if (spell != null) {
                                    olSpells.remove(spell);
                                }
                                break;
                            }
                            case "magic item": {
                                MagicItem magicItem = null;
                                for (MagicItem mi : olMagicItems) {
                                    if (mi.getMagicItemsIndex().equals(index)) {
                                        magicItem = mi;
                                        break;
                                    }
                                }
                                if (magicItem != null) {
                                    olMagicItems.remove(magicItem);
                                }
                                break;
                            }
                            case "equipment item": {
                                EquipmentItem equipmentItem = null;
                                for (EquipmentItem ei : olEquipmentItems) {
                                    if (ei.getEquipmentItemsIndex().equals(index)) {
                                        equipmentItem = ei;
                                        break;
                                    }
                                }
                                if (equipmentItem != null) {
                                    olEquipmentItems.remove(equipmentItem);
                                }
                                break;
                            }
                        }

                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Operation result");
                            alert.setHeaderText(null);

                            switch (type) {
                                case "spell": {
                                    alert.setContentText("Spell removed.");
                                    break;
                                }
                                case "magic item": {
                                    alert.setContentText("Magic item removed.");
                                    break;
                                }
                                case "equipment item": {
                                    alert.setContentText("Equipment item removed.");
                                    break;
                                }
                            }

                            alert.showAndWait();
                        });
                        break;
                    }
                    case 404: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Equipment item not in the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                    default: {
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(AlertType.ERROR);
                            errorAlert.setTitle("Error during the operation");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Can't connect with the database.");
                            errorAlert.showAndWait();
                        });
                        break;
                    }
                }

            }
        }.start();
    }

    public int removeData(String path, String index) {
        int responseCode = 0;
        try {

            URL url = new URL("http://localhost:8080/db/" + path);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String params = "index=" + URLEncoder.encode(index, StandardCharsets.UTF_8);

            try (OutputStream os = con.getOutputStream()) {
                os.write(params.getBytes());
            }

            responseCode = con.getResponseCode();

            StringBuilder content;
            InputStreamReader str;
            if (responseCode == 200) {
                str = new InputStreamReader(con.getInputStream());
            } else {
                str = new InputStreamReader(con.getErrorStream());
            }

            try (BufferedReader in = new BufferedReader(str)) {
                String inputLine;
                content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            return con.getResponseCode();

        } catch (IOException ioe) {
            System.err.println("ERROR -> " + ioe.getMessage());
            return responseCode;
        }
    }

}
