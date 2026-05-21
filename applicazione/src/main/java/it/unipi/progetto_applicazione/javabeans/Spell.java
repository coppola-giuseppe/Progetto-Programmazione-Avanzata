/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author coppo
 */
package it.unipi.progetto_applicazione.javabeans;

import java.io.Serializable;
import java.util.List;

public class Spell implements Serializable, GameData {

    private String spellIndex;

    private String name;

    private Integer level;

    private String spellRange;

    private List<String> components;

    private String material;

    private Boolean ritual;

    private Boolean concentration;

    private String duration;

    private String castingTime;

    private String attackType;

    private List<String> description;

    private List<String> higherLevel;

    private String school;

    private String areaOfEffect;

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
        this.homebrew = true;
    }

    public Spell() {
    }

}
