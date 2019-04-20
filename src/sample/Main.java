package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

import javafx.scene.layout.HBox;

import javax.xml.soap.Text;
import java.util.Timer;
import java.util.TimerTask;


class EnableDynamicLogo extends TimerTask {
    EnableDynamicLogo(ImageView staticView, ImageView dynamicView, Timer target) {
        staticImage = staticView;
        dynamicImage = dynamicView;
        targetTimer = target;
    }

    @Override
    public void run() {
        dynamicImage.setVisible(true);
        staticImage.setVisible(false);
        targetTimer.schedule(new DisableDynamicLogo(staticImage, dynamicImage), 2 * 1000);
    }

    private ImageView staticImage;
    private ImageView dynamicImage;
    Timer targetTimer;
}

class DisableDynamicLogo extends TimerTask {
    DisableDynamicLogo(ImageView staticView, ImageView dynamicView) {
        staticImage = staticView;
        dynamicImage = dynamicView;
    }

    @Override
    public void run() {
        dynamicImage.setVisible(false);
        staticImage.setVisible(true);
    }

    private ImageView staticImage;
    private ImageView dynamicImage;

}

class TableRowBase
{
    TableRowBase(String id) { m_ID = id; }

    public String GetID(){ return m_ID; }

    private String m_ID;
}
class TableRow<T> extends TableRowBase
{
    TableRow(String id, T data) { super(id); m_Data = data;}

    public T GetData() { return m_Data; }
    public void SetData(T data) { m_Data = data;}

    private T m_Data;
}



public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    ImageView logoDynamic;
    ImageView logoStatic;
    Timer logoTimer;
    private int logoChangeRate = 5;
    TextField sourceField, buildField;
    ComboBox<String> generatorBox;
    TableView optionsTable;

    String generators[] =
            {
                    "Visual Studio 15 2017",
                    "Visual Studio 15 2017 ARM",
                    "Visual Studio 15 2017 Win64",
                    "Visual Studio 14 2015",
                    "Visual Studio 14 2015 ARM",
                    "Visual Studio 14 2015 Win64",
                    "Visual Studio 12 2013",
                    "Visual Studio 12 2013 ARM",
                    "Visual Studio 12 2013 Win64",
                    "Visual Studio 11 2012",
                    "Visual Studio 11 2012 ARM",
                    "Visual Studio 11 2012 Win64",
                    "Visual Studio 10 2010",
                    "Visual Studio 10 2010 ARM",
                    "Visual Studio 10 2010 IA64",
                    "Visual Studio 9 2008",
                    "Visual Studio 9 2008 ARM",
                    "Visual Studio 9 2008 IA64",
                    "Borland Makefiles",
                    "NMake Makefiles",
                    "NMake Makefiles JOM",
                    "Green Hills MULTI",
                    "MSYS Makefiles",
                    "MinGW Makefiles",
                    "Unix Makefiles",
                    "Ninja",
                    "Watcom WMake",
                    "CodeBlocks - MinGW Makefiles",
                    "CodeBlocks - NMake Makefiles",
                    "CodeBlocks - NMake Makefiles JOM",
                    "CodeBlocks - Ninja",
                    "CodeBlocks - Unix Makefiles",
                    "CodeLite - MinGW Makefiles",
                    "CodeLite - NMake Makefiles",
                    "CodeLite - Ninja",
                    "CodeLite - Unix Makefiles",
                    "Sublime Text 2 - MinGW Makefiles",
                    "Sublime Text 2 - NMake Makefiles",
                    "Sublime Text 2 - Ninja",
                    "Sublime Text 2 - Unix Makefiles",
                    "Kate - MinGW Makefiles",
                    "Kate - NMake Makefiles",
                    "Kate - Ninja",
                    "Kate - Unix Makefiles",
                    "Eclipse CDT4 - NMake Makefiles",
                    "Eclipse CDT4 - MinGW Makefiles",
                    "Eclipse CDT4 - Ninja",
                    "Eclipse CDT4 - Unix Makefiles"

            };

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

        logoDynamic = (ImageView) scene.lookup("#logoDynamic");
        logoStatic = (ImageView) scene.lookup("#logoStatic");
        generatorBox = (ComboBox<String>) scene.lookup("#generatorComboBox");
        optionsTable = (TableView) scene.lookup("#optionsTable");
        sourceField = (TextField) scene.lookup("#sourceField");
        buildField = (TextField) scene.lookup("#buildField");

        generatorBox.setItems(FXCollections.observableArrayList(generators));

        hyperLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://github.com/inanevin/LinaEngine");
            }
        });




        TableColumn<String, TableRowBase> column1 = new TableColumn("Option");
        column1.setCellValueFactory(new PropertyValueFactory<>("Option"));


        TableColumn<String, TableRowBase> column2 = new TableColumn("Value");
        column1.setCellValueFactory(new PropertyValueFactory<>("Value"));

        optionsTable.getItems().add(new TableRow<Boolean>("BUILD_SHARED_LIBS", false));
        optionsTable.getItems().add(new TableRow<String>("CMAKE_CONFIGURATION_TYPES", "Debug;Release;MinSizeRel;RelWithDebInfo;"));
        optionsTable.getItems().add(new TableRow<Boolean>("LINA_BUILD_SANDBOX", true));
        optionsTable.getItems().add(new TableRow<Boolean>("LINA_ENABLE_LOGGING", true));




        primaryStage.setTitle("Lina Engine Build Launcher");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.getScene().getStylesheets().setAll(Main.class.getResource("main.css").toString());
        primaryStage.show();

        logoTimer.scheduleAtFixedRate(new EnableDynamicLogo(logoStatic, logoDynamic, logoTimer), logoChangeRate * 1000, logoChangeRate * 1000);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

