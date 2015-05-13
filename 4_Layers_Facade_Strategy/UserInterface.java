/**
 * Created by LiYecheng on 05/06/15.
 */
public class UserInterface {

    private MachineControl machineControl;

    public UserInterface() {
        this.machineControl = new MachineControl();
    }

    public String setControlValues(int pressure, int current){
        this.machineControl.setControlParams(pressure, current);
        return this.getControlValues();
    }

    public String getControlValues(){
        return "Current control values: \n"+this.machineControl.getControlParams();
    }

    public String runRecipe(String recipePath){
        return "\nRunning recipe... Recipe file: "+recipePath+"\nResult: "+this.machineControl.runMachine(recipePath);
    }

    public String runManually(int secondsToRun){
        return "\nRunning in manual mode...\n" +"Result: "+this.machineControl.runMachine(secondsToRun);
    }
}

// 1. allow the user to manually set control values in the hardware system
// 2. allow the user to read the control values
// 3. start the system using manually controlled values, let it run for T seconds, and then automatically stop
// 4. allow the user to select a recipe that is used to manufacture a particular part and execute that recipe

// SetControlValue, GetControlValue, ManualRun, and ExecuteRecipe