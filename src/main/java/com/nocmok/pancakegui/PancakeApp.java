package com.nocmok.pancakegui;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.nocmok.pancake.Pancake;
import com.nocmok.pancakegui.scenes.MainScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class PancakeApp extends Application {

    private static PancakeApp app;

    static {
        Pancake.load();
    }

    private Stage primaryStage;

    private Session session;

    private ExecutorService worker = Executors.newFixedThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            return t;
        }
    });

    @Override
    public void start(Stage primaryStage) throws Exception {
        PancakeApp.app = this;
        this.primaryStage = primaryStage;
        session = new Session();

        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
        primaryStage.setTitle("Pancake");
        primaryStage.setScene(new MainScene());
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    @Override
    public void stop() {
        worker.shutdown();
    }

    public Stage primaryStage() {
        return primaryStage;
    }

    public Session session() {
        return session;
    }

    public ExecutorService worker() {
        return worker;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static PancakeApp app() {
        return app;
    }

    public static URL getLayout(String name) {
        return PancakeApp.class.getClassLoader().getResource("layouts/" + name);
    }
}