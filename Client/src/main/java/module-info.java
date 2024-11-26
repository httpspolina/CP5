module Client {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.media;
    requires transitive javafx.swing;
    requires transitive javafx.web;
    requires java.sql;

    opens client;
    opens client.controllers;
}