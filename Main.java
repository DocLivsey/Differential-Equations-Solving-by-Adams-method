import MathModule.LinearAlgebra.Matrix;
import MathModule.LinearAlgebra.Point2D;
import MathModule.MathFunction;
import OtherThings.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        double coefficient = 1, leftBord = 0, rightBord = 1;
        MathFunction initFunc = (arg) -> new Point2D(arg.get(0), Math.cos(3 * Math.PI * arg.get(0) / 2));
        MathFunction left = (arg) -> new Point2D(arg.get(0), 0);
        MathFunction right = (arg) -> new Point2D(arg.get(0),
                (2 / (3 * Math.PI)) * Math.exp(-Math.pow(3 * Math.PI / 2, 2) * arg.get(0)));
        //MathFunction right = (arg) -> new Point2D(arg.get(0), 0);
        ThermalConductivityProblem problem = new ThermalConductivityProblem(coefficient, leftBord, rightBord,
                initFunc, left, right);
        problem.solveProblem("txt_files/diffEquationSettingFile.txt",
                "txt_files/matrixOutputFile.txt");
    }

    public static void test() throws ReflectiveOperationException, IOException {
        ArrayList<Double> list = new ArrayList<>(List.of(1.0, 0.0, 2.0, 0.0, -1.0));
        Matrix matrix = new Matrix(new ArrayList<>(List.of(list)));
        System.out.println(matrix);
        matrix.addRow(List.of(1.0, 1.0, 1.0, Double.NaN, 0.0));
        System.out.println(matrix);
    }
}
