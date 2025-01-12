module com.monopoly.graphics {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires com.monopoly.core;
    exports com.monopoly.graphics;
//    exports com.monopoly.graphics to javafx.graphics;
    exports com.monopoly.graphics.rendering to javafx.graphics;
    opens com.monopoly.graphics to javafx.fxml;
}
