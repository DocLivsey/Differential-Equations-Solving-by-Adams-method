import LinearAlgebra.*;

import java.io.*;

public class ConsoleTest {
    public static void main(String[] args) throws IOException {
        numericalAdamsMethodThirdTest();
    }
    public static void numericalAdamsMethodFirstTest() throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                1 / (Math.sqrt(Math.pow(x.getItem(0), 2))));
        MathImplicitFunctionOperations implicitFunction = new MathImplicitFunctionOperations(2,
                pathToImplicitFuncPoints, implFunction);
        implicitFunction.printPoints();
        DifferentialEquation diffEquation = new DifferentialEquation(implicitFunction, initCondition);
        diffEquation.implicitAdamsMethod(null);
    }
    public static void numericalAdamsMethodSecondTest() throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                (x.getItem(1) / x.getItem(0)) + x.getItem(0) * Math.cos(x.getItem(0)));
        MathImplicitFunctionOperations implicitFunction = new MathImplicitFunctionOperations(2,
                pathToImplicitFuncPoints, implFunction);
        implicitFunction.printPoints();
        DifferentialEquation diffEquation = new DifferentialEquation(implicitFunction, initCondition);
        diffEquation.implicitAdamsMethod(null);
    }
    public static void numericalAdamsMethodThirdTest() throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x,
                (Math.pow(x.getItem(1), 2) / x.getItem(0)) + (x.getItem(1) / x.getItem(0)));
        MathImplicitFunctionOperations implicitFunction = new MathImplicitFunctionOperations(2,
                pathToImplicitFuncPoints, implFunction);
        implicitFunction.printPoints();
        DifferentialEquation diffEquation = new DifferentialEquation(implicitFunction, initCondition);
        diffEquation.implicitAdamsMethod(null);
    }
}
