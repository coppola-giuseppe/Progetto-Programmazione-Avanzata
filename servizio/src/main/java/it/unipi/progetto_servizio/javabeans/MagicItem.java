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
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author coppo
 */
@Entity
@Table(name = "magic_items", schema = "603355")
public class MagicItem implements Serializable, GameData {

    @Id
    @Column(name = "magic_items_index", length = 100)
    private String magicItemsIndex;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "json")
    @Convert(converter = ComponentListJsonConverter.class)
    private List<String> description;

    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @Column(name = "rarity", length = 50)
    private String rarity;

    @Column(name = "homebrew")
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
    
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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
    
    
    public boolean isHasImage() {
        return image != null && image.length > 0;
    }
    
    public String getType(){
        return "magic-item";
    }

    public MagicItem(String magicItemsIndex, String name, List<String> description, byte[] image, String rarity, Boolean homebrew) {
        this.magicItemsIndex = magicItemsIndex;
        this.name = name;
        this.description = description;
        this.image = image;
        this.rarity = rarity;
        this.homebrew = homebrew;
    }

    public MagicItem() {
    }

}
