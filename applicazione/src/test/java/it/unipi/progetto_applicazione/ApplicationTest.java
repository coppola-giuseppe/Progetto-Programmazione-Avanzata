/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_applicazione;

import it.unipi.progetto_applicazione.javabeans.EquipmentItem;
import it.unipi.progetto_applicazione.javabeans.MagicItem;
import it.unipi.progetto_applicazione.javabeans.Spell;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author coppo
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    private final Spell spellTest;
    private final MagicItem magicItemTest;
    private final EquipmentItem equipmentItemTest;

    public ApplicationTest() {
        spellTest = new Spell();
        spellTest.setSpellIndex("testSpellIndex");
        spellTest.setName("testSpellName");
        spellTest.setLevel(9);
        spellTest.setSpellRange("testSpellRange");
        List testComponents = new ArrayList<String>();
        testComponents.add("V");
        testComponents.add("S");
        testComponents.add("M");
        spellTest.setComponents(testComponents);
        spellTest.setMaterial("testSpellMaterial");
        spellTest.setRitual(true);
        spellTest.setConcentration(true);
        spellTest.setDuration("testSpellDuration");
        spellTest.setCastingTime("testSpellCastingTime");
        spellTest.setAttackType("testSpellAttackTipe");
        List testSpellDescription = new ArrayList<String>();
        testSpellDescription.add("testSpellDescription1");
        testSpellDescription.add("testSpellDescription2");
        spellTest.setDescription(testSpellDescription);
        List testHigherLevel = new ArrayList<String>();
        testHigherLevel.add("testSpellHihgerLevel1");
        testHigherLevel.add("testSpellHigherLevel2");
        spellTest.setHigherLevel(testHigherLevel);
        spellTest.setSchool("testSpellSchool");
        spellTest.setAreaOfEffect("testSpellAreaOfEffect");
        spellTest.setHomebrew(true);

        magicItemTest = new MagicItem();
        magicItemTest.setMagicItemsIndex("testMagicItemIndex");
        magicItemTest.setName("testMagicItemName");
        List testMagicItemDescription = new ArrayList<String>();
        testSpellDescription.add("testMagicItemDescription1");
        testSpellDescription.add("testMagicItemDescription2");
        magicItemTest.setDescription(testMagicItemDescription);
        magicItemTest.setImage("");
        magicItemTest.setRarity("testMagicItemRarity");
        magicItemTest.setHomebrew(true);

        equipmentItemTest = new EquipmentItem();
        equipmentItemTest.setEquipmentItemsIndex("testEquipmentItemIndex");
        equipmentItemTest.setName("testEquipemntItemName");
        equipmentItemTest.setPrice("testEquipmentItemPrice");
        equipmentItemTest.setCurrency("testEquipmentItemCurrency");
        equipmentItemTest.setWeight("testEquipmentItemWeight");
        List testEquipmentItemDescription = new ArrayList<String>();
        testEquipmentItemDescription.add("testEquipmentItemDescription1");
        testEquipmentItemDescription.add("testEquipmentItemDescription2");
        equipmentItemTest.setDescription(testEquipmentItemDescription);
        equipmentItemTest.setHomebrew(true);

    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of initializing the database: adding data, initialize db and then
     * check if the data is still present and the return message from the
     * service is correct
     */
    @Test
    @Order(1)
    public void testInitializeDb() {
        System.out.printf("TEST INITIALIZE THE DATABASE --- START\n");
        try {
            AddDataController addController = new AddDataController();
            InitializeController initController = new InitializeController();
            addController.sendDataToDatabase(spellTest);

            URL url = new URL("http://localhost:8080/db/spells/" + spellTest.getSpellIndex());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals(200, responseCode, "TEST INITIALIZE THE DATABASE --- FAIL: error during data add");

            System.out.println("Starting the database initialize...");
            String result = initController.initializeDatabase();

            url = new URL("http://localhost:8080/db/spells/" + spellTest.getSpellIndex());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals("Database initialized.", result, "TEST INITIALIZE THE DATABASE --- FAIL: Service failed to initialize the db");
            assertEquals(404, responseCode, "TEST INITIALIZE THE DATABASE --- FAIL: Service failed to initialize the db");
            System.out.printf("TEST INITIALIZE THE DATABASE --- SUCCESS\n\n");
        } catch (IOException ioe) {
            fail("TEST INITIALIZE THE DATABASE --- FAIL: Error during the connection with the service.");
        }

    }

    /**
     * Test of adding data in the databas
     *
     */
    @Test
    @Order(2)
    public void testAddData() {
        System.out.printf("TEST ADD DATA IN THE DATABASE --- START\n");

        try {
            AddDataController testController = new AddDataController();
            //
            //adding a spell and then check the service response and the effective presence of the spell
            //
            System.out.println("Adding the test spell...");
            String[] result = testController.sendDataToDatabase(spellTest);
            URL url = new URL("http://localhost:8080/db/spells/" + spellTest.getSpellIndex());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals("201", result[0], "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            assertEquals(200, responseCode, "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            //
            //adding a magic item and then check the service response and the effective presence of the magic item
            //
            System.out.println("Adding the test magic item...");
            result = testController.sendDataToDatabase(magicItemTest);
            url = new URL("http://localhost:8080/db/magic_items/" + magicItemTest.getMagicItemsIndex());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            assertEquals("201", result[0], "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            assertEquals(200, responseCode, "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            //
            //adding an equipment item and then check the service response and the effective presence of the equipment item
            //
            System.out.println("Adding the test equipment item...");
            result = testController.sendDataToDatabase(equipmentItemTest);
            url = new URL("http://localhost:8080/db/equipment_items/" + equipmentItemTest.getEquipmentItemsIndex());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            assertEquals("201", result[0], "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            assertEquals(200, responseCode, "TEST ADD DATA IN THE DATABASE --- FAIL:  " + result[1]);
            //
            System.out.printf("TEST ADD DATA IN THE DATABASE --- SUCCESS\n\n");
        } catch (IOException ioe) {
            fail("TEST ADD DATA IN THE DATABASE --- FAIL: Error during the connection with the service.");
        }
    }

    /**
     * Test of removing data from the databas
     *
     */
    @Test
    @Order(3)
    public void testRemoveData() {
        System.out.printf("TEST REMOVE DATA FROM THE DATABASE --- START\n");
        try {
            DatabaseController testController = new DatabaseController();
            //
            //removing a spell and then check the service response and the effective presence of the spell
            //
            System.out.println("Removing the test spell...");
            int responseCode = testController.removeData("spells/delete", spellTest.getSpellIndex());
            if (responseCode == 0) {
                throw new IOException();
            }
            assertEquals(200, responseCode, "TEST REMOVE DATA FROM THE DATABASE --- FAIL: Removal failed, wrong response code.");
            URL url = new URL("http://localhost:8080/db/spells/" + spellTest.getSpellIndex());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals(404, responseCode, "TEST REMOVE DATA FROM THE DATABASE --- FAIL: Spell still in the database.");
            //
            //removing a magic item and then check the service response and the effective presence of the magic item
            //
            System.out.println("Removing the test magic item...");
            responseCode = testController.removeData("magic_items/delete", magicItemTest.getMagicItemsIndex());
            assertEquals(200, responseCode, "TEST REMOVE DATA FROM THE DATABASE --- FAIL: Removal failed, wrong response code.");
            url = new URL("http://localhost:8080/db/magic_items/" + magicItemTest.getMagicItemsIndex());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals(404, responseCode, "TEST REMOVE DATA FROM THE DATABASE --- FAIL: Magic item still in the database.");
            //
            //removing a equipment item and then check the service response and the effective presence of the equipment item
            //
            System.out.println("Removing the test equipment item...");
            responseCode = testController.removeData("equipment_items/delete", equipmentItemTest.getEquipmentItemsIndex());
            assertEquals(200, responseCode, "TEST REMOVE DATA FROM THE DATABASE --- FAIL: Removal failed, wrong response code.");
            url = new URL("http://localhost:8080/db/magic_items/" + magicItemTest.getMagicItemsIndex());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            con.disconnect();
            assertEquals(404, responseCode, "TEST REMOVE DATA IN THE DATABASE --- FAIL: Equipment item still in the database.");
            //
            System.out.printf("TEST REMOVE DATA FROM THE DATABASE --- SUCCESS\n\n");
        } catch (IOException ioe) {
            fail("TEST REMOVE DATA FROM THE DATABASE --- FAIL: Error during the connection with the service.");
        }
    }
}
