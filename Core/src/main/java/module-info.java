module com.monopoly.core {
    exports com.monopoly.game.component.area;
    exports com.monopoly.game.config;
    exports com.monopoly.game;
    exports com.monopoly.game.component.model;
    exports com.monopoly.game.component.money;
    exports com.monopoly.game.manager;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires static lombok;
}