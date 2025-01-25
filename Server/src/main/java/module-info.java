module com.monopoly.server {
    requires com.monopoly.graphics;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires com.monopoly.core;
    requires com.monopoly.common;
    exports com.monopoly.server;
    exports com.monopoly.server.message;
}
