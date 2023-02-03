package sample;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class lSys{
    boolean stop = false;
    boolean skip = false;
    double stepLength = 1;
    String move = "F";
    String end = "X";
    String rR = "+";
    String rL = "-";
    String push = "[";
    String pop = "]";
    String alpha = "25";
    String beta = "25";
    String[] actionSet;
    //
    String[] rule1 = {"F","X"};
    String[] rule2 = {"FF","F+[[X]-X]-F[-FX]+X"};
    //
    Deque<double[]> coordinateStack = new ArrayDeque<>();
    double[] coordinate = {0,0,0};
    int iteration = 0;
    String current = "";
    //
    static double myRandom(double floor, double ceil){
        double out = (Math.random()*(ceil-floor))+floor;
        return out;
    }
    lSys setAngle(String... in){
        double a = Double.valueOf(in[0]);
        double b = Double.valueOf(in[1]);
        if(a<b){
            alpha=String.valueOf(a);beta=String.valueOf(b);
            System.out.println(alpha);
            System.out.println(beta);
        }
        return this;
    }
    //
    lSys setCoordinate(double... in){
        for (int i = 0; i < coordinate.length; i++)coordinate[i]=in[i];
        return this;
    }
    lSys setStepLength(double in){stepLength = in;System.out.println(stepLength);return this;}
    lSys setRule(String[]... in){
        //{1,123123}
        int length = in.length;
        String[] r1 = new String[length];
        String[] r2 = new String[length];
        //String[] as = getActionSet();
        for (int i = 0; i < length; i++){
            //String index1 = in[i][1];
            r1[i] = String.valueOf(in[i][0].charAt(0));
            r2[i] = in[i][1];
            /*
            r2[i] = "";
            for (int j = 0; j < index1.length(); j++){
                r2[i] += as[Integer.valueOf(String.valueOf(index1.charAt(j)))];
            }
            */
        }
        rule1 = r1;
        rule2 = r2;
        return this;
    }
    lSys setString(String... in){
        move    =String.valueOf(in[0].charAt(0));
        end     =String.valueOf(in[1].charAt(0));
        rR      =String.valueOf(in[2].charAt(0));
        rL      =String.valueOf(in[3].charAt(0));
        push    =String.valueOf(in[4].charAt(0));
        pop     =String.valueOf(in[5].charAt(0));
        return this;
    }
    double getStepLength(){return stepLength;}
    String[] getString(){
        return getActionSet();
    }
    String[] getAngle(){return new String[]{alpha,beta};}
    String[] getRule(){
        String[] out = new String[rule1.length];
        for (int i = 0; i < rule1.length; i++){
            out[i] = "";
            out[i] += rule1[i];
            out[i] += "->";
            out[i] += rule2[i];
        }
        return out;
    }
    String[] getActionSet(){return actionSet = new String[]{move,end,rR,rL,push,pop};}
    String lSys(int t, String in){
        if (t<1)return current=in;
        //
        String out = "";

        for (int i = 0; i < in.length(); i++){
            String add = String.valueOf(in.charAt(i));
            for (int j = 0; j < rule1.length; j++){
                if (in.charAt(i)==rule1[j].charAt(0)){
                    add = rule2[j];
                    break;
                }
            }
            out+=add;
        }
        System.out.println(out);
        return lSys(--t,out);
    }
    double[] getCopyCoord(){
        double[] out = new double[coordinate.length];
        for (int i = 0; i < coordinate.length; i++){
            out[i] = coordinate[i];
        }
        return out;
    }
    double[] getCoordinate(){return coordinate;}
    void processWhole(String in){
        for (int i = 0; i < in.length(); i++){
            System.out.println(Arrays.toString(coordinate) + " " +coordinate[2]*180/Math.PI);
            processStep(in.charAt(i));
            System.out.println(Arrays.toString(coordinate) + " " +coordinate[2]*180/Math.PI);
            System.out.println();
        }
    }
    double[] processStep(char in){
        System.out.println(in);
        String[] as = getActionSet();
        if (in==as[0].charAt(0)){
            coordinate[0]-=stepLength*Math.sin(coordinate[2]);
            coordinate[1]+=stepLength*Math.cos(coordinate[2]);
        }
        if (in==as[2].charAt(0)){
            coordinate[2]+=myRandom(Double.valueOf(alpha),Double.valueOf(beta))*Math.PI/180;
        }
        if (in==as[3].charAt(0)){
            coordinate[2]-=myRandom(Double.valueOf(alpha),Double.valueOf(beta))*Math.PI/180;
        }
        if (in==as[4].charAt(0)){
            coordinateStack.push(coordinate);
        }
        if (in==as[5].charAt(0)){
            coordinate=coordinateStack.pop();
        }
        return coordinate;
    }
    void resetItertion(){iteration =0;setStop(false);}
    void resetCoord(){coordinate = new double[]{0,0,0};}
    void reset(){resetItertion();resetCoord();}
    void setStop(boolean in){stop = in;}
    boolean getStop(){return stop;}
    void setSkip(boolean in){skip=in;}
    boolean getSkip(){return skip;}

    double[] processStep(){
        if (iteration >=current.length()){setStop(true);return null;}
        char c = current.charAt(iteration++);
        System.out.println(c);
        //System.out.println(Arrays.toString(coordinate) + " " +coordinate[2]*180/Math.PI);
        String[] as = getActionSet();
        if (c==as[0].charAt(0)){
            coordinate[0]-=stepLength*Math.sin(coordinate[2]);
            coordinate[1]+=stepLength*Math.cos(coordinate[2]);
        }
        if (c==as[2].charAt(0)){
            coordinate[2]+=myRandom(Double.valueOf(alpha),Double.valueOf(beta))*Math.PI/180;
        }
        if (c==as[3].charAt(0)){
            coordinate[2]-=myRandom(Double.valueOf(alpha),Double.valueOf(beta))*Math.PI/180;
        }
        if (c==as[4].charAt(0)){
            coordinateStack.push(getCopyCoord());
        }
        if (c==as[5].charAt(0)){
            coordinate=coordinateStack.pop();
            setSkip(true);
        }
        //System.out.println(Arrays.toString(coordinate) + " " +coordinate[2]*180/Math.PI);
        return coordinate;
    }
}
