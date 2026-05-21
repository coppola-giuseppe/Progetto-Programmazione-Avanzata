/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.controllers.magicItems;

import it.unipi.progetto_servizio.javabeans.MagicItem;
import it.unipi.progetto_servizio.javabeans.MagicItemEncoded;
import it.unipi.progetto_servizio.repositories.MagicItemRepository;
import java.net.URI;
import java.util.Base64;
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
@RequestMapping(path = "/db/magic_items")
public class MagicItemsController {

    @Autowired
    private MagicItemRepository magicItemRepository;

    @GetMapping(path = "/filter/all")
    public @ResponseBody
    Iterable<MagicItem> getAllMagicItems() {
        return magicItemRepository.findAll();
    }

    @GetMapping(path = "/filter/homebrew")
    public @ResponseBody
    Iterable<MagicItem> getHomebrewMagicItems() {
        return magicItemRepository.findByHomebrewTrue();
    }

    @GetMapping(path = "/filter/official")
    public @ResponseBody
    Iterable<MagicItem> getOfficialMagicItems() {
        return magicItemRepository.findByHomebrewFalse();
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addNewMagicItem(@RequestBody MagicItemEncoded mie) {

        MagicItem mi = new MagicItem(
                mie.getMagicItemsIndex(),
                mie.getName(),
                mie.getDescription(),
                mie.getImage() == null ? null : Base64.getDecoder().decode(mie.getImage()),
                mie.getRarity(),
                true
        );

        if (!magicItemRepository.existsByMagicItemsIndex(mi.getMagicItemsIndex())) {
            MagicItem savedMagicItem = magicItemRepository.save(mi);

            String location = "http://localhost:8080/db/magic_items/" + savedMagicItem.getMagicItemsIndex();
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create(location));

            return new ResponseEntity<>("Magic item added.", header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Magic item already in the database, change name.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> removeMagicItem(@RequestParam String index) {
        if (magicItemRepository.existsByMagicItemsIndex(index)) {
            magicItemRepository.deleteById(index);
            return new ResponseEntity<>("Magic item removed.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Magic item not in the database.", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/search/{name}")
    public @ResponseBody
    ResponseEntity<?> getMagicItemByName(@PathVariable String name) {

        MagicItem magicItem = magicItemRepository.findByName(name);

        if (magicItem != null) {
            return new ResponseEntity<>(magicItem, HttpStatus.OK);
        }
        return new ResponseEntity<>("Magic item with name '" + name + "' not found.", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{index}")
    public @ResponseBody
    ResponseEntity<?> getMagicItemById(@PathVariable String index) {

        Optional<MagicItem> magicItem = magicItemRepository.findById(index);

        if (magicItem.isPresent()) {
            return new ResponseEntity<>(magicItem, HttpStatus.OK);
        }
        return new ResponseEntity<>("Magic item with index '" + index + "' not found.", HttpStatus.NOT_FOUND);
    }
}
