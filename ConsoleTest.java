import LinearAlgebra.*;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.io.*;
import java.util.ArrayList;

public class ConsoleTest {
    public static void main(String[] args) throws IOException, ReflectiveOperationException {
        String pathToParametersFile = "diffEquationSettingFile.txt";
        numericalAdamsMethodsExamples(pathToParametersFile);
        CauchyProblemSolving.System system = new CauchyProblemSolving.System(
                null, null, null);
    }
    public static void numericalAdamsMethodsExamples(String pathToSettingsFile) throws IOException, ReflectiveOperationException {
        double example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example");
        MathImplicitFunction function = getFunctionExample((int)example_number);
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        CauchyProblemSolving diffEquation = new CauchyProblemSolving(
                pathToImplicitFuncPoints, initCondition, function);
        diffEquation.implicitAdamsMethod(pathToSettingsFile);
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
