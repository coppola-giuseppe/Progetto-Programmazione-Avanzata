/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.repositories;

import it.unipi.progetto_servizio.javabeans.MagicItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author coppo
 */
public interface MagicItemRepository extends CrudRepository<MagicItem, String> {

    MagicItem findByName(String n);

    Iterable<MagicItem> findByHomebrewTrue();

    Iterable<MagicItem> findByHomebrewFalse();

    boolean existsByMagicItemsIndex(String magicItemsIndex);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM MagicItem")
    void truncateTable();
}
