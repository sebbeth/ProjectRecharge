package ProjectRecharge;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

/**
 * Created by seb on 28/3/17.
 */
public class TaskView {


    public Pane pane;
    public ProgressBar progressBar;
    public Label titleLabel;
    public Button rechargeButton;
    public Button editButton;


    public TaskView(String title) {

        pane = new Pane();
        titleLabel = new Label(title);
        progressBar = new ProgressBar();
        rechargeButton = new Button();
        editButton = new Button();


        pane.getChildren().add(titleLabel);
        pane.getChildren().add(progressBar);
        pane.getChildren().add(rechargeButton);
        pane.getChildren().add(editButton);

    }

}
