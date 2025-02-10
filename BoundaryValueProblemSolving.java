import MathModule.LinearAlgebra.*;
import MathModule.*;
import MathModule.LinearAlgebra.AlgebraicSystem.AlgebraicSystem;
import MathModule.LinearAlgebra.AlgebraicSystem.TridiagonalSystemSolver;
import MathModule.LinearAlgebra.Vector;
import OtherThings.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class BoundaryValueProblemSolving extends NumericalBase { // МЕТОД НУМЕРОВА О(h^4)
    // y``(x) - p(x)y(x) = f(x), x э [a, b]
    protected MathFunctionOperations rightSideFunction; // f(x)
    protected MathFunctionOperations multiplierFunction; // p(x)
    protected MathFunctionOperations solutionFunction; // y(x)
    protected Point2D leftBoarder; // y(a) = y1
    protected Point2D rightBoarder; // y(b) = y2

    protected BoundaryValueProblemSolving(String pathToParametersFile, MathFunctionOperations rightSideFunction,
                                          String pathToLeftBoarderFile, MathFunctionOperations multiplierFunction,
                                          String pathToRightBoarderFile, MathFunctionOperations solutionFunction,
                                          Point2D leftBoarder, Point2D rightBoarder, MathFunction rightSideFunc,
                                          MathFunction multiplierFunc, MathFunction solutionFunc)
            throws IOException, ReflectiveOperationException {
        if (pathToParametersFile != null) {
            super.setFields(pathToParametersFile);
            this.setFields(pathToParametersFile);
        } if (pathToLeftBoarderFile != null && this.leftBoarder == null) {
            this.leftBoarder = Point2D.readPointFromFile(pathToLeftBoarderFile);
        } else {
            this.leftBoarder = Objects.requireNonNullElseGet(leftBoarder, Point2D::new);
        } if (pathToRightBoarderFile != null && this.rightBoarder == null) {
            this.rightBoarder = Point2D.readPointFromFile(pathToRightBoarderFile);
        } else {
            this.rightBoarder = Objects.requireNonNullElseGet(rightBoarder, Point2D::new);
        } if (rightSideFunction != null && this.rightSideFunction == null) {
            this.rightSideFunction = rightSideFunction;
        } else {
            this.rightSideFunction = new MathFunctionOperations(pathToParametersFile, null,
                    null, null, rightSideFunc);
        } if (multiplierFunction != null && this.multiplierFunction == null) {
            this.multiplierFunction = multiplierFunction;
        } else {
            this.multiplierFunction = new MathFunctionOperations(pathToParametersFile, null,
                    null, null, multiplierFunc);
        } if (solutionFunction != null && this.solutionFunction == null) {
            this.solutionFunction = solutionFunction;
        } else {
            this.solutionFunction = new MathFunctionOperations(pathToParametersFile, null,
                    null, null, solutionFunc);
        }
    }
    public BoundaryValueProblemSolving(String pathToParametersFile, String pathToLeftBoarderFile, String pathToRightBoarderFile,
                                       MathFunctionOperations rightSideFunction, MathFunctionOperations multiplierFunction)
            throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, rightSideFunction, pathToLeftBoarderFile, multiplierFunction, pathToRightBoarderFile,
                null, null, null, null, null, null);
    }
    public BoundaryValueProblemSolving(String pathToLeftBoarderFile, String pathToRightBoarderFile,
                                       MathFunctionOperations rightSideFunction, MathFunctionOperations multiplierFunction)
            throws IOException, ReflectiveOperationException {
        this((String) null, pathToLeftBoarderFile, pathToRightBoarderFile, rightSideFunction,
                multiplierFunction);
    }
    public BoundaryValueProblemSolving(String pathToParametersFile, String pathToLeftBoarderFile, String pathToRightBoarderFile,
                                       MathFunction rightSideFunc, MathFunction multiplierFunc)
            throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, null, pathToLeftBoarderFile, null, pathToRightBoarderFile,
                null, null, null, rightSideFunc, multiplierFunc, null);
    }
    public BoundaryValueProblemSolving(String pathToLeftBoarderFile, String pathToRightBoarderFile,
                                       MathFunction rightSideFunc, MathFunction multiplierFunc)
            throws IOException, ReflectiveOperationException {
        this(null, pathToLeftBoarderFile, pathToRightBoarderFile,
                rightSideFunc, multiplierFunc);
    }
    public BoundaryValueProblemSolving(String pathToParametersFile, MathFunctionOperations rightSideFunction,
                                       MathFunctionOperations multiplierFunction, Point2D leftBoarder,
                                       Point2D rightBoarder) throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, rightSideFunction, null, multiplierFunction, null,
                null, leftBoarder, rightBoarder, null, null, null);
    }
    public BoundaryValueProblemSolving(MathFunctionOperations rightSideFunction, MathFunctionOperations multiplierFunction,
                                       Point2D leftBoarder, Point2D rightBoarder)
            throws IOException, ReflectiveOperationException {
        this(null, rightSideFunction, multiplierFunction, leftBoarder, rightBoarder);
    }
    public BoundaryValueProblemSolving(String pathToParametersFile, Point2D leftBoarder, Point2D rightBoarder,
                                       MathFunction rightSideFunc, MathFunction multiplierFunc)
            throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, null, null, null, null,
                null, leftBoarder, rightBoarder, rightSideFunc, multiplierFunc, null);
    }
    public BoundaryValueProblemSolving(Point2D leftBoarder, Point2D rightBoarder, MathFunction rightSideFunc,
                                       MathFunction multiplierFunc) throws IOException, ReflectiveOperationException {
        this(null, leftBoarder, rightBoarder, rightSideFunc, multiplierFunc);
    }

    public MathFunctionOperations getRightSideFunction() {
        return rightSideFunction;
    }
    public MathFunctionOperations getMultiplierFunction() {
        return multiplierFunction;
    }
    public MathFunctionOperations getSolutionFunction() {
        return solutionFunction;
    }
    public Point2D getLeftBoarder() {
        return leftBoarder;
    }
    public Point2D getRightBoarder() {
        return rightBoarder;
    }
    public void setRightSideFunction(MathFunctionOperations rightSideFunction) {
        this.rightSideFunction = rightSideFunction;
    }
    public void setMultiplierFunction(MathFunctionOperations multiplierFunction) {
        this.multiplierFunction = multiplierFunction;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public void setLeftBoarder(Point2D leftBoarder) {
        this.leftBoarder = leftBoarder;
    }
    public void setRightBoarder(Point2D rightBoarder) {
        this.rightBoarder = rightBoarder;
    }

    public Pair<Double, List<Double>> getCoefficients(int rowIndex, int stepsCount, double step) {
        List<Double> coefficientLine = new ArrayList<>(Collections.nCopies(stepsCount + 1, 0.0));
        if (rowIndex == 0)
            return new Pair<>(
                    leftBoarder.getY(),
                    new ArrayList<>(){{
                        add(1.0);
                        addAll(Collections.nCopies(stepsCount, 0.0));
                    }}
            );
        if (rowIndex == stepsCount)
            return new Pair<>(
                    rightBoarder.getY(),
                    new ArrayList<>(){{
                        addAll(Collections.nCopies(stepsCount, 0.0));
                        add(1.0);
                    }}
            );
        double currentX = rowIndex * step + leftBoarder.getX();
        double prevX = currentX - step;
        double nextX = currentX + step;
        IntStream.range(0, coefficientLine.size()).forEach(index -> {
            if (index >= coefficientLine.size()) {
                throw new RuntimeException(PrettyOutput.ANSI_RED_BACKGROUND + "Index out of range" + PrettyOutput.RESET);
            } if (index == rowIndex + 1) {
                double prevPx = multiplierFunction.calculatePoint(prevX).getY();
                double coefficient = (12 - Math.pow(step, 2) * prevPx) / (12 * Math.pow(step, 2));
                coefficientLine.set(index, coefficient);
            } else if (index == rowIndex - 1) {
                double nextPx = multiplierFunction.calculatePoint(nextX).getY();
                double coefficient = (12 - Math.pow(step, 2) * nextPx) / (12 * Math.pow(step, 2));
                coefficientLine.set(index, coefficient);
            } else if (index == rowIndex) {
                double currentPx = multiplierFunction.calculatePoint(currentX).getY();
                double coefficient = - ((24 + 10 * currentPx * Math.pow(step, 2)) / (12 * Math.pow(step, 2)));
                coefficientLine.set(index, coefficient);
            }
        });
        double nextRightSide = rightSideFunction.calculatePoint(nextX).getY();
        double currentRightSide = rightSideFunction.calculatePoint(currentX).getY();
        double prevRightSide = rightSideFunction.calculatePoint(prevX).getY();
        return new Pair<>(
                (nextRightSide + 10 * currentRightSide + prevRightSide) / 12,
                coefficientLine
        );
    }
    public TridiagonalSystemSolver getSweepMatrix(int stepsCount, double step) throws ReflectiveOperationException, IOException {
        Collection<Collection<Double>> collectMatrix = new ArrayList<>();
        ArrayList<Double> collectVector = new ArrayList<>();
        IntStream.range(0, stepsCount + 1).forEach(index -> {
            Pair<Double, List<Double>> coefficients = getCoefficients(index, stepsCount, step);
            collectVector.add(coefficients.getFirst());
            collectMatrix.add(coefficients.getSecond());
        });
        AlgebraicSystem system = new Matrix(collectMatrix).makeAlgebraicSystem(new Vector(collectVector));
        return new TridiagonalSystemSolver(system.getCoefficientMatrix(), system.getAdjoinVector());
    }

    public void numerovsSolutionMethod(String pathToFileWithParameters, String pathToOutputFile)
            throws IOException, ReflectiveOperationException {
        if (!this.isParameterOfMethodUpload(List.of("count of steps"))) {
            this.updateVariablesList(List.of("count of steps"));
            this.setParametersTable(pathToFileWithParameters);
        }
        int stepsCount = this.parametersTable.get("CountOfSteps".toLowerCase()).intValue();
        double step = (leftBoarder.getX() + rightBoarder.getX()) / stepsCount;
        TridiagonalSystemSolver sweepSolver = getSweepMatrix(stepsCount, step);
        sweepSolver.solve();
        Vector approximations = sweepSolver.getSolution();
        IntStream.range(0, approximations.getVectorSize()).forEach(index -> {
            double x = leftBoarder.getX() + step * index;
            solutionFunction.addPoint(
                    new Point2D(x, approximations.getElementAt(index))
            );
        });
        solutionFunction.writePointsInFile(pathToOutputFile);
    }
}
