/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.controllers.spells;

import it.unipi.progetto_servizio.javabeans.Spell;
import it.unipi.progetto_servizio.repositories.SpellRepository;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping(path = "/db/spells")
public class SpellController {

    @Autowired
    private SpellRepository spellRepository;

    @GetMapping(path = "/filter/all")
    public @ResponseBody
    Iterable<Spell> getAllSpells() {
        return spellRepository.findAll();
    }

    @GetMapping(path = "/filter/homebrew")
    public @ResponseBody
    Iterable<Spell> getHomebrewSpells() {
        return spellRepository.findByHomebrewTrue();
    }

    @GetMapping(path = "/filter/official")
    public @ResponseBody
    Iterable<Spell> getOfficialSpells() {
        return spellRepository.findByHomebrewFalse();
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addNewSpell(@RequestBody Spell s) {
        if (!spellRepository.existsBySpellIndex(s.getSpellIndex())) {
            Spell savedSpell = spellRepository.save(s);

            String location = "http://localhost:8080/db/spells/" + savedSpell.getSpellIndex();
            HttpHeaders header = new HttpHeaders();
            header.setLocation(URI.create(location));

            return new ResponseEntity<>("Spell added.", header, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Spell already in the database, change name.", HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "/delete")
    @Transactional
    public @ResponseBody
    ResponseEntity<String> removeSpell(@RequestParam String index) {
        if (spellRepository.existsById(index)) {
            spellRepository.deleteById(index);
            return new ResponseEntity<>("Spell removed.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Spell not in the database.", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/search/{name}")
    public @ResponseBody
    ResponseEntity<?> getSpellByName(@PathVariable String name) {
        Spell spell = spellRepository.findByName(name);

        if (spell != null) {
            return new ResponseEntity<>(spell, HttpStatus.OK);
        }
        return new ResponseEntity<>("Spell with name '" + name + "' not found.", HttpStatus.NOT_FOUND);
    }
    
    
    
    @GetMapping(path = "/{index}")
    public @ResponseBody
    ResponseEntity<?> getMagicItemById(@PathVariable String index) {

        Optional<Spell> spell = spellRepository.findById(index);

        if (spell.isPresent()) {
            return new ResponseEntity<>(spell, HttpStatus.OK);
        }
        return new ResponseEntity<>("Spell with index '" + index + "' not found.", HttpStatus.NOT_FOUND);
    }
}
