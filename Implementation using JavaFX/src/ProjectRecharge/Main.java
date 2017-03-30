package ProjectRecharge;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends Application {

    /**
     * JavaFX interface controller for ProjectRecharge
     *
     *
     */

    private static Pane root;
    private static List<TaskView> taskViews = new LinkedList();
    private static List<TaskDataModel> taskDataModels = new LinkedList();
    private static final int distanceBetweenRows = 100;
    private static DatabaseManager databaseManager;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Recharge");

        databaseManager = new DatabaseManager("Recharge-Database");


        root = new Pane();



        /*

        Populate the list of data structures.
         */

        taskDataModels = databaseManager.queryDatabaseForAllTasks();




        /*
        Now create the UI objects
         */


        for (int i = 0; i < taskDataModels.size(); i++ ) {

            addATaskToUI(taskDataModels.get(i),i+1);

        }

        /* Bring All views back to the front starting with the last added.
        For some reason this is needed for the button to work, the panes seem to overlap each other, weird :/
         */

        bringSubViewsToFront();





        /******** Generate static UI objects ********/


        // New event button
        Button newEventButton = new Button();
        newEventButton.setText("+");
        newEventButton.setLayoutX(10);
        newEventButton.setLayoutY(10);
        newEventButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                System.out.println("New!");

                TaskDataModel newTask = new TaskDataModel(0,"New Title","m","Notes go here",new Date());

                databaseManager.createNewTask(newTask);

                refreshGUI();


            }
        });

        root.getChildren().add(newEventButton);
        newEventButton.toFront();

        /******************************************/







        primaryStage.setScene(new Scene(root, 350, 250));

        primaryStage.show();


        /*
        Schedule periodic UI updates
         */


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {


             //    taskDataModels.get(0).setCurrentCharge(taskDataModels.get(0).getCurrentCharge() - 0.1);

                refreshGUI();

            }
        };


        timer.schedule(timerTask,2000,2000);
    }




    private static void addATaskToUI(TaskDataModel taskDataModel, int position) {





        /*
        Construct UI
         */

        TaskView taskView = new TaskView(taskDataModel.getTitle());

        taskViews.add(taskView);




        // Set the label position
        taskView.titleLabel.setLayoutX(10);
        taskView.titleLabel.setLayoutY(distanceBetweenRows * position);

        taskView.progressBar.setLayoutX(80);
        taskView.progressBar.setLayoutY(distanceBetweenRows * position);
        taskView.progressBar.setProgress(taskDataModel.calculateCharge());




        // Recharge button
        taskView.rechargeButton.setText("Recharge");
        taskView.rechargeButton.setLayoutX(200);
        taskView.rechargeButton.setLayoutY(distanceBetweenRows * position);
        taskView.rechargeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                taskDataModel.rechargeTask();

                databaseManager.modifyExistingTask(taskDataModel);

                refreshGUI();


            }
        });

        // Edit button
        taskView.editButton.setText("Edit");
        taskView.editButton.setLayoutX(280);
        taskView.editButton.setLayoutY(distanceBetweenRows * position);
        taskView.editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });



        root.getChildren().add(taskView.pane);





    }






    public static void refreshGUI() {

        // Refresh data model




        for (int i = 0; i < taskDataModels.size(); i++) {

            ProgressBar progressBar = taskViews.get(i).progressBar;
            TaskDataModel taskDataModel = taskDataModels.get(i);

            progressBar.setProgress(taskDataModel.calculateCharge());

        }


    }

    private void bringSubViewsToFront() {

          /* Bring All views back to the front starting with the last added.
        For some reason this is needed for the button to work, the panes seem to overlap each other, weird :/
         */
        for (int i = taskViews.size() - 1; i >= 0; i-- ) {

            taskViews.get(i).pane.toFront();
        }

    }


}
