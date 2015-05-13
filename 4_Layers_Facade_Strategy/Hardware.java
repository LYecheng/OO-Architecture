import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by LiYecheng on 05/06/15.
 */
public class Hardware {
    public static final int MIN_PRESSURE = 0;
    public static final int MAX_PRESSURE = 200;
    public static final int MIN_CURRENT = 0;
    public static final int MAX_CURRENT = 200;

    private static final int SEC_PER_MILLISEC = 1000;
    public static final String PRESSURE_INPUT_KEY = "air pressure";
    public static final String CURRENT_INPUT_KEY = "current";
    public static final String SECOND_INPUT_KEY = "seconds";
    public static final int DEFAULT_SEC = 1;

    private int pressure_PSI;
    private int current_AMP;
    private boolean online;
    private String curLogFile;

    // constructor, set initial condition
    public Hardware(){
        this.online=false;
        this.pressure_PSI=0;
        this.current_AMP=0;
        this.curLogFile="";
    }

    // return whether the the work can be finished with a set (1 to N) of control parameters
    public boolean runWithParams(HashMap<String, Integer> controlParams){
        if(!this.online){
            System.out.println("Hardware Failure 1: the machine is currently offline.");
            return false;
        }
        if(controlParams.get(SECOND_INPUT_KEY)==null){
            System.out.println("Hardware Failure 2: invalid input of second.");
            return false;
        }

        int seconds = controlParams.get(SECOND_INPUT_KEY);
        for(int i=0; i<seconds; i++){
            if(!this.runForSomeTimeWithParams(controlParams, i))
                return false;
        }
        return true;
    }

    // return whether the work can be finished with the input seconds parameter
    public boolean runWithSeconds(int seconds){
        HashMap<String, Integer> controlParams = new HashMap<String, Integer>();
        controlParams.put(PRESSURE_INPUT_KEY, this.pressure_PSI);
        controlParams.put(CURRENT_INPUT_KEY, this.current_AMP);
        controlParams.put(SECOND_INPUT_KEY, seconds);
        return this.runWithParams(controlParams);
    }

    public boolean runWithDefaultTime(){
        return this.runWithSeconds(DEFAULT_SEC);
    }

    // write log file and return whether the machine can finish the task with the given control parameters and current second
    public boolean runForSomeTimeWithParams(HashMap<String, Integer> controlParams, int currentSec){
        assert controlParams.get(PRESSURE_INPUT_KEY)!=null;
        assert controlParams.get(CURRENT_INPUT_KEY)!=null;
        this.setPressure(controlParams.get(PRESSURE_INPUT_KEY));
        this.setCurrent(controlParams.get(CURRENT_INPUT_KEY));

        try{
            Thread.sleep(1 * SEC_PER_MILLISEC);
        }catch (InterruptedException e){
            System.out.println("Hardware Failure 3: machine failed due to finish work for given time.");
            return false;
        }

        boolean finish=true;
        File logfile = new File(this.curLogFile);
        FileWriter fin = null;
        try{
           fin = new FileWriter(logfile.getAbsolutePath(), true);
        }catch (IOException e){
            System.out.println("Hardware Failure 4: machine failed to find log file");
            finish=false;
        }

        BufferedWriter bw = new BufferedWriter(fin);
        try{
            if(currentSec!=0)
                bw.write("\n");
            bw.write(currentSec+", "+this.pressure_PSI+", "+this.current_AMP);
        } catch (IOException e){
            System.out.println("Hardware Failure 5: machine failed to write to log file");
            finish=false;
        }

        try{
            bw.close();
        } catch (IOException e){
            System.out.println("Hardware Failure 6: machine failed to close log file");
            finish=false;
        }

        return finish;
    }

    // turn on hardware, create log file
    public boolean turnOn(){
        this.online=true;

        int fileCreateCounter=0;
        boolean logFileCreated=false;

        do{
            fileCreateCounter++;

            long timestamp = System.currentTimeMillis();
            String filename = "files/LogFile_"+timestamp+".csv";

            File logFile = new File(filename);
            if(!logFile.exists()){
                try{
                    logFile.createNewFile();
                    logFileCreated=true;
                    this.curLogFile=filename;
                }catch (IOException e){
                    System.out.println("Hardware Failure 7: failed to write LogFile.");
                }
            }
        }while(fileCreateCounter<=3 && logFileCreated==false);

        return logFileCreated;
    }

    // turn off hardware, set parameters to initial value and return name of log file
    public String turnOff(){
        String logFile = this.curLogFile;
        this.online=false;
        this.curLogFile="";
        this.pressure_PSI=MIN_PRESSURE;
        this.current_AMP=MIN_CURRENT;

        return logFile;
    }


    // setter and getter

    public void setPressure(int pressure){
        if(pressure>MAX_PRESSURE)
            this.pressure_PSI=MAX_PRESSURE;
        else if(pressure<MIN_PRESSURE)
            this.pressure_PSI=MIN_PRESSURE;
        else
            this.pressure_PSI=pressure;
    }

    public int getPressure(){
        return this.pressure_PSI;
    }

    public void setCurrent(int current){
        if(current>MAX_CURRENT)
            this.current_AMP=current;
        else if(current<MIN_CURRENT)
            this.current_AMP=current;
        else
            this.current_AMP=current;
    }

    public int getCurrent(){
        return this.current_AMP;
    }

    public String getLogFile(){
        return this.curLogFile;
    }

    public boolean isOnline(){
        return this.online;
    }

}

// present a simple facade to the MachineControl layer