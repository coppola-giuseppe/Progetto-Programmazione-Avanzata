/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_applicazione.javabeans;

import java.util.List;

/**
 *
 * @author coppo
 */
public class Homebrew implements GameData {

    private String homebrewIndex;

    private String name;

    private String type;

    private List<String> description;

    public String getHomebrewIndex() {
        return homebrewIndex;
    }

    public void setHomebrewIndex(String homebrewIndex) {
        this.homebrewIndex = homebrewIndex;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Homebrew(String homebrewIndex, String name, String type, List<String> description) {
        this.homebrewIndex = homebrewIndex;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public Homebrew() {
    }

}
