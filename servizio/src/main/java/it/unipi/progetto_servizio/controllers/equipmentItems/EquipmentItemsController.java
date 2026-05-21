/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.controllers.equipmentItems;

import it.unipi.progetto_servizio.javabeans.EquipmentItem;
import it.unipi.progetto_servizio.repositories.EquipmentItemRepository;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author coppo
 */
@Controller
@RequestMapping(path = "/db/equipment_items")
public class EquipmentItemsController {

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @GetMapping(path = "/filter/all")
    public @ResponseBody
    Iterable<EquipmentItem> getAllEquipmentItems() {
        return equipmentItemRepository.findAll();
    }

    @GetMapping(path = "/filter/homebrew")
    public @ResponseBody
    Iterable<EquipmentItem> getHomebrewEquipmentItems() {
        return equipmentItemRepository.findByHomebrewTrue();
    }

    @GetMapping(path = "/filter/official")
    public @ResponseBody
    Iterable<EquipmentItem> getOfficialEquipmentItems() {
        return equipmentItemRepository.findByHomebrewFalse();
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addNewEquipmentItem(@RequestBody EquipmentItem ei) {
        if (!equipmentItemRepository.existsByEquipmentItemsIndex(ei.getEquipmentItemsIndex())) {
            EquipmentItem savedEquipmentItem = equipmentItemRepository.save(ei);

            String location = "http://localhost:8080/db/equipment_items/" + savedEquipmentItem.getEquipmentItemsIndex();
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create(location));

            return new ResponseEntity<>("Equipment item added.", header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Equipment item already in the database, change name.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> removeEquipmentItem(@RequestParam String index) {
        if (equipmentItemRepository.existsByEquipmentItemsIndex(index)) {
            equipmentItemRepository.deleteById(index);
            return new ResponseEntity<>("Equipment item removed.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Equipment item not in the database.", HttpStatus.NOT_FOUND);

    }

    @GetMapping(path = "/search/{name}")
    public @ResponseBody
    ResponseEntity<?> getEquipmentItemByName(@PathVariable String name) {

        EquipmentItem equipmentItem = equipmentItemRepository.findByName(name);

        if (equipmentItem != null) {
            return new ResponseEntity<>(equipmentItem, HttpStatus.OK);
        }
        return new ResponseEntity<>("Equipment item with name '" + name + "' not found.", HttpStatus.NOT_FOUND);

    }

    @GetMapping(path = "/{index}")
    public @ResponseBody
    ResponseEntity<?> getEquipmentItemById(@PathVariable String index) {

        Optional<EquipmentItem> equipmentItem = equipmentItemRepository.findById(index);

        if (equipmentItem.isPresent()) {
            return new ResponseEntity<>(equipmentItem, HttpStatus.OK);
        }
        return new ResponseEntity<>("Equipment item with index '" + index + "' not found.", HttpStatus.NOT_FOUND);
    }
}
