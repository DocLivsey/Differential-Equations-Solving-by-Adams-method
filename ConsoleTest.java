import LinearAlgebra.*;

import java.io.*;

public class ConsoleTest {
    public static void main(String[] args) throws IOException {
        String pathToParametersFile = "diffEquationSettingFile.txt";
        numericalAdamsMethodSecondTest(pathToParametersFile);
    }
    public static void numericalAdamsMethodFirstTest(String pathToParametersFile) throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                1 / (Math.sqrt(Math.pow(x.getItem(0), 2))));
        DifferentialEquation diffEquation = new DifferentialEquation(
                pathToImplicitFuncPoints, initCondition, implFunction);
        diffEquation.implicitAdamsMethod(pathToParametersFile);
    }
    public static void numericalAdamsMethodSecondTest(String pathToParametersFile) throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                (x.getItem(1) / x.getItem(0)) + x.getItem(0) * Math.cos(x.getItem(0)));
        DifferentialEquation diffEquation = new DifferentialEquation(
                pathToImplicitFuncPoints, initCondition, implFunction);
        diffEquation.implicitAdamsMethod(pathToParametersFile);
    } // ФИЛИППОВ 141
    public static void numericalAdamsMethodThirdTest(String pathToParametersFile) throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                Math.pow(x.getItem(0), 2) + 1 + (2*x.getItem(0)*x.getItem(1))
                        / (1 + Math.pow(x.getItem(0), 2)));
        DifferentialEquation diffEquation = new DifferentialEquation(
                pathToImplicitFuncPoints, initCondition, implFunction);
        diffEquation.implicitAdamsMethod(pathToParametersFile);
    }
    public static void numericalAdamsMethodFourthTest(String pathToParametersFile) throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                x.getItem(1) + 2 * x.getItem(0) - 3);
        DifferentialEquation diffEquation = new DifferentialEquation(
                pathToImplicitFuncPoints, initCondition, implFunction);
        diffEquation.implicitAdamsMethod(pathToParametersFile);
    } // ФИЛИППОВ 63
    public static void numericalAdamsMethodFifthTest(String pathToParametersFile) throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                1 / (x.getItem(0) + 2 * x.getItem(1)));
        DifferentialEquation diffEquation = new DifferentialEquation(
                pathToImplicitFuncPoints, initCondition, implFunction);
        diffEquation.implicitAdamsMethod(pathToParametersFile);
    }
}
