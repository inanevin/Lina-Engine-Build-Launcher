package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

    private final String errorLogFile = "error_log.txt";
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
    private Stage mainStage;
    private AnchorPane rootNode;
    private ProgressForm currentProgressForm;
    private boolean runBuildCommandAfter;

    private static Pair<String, Object> pair(String name, Object value) {
        return new Pair<>(name, value);
    }

    static ObservableList<Pair<String, Object>> data = FXCollections.observableArrayList(
            //pair("CMAKE_CONFIGURATION_TYPES", "Debug;Release;MinSizeRel;RelWithDebInfo;"),
            pair("LINA_BUILD_SANDBOX", false),
            pair("LINA_ENABLE_LOGGING", true)
    );

    static ArrayList<BuildOption> buildOptionList = new ArrayList<>();

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
        // USERDATA
        //--------------------------------------------------------------------
        Utility.ReadUserData();

        //--------------------------------------------------------------------
        // ROOT SETTINGS
        //--------------------------------------------------------------------

        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        rootNode = (AnchorPane) root;

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
                    Utility.userData.setLastSourceDir(selectedDirectory.getAbsolutePath());
                    Utility.WriteUserData();
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
                    Utility.userData.setLastBuildDir(selectedDirectory.getAbsolutePath());
                    Utility.WriteUserData();
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
        // TEXTFIELD SETTINGS
        //--------------------------------------------------------------------
        if (Utility.userData == null) Utility.userData = new UserData();

        String lastSourceDirectory = Utility.userData.getLastSourceDir();
        String lastBuildDirectory = Utility.userData.getLastBuildDir();

        sourceField.setText(lastSourceDirectory);
        buildField.setText(lastBuildDirectory);

        //--------------------------------------------------------------------
        // OPTION TABLE VIEW SETTINGS
        //--------------------------------------------------------------------


        // Set data & build option list according to the data.
        optionsTable.getItems().setAll(data);

        Iterator<Pair<String, Object>> it = data.iterator();

        while (it.hasNext())
        {
            Pair<String, Object> pair = it.next();
            buildOptionList.add(new BuildOption(pair.getKey(), pair.getValue()));
        }

        // Set columns
        TableColumn<Pair<String, Object>, String> nameColumn = new TableColumn<>("OPTION");
        nameColumn.setPrefWidth(100);
        TableColumn<Pair<String, Object>, Object> valueColumn = new TableColumn<>("VALUE");
        valueColumn.setSortable(false);
        valueColumn.setPrefWidth(150);

        nameColumn.setCellValueFactory(new PairKeyFactory());
        valueColumn.setCellValueFactory(new PairValueFactory());

        optionsTable.getColumns().setAll(nameColumn, valueColumn);
        valueColumn.setCellFactory(new Callback<TableColumn<Pair<String, Object>, Object>, TableCell<Pair<String, Object>, Object>>()
        {
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
            if (isSourceDirectoryValid.equals("-1")) return;

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
        // CHECK IF BUILD FILE IS VALID
        //--------------------------------------------------------------------

        if (!new File(buildField.getText()).exists())
        {
            // Create alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            // Set alert icon.
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("logo_static.png").toString()));

            // Configure alert.
            alert.setTitle("Warning about Build Directory!");
            alert.setHeaderText(null);
            alert.setContentText("Build directory does not exist. Do you want to create it then build?");

            // Create alert buttons.
            ButtonType continueButton = new ButtonType("Create Directory");
            ButtonType cancelButton = new ButtonType("Cancel");

            // Set buttons & show alert.
            alert.getButtonTypes().setAll(continueButton, cancelButton);
            Optional<ButtonType> result = alert.showAndWait();

            // Check button click result.
            if (result.get() == cancelButton)
            {
                return;
            }
            else if (result.get() == continueButton)
            {
                boolean isWindows = System.getProperty("os.name")
                        .toLowerCase().startsWith("windows");

                if (isWindows)
                {
                    ShellCommand("md " + "\"" + buildField.getText() + "\"", "", false);

                }
                else
                {
                    ShellCommand("mkdir " + "\"" + buildField.getText() + "\"", "", false);
                }

            }

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

        Iterator<BuildOption> it = buildOptionList.iterator();

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

        // Set flag
        runBuildCommandAfter = buildAsWell;

        // Concat the command for generating project files.
        String projectFileGenerateCommand = "cmake " + optionsString + generatorString + " " + sourceField.getText();
        ShellCommand(projectFileGenerateCommand, buildField.getText(), true);
    }


    void ShellCommand(String command, String dir, boolean isLongTask)
    {
        //--------------------------------------------------------------------
        // EXECUTE COMMAND
        //--------------------------------------------------------------------

        // Get process
        ProcessBuilder processBuilder = new ProcessBuilder();

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        // Feed command & dir.
        if (isWindows)
        {
            processBuilder.command("cmd.exe", "/c", command);
        }
        else
        {
            processBuilder.command("bash", "-c", command);
        }
        if (!dir.equals(""))
        {
            processBuilder.directory(new File(dir));
        }


        //--------------------------------------------------------------------
        // ERROR FILE
        //--------------------------------------------------------------------
        File errFile = new File(errorLogFile);
        processBuilder.redirectError(errFile);


        if (isLongTask)
        {

            //--------------------------------------------------------------------
            // CREATE PROGRESS FORM FOR THE PROCESS
            //--------------------------------------------------------------------
            ProgressForm progressForm = new ProgressForm();
            progressForm.activate();
            currentProgressForm = progressForm;

            //--------------------------------------------------------------------
            // ASSIGN PROCESS TO TASK & RUN THREAD WITH IT
            //--------------------------------------------------------------------
            Process process = null;
            Task<Void> task = new Task<Void>()
            {
                @Override
                public Void call() throws InterruptedException {

                    ShellCommandTask(processBuilder, progressForm);
                    return null;
                }
            };

            Thread thread = new Thread(task);
            thread.start();
        }
        else
        {
            ShellCommandTask(processBuilder);
        }
    }

    String IsBuildDirectoryValid() {

        //--------------------------------------------------------------------
        // VALIDATE BUILD DIRECTORY
        //--------------------------------------------------------------------

        // Check for CMakeCache.txt file in the build directory.
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
            ShowExceptionDialog(e, "File not found exception!");
            return "-1";
        }
        catch (IOException e)
        {
            ShowExceptionDialog(e, "IO Exception!");
            return "-1";
        }


    }

    void ShowExceptionDialog(Exception e, String contentMsg)
    {
        //--------------------------------------------------------------------
        // Create an alert with a text area in it to show an exception.
        //--------------------------------------------------------------------

        // Create alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Occured!");
        alert.setHeaderText(null);
        alert.setContentText(contentMsg);

        // Get exception data.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        // Create label & text area.
        Label label = new Label("The exception stacktrace was:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        // Create grid pane & assign.
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Show alert.
        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }

    void ShowExceptionDialog(String err, String contentMsg)
    {
        //--------------------------------------------------------------------
        // Create an alert with a text area in it to show an exception.
        //--------------------------------------------------------------------

        // Create alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Occured!");
        alert.setHeaderText(null);
        alert.setContentText(contentMsg);

        // Create label & text area.
        Label label = new Label("The error  was:");
        TextArea textArea = new TextArea(err);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        // Create grid pane & assign.
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Show alert.
        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }

    public static void OptionTableItemEdited(int itemIndex, boolean value)
    {
        buildOptionList.get(itemIndex).setParticularValue(value);
    }

    public static void OptionTableItemEdited(int itemIndex, String value)
    {
        buildOptionList.get(itemIndex).setParticularValue(value);
    }


    void ShellCommandTask(ProcessBuilder processBuilder)
    {
        //----------------------------------------------------------------------------
        // Runs a process based on the builder. This is used for mkdir & md commands.
        //----------------------------------------------------------------------------

        try
        {
            // Start process.
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // Process error
            if (exitCode == 1)
            {
                // Get buffer & read.
                BufferedReader errReader = new BufferedReader(new FileReader(errorLogFile));

                // Show error as alert.
                String errLine = "";
                String allLines = "";

                while ((errLine = errReader.readLine()) != null)
                {
                    allLines += errLine + System.lineSeparator();
                }

                ShowExceptionDialog(allLines, "Error while generating project files!");
            }
        }
        catch (IOException e)
        {
            ShowExceptionDialog(e, "IO Exception!");
        }
        catch (InterruptedException e)
        {
            ShowExceptionDialog(e, "Interrupted Exception!");
        }
    }

    void ShellCommandTask(ProcessBuilder processBuilder, ProgressForm progressForm)
    {
        //-----------------------------------------------------------------------------------
        // Runs a process based on the builder. This is used in project generation & build commands.
        //-----------------------------------------------------------------------------------
        try
        {
            // Start process
            Process process = processBuilder.start();

            // Redirect streams to gobblers.
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), this, progressForm);
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), this, progressForm);

            // Start gobblers.
            outputGobbler.start();
            errorGobbler.start();

            // Check error.
            int exitCode = process.waitFor();
            if (exitCode != 0)
            {
                File errFile = new File(errorLogFile);

                // Get buffer & read.
                BufferedReader errReader = new BufferedReader(new FileReader(errFile));
                String errLine = "";

                // Update feed if error is present.
                while ((errLine = errReader.readLine()) != null)
                {
                    progressForm.UpdateInputFeed(errLine);
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run() {
                            UpdateTextArea();
                        }
                    });
                }

                // Show error label
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() {
                        ShowErrorOnProgress();
                    }
                });

            }
            else
            {
                // Show success label.
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run() {
                        ShowSuccessOnProgress();
                        ProgressCommandSuccessful();
                    }
                });
            }
        }
        catch (IOException e)
        {
            ShowExceptionDialog(e, "IO Exception!");
        }
        catch (InterruptedException e)
        {
            ShowExceptionDialog(e, "Interrupted Exception!");
        }
    }

    void UpdateTextArea()
    {
        currentProgressForm.GetTextArea().setText(currentProgressForm.GetInputFeed());
        currentProgressForm.GetTextArea().setScrollTop(Double.MAX_VALUE);
    }

    void ShowErrorOnProgress()
    {
        currentProgressForm.getInfoLabel().setVisible(true);
        currentProgressForm.getProgressBar().setVisible(false);
        currentProgressForm.getProgressIndicator().setVisible(false);
        currentProgressForm.getQuitButton().setVisible(true);
        currentProgressForm.getInfoLabel().setText("Error on Progress");
        currentProgressForm.getInfoLabel().setTextFill(Color.web("#ff0000"));
    }

    void ShowSuccessOnProgress()
    {
        currentProgressForm.getInfoLabel().setVisible(true);
        currentProgressForm.getProgressBar().setVisible(false);
        currentProgressForm.getProgressIndicator().setVisible(false);
        currentProgressForm.getQuitButton().setVisible(true);
        currentProgressForm.getInfoLabel().setText("Success!");
        currentProgressForm.getInfoLabel().setTextFill(Color.web("#00ff00"));
    }

    void CloseCurrentProgressForm()
    {
        currentProgressForm.getDialogStage().close();
    }
    void ProgressCommandSuccessful()
    {
        //----------------------------------------------------------------------------
        // Generate & run build command if requested.
        //----------------------------------------------------------------------------
        if(runBuildCommandAfter)
        {
            runBuildCommandAfter = false;
            CloseCurrentProgressForm();
            // Concat the command for generating project files.
            String projectFileGenerateCommand = "cmake --build .";
            ShellCommand(projectFileGenerateCommand, buildField.getText(), true);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}

