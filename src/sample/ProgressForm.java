package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ProgressForm {

    private final Stage dialogStage;
    private final ProgressBar progressBar = new ProgressBar();
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
    private Label infoLabel;
    private TextArea textArea;
    private String inputFeed;
    private Button quitButton;

    private double sceneXOffset;
    private double sceneYOffset;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public Button getQuitButton() {
        return quitButton;
    }


    public String GetInputFeed() { return inputFeed;}
    public TextArea GetTextArea() { return textArea; }

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


        VBox verticalContainer = new VBox();
        verticalContainer.setSpacing(20);
        verticalContainer.setAlignment(Pos.CENTER);

        final HBox horizontalBox = new HBox();
        horizontalBox.setSpacing(15);
        horizontalBox.setAlignment(Pos.CENTER);


        quitButton = new Button();
        quitButton.setPrefWidth(150);
        quitButton.setPrefHeight(25);
        quitButton.setText("OK");
        quitButton.setVisible(false);

        quitButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) {
                dialogStage.close();
            }
        });
        infoLabel = new Label();
        infoLabel.setVisible(false);

        textArea = new TextArea();
        textArea.setEditable(false);
       // textArea.setWrapText(true);
        textArea.setCache(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setPrefWidth(800);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.SOMETIMES);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
       // expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        final HBox textAreaBox = new HBox();
        textAreaBox.setSpacing(15);
        textAreaBox.setAlignment(Pos.CENTER);
        textAreaBox.getChildren().addAll(new Region(), expContent, new Region());


        horizontalBox.getChildren().addAll(new Region(), progressBar, progressIndicator, new Region());

        verticalContainer.getChildren().addAll(horizontalBox, infoLabel, textAreaBox, quitButton, new Region());

        Scene scene = new Scene(verticalContainer);
        dialogStage.setScene(scene);
        dialogStage.initStyle(StageStyle.UNDECORATED);

        verticalContainer.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                sceneXOffset = event.getSceneX();
                sceneYOffset = event.getSceneY();
            }
        });
        verticalContainer.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                dialogStage.setX(event.getScreenX() - sceneXOffset);
                dialogStage.setY(event.getScreenY() - sceneYOffset);
            }
        });

    }

    public void activate()  {

        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    void UpdateInputFeed(String text)
    {
        inputFeed += text + System.lineSeparator();
    }

}