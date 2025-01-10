module com.monopoly.core {
    exports com.monopoly.game.component.area; // Экспорт пакета для использования в других модулях
    exports com.monopoly.game.config;         // Экспорт конфигурации
    exports com.monopoly.game;
    requires org.slf4j;                       // Зависимость от slf4j
    requires ch.qos.logback.classic;          // Зависимость от Logback (при необходимости)
    requires static lombok;                   // Указание, что lombok - это статическая зависимость
}