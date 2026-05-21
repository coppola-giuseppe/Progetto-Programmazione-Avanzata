module it.unipi.progetto_applicazione {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;

    opens it.unipi.progetto_applicazione to javafx.fxml;
    exports it.unipi.progetto_applicazione;
    
    
    opens it.unipi.progetto_applicazione.javabeans to com.google.gson, javafx.base;
    
}
