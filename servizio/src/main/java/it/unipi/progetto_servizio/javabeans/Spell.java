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
@Table(name = "spells", schema = "603355")
public class Spell implements Serializable, GameData {

    @Id
    @Column(name = "spell_index", length = 100)
    private String spellIndex;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "level")
    private Integer level;

    @Column(name = "spell_range", length = 50)
    private String spellRange;

    @Column(name = "components", columnDefinition = "json")
    @Convert(converter = ComponentListJsonConverter.class)
    private List<String> components;

    @Column(name = "material", columnDefinition = "TEXT")
    private String material;

    @Column(name = "ritual")
    private Boolean ritual;

    @Column(name = "concentration")
    private Boolean concentration;

    @Column(name = "duration", length = 50)
    private String duration;

    @Column(name = "casting_time", length = 50)
    private String castingTime;

    @Column(name = "attack_type", length = 50)
    private String attackType;

    @Column(name = "description", columnDefinition = "json")
    @Convert(converter = ComponentListJsonConverter.class)
    private List<String> description;

    @Column(name = "higher_level", columnDefinition = "json")
    @Convert(converter = ComponentListJsonConverter.class)
    private List<String> higherLevel;

    @Column(name = "school", length = 50)
    private String school;

    @Column(name = "area_of_effect", length = 50)
    private String areaOfEffect;

    @Column(name = "homebrew")
    private Boolean homebrew;

    public String getSpellIndex() {
        return spellIndex;
    }

    public void setSpellIndex(String spellIndex) {
        this.spellIndex = spellIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSpellRange() {
        return spellRange;
    }

    public void setSpellRange(String spellRange) {
        this.spellRange = spellRange;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Boolean getRitual() {
        return ritual;
    }

    public void setRitual(Boolean ritual) {
        this.ritual = ritual;
    }

    public Boolean getConcentration() {
        return concentration;
    }

    public void setConcentration(Boolean concentration) {
        this.concentration = concentration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getHigherLevel() {
        return higherLevel;
    }

    public void setHigherLevel(List<String> higherLevel) {
        this.higherLevel = higherLevel;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(String areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }

    public Boolean getHomebrew() {
        return homebrew;
    }

    public void setHomebrew(Boolean homebrew) {
        this.homebrew = homebrew;
    }

    public String getType() {
        return "spell";
    }

    public Spell(String spellIndex, String name, Integer level, String spellRange, List<String> components, String material, Boolean ritual, Boolean concentration, String duration, String castingTime, String attackType, List<String> description, List<String> higherLevel, String school, String areaOfEffect, Boolean homebrew) {
        this.spellIndex = spellIndex;
        this.name = name;
        this.level = level;
        this.spellRange = spellRange;
        this.components = components;
        this.material = material;
        this.ritual = ritual;
        this.concentration = concentration;
        this.duration = duration;
        this.castingTime = castingTime;
        this.attackType = attackType;
        this.description = description;
        this.higherLevel = higherLevel;
        this.school = school;
        this.areaOfEffect = areaOfEffect;
        this.homebrew = homebrew;
    }

    public Spell(String spellIndex, String name, Integer level, String spellRange, List<String> components, String material, Boolean ritual, Boolean concentration, String duration, String castingTime, String attackType, List<String> description, List<String> higherLevel, String school, String areaOfEffect) {
        this.spellIndex = spellIndex;
        this.name = name;
        this.level = level;
        this.spellRange = spellRange;
        this.components = components;
        this.material = material;
        this.ritual = ritual;
        this.concentration = concentration;
        this.duration = duration;
        this.castingTime = castingTime;
        this.attackType = attackType;
        this.description = description;
        this.higherLevel = higherLevel;
        this.school = school;
        this.areaOfEffect = areaOfEffect;
        this.homebrew = false;
    }

    public Spell() {
    }

}
