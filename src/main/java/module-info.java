module com.mycompany.sokovangame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.sokovangame to javafx.fxml;
    exports com.mycompany.sokovangame;
}
