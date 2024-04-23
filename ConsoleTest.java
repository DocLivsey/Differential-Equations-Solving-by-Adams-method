import MathModule.*;
import MathModule.LinearAlgebra.*;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.util.ArrayList;
import java.util.List;

public class ConsoleTest {
    public static String pathToDifferentialEquationSettingsFile = "diffEquationSettingFile.txt";
    public static String pathToDifferentialEquationsSystemSettingsFile = "diffEqSystemSettingFile.txt";
    public static void main(String[] args) throws Exception {
        numericalSystemExamples(pathToDifferentialEquationsSystemSettingsFile);
    }
    public static void numericalSystemExamples(String pathToSettingsFile) throws Exception {
        String pathToInitConditions = "pointsForImplicitFuncInput.txt";
        ArrayList<MathImplicitFunction> functions = getFunctionsSystem(4);
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToInitConditions, functions.size());
        CauchyProblemSolving_OLD.SystemSolving systemSolving = new CauchyProblemSolving_OLD.SystemSolving(
                functions, initCondition, pathToSettingsFile);
        systemSolving.equationsSolving(pathToSettingsFile, "");
    }

    public static void numericalSystemAdamsMethodExamples(String pathToSettingsFile) throws Exception {
        String pathToInitConditions = "pointsForImplicitFuncInput.txt";
        ArrayList<MathImplicitFunction> functions = getFunctionsSystem(2);
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToInitConditions, functions.size());
        CauchyProblemSolving_OLD.SystemSolving systemSolving = new CauchyProblemSolving_OLD.SystemSolving(
                functions, initCondition, pathToSettingsFile);
        systemSolving.implicitAdamsMethod(pathToSettingsFile,
                "solutionFunctionOfSystemPointsOutput.txt");
    }
    public static ArrayList<MathImplicitFunction> getFunctionsSystem(int number)
    {
        return switch (number) {
            // SYSTEM FIRST EXAMPLE №1141 (0, 1, 0.5)
            case 1 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, x.getElementAt(0) / x.getElementAt(2)),
                    (x) -> new PointMultiD(x, - x.getElementAt(0) / x.getElementAt(1))
                )
            );
            // SYSTEM SECOND EXAMPLE №796 (0, 3, -2, -3)
            case 2 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, x.getElementAt(1) + x.getElementAt(3) - x.getElementAt(2)),
                    (x) -> new PointMultiD(x, x.getElementAt(1) + x.getElementAt(2) - x.getElementAt(3)),
                    (x) -> new PointMultiD(x, 2 * x.getElementAt(1) - x.getElementAt(2))
            ));
            // SYSTEM THIRD EXAMPLE №805 (0, 2, 1, 1)
            case 3 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, 2 * x.getElementAt(1) - x.getElementAt(2) - x.getElementAt(3)),
                    (x) -> new PointMultiD(x, 3 * x.getElementAt(1) - 2 * x.getElementAt(2) - 3 * x.getElementAt(3)),
                    (x) -> new PointMultiD(x, 2 * x.getElementAt(3) - x.getElementAt(1) + x.getElementAt(2))
            ));
            // UNSTABLE SYSTEM EXAMPLE (0, 1, 1)
            case 4 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, Math.log(x.getElementAt(1) +
                            2 * Math.pow(Math.sin(x.getElementAt(0) / 2), 2)) - x.getElementAt(2) / 2),
                    (x) -> new PointMultiD(x, (4 - Math.pow(x.getElementAt(1), 2)) *
                            Math.cos(x.getElementAt(0)) - 2 * x.getElementAt(1) *
                            Math.pow(Math.sin(x.getElementAt(0)), 2) - Math.pow(Math.cos(x.getElementAt(0)), 3))
            ));
            // ONE EQUATION EXAMPLE FOR TEST THE METHOD (0, 0)
            case 5 -> new ArrayList<>(List.of(
                    (x) -> new PointMultiD(x, Math.exp(-Math.sin(x.getElementAt(0))) -
                            x.getElementAt(1) * Math.cos(x.getElementAt(0)))));
            default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                    PrettyOutput.COMMENT + number + PrettyOutput.RESET);
        };
    }

    public static void numericalAdamsMethodsExamples(String pathToSettingsFile) throws Exception {
        double example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example");
        MathImplicitFunction function = getFunctionExample((int)example_number);
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToImplicitFuncPoints, 1);
        CauchyProblemSolving_OLD diffEquation = new CauchyProblemSolving_OLD(
                pathToImplicitFuncPoints, initCondition, function);
        diffEquation.implicitAdamsMethod(pathToSettingsFile, "solutionFunctionPointsOutput.txt");
    }
    public static MathImplicitFunction getFunctionExample(int number)
    {
        return switch (number) {
            case 1 -> (x) -> new PointMultiD(x,
                    Math.exp(-Math.sin(x.getElementAt(0))) - x.getElementAt(1) * Math.cos(x.getElementAt(0)));
            case 2 -> (x) -> new PointMultiD(x,
                    (x.getElementAt(1) / x.getElementAt(0)) + x.getElementAt(0) * Math.cos(x.getElementAt(0)));
            case 3 -> (x) -> new PointMultiD(x,
                    Math.pow(x.getElementAt(0), 2) + 1 + (2 * x.getElementAt(0) * x.getElementAt(1))
                            / (1 + Math.pow(x.getElementAt(0), 2)));
            case 4 -> (x) -> new PointMultiD(x,
                    x.getElementAt(1) + 2 * x.getElementAt(0) - 3);
            case 5 -> (x) -> new PointMultiD(x,
                    1 / (x.getElementAt(0) + 2 * x.getElementAt(1)));
            case 6 -> (x) -> new PointMultiD(x, 2*x.getElementAt(0)*x.getElementAt(1) -
                    Math.pow(x.getElementAt(1), 2) - Math.pow(x.getElementAt(0), 2) + 5);
            default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                    PrettyOutput.COMMENT + number + PrettyOutput.RESET);
        };
    }
}
