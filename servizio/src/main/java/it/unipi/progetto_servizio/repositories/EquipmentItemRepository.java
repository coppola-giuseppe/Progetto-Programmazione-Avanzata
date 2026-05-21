/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.repositories;

import it.unipi.progetto_servizio.javabeans.EquipmentItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author coppo
 */
public interface EquipmentItemRepository extends CrudRepository<EquipmentItem, String> {

    EquipmentItem findByName(String n);

    Iterable<EquipmentItem> findByHomebrewTrue();

    Iterable<EquipmentItem> findByHomebrewFalse();
    
    boolean existsByEquipmentItemsIndex(String equipmentItemsIndex);

    @Modifying
    @Transactional
    @Query("DELETE FROM EquipmentItem")
    void truncateTable();
}
