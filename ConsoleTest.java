public class ConsoleTest {
    public static String pathToDifferentialEquationSettingsFile = "txt_files/diffEquationSettingFile.txt";
    public static String pathToDifferentialEquationsSystemSettingsFile = "txt_files/diffEqSystemSettingFile.txt";
    public static void main(String[] args) throws Exception {
        EquationExample.ThermalConductivityProblemExample.solvingExample(pathToDifferentialEquationSettingsFile);
    }
}