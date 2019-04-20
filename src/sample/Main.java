package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import javafx.scene.layout.HBox;

import java.util.Timer;
import java.util.TimerTask;

class WindowButtons extends HBox {

    public WindowButtons() {
        Button closeBtn = new Button("X");

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        this.getChildren().add(closeBtn);
    }
}

class EnableDynamicLogo extends TimerTask
{
    EnableDynamicLogo(ImageView staticView, ImageView dynamicView)
    {
        staticImage = staticView;
        dynamicImage = dynamicView;
    }

    @Override
    public void run()
    {
        dynamicImage.setVisible(true);
        staticImage.setVisible(false);
        targetTimer.schedule(new DisableDynamicLogo(staticImage, dynamicImage), 1*1000);
    }

    private ImageView staticImage;
    private ImageView dynamicImage;
    Timer targetTimer;
}

class DisableDynamicLogo extends TimerTask
{
    DisableDynamicLogo(ImageView staticView, ImageView dynamicView)
    {
        staticImage = staticView;
        dynamicImage = dynamicView;
    }

    @Override
    public void run()
    {
        dynamicImage.setVisible(false);
        staticImage.setVisible(true);
    }

    private ImageView staticImage;
    private ImageView dynamicImage;

}

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    ImageView logoDynamic;
    ImageView logoStatic;
    Timer logoTimer;
    private int logoChangeRate = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {

        logoTimer = new Timer(true);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });


        Scene scene = new Scene(root, 1024, 576);
        scene.setFill(Color.TRANSPARENT);
        Hyperlink hyperLink = (Hyperlink) scene.lookup("#hyperlink");

        logoDynamic = (ImageView)scene.lookup("#logoDynamic");
        logoStatic = (ImageView)scene.lookup("#logoStatic");

        hyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://github.com/inanevin/LinaEngine");
            }
        });
        primaryStage.setTitle("Lina Engine Build Launcher");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.getScene().getStylesheets().setAll(Main.class.getResource("main.css").toString());
        primaryStage.show();

        logoTimer.scheduleAtFixedRate(new EnableDynamicLogo(logoStatic, logoDynamic), logoChangeRate*1000, logoChangeRate*1000);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
