/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio;

import it.unipi.progetto_servizio.javabeans.GameData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unipi.progetto_servizio.javabeans.EquipmentItem;
import it.unipi.progetto_servizio.javabeans.MagicItem;
import it.unipi.progetto_servizio.javabeans.Spell;
import it.unipi.progetto_servizio.repositories.EquipmentItemRepository;
import it.unipi.progetto_servizio.repositories.MagicItemRepository;
import it.unipi.progetto_servizio.repositories.SpellRepository;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author coppo
 */
@Component
public class DataInitializer {

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private MagicItemRepository magicItemRepository;

    @Autowired
    private SpellRepository spellRepository;

    private static final int NUM_THREADS = 5;

    public void populateDatabase() throws Exception {
        System.out.println("START: initialize DB");

        long timeStart = System.currentTimeMillis();
        //empty the database

        spellRepository.truncateTable();
        magicItemRepository.truncateTable();
        equipmentItemRepository.truncateTable();

        List<String> DataUrlList = fetchAllDataUrls();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Callable<GameData>> tasks = new ArrayList<>();

        for (String data : DataUrlList) {
            tasks.add(() -> threadSingleData(data));
        }

        List<Future<GameData>> futures = executor.invokeAll(tasks);

        //the main thread wait the end of all tasks
        executor.shutdown();
        executor.awaitTermination(180, TimeUnit.SECONDS);

        List<Spell> allSpells = new ArrayList<>();

        List<MagicItem> allMagicItems = new ArrayList<>();

        List<EquipmentItem> allEquipmentItems = new ArrayList<>();

        //checking what type of GameData and put it in the right list
        for (Future<GameData> f : futures) {
            try {
                GameData data = f.get();
                if (data != null) {
                    switch (data) {
                        case Spell spell ->
                            allSpells.add(spell);
                        case MagicItem magicItem ->
                            allMagicItems.add(magicItem);
                        case EquipmentItem equipmentItem ->
                            allEquipmentItems.add(equipmentItem);
                        default -> {
                            System.out.println("Unknown GameData type: " + data.getClass());
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Thread error -> " + e.getMessage());
            }
        }

        //saving each data singularly so if one fails the others can still be saved
        for (Spell s : allSpells) {
            try {
                spellRepository.save(s);
            } catch (Exception e) {
                System.err.println("Error saving spell: " + s.getName());
                System.err.println("Cause: " + e.getMessage());
            }
        }

        for (MagicItem mi : allMagicItems) {
            try {
                magicItemRepository.save(mi);
            } catch (Exception e) {
                System.err.println("Error saving Magic Item: " + mi.getName());
                System.err.println("Cause: " + e.getMessage());

            }
        }

        for (EquipmentItem ei : allEquipmentItems) {
            try {
                equipmentItemRepository.save(ei);
            } catch (Exception e) {
                System.err.println("Error saving Equipment: " + ei.getName());
                System.err.println("Cause: " + e.getMessage());

            }
        }

        long duration = System.currentTimeMillis() - timeStart;
        System.out.println("END: initialize DB. Duration: " + duration / 1000 + " seconds.");
    }

    private List<String> fetchAllDataUrls() throws IOException {
        int maxRetries = 10;
        long defaultWaitTimeSeconds = 5;

        int count = 0;

        //for each type of data from the API, I setup the URL, save all URLs in allResults and return
        String[] dataAvailable = {"spells", "magic-items", "equipment"};
        List<String> allResults = new ArrayList<>();
        HttpURLConnection con = null;
        while (count < dataAvailable.length) {

            URL url = new URL("https://www.dnd5eapi.co/api/" + dataAvailable[count]);
            for (int retry = 0; retry < maxRetries; retry++) {
                try {

                    con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(5000);
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    // 429 : Too many requests
                    if (responseCode == 429) {
                        String retryAfterHeader = con.getHeaderField("Retry-After");
                        long waitTimeSeconds = defaultWaitTimeSeconds;

                        if (retryAfterHeader != null) {
                            try {
                                waitTimeSeconds = Long.parseLong(retryAfterHeader);
                            } catch (NumberFormatException nfe) {
                                //ignored, used the default value
                            }
                        }

                        System.out.printf("RATE LIMIT (429) for URL list. Waiting %d seconds. Try %d/%d.\n", waitTimeSeconds, retry + 1, maxRetries);

                        Thread.sleep(waitTimeSeconds * 1000);

                        continue;
                    }
                    //if not 200 OK, throw an exception
                    if (responseCode != 200) {
                        throw new IOException("HTTP code " + responseCode + " received during initial list fetch.");
                    }
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        Gson gson = new Gson();
                        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
                        JsonObject rootObject = json.getAsJsonObject();
                        JsonArray data = rootObject.get("results").getAsJsonArray();
                        for (int i = 0; i < data.size(); i++) {
                            JsonObject d = data.get(i).getAsJsonObject();
                            allResults.add((dataAvailable[count] + "," + d.get("url").getAsString()));
                        }
                        break;
                    }

                } catch (IOException ioe) {
                    System.err.printf("Critical error during fetch list -> %s. Try %d/%d.\n", ioe.getMessage(), retry + 1, maxRetries);
                    if (retry == maxRetries - 1) {
                        throw ioe;
                    }

                    try {
                        //waits 2 seconds and then retry the connection.
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during waiting: ", e);
                }
            }
            con.disconnect();
            count++;
        }
        return allResults;

    }

    private GameData threadSingleData(String info) {
        int maxRetries = 10;
        int currentRetry = 0;
        long defaultWaitTimeSeconds = 5;
        HttpURLConnection con = null;
        while (currentRetry < maxRetries) {
            try {
                String[] split = info.split(",");

                URL url = new URL("https://www.dnd5eapi.co" + split[1]);

                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();

                if (responseCode == 429) {
                    String retryAfterHeader = con.getHeaderField("Retry-After");

                    long waitTimeSeconds = defaultWaitTimeSeconds;
                    if (retryAfterHeader != null) {
                        try {
                            waitTimeSeconds = Long.parseLong(retryAfterHeader);
                        } catch (NumberFormatException nfe) {
                            //ignored, used the default value
                        }
                    }

                    System.out.printf("Response code 429 for (%s). Waiting %d seconds. Try %d/%d.\n",
                            info, waitTimeSeconds, currentRetry + 1, maxRetries);

                    Thread.sleep(waitTimeSeconds * 1000);

                    currentRetry++;
                    if (currentRetry == maxRetries) {
                        throw new IOException("Failed to fetch data after " + maxRetries + " retries due to 429.");
                    }
                    continue;
                }

                if (responseCode != 200) {
                    throw new IOException("HTTP code " + responseCode + " received.");
                }

                StringBuilder content;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                }

                Gson g = new Gson();
                JsonElement json = g.fromJson(content.toString(), JsonElement.class);

                //extraction of data from JSON and creation of the new Spell/MagicItem/EquipmentItem
                //the API gives a JSON with ALL values OPTIONAL, so i check if the value is present before getting it
                switch (split[0]) {
                    case "spells" -> {
                        JsonObject infoSpell = json.getAsJsonObject();

                        Spell spell;

                        String spellIndex = infoSpell.has("index") ? infoSpell.get("index").getAsString() : null;
                        String name = infoSpell.has("name") ? infoSpell.get("name").getAsString() : null;
                        Integer level = infoSpell.has("level") ? infoSpell.get("level").getAsInt() : null;
                        String spellRange = infoSpell.has("range") ? infoSpell.get("range").getAsString() : null;

                        JsonArray componentsArray = infoSpell.has("components") ? infoSpell.get("components").getAsJsonArray() : null;
                        List<String> components = new ArrayList<>();
                        if (componentsArray != null) {
                            for (int k = 0; k < componentsArray.size(); k++) {
                                components.add(componentsArray.get(k).getAsString());
                            }
                        }

                        String material = infoSpell.has("material") ? infoSpell.get("material").getAsString() : null;
                        Boolean ritual = infoSpell.has("ritual") ? infoSpell.get("ritual").getAsBoolean() : null;
                        Boolean concentration = infoSpell.has("concentration") ? infoSpell.get("concentration").getAsBoolean() : null;
                        String duration = infoSpell.has("duration") ? infoSpell.get("duration").getAsString() : null;
                        String castingTime = infoSpell.has("casting_time") ? infoSpell.get("casting_time").getAsString() : null;
                        String attackType = infoSpell.has("attack_type") ? infoSpell.get("attack_type").getAsString() : null;

                        JsonArray descArray = infoSpell.has("desc") ? infoSpell.get("desc").getAsJsonArray() : null;
                        List<String> description = new ArrayList<>();
                        if (descArray != null) {
                            for (int k = 0; k < descArray.size(); k++) {
                                description.add(descArray.get(k).getAsString());
                            }
                        }

                        JsonArray higherLevelArray = infoSpell.has("higher_level") ? infoSpell.get("higher_level").getAsJsonArray() : null;
                        List<String> higherLevel = new ArrayList<>();
                        if (higherLevelArray != null) {
                            for (int k = 0; k < higherLevelArray.size(); k++) {
                                higherLevel.add(higherLevelArray.get(k).getAsString());
                            }
                        }

                        String school = infoSpell.has("school")
                                ? infoSpell.get("school").getAsJsonObject().get("name").getAsString()
                                : null;

                        JsonObject aoeObject = infoSpell.has("area_of_effect")
                                ? infoSpell.get("area_of_effect").getAsJsonObject()
                                : null;

                        String areaOfEffect = null;
                        if (aoeObject != null) {
                            areaOfEffect
                                    = (aoeObject.has("type") ? aoeObject.get("type").getAsString() : "")
                                    + ","
                                    + (aoeObject.has("size") ? aoeObject.get("size").getAsString() : "");
                        }

                        spell = new Spell(
                                spellIndex, name, level, spellRange, components,
                                material, ritual, concentration, duration, castingTime,
                                attackType, description, higherLevel, school, areaOfEffect
                        );
                        con.disconnect();
                        return spell;
                    }

                    case "magic-items" -> {
                        JsonObject infoMagicItem = json.getAsJsonObject();

                        MagicItem magicItem;

                        String magicItemIndex = infoMagicItem.has("index") ? infoMagicItem.get("index").getAsString() : null;
                        String name = infoMagicItem.has("name") ? infoMagicItem.get("name").getAsString() : null;

                        JsonArray descArray = infoMagicItem.has("desc") ? infoMagicItem.get("desc").getAsJsonArray() : null;
                        List<String> description = new ArrayList<>();
                        if (descArray != null) {
                            for (int k = 0; k < descArray.size(); k++) {
                                description.add(descArray.get(k).getAsString());
                            }
                        }

                        String imageUrl = infoMagicItem.has("image") ? infoMagicItem.get("image").getAsString() : null;
                        byte[] imageBlob = null;
                        if (imageUrl != null) {
                            url = new URL("https://www.dnd5eapi.co" + imageUrl);
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5000);
                            con.setReadTimeout(5000);
                            con.setRequestMethod("GET");

                            responseCode = con.getResponseCode();

                            //some images returns a 403 : HTTP Forbidden, so i check if the responseCode is 200 otherwise the image is null
                            if (responseCode == 200) {

                                try (InputStream is = con.getInputStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                                    byte[] buffer = new byte[4096];
                                    int bytesRead;
                                    while ((bytesRead = is.read(buffer)) != -1) {
                                        baos.write(buffer, 0, bytesRead);
                                    }
                                    imageBlob = baos.toByteArray();
                                } catch (IOException e) {
                                    System.err.println("Error during image download: " + e.getMessage());
                                }
                            }
                        }

                        String rarity = infoMagicItem.has("rarity") ? infoMagicItem.getAsJsonObject("rarity").get("name").getAsString() : null;

                        magicItem = new MagicItem(magicItemIndex, name, description, imageBlob, rarity, false);
                        con.disconnect();
                        return magicItem;

                    }

                    case "equipment" -> {
                        JsonObject infoEquipmentItem = json.getAsJsonObject();

                        EquipmentItem equipmentItem;

                        String index = infoEquipmentItem.has("index") ? infoEquipmentItem.get("index").getAsString() : null;
                        String name = infoEquipmentItem.has("name") ? infoEquipmentItem.get("name").getAsString() : null;

                        JsonArray descArray = infoEquipmentItem.has("desc") ? infoEquipmentItem.get("desc").getAsJsonArray() : null;
                        List<String> description = new ArrayList<>();
                        if (descArray != null) {
                            for (int k = 0; k < descArray.size(); k++) {
                                description.add(descArray.get(k).getAsString());
                            }
                        }
                        String price = null;
                        String currency = null;
                        JsonObject costObject = infoEquipmentItem.has("cost")
                                ? infoEquipmentItem.get("cost").getAsJsonObject()
                                : null;

                        if (costObject != null) {
                            price = (costObject.has("quantity") ? costObject.get("quantity").getAsNumber().toString() : "");
                            currency = (costObject.has("unit") ? costObject.get("unit").getAsString() : "");
                        }

                        String weight = infoEquipmentItem.has("weight") ? infoEquipmentItem.get("weight").getAsString() : null;

                        equipmentItem = new EquipmentItem(index, name, price, currency, weight, description, false);
                        con.disconnect();
                        return equipmentItem;
                    }

                    default -> {
                        con.disconnect();
                        return null;
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.printf("Thread interrupted for (%s)\n", info);
                if (con != null) {
                    con.disconnect();
                }
                return null;

            } catch (JsonSyntaxException jse) {
                System.err.printf("Critical Json Syntax Error (no retry) for (%s) -> %s\n", info, jse.getMessage());
                if (con != null) {
                    con.disconnect();
                }
                return null;

            } catch (IOException ioe) {
                String errorMessage = ioe.getMessage();

                System.err.printf("I/O Exception for (%s) -> %s\n", info, errorMessage);

                if (errorMessage != null && (errorMessage.contains("Read timed out") || errorMessage.contains("Connection timed out") || errorMessage.contains("Connect timed out"))) {

                    currentRetry++;

                    if (currentRetry < maxRetries) {
                        System.out.printf("Retrying after I/O timeout for (%s). Waiting 2 seconds. Try %d/%d. \n", info, currentRetry, maxRetries);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            if (con != null) {
                                con.disconnect();
                            }
                            return null;
                        }
                        continue;
                    }
                }
                if (con != null) {
                    con.disconnect();
                }
                return null;
            }
        }
        System.err.printf("Failed to fetch data for (%s) after %d retries.\n", info, maxRetries);
        return null;
    }
}
