package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressForm {

    private final Stage dialogStage;
    private final ProgressBar progressBar = new ProgressBar();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private Label errorLabel;
    private TextArea textArea;
    private String inputFeed;

    private StringProperty inputFeedProperty = new SimpleStringProperty();
    private StringProperty errorProperty = new SimpleStringProperty();

    public ProgressForm() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Working on process...");



        progressBar.setProgress(-1F);
        progressBar.setPrefSize(256, 24);
        progressIndicator.setProgress(-1F);
        progressIndicator.setPrefSize(24,24);


        final Separator topSeparator = new Separator();
        topSeparator.setOrientation(Orientation.HORIZONTAL);
        final Separator botSeparator = new Separator();
        topSeparator.setOrientation(Orientation.HORIZONTAL);

        final VBox verticalBox = new VBox();
        verticalBox.setSpacing(20);
        verticalBox.setAlignment(Pos.CENTER);

        final HBox horizontalBox = new HBox();
        horizontalBox.setSpacing(15);
        horizontalBox.setAlignment(Pos.CENTER);

        final Separator leftSeperator = new Separator();
        leftSeperator.setOrientation(Orientation.HORIZONTAL);
        final Separator rightSeperator = new Separator();
        rightSeperator.setOrientation(Orientation.HORIZONTAL);


        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#ff0000"));

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.SOMETIMES);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
       // expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);


        horizontalBox.getChildren().addAll(leftSeperator, progressBar, progressIndicator, rightSeperator);

        verticalBox.getChildren().addAll(topSeparator, horizontalBox, errorLabel, botSeparator, expContent);

        Scene scene = new Scene(verticalBox);
        dialogStage.setScene(scene);
    }

    public void activate()  {

        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void AddInputToFeed(String inp)
    {
        inputFeed += inp + System.lineSeparator();
        textArea.setText(inputFeed);
    }

    public void ShowError(String err)
    {
        progressBar.setVisible(false);
        progressIndicator.setVisible(false);
        errorLabel.setText(err);
    }
}