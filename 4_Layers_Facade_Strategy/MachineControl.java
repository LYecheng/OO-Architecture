import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.print.attribute.HashPrintRequestAttributeSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by LiYecheng on 05/06/15.
 */
public class MachineControl {
    public static final int CONSTANT_PRESSURE_MODE_RUNTIME = 10;
    public static final int CONSTANT_CURRENT_MODE_RUNTIME = 20;
    public static final int RAMP_MODE_RUNTIME = 30;
    public static final String CONSTANT_PRESSURE_RECIPE_KEY = "ConstantPressure";
    public static final String CONSTANT_CURRENT_RECIPE_KEY = "ConstantCurrent";
    public static final String RAMP_RECIPE_KEY = "Ramp";

    private static final int CONSTANT_PRESSURE_MIN = 100;
    private static final int CONSTANT_CURRENT_MIN = 50;
    private static final int CONSTANT_CURRENT_PRESSURE_MIN = 10;
    private static final int CONSTANT_CURRENT_PRESSURE_MAX = 50;
    private static final int RAMP_PRESSURE_MAX = 100;
    private static final int RAMP_CURRENT_MAX = 200;

    private Hardware hardware;

    public MachineControl(){
        this.hardware = new Hardware();
    }

    public void setControlParams(int pressure, int current){
        this.hardware.setPressure(pressure);
        this.hardware.setCurrent(current);
    }

    public String getControlParams(){
        StringBuilder sb = new StringBuilder();
        sb.append("Pressure: "+this.hardware.getPressure()+" PSI; ");
        sb.append("Current: "+this.hardware.getCurrent()+" amps");
        return sb.toString();
    }

    public String runMachine(int seconds){
        boolean start = this.hardware.turnOn();
        String output;

        if(!start)
            return new String("Machine Failure 1: hardware fail to start");

        boolean finish = this.hardware.runWithSeconds(seconds);
        if(finish){
            output = "Good part.\nCorresponding log file: "+this.hardware.getLogFile();
            this.hardware.turnOff();
        } else {
            output = "Bad part.\nAttempt made to write file "+this.hardware.getLogFile();
            this.hardware.turnOff();
        }
        return output;
    }

    public String runMachine(String recipePath){
        File recipeFile = new File(recipePath);
        BufferedReader br = null;
        try {
            String result;
            String line;
            br = new BufferedReader(new FileReader(recipeFile));
            line = br.readLine();

            String[] fragments = line.split(",");
            String referenceFile = fragments[0];
            String recipe = fragments[1];
            int size = Integer.parseInt(fragments[2]);

            if(recipe.equals(CONSTANT_PRESSURE_RECIPE_KEY))
                result=constantPressureMode(CONSTANT_PRESSURE_MODE_RUNTIME, size);
            else if(recipe.equals(CONSTANT_CURRENT_RECIPE_KEY))
                result=constantCurrentMode(CONSTANT_CURRENT_MODE_RUNTIME, size);
            else if(recipe.equals(RAMP_RECIPE_KEY))
                result=rampMode(RAMP_MODE_RUNTIME, size);
            else
                throw new IllegalArgumentException("Machine Failure 2: invalid recipe "+recipe);

            boolean success=checkLogVsReferenceFile(result,referenceFile);

            if(success)
                return "Good part.\nCorresponding log file: "+result;
            else
                return "Bad part.\nCorresponding log file: "+result;

        } catch (IOException e){
            return "Machine Failure 3: failed to read recipe from file.";
        }
    }

    private String constantPressureMode(int seconds, int size){
        this.hardware.turnOn();

        for(int i=0; i<=seconds; i++){
            HashMap<String, Integer> params = new HashMap<String, Integer>();
            params.put(Hardware.CURRENT_INPUT_KEY, i*2);
            params.put(Hardware.PRESSURE_INPUT_KEY, size+CONSTANT_PRESSURE_MIN);
            params.put(Hardware.SECOND_INPUT_KEY, 1);

            if(!this.hardware.runForSomeTimeWithParams(params,i)){
                this.hardware.turnOff();
                throw new IllegalStateException("Machine Failure 4: hardware failed to operate for one second.");
            }
        }
        return this.hardware.turnOff();
    }

    private String constantCurrentMode(int seconds, int size){
        this.hardware.turnOn();

        for(int i=0; i<=seconds; i++){
            HashMap<String, Integer> params = new HashMap<String, Integer>();
            params.put(Hardware.CURRENT_INPUT_KEY, CONSTANT_CURRENT_MIN+size);
            if((CONSTANT_CURRENT_PRESSURE_MAX-2*i) <= CONSTANT_CURRENT_PRESSURE_MIN)
                params.put(Hardware.PRESSURE_INPUT_KEY, CONSTANT_CURRENT_PRESSURE_MIN);
            else
                params.put(Hardware.PRESSURE_INPUT_KEY, CONSTANT_CURRENT_PRESSURE_MAX-2*i);
            params.put(Hardware.SECOND_INPUT_KEY, 1);

            if(!this.hardware.runForSomeTimeWithParams(params,i)){
                this.hardware.turnOff();
                throw new IllegalStateException("Machine Failure 5: hardware failed to operate for one second.");
            }
        }
        return this.hardware.turnOff();
    }

    private String rampMode(int seconds, int size){
        if(size<50)
            throw new IllegalArgumentException("Machine Failure 6: minimum part size is 51.");

        this.hardware.turnOn();

        for(int i=0; i<=seconds; i++){
            HashMap<String, Integer> params = new HashMap<String, Integer>();
            int pressure = i*10;
            int current = size+i*20;

            if(pressure>=RAMP_PRESSURE_MAX)
                params.put(Hardware.PRESSURE_INPUT_KEY, RAMP_PRESSURE_MAX);
            else
                params.put(Hardware.PRESSURE_INPUT_KEY, pressure);

            if(current>=RAMP_CURRENT_MAX)
                params.put(Hardware.CURRENT_INPUT_KEY, RAMP_CURRENT_MAX);
            else
                params.put(Hardware.CURRENT_INPUT_KEY, current);

            if(!this.hardware.runForSomeTimeWithParams(params,i)){
                this.hardware.turnOff();
                throw new IllegalStateException("Machine Failure 7: hardware failed to operate for one second.");
            }
        }
        return this.hardware.turnOff();
    }

    private boolean checkLogVsReferenceFile(String logfile, String recipe){
        String filePath = "files/"+recipe+".reference.csv";
        File refFile = new File(filePath);
        File logFile = new File(logfile);

        LinkedList<FileHelper> refFiles = new LinkedList<FileHelper>();
        LinkedList<FileHelper> logFiles = new LinkedList<FileHelper>();

        try{
            Scanner in = new Scanner(refFile);
            String line;
            String[] parseLine;
            while(in.hasNextLine()){
                line=in.nextLine();
                parseLine=line.split(",");
                refFiles.addLast(new FileHelper(parseLine[0], parseLine[1], parseLine[2]));
            }
        } catch (IOException e){
            throw new IllegalArgumentException("Failed to open reference file "+filePath);
        }

        try{
            Scanner in = new Scanner(logFile);
            String line;
            String[] parseLine;
            while(in.hasNextLine()){
                line=in.nextLine();
                parseLine=line.split(",");
                logFiles.addLast(new FileHelper(parseLine[0].trim(), parseLine[1].trim(), parseLine[2].trim()));
            }
        } catch (IOException e){
            throw new IllegalArgumentException("Failed to open log file");
        }

        if(refFiles.size()!=logFiles.size())
            return false;
        else {
            for(int i=0; i<refFiles.size(); i++){
                if(logFiles.get(i).current!=refFiles.get(i).current || logFiles.get(i).pressure!=refFiles.get(i).pressure || logFiles.get(i).second!=refFiles.get(i).second)
                    return false;
            }
        }
        return true;
    }

    class FileHelper {
        int pressure;
        int current;
        int second;

        FileHelper(String second, String pressure, String current){
            this.second=Integer.parseInt(second);
            this.pressure=Integer.parseInt(pressure);
            this.current=Integer.parseInt(current);
        }
    }
}

// present a simple facade to the UserInterface layer