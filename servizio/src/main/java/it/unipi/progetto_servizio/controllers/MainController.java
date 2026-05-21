/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.controllers;

import it.unipi.progetto_servizio.DataInitializer;
import it.unipi.progetto_servizio.javabeans.GameData;
import it.unipi.progetto_servizio.javabeans.EquipmentItem;
import it.unipi.progetto_servizio.javabeans.MagicItem;
import it.unipi.progetto_servizio.javabeans.Spell;
import it.unipi.progetto_servizio.repositories.EquipmentItemRepository;
import it.unipi.progetto_servizio.repositories.MagicItemRepository;
import it.unipi.progetto_servizio.repositories.SpellRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author coppo
 */
@Controller
@RequestMapping(path = "/db")
public class MainController {

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private MagicItemRepository magicItemRepository;

    @Autowired
    private SpellRepository spellRepository;

    @Autowired
    private DataInitializer dataInitializer;

    @GetMapping(path = "/initialize")
    public @ResponseBody
    String initializeDatabase() {
        try {
            dataInitializer.populateDatabase();
            return "Database initialized.";
        } catch (Exception e) {
            System.err.printf("Error -> %s\n", e.getMessage());
            return "Initialization failed -> " + e.getMessage();
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<GameData> getAllData() {

        List<GameData> allData = new ArrayList<>();

        spellRepository.findAll().forEach(allData::add);

        magicItemRepository.findAll().forEach(allData::add);

        equipmentItemRepository.findAll().forEach(allData::add);

        return allData;
    }

    @GetMapping(path = "/official")
    public @ResponseBody
    Iterable<GameData> getAllOfficialData() {

        List<GameData> allData = new ArrayList<>();

        spellRepository.findByHomebrewFalse().forEach(allData::add);

        magicItemRepository.findByHomebrewFalse().forEach(allData::add);

        equipmentItemRepository.findByHomebrewFalse().forEach(allData::add);

        return allData;
    }

    @GetMapping(path = "/homebrew")
    public @ResponseBody
    Iterable<GameData> getAllHomebrewData() {

        List<GameData> allData = new ArrayList<>();

        spellRepository.findByHomebrewTrue().forEach(allData::add);

        magicItemRepository.findByHomebrewTrue().forEach(allData::add);

        equipmentItemRepository.findByHomebrewTrue().forEach(allData::add);

        return allData;
    }

    @GetMapping(path = "/delete/homebrew")
    public @ResponseBody
    String deleteHomebrewData() {

        Iterable<Spell> homebrewSpells = spellRepository.findByHomebrewTrue();
        spellRepository.deleteAll(homebrewSpells);

        Iterable<MagicItem> homebrewMagicItems = magicItemRepository.findByHomebrewTrue();
        magicItemRepository.deleteAll(homebrewMagicItems);

        Iterable<EquipmentItem> homebrewEquipmentItems = equipmentItemRepository.findByHomebrewTrue();
        equipmentItemRepository.deleteAll(homebrewEquipmentItems);
        return "All homebrew data has been deleted.";
    }

    @GetMapping(path = "/delete/official")
    public @ResponseBody
    String deleteOfficialData() {

        Iterable<Spell> OfficialSpells = spellRepository.findByHomebrewFalse();
        spellRepository.deleteAll(OfficialSpells);

        Iterable<MagicItem> OfficialMagicItems = magicItemRepository.findByHomebrewFalse();
        magicItemRepository.deleteAll(OfficialMagicItems);

        Iterable<EquipmentItem> OfficialEquipmentItems = equipmentItemRepository.findByHomebrewFalse();
        equipmentItemRepository.deleteAll(OfficialEquipmentItems);
        return "All Official data has been deleted.";
    }
}
