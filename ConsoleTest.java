import LinearAlgebra.*;
import OtherThings.PrettyOutput;

import java.io.*;
import java.util.*;

public class ConsoleTest {
    public static void main(String[] args) throws IOException {
        numericalAdamsMethodTest();
    }
    public static void numericalAdamsMethodTest() throws IOException {
        String pathToImplicitFuncPoints = "pointsForImplicitFuncInput.txt";
        Point2D initCondition = Point2D.readPointFromFile(pathToImplicitFuncPoints);
        MathImplicitFunction implFunction = (x) -> new PointMultiD(x, x.getItem(0)
                - Math.pow(x.getItem(1), 2));
        MathImplicitFunctionOperations implicitFunction = new MathImplicitFunctionOperations(2,
                pathToImplicitFuncPoints, implFunction);
        implicitFunction.printPoints();
        DifferentialEquation diffEquation = new DifferentialEquation(implicitFunction, initCondition);
        diffEquation.AdamsMethod(null);
    }
}
