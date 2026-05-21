/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.progetto_servizio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author coppo
 */
@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private DataInitializer dataInitializer;

    @Override
    public void run(String... args) throws Exception {
        try {
            dataInitializer.populateDatabase();
        } catch (Exception e) {
            System.err.println("Error during DB population: " + e.getMessage());
            System.exit(1);
        }
    }
}
