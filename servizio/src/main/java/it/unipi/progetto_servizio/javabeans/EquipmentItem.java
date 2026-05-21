/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio.javabeans;

import it.unipi.progetto_servizio.converter.ComponentListJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author coppo
 */
@Entity
@Table(name = "equipment_items", schema = "603355")
public class EquipmentItem implements Serializable, GameData {

    @Id
    @Column(name = "equipment_items_index", length = 100)
    private String equipmentItemsIndex;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "price", length = 50)
    private String price;

    @Column(name = "currency", length = 50)
    private String currency;

    @Column(name = "weight", length = 50)
    private String weight;

    @Column(name = "description", columnDefinition = "json")
    @Convert(converter = ComponentListJsonConverter.class)
    private List<String> description;

    @Column(name = "homebrew")
    private Boolean homebrew;

    public String getEquipmentItemsIndex() {
        return equipmentItemsIndex;
    }

    public void setEquipmentItemsIndex(String equipmentItemsIndex) {
        this.equipmentItemsIndex = equipmentItemsIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public Boolean getHomebrew() {
        return homebrew;
    }

    public void setHomebrew(Boolean homebrew) {
        this.homebrew = homebrew;
    }
    
    public String getType(){
        return "equipment-item";
    }

    public EquipmentItem(String equipmentItemsIndex, String name, String price, String currency, String weight, List<String> description, Boolean homebrew) {
        this.equipmentItemsIndex = equipmentItemsIndex;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.weight = weight;
        this.description = description;
        this.homebrew = homebrew;
    }

    public EquipmentItem() {
    }

}
