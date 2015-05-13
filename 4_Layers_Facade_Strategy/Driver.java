/**
 * Created by LiYecheng on 05/07/15.
 */
public class Driver {

    // Note: just run Driver directly :)

    public static final String RECIPE_ONE = "files/LAB4_ConstantPressure.csv";
    public static final String RECIPE_TWO = "files/LAB4_ConstantCurrent.csv";
    public static final String RECIPE_THREE = "files/LAB4_Ramp.csv";

    public static void main(String[] args) {
        UserInterface userInterface = new UserInterface();

        int pressure = 100;
        int current = 100;
        System.out.println(userInterface.setControlValues(pressure, current));

        int seconds = 10;
        System.out.println(userInterface.runManually(seconds));
        System.out.println(userInterface.runRecipe(RECIPE_ONE));
        System.out.println(userInterface.runRecipe(RECIPE_TWO));
        System.out.println(userInterface.runRecipe(RECIPE_THREE));
    }
}
