import MathModule.*;
import MathModule.LinearAlgebra.*;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConsoleTest {
    public static String pathToDifferentialEquationSettingsFile = "diffEquationSettingFile.txt";
    public static String pathToDifferentialEquationsSystemSettingsFile = "diffEqSystemSettingFile.txt";
    public static void main(String[] args) throws Exception {
        numericalSystemAdamsMethodExamples(pathToDifferentialEquationsSystemSettingsFile);
    }
    public static void numericalSystemAdamsMethodExamples(String pathToSettingsFile)
            throws Exception {
        String pathToInitConditions = "pointsForImplicitFuncInput.txt";
        ArrayList<MathImplicitFunction> functions = getFunctionsSystem(1);
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToInitConditions, functions.size());
        CauchyProblemSolving.SystemSolving systemSolving = new CauchyProblemSolving.SystemSolving(
                functions, initCondition, pathToSettingsFile);
        systemSolving.implicitAdamsMethod(pathToSettingsFile,
                "solutionFunctionOfSystemPointsOutput.txt");
    }
    public static void numericalAdamsMethodsExamples(String pathToSettingsFile) throws Exception {
        double example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example");
        MathImplicitFunction function = getFunctionExample((int)example_number);
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToImplicitFuncPoints, 1);
        CauchyProblemSolving diffEquation = new CauchyProblemSolving(
                pathToImplicitFuncPoints, initCondition, function);
        diffEquation.implicitAdamsMethod(pathToSettingsFile, "solutionFunctionPointsOutput.txt");
    }
    public static ArrayList<MathImplicitFunction> getFunctionsSystem(int number)
    {
        return switch (number) {
            case 1 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, x.getItem(0) / x.getItem(2)),
                    (x) -> new PointMultiD(x, - x.getItem(0) / x.getItem(1))));
            case 2 -> new ArrayList<>(List.of((x) -> new PointMultiD(), (x) -> new PointMultiD()));
            default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                    PrettyOutput.COMMENT + number + PrettyOutput.RESET);
        };
    }
    public static MathImplicitFunction getFunctionExample(int number)
    {
        return switch (number) {
            case 1 -> (x) -> new PointMultiD(x,
                    Math.exp(-Math.sin(x.getItem(0))) - x.getItem(1) * Math.cos(x.getItem(0)));
            case 2 -> (x) -> new PointMultiD(x,
                    (x.getItem(1) / x.getItem(0)) + x.getItem(0) * Math.cos(x.getItem(0)));
            case 3 -> (x) -> new PointMultiD(x,
                    Math.pow(x.getItem(0), 2) + 1 + (2 * x.getItem(0) * x.getItem(1))
                            / (1 + Math.pow(x.getItem(0), 2)));
            case 4 -> (x) -> new PointMultiD(x,
                    x.getItem(1) + 2 * x.getItem(0) - 3);
            case 5 -> (x) -> new PointMultiD(x,
                    1 / (x.getItem(0) + 2 * x.getItem(1)));
            case 6 -> (x) -> new PointMultiD(x, 2*x.getItem(0)*x.getItem(1) -
                    Math.pow(x.getItem(1), 2) - Math.pow(x.getItem(0), 2) + 5);
            default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                    PrettyOutput.COMMENT + number + PrettyOutput.RESET);
        };
    }
}
