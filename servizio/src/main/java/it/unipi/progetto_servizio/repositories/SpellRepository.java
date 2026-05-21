/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.repositories;

import it.unipi.progetto_servizio.javabeans.Spell;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author coppo
 */
public interface SpellRepository extends CrudRepository<Spell, String> {

    Spell findByName(String n);

    Iterable<Spell> findByHomebrewTrue();

    Iterable<Spell> findByHomebrewFalse();

    boolean existsBySpellIndex(String spellIndex);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Spell")
    void truncateTable();
}
