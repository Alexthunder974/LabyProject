module com.example.labyrinthefx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labyrinthefx to javafx.fxml;
    exports com.example.labyrinthefx;
}