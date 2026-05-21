/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_applicazione.javabeans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author coppo
 */
public class EquipmentItem implements Serializable, GameData {

    private String equipmentItemsIndex;

    private String name;

    private String price;

    private String currency;

    private String weight;

    private List<String> description;

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

    public EquipmentItem(String equipmentItemsIndex, String name, String price, String currency, String weight, List<String> description) {
        this.equipmentItemsIndex = equipmentItemsIndex;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.weight = weight;
        this.description = description;
        this.homebrew = true;
    }

    public EquipmentItem() {
    }

}
