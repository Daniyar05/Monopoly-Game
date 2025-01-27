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
    requires com.monopoly.common;

    exports com.monopoly.graphics;
    exports com.monopoly.graphics.rendering to javafx.graphics, com.monopoly.server;
    opens com.monopoly.graphics to javafx.fxml;
}
