module org.example._4equals10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;


    opens org.example._4equals10 to javafx.fxml;
    exports org.example._4equals10;
}