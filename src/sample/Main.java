package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.*;
import java.util.*;


class EnableDynamicLogo extends TimerTask
{
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

class DisableDynamicLogo extends TimerTask
{
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

public class Main extends Application
{

    private double sceneXOffset;
    private double sceneYOffset;
    private int logoChangeRate = 5;
    private String sourceDirectoryIdentifier = "#linaenginebuildlauncherentrypointv100";

    private ImageView logoDynamic;
    private ImageView logoStatic;
    private Timer logoTimer;
    private TextField sourceField, buildField;
    private ComboBox<String> generatorBox;
    private TableView<Pair<String, Object>> optionsTable = new TableView<>();
    private Button generateButton;
    private Button generateAndBuildButton;
    private Button locateSourceButton;
    private Button locateBuildButton;

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


        //--------------------------------------------------------------------
        // ROOT SETTINGS
        //--------------------------------------------------------------------

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                sceneXOffset = event.getSceneX();
                sceneYOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - sceneXOffset);
                primaryStage.setY(event.getScreenY() - sceneYOffset);
            }
        });


        Scene scene = new Scene(root, 1024, 576);
        scene.setFill(Color.TRANSPARENT);

        //--------------------------------------------------------------------
        // GET NODES
        //--------------------------------------------------------------------

        Hyperlink githubHyperlink = (Hyperlink) scene.lookup("#githubHyperlink");
        Hyperlink inanevinHyperlink = (Hyperlink) scene.lookup("#inanevinHyperlink");
        logoDynamic = (ImageView) scene.lookup("#logoDynamic");
        logoStatic = (ImageView) scene.lookup("#logoStatic");
        generatorBox = (ComboBox<String>) scene.lookup("#generatorComboBox");
        optionsTable = (TableView) scene.lookup("#optionsTable");
        sourceField = (TextField) scene.lookup("#sourceField");
        buildField = (TextField) scene.lookup("#buildField");
        generateButton = (Button) scene.lookup("#generateButton");
        generateAndBuildButton = (Button) scene.lookup("#generateAndBuildButton");
        locateSourceButton = (Button) scene.lookup("#locateSourceButton");
        locateBuildButton = (Button) scene.lookup("#locateBuildButton");

        //--------------------------------------------------------------------
        // BUTTON CALLBACKS
        //--------------------------------------------------------------------

        generateButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {

                GenerateProjectFiles(false);

            }
        });
        generateAndBuildButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                GenerateProjectFiles(true);
            }
        });
        locateSourceButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = new File(sourceField.getText());
                if (dir.exists())
                {
                    directoryChooser.setInitialDirectory(dir);
                }
                File selectedDirectory = directoryChooser.showDialog(primaryStage);

                if (selectedDirectory != null)
                {
                    sourceField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        locateBuildButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = new File(buildField.getText());
                if (dir.exists())
                {
                    directoryChooser.setInitialDirectory(dir);
                }
                File selectedDirectory = directoryChooser.showDialog(primaryStage);

                if (selectedDirectory != null)
                {
                    buildField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });

        //--------------------------------------------------------------------
        // GENERATOR COMBOBOX SETTINGS
        //--------------------------------------------------------------------
        generatorBox.setItems(FXCollections.observableArrayList(generators));
        generatorBox.setValue(generators[0]);

        //--------------------------------------------------------------------
        // HYPERLINK SETTINGS
        //--------------------------------------------------------------------
        githubHyperlink.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://github.com/inanevin/LinaEngine");
            }
        });

        inanevinHyperlink.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://inanevin.com");
            }
        });

        //--------------------------------------------------------------------
        // OPTION TABLE VIEW SETTINGS
        //--------------------------------------------------------------------

        // Create & set data.
        ObservableList<Pair<String, Object>> data = FXCollections.observableArrayList(
        pair("CMAKE_CONFIGURATION_TYPES", "Debug;Release;MinSizeRel;RelWithDebInfo;"),
                pair("LINA_BUILD_SANDBOX", false),
                pair("LINA_ENABLE_LOGGING",  true)
        );

        optionsTable.getItems().setAll(data);

        // Set columns
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


        //--------------------------------------------------------------------
        // STAGE SETTINGS
        //--------------------------------------------------------------------
        primaryStage.setTitle("Lina Engine Build Launcher");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        //--------------------------------------------------------------------
        // DYNAMIC LOGO TIMER
        //--------------------------------------------------------------------
        logoTimer = new Timer(true);
        logoTimer.scheduleAtFixedRate(new EnableDynamicLogo(logoStatic, logoDynamic, logoTimer), logoChangeRate * 1000, logoChangeRate * 1000);
    }

    void GenerateProjectFiles(boolean buildAsWell) {

        //--------------------------------------------------------------------
        // CHECK IF BUILD FIELD IS EMPTY
        //--------------------------------------------------------------------
        if (buildField.getText().equals(""))
        {
            // Create alert
            Alert alert = new Alert(Alert.AlertType.ERROR);

            // Set alert icon
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("logo_static.png").toString()));

            // Configure alert & show.
            alert.setTitle("Error on  Build Directory!");
            alert.setHeaderText(null);
            alert.setContentText("Please specify a build directory!");
            alert.showAndWait();
            return;
        }

        //--------------------------------------------------------------------
        // CHECK IF SOURCE FIELD IS VALID
        //--------------------------------------------------------------------
        String isSourceDirectoryValid = IsSourceDirectoryValid();
        if (!isSourceDirectoryValid.equals(""))
        {
            // Create alert
            Alert alert = new Alert(Alert.AlertType.ERROR);

            // Set alert icon.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("logo_static.png").toString()));

            // Configura alert & show.
            alert.setTitle("Error on Source Directory!");
            alert.setHeaderText(null);
            alert.setContentText(isSourceDirectoryValid);
            alert.showAndWait();
            return;
        }

        //--------------------------------------------------------------------
        // CHECK IF BUILD DIRECTORY IS VALID
        //--------------------------------------------------------------------
        String isBuildDirectoryValid = IsBuildDirectoryValid();
        if (!isBuildDirectoryValid.equals(""))
        {
            // Create alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            // Set alert icon.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("logo_static.png").toString()));

            // Configure alert.
            alert.setTitle("Warning about Build Directory!");
            alert.setHeaderText(null);
            alert.setContentText(isBuildDirectoryValid);

            // Create alert buttons.
            ButtonType continueButton = new ButtonType("Continue Anyway");
            ButtonType cancelButton = new ButtonType("Cancel");

            // Set buttons & show alert.
            alert.getButtonTypes().setAll(continueButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();

            // Check button click result.
            if (result.get() == cancelButton)
            {
                return;
            }
        }

        //--------------------------------------------------------------------
        // CMAKE COMMAND GENERATION
        //--------------------------------------------------------------------

        // Define strings
        String generatorString = "-G " + "\"" + generatorBox.getValue() + "\"";
        String optionsString = "";

        // Get item list from the options table & an iterator for it.
       /* ObservableList<BuildOption> items = optionsTable.getItems();
        Iterator<BuildOption> it = items.iterator();

        // Iterate all the items.
        while (it.hasNext())
        {

            // Item & strings dec.
            BuildOption row = it.next();
            String typeString = "";
            String valueString = "";

            // Check item type and set type string as well as the value string accordingly.
            if (row.getParticularValue() instanceof String)
            {
                typeString = "STRING";
                valueString = row.getParticularValue().toString();
            }
            else if (row.getParticularValue() instanceof Boolean)
            {
                typeString = "BOOL";
                BuildOption boolRow = (BuildOption) row;
                if ((boolean) boolRow.getParticularValue())
                {
                    valueString = "ON";
                }
                else
                {
                    valueString = "OFF";
                }
            }

            // Increment option string.
            optionsString += "-D" + row.getFirstName() + ":" + typeString + "=" + valueString + " ";

        }

        // Concat the command for generating project files.
        String projectFileGenerateCommand = "cmake " + optionsString + generatorString + " " + sourceField.getText();
        System.out.println(projectFileGenerateCommand);
*/
        if (buildAsWell)
        {

        }
    }

    String IsBuildDirectoryValid() {

        //--------------------------------------------------------------------
        // VALIDATE BUILD DIRECTORY
        //--------------------------------------------------------------------

        // Check FOR CMakeCache.txt file in the build directory.
        File dir = new File(buildField.getText());
        File[] matches = dir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("CMakeCache") && name.endsWith(".txt");
            }
        });

        // If a match exist, return the warning.
        if (matches != null && matches.length != 0)
        {
            return "This directory already contains a CMakeCache.txt meaning that it was used for a CMakeBuild before." +
                    "If Lina Engine was previously built here, build configurations may not work. Consider deleting the CMakeCache.txt or building to a different directory.";
        }

        return "";
    }

    String IsSourceDirectoryValid() {


        //--------------------------------------------------------------------
        // VALIDATE SOURCE DIRECTORY
        //--------------------------------------------------------------------

        // Return if directory is empty.
        if (sourceField.getText().equals("")) return "Please specify Lina Engine source directory!";

        // Check if CMakeLists.txt exists.
        File dir = new File(sourceField.getText());
        File[] matches = dir.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("CMakeLists") && name.endsWith(".txt");
            }
        });

        // Return if there is no CMakeLists.txt
        if (matches == null || matches.length == 0)
        {
            return "CMakeLists.txt could not be found in the source directory. Please make sure you select the root folder of Lina Engine source directory.";
        }

        // Read the first line of CMakeLists.txt and check if matches the token.
        File cmakeFile = matches[0];

        try
        {

            // Get buffer & read.
            BufferedReader reader = new BufferedReader(new FileReader(cmakeFile));
            String text = reader.readLine();

            // Return no errors if matches, return error if not.
            if (text.equals(sourceDirectoryIdentifier))
            {
                // Correct dir.
                return "";
            }
            else
            {
                return "CMakeLists file in the source directory looks corrupted. Please make sure it includes the source directory identifier in the first line.";
            }
        }
        catch (FileNotFoundException e)
        {
            return e.getMessage();
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
    }

    public static void OptionTableItemEdited(int itemIndex, boolean value)
    {
        System.out.println("Index: " + itemIndex + "Value: " + Boolean.toString(value));
    }

    public static void OptionTableItemEdited(int itemIndex, String value)
    {
        System.out.println("Index: " + itemIndex + "Value: " + value);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

