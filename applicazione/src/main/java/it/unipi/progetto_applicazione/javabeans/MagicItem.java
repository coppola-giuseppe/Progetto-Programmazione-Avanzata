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
public class MagicItem implements Serializable, GameData {

    private String magicItemsIndex;

    private String name;

    private List<String> description;

    private String image;

    private String rarity;

    private Boolean homebrew;

    public String getMagicItemsIndex() {
        return magicItemsIndex;
    }

    public void setMagicItemsIndex(String magicItemsIndex) {
        this.magicItemsIndex = magicItemsIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public Boolean getHomebrew() {
        return homebrew;
    }

    public void setHomebrew(Boolean homebrew) {
        this.homebrew = homebrew;
    }


    public MagicItem(String magicItemsIndex, String name, List<String> description, String image, String rarity) {
        this.magicItemsIndex = magicItemsIndex;
        this.name = name;
        this.description = description;
        this.image = image;
        this.rarity = rarity;
        this.homebrew = true;
    }

    public MagicItem() {
    }

}
