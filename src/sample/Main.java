package sample;

import javafx.application.Application;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;

import javafx.util.Callback;
import javafx.util.Pair;


import java.util.Timer;
import java.util.TimerTask;
import javafx.util.Pair;


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


class TableRowString
{
    TableRowString() {};
    TableRowString(String id, String data) { m_Data = data; m_ID = id;};

    public String getValue() { return m_Data; }
    public String getOption() { return m_ID; }

    private String m_Data;
    private String m_ID;
}

class TableRowBool
{
    TableRowBool() {};
    TableRowBool(String id, Boolean data) { m_Data = data; m_ID = id;};

    public Boolean getValue() { return m_Data; }
    public String getOption() { return m_ID; }

    private Boolean m_Data;
    private String m_ID;
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
    TableView<Pair<String, Object>> optionsTable = new TableView<>();

    private Pair<String, Object> pair(String name, Object value) {
        return new Pair<>(name, value);
    }

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

        ObservableList<Pair<String, Object>> data = FXCollections.observableArrayList(
                pair("CMAKE_CONFIGURATION_TYPES","Debug;Release;MinSizeRel;RelWithDebInfo;"),
                pair("LINA_BUILD_SANDBOX",true),
                pair("LINA_ENABLE_LOGGING",true )
        );

        optionsTable.getItems().setAll(data);

        TableColumn<Pair<String, Object>, String> nameColumn = new TableColumn<>("OPTION");
        nameColumn.setPrefWidth(100);
        TableColumn<Pair<String, Object>, Object> valueColumn = new TableColumn<>("VALUE");
        valueColumn.setSortable(false);
        valueColumn.setPrefWidth(150);

        nameColumn.setCellValueFactory(new PairKeyFactory());
        valueColumn.setCellValueFactory(new PairValueFactory());

        optionsTable.getColumns().setAll(nameColumn, valueColumn);
        valueColumn.setCellFactory(new Callback<TableColumn<Pair<String, Object>, Object>, TableCell<Pair<String, Object>, Object>>() {
            @Override
            public TableCell<Pair<String, Object>, Object> call(TableColumn<Pair<String, Object>, Object> column) {
                return new PairValueCell();
            }
        });
      /*  BuildOption<String> option1 = new BuildOption<>("CMAKE_CONFIGURATION_TYPES","Debug;Release;MinSizeRel;RelWithDebInfo;" );
        BuildOption<Boolean> option2 = new BuildOption<>("LINA_BUILD_SANDBOX",true );
        BuildOption<Boolean> option3 = new BuildOption<>("LINA_ENABLE_LOGGING",true );

        final ObservableList<BuildOption<?>> data = FXCollections.observableArrayList(option1, option2, option3);

        final TableColumn<String, BuildOption<?>> nameColumn = new TableColumn<>( "Option" );
        nameColumn.setCellValueFactory( new PropertyValueFactory<>( "firstName" ));

        TableColumn<String, BuildOption<?>> particularValueCol = new TableColumn<>("Value");
        particularValueCol.setCellValueFactory(new PropertyValueFactory<>("particularValue"));
        particularValueCol.setCel


       optionsTable.setItems(data);
       optionsTable.getColumns().addAll(nameColumn, particularValueCol);*/




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

