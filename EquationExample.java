import MathModule.LinearAlgebra.Point2D;
import MathModule.LinearAlgebra.PointMultiD;
import MathModule.MathFunction;
import MathModule.MathImplicitFunction;
import OtherThings.Pair;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.util.ArrayList;

public class EquationExample {

    public static void numericalAdamsMethodsExamples(String pathToSettingsFile) throws Exception {
        int example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example").intValue();
        MathImplicitFunction function = getFunctionExample(example_number);
        String pathToImplicitFuncPoints = "txt_files/pointsForImplicitFuncInput.txt";
        InitialCondition initCondition = InitialCondition
                .readInitConditionFromFile(pathToImplicitFuncPoints, 1);
        CauchyProblemSolving_OLD diffEquation = new CauchyProblemSolving_OLD(
                pathToImplicitFuncPoints, initCondition, function);
        diffEquation.implicitAdamsMethod(pathToSettingsFile,
                "txt_files/solutionFunctionPointsOutput.txt");
    }
    public static MathImplicitFunction getFunctionExample(int number) {
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

    public static class BoundaryProblemExamples {
        private static final String pathToLeftBoundary = "txt_files/LeftBoundaryInput.txt";
        private static final String pathToRightBoundary = "txt_files/RightBoundaryInput.txt";

        private static final String output = "txt_files/solutionFunctionPointsOutput.txt";

        public static void boundaryProblemSolvingExample(String pathToSettingsFile) throws Exception {
            int example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example").intValue();
            MathFunction function = getF(example_number);
            MathFunction multiply = getP(example_number);
            Point2D leftBoundary = Point2D.readPointFromFile(pathToLeftBoundary);
            Point2D rightBoundary = Point2D.readPointFromFile(pathToRightBoundary);
            BoundaryValueProblemSolving solving = new BoundaryValueProblemSolving(pathToSettingsFile,
                    leftBoundary, rightBoundary, function, multiply);
            solving.numerovsSolutionMethod(pathToSettingsFile, output);
        }

        public static MathFunction getF(int number) {
            return switch (number) {
                // y(0) = 0 y(1) = -1
                case 1 -> (args) -> new Point2D(args.get(0), args.get(0) * 2);
                // y (0) = y(3.14159265359) = 0
                case 2 -> (args) -> new Point2D(args.get(0), args.get(0) * 2 - Math.PI);
                // y(0) = y(1.57079632679) = 0 / y(32.9867228627) = 0 / y(20.4203522483) = 0
                case 3 -> (args) -> new Point2D(args.get(0), 1);
                default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                        PrettyOutput.COMMENT + number + PrettyOutput.RESET);
            };
        }
        public static MathFunction getP(int number) {
            return switch (number) {
                case 1 -> (agrs) -> new Point2D(agrs.get(0), 1);
                case 2, 3 -> (args) -> new Point2D(args.get(0), -1);
                default -> throw new IllegalStateException("Unexpected value: " + number);
            };
        }
    }

    public static class ThermalConductivityProblemExample {
        private static final String pathToLeftBoundary = "txt_files/LeftBoundaryInput.txt";
        private static final String pathToRightBoundary = "txt_files/RightBoundaryInput.txt";

        private static final String output = "txt_files/txt_files/matrixOutputFile.txt";

        public static void solvingExample(String pathToSettingsFile) throws Exception {
            int example_number = FileParser.SettingsParser.getParametersTable(pathToSettingsFile).get("example").intValue();
            double coefficient = getCoefficient(example_number);
            MathFunction init = getInit(example_number);
            Pair<Double, MathFunction> left = getLeft(example_number);
            Pair<Double, MathFunction> right = getRight(example_number);
            ThermalConductivityProblem problem = new ThermalConductivityProblem(coefficient, init, left, right);
            problem.solveProblem("txt_files/diffEquationSettingFile.txt",
                    "txt_files/matrixOutputFile.txt");
        }

        private static double getCoefficient(int number) {
            return switch (number) {
                case 1, 3 -> 1;
                case 2 -> 0.01;
                default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no coefficient at the given number: " +
                        PrettyOutput.COMMENT + number + PrettyOutput.RESET);
            };
        }
        private static MathFunction getInit(int number) {
            return switch (number) {
                case 1 -> (arg) -> new Point2D(arg.get(0), Math.cos(3 * Math.PI * arg.get(0) / 2));
                case 2 -> (arg) -> new Point2D(arg.get(0), Math.cos(Math.PI * arg.get(0)));
                case 3 -> (arg) -> new Point2D(arg.get(0), arg.get(0));
                default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no function at the given number: " +
                        PrettyOutput.COMMENT + number + PrettyOutput.RESET);
            };
        }
        private static Pair<Double, MathFunction> getLeft(int number) {
            return switch (number) {
                case 1, 2 -> new Pair<>(0.0, (MathFunction) args -> new Point2D(args.get(0), 0));
                case 3 -> new Pair<>(0.0, (MathFunction) args -> new Point2D(args.get(0), Math.cos(2 * args.get(0))));
                default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no condition at the given number: " +
                        PrettyOutput.COMMENT + number + PrettyOutput.RESET);
            };
        }
        private static Pair<Double, MathFunction> getRight(int number) {
            return switch (number) {
                case 1 -> new Pair<>(1.0, (MathFunction) args -> new Point2D(args.get(0),
                        (2 / (3 * Math.PI)) * Math.exp(-Math.pow(3 * Math.PI / 2, 2) * args.get(0))));
                case 2 -> new Pair<>(1.0, (MathFunction) args -> new Point2D(args.get(0), 0));
                case 3 -> new Pair<>(1.0, (MathFunction) args -> new Point2D(args.get(0),
                        -2 * Math.sin(2 * args.get(0))));
                default -> throw new RuntimeException(PrettyOutput.ERROR + "there is no condition at the given number: " +
                        PrettyOutput.COMMENT + number + PrettyOutput.RESET);
            };
        }

    }
}
