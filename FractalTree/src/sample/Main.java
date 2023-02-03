package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.Arrays;

public class Main extends Application {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double WIDTH = screenSize.getWidth();
    public static double HEIGHT = screenSize.getHeight();

    public static Stage mainStage;
    public static Pane boardPane;
    public static FlowPane flowPane;
    public static BorderPane mainPane = new BorderPane();
    public static Scene scn1;
    public static boolean stop = true;
    public static double myHeight = HEIGHT/5;
    public static double myWidth = WIDTH/5;
    public static lSys lsys;
    public static int lsysIteration = 5;
    public static String initString = "X";
    public static AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            try {
                refresh();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void start(Stage primaryStage) throws Exception{
        initStage(primaryStage);
        initAllFunction();
        //
        finalInit();
    }
    public static void initStage(Stage in){
        mainStage=in;
    }

    public static void refresh() throws InterruptedException {
        Thread.sleep(1);
        step();
    }
    public static void step(){
        if (!lsys.getStop()){
            double[] start = lsys.getCopyCoord();
            double[] end = lsys.processStep();
            if (lsys.getSkip()){lsys.setSkip(false);return;}
            double[] out = new double[4];
            out[0] = start[0];
            out[1] = start[1];
            out[2] = end[0];
            out[3] = end[1];
            if (!(out[0]==out[2] && out[1]==out[3])){
                draw(out);
            }
        }else{
            lsys.reset();
            {
                lsys.setCoordinate(new double[]{0, -boardPane.getMaxHeight() * 20, 0});
                lsys.lSys(lsysIteration, initString);
            }
            initBoard();
        }

    }
    public static void initAllFunction(){
        initMenu();
        initBoard();
        initScene();
        //
        initLSys();
    }
    public static void initMenu(){
        TextField textInput = new TextField("input");
        textInput.setOnMouseClicked(e->textInput.clear());
        Button stopBtn = new Button("Start");
        Button reset = new Button("Reset");
        reset.setOnMouseClicked(e->{
            lsys.reset();
            {
                lsys.setCoordinate(new double[]{0, -boardPane.getMaxHeight() * 20, 0});
                lsys.lSys(lsysIteration, initString);
            }
            initBoard();
            timer.stop();stop=true;stopBtn.setText("Start");
        });
        Button hardReset = new Button("Hard Reset");
        hardReset.setOnMouseClicked(e->{
            initLSys();
            initBoard();
            timer.stop();stop=true;stopBtn.setText("Start");
        });
        Text textOutput = new Text();
        stopBtn.setOnMouseClicked(e->{
            if (stop){timer.start();stop=false;stopBtn.setText("Stop");}
            else {timer.stop();stop=true;stopBtn.setText("Resume");}
        });
        Button step = new Button("Step");
        step.setOnMouseClicked(e->{
            step();
        });
        Button setString = new Button("Set string");
        setString.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            int length = in.length>6?6:in.length;
            String[] out = new String[length];
            for (int i = 0; i < out.length; i++){
                out[i] = in[i];
            }
            System.out.println(Arrays.toString(out));
            lsys.setString(out);
            textOutput.setText("Set string to " + Arrays.toString(lsys.getActionSet()));
        });
        Button setAngle = new Button("Set angle");
        setAngle.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            int length = in.length>2?2:in.length;
            String[] out = new String[length];
            for (int i = 0; i < out.length; i++){
                out[i] = in[i];
            }
            System.out.println(Arrays.toString(out));
            lsys.setAngle(out);
            textOutput.setText("Set angle to " + Arrays.toString(lsys.getAngle()));
        });
        Button setRule = new Button("Set rule");
        setRule.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            String[][] out = new String[in.length][2];
            for (int i = 0; i < out.length; i++){
                String[] tmp = in[i].split("->");
                out[i][0] = tmp[0];
                out[i][1] = tmp[1];
            }
            for (String[] t : out)System.out.println(Arrays.toString(t));
            lsys.setRule(out);
            textOutput.setText("Set rule to " + Arrays.toString(lsys.getRule()));
        });
        Button setStepLength = new Button("Set step length");
        setStepLength.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            double out = Double.valueOf(in[0]);
            System.out.println(out);
            lsys.setStepLength(out);
            textOutput.setText("Set step length to " + lsys.getStepLength());
        });
        Button setIteration = new Button("Set iteration length");
        setIteration.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            int out = Integer.valueOf(in[0]);
            System.out.println(out);
            lsysIteration = out;
            textOutput.setText("Set iteration length " + lsysIteration);
        });
        Button setInitString = new Button("Set init string");
        setInitString.setOnMouseClicked(e->{
            String[] in = textInput.getText().split(" ");
            String out = in[0];
            System.out.println(out);
            initString = out;
            textOutput.setText("Set init string " + initString);
        });
        Text handbook = new Text();
        handbook.setText("String: Forward, Auxiliary, RotateR, RotateL, Push, Pop. e.g. FARLUO" +
                "\nRule: e.g. F->FARLUO A->FARFAR" +
                "\nAngle: Alpha, Beta. e.g. 24 25" +
                "\nStep Length: e.g. 5" +
                "\nIteration Length: e.g. 5" +
                "\nInit String: e.g. X");
        flowPane = new FlowPane();
        flowPane.setPadding(new Insets(10,10,10,10));
        flowPane.getChildren().addAll(hardReset,reset,stopBtn,step,textInput,setString,setRule,setAngle,setStepLength,setIteration,setInitString,textOutput,handbook);
        mainPane.setTop(flowPane);
    }
    public static void initLSys(){
        lsysIteration = 5;
        initString = "X";
        lsys = new lSys();
        lsys.setStepLength(5).setCoordinate(new double[]{0,-boardPane.getMaxHeight()*20,0});;
        lsys.lSys(lsysIteration,initString);
    }
    public static void initBoard(){
        boardPane = new Pane();
        //boardPane.setStyle("-fx-background-color: brown");
        boardPane.setMaxSize(myHeight/10,myHeight/10);
        boardPane.setPadding(new Insets(10,10,10,10));
        mainPane.setCenter(boardPane);
    }
    public static void initScene(){
        scn1 = new Scene(mainPane,myWidth,myHeight);
    }
    public static void draw(double[] in){
        Line line = new Line(in[0],-in[1],in[2],-in[3]);
        line.setStrokeWidth(1);
        line.setStroke(Color.GREENYELLOW);
        boardPane.getChildren().add(line);
    }
    public static void finalInit(){
        mainStage.setTitle("Hello World");
        mainStage.setScene(scn1);
        mainStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
