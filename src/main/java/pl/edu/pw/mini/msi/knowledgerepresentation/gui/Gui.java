package pl.edu.pw.mini.msi.knowledgerepresentation.gui;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.mini.msi.knowledgerepresentation.Executor;
import pl.edu.pw.mini.msi.knowledgerepresentation.Interpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class Gui extends Application {
    private static final Logger log = LoggerFactory.getLogger(Gui.class);
    private FileChooser fileChooser;
    private TextArea definitionsTextArea;
    private TextArea queriesTextArea;

    @Override
    public void start(final Stage stage) throws Exception {
        fileChooser = new FileChooser();

        StackPane root = new StackPane();

        HBox hbox = new HBox(30);

        VBox vbox = new VBox(10);
        Label definitionsLabel = new Label("Definitions");
        definitionsTextArea = new TextArea();
        definitionsTextArea.setPrefWidth(100);
        definitionsTextArea.setEditable(false);
        definitionsTextArea.setText(IOUtils.toString(getClass().getResource("/definition_w_01.al")));
        VBox.setVgrow(definitionsTextArea, Priority.ALWAYS);
        HBox.setHgrow(vbox, Priority.ALWAYS);
        vbox.getChildren().addAll(definitionsLabel, definitionsTextArea);

        VBox vbox2 = new VBox(10);
        Label queriesLabel = new Label("Queries");
        queriesTextArea = new TextArea();
        queriesTextArea.setPrefWidth(100);
        queriesTextArea.setText(IOUtils.toString(getClass().getResource("/queries_empty.al")));
        HBox buttonsBox = new HBox(10);
        Button openButton = new Button("Open");
        Button computeButton = new Button("Compute");
        ProgressBar bar = new ProgressBar();
        bar.setVisible(false);
        buttonsBox.getChildren().addAll(openButton, computeButton, bar);
        vbox2.getChildren().addAll(queriesLabel, queriesTextArea, buttonsBox);

        HBox.setHgrow(vbox2, Priority.ALWAYS);

        hbox.setPadding(new Insets(20));

        hbox.getChildren().addAll(vbox, vbox2);
        root.getChildren().add(hbox);
        Scene scene = new Scene(root, 600, 400);

        handleOpenButtonClick(stage, openButton);
        handleComputButtonClick(computeButton);

        stage.setTitle("Knowledge Representation");
        stage.setScene(scene);
        stage.show();
    }

    private void handleOpenButtonClick(Stage stage, Button openButton) {
        openButton.setOnAction(
                e -> {
                    try {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {

                            definitionsTextArea.setText(Files.toString(file, Charset.defaultCharset()));
                        }
                    } catch (IOException e1) {
                        log.warn("Problem with loading file", e1);
                        showDialog(Alert.AlertType.ERROR, "Error", "Cannot open file", e1.getMessage());
                    }
                });
    }

    private void handleComputButtonClick(Button computeButton) {
        computeButton.setOnAction(
                e -> {
                    try {
                        String code = definitionsTextArea.getText() + "\n" + queriesTextArea.getText();
                        //List<Boolean> returns = new Interpreter().eval(code);
                        List<Boolean> returns = new Executor().getResults(code);
                        log.info(Joiner.on(", ").useForNull("null").join(returns));

                        showDialog(Alert.AlertType.INFORMATION, "Info", "Computation complete", Joiner.on("\n").useForNull("null").join(returns));
                    } catch (RuntimeException e2) {
                        log.error(e2.getMessage(), e2);
                        showDialog(Alert.AlertType.ERROR, "Error", "Syntax error", e2.getMessage());
                    }
                });
    }

    private void showDialog(Alert.AlertType type, String title, String headerText, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}