import LinearAlgebra.Vector;
import OtherThings.*;
import LinearAlgebra.*;

import java.io.*;
import java.util.*;

public class DifferentialEquation extends MathBase {
    protected final HashMap<String, Double> parametersTable = new HashMap<>();
    protected double epsilon = 1E-5;
    protected MathImplicitFunctionOperations rightSideFunction; // F(x, y)
    protected MathFunctionOperations solutionFunction; // y(x)
    protected Point2D initialCondition; // y(x0) = y0 - The Cauchy`s problem
    public DifferentialEquation(String pathToParametersFile, String pathToPoints3D, MathImplicitFunction function,
                                ArrayList<PointMultiD> points3D, Point2D initialCondition,
                                MathImplicitFunctionOperations rightSideFunction) throws IOException {
        super.updateVariablesList(List.of("step of method", "x0", "y0"));
        if (pathToParametersFile != null) {
            this.setParametersTable(pathToParametersFile);
            this.setEpsilon();
        } else if (initialCondition != null && !initialCondition.isNullPoint() && this.initialCondition == null) {
            this.initialCondition = initialCondition;
        } else
            throw new RuntimeException(PrettyOutput.ERROR +
                    "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
        if (rightSideFunction != null) {
            this.rightSideFunction = rightSideFunction;
        } else {
            this.rightSideFunction = new MathImplicitFunctionOperations(
                    pathToParametersFile, pathToPoints3D, points3D, 2, function);
        }
    }
    public DifferentialEquation(String pathToParametersFile, String pathToPoints3D, Point2D initialCondition,
                                MathImplicitFunction function) throws IOException {
        this(pathToParametersFile, pathToPoints3D, function, null, initialCondition, null);
    }
    public DifferentialEquation(String pathToPoints3D, Point2D initialCondition,
                                MathImplicitFunction function) throws IOException {
        this(null, pathToPoints3D, initialCondition, function);
    }
    public DifferentialEquation(String pathToParametersFile, Point2D initialCondition, ArrayList<PointMultiD> points3D,
                                MathImplicitFunction function) throws IOException {
        this(pathToParametersFile, null, function, points3D, initialCondition, null);
    }
    public DifferentialEquation(Point2D initialCondition, ArrayList<PointMultiD> points3D,
                                MathImplicitFunction function) throws IOException {
        this(null, initialCondition, points3D, function);
    }
    public DifferentialEquation(String pathToParametersFile, MathImplicitFunctionOperations rightSideFunction,
                                Point2D initialCondition) throws IOException {
        this(pathToParametersFile, null, null, null, initialCondition, rightSideFunction);
    }
    public DifferentialEquation(MathImplicitFunctionOperations rightSideFunction,
                                Point2D initialCondition) throws IOException {
        this(null, rightSideFunction, initialCondition);
    }
    public DifferentialEquation(MathImplicitFunctionOperations rightSideFunction,
                                String pathToParametersFile) throws IOException {
        this(pathToParametersFile, rightSideFunction, null);
    }
    public DifferentialEquation (Point2D initialCondition, MathImplicitFunctionOperations rightSideFunction)
    {
        if (initialCondition.isNullPoint())
            throw new RuntimeException(PrettyOutput.ERROR +
                    "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
        this.rightSideFunction = rightSideFunction;
        this.initialCondition = initialCondition;
    }
    public HashMap<String, Double> getParametersTable() {
        return parametersTable;
    }
    public double getEpsilon() {
        return epsilon;
    }
    public MathImplicitFunctionOperations getRightSideFunction() {
        return rightSideFunction;
    }
    public MathFunctionOperations getSolutionFunction() {
        return solutionFunction;
    }
    public Point2D getInitialCondition() {
        return initialCondition;
    }
    public void setParametersTable(String pathToParametersFile) throws IOException {
        this.parametersTable.putAll(getVariablesTable(pathToParametersFile));
    }
    public void setEpsilon() {
        if (this.parametersTable.containsKey("epsilon"))
            if (this.parametersTable.get("epsilon") != null)
                this.epsilon = parametersTable.get("epsilon");
    }
    public void setRightSideFunction(MathImplicitFunctionOperations rightSideFunction) {
        this.rightSideFunction = rightSideFunction;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public void setInitialCondition(Point2D initialCondition) {
        this.initialCondition = initialCondition;
    }
    public boolean isParameterOfMethodUpload(Collection<String> parameters)
    {
        for (String parameter : parameters)
        {
            if (!this.parametersTable.containsKey(parameter))
                return false;
            else
                if (this.parametersTable.get(parameter) == null)
                    return false;
        }
        return true;
    }
    public Point2D explicitAdamsMethod(Point2D prevApproximation, double step)
    {
        Vector prevPointValues = new Vector(new double[]{
                prevApproximation.getX(), prevApproximation.getY()}, 2);
        double prevRightSideFunctionValue = this.rightSideFunction.calculatePoint(prevPointValues).getY();
        return new Point2D(prevApproximation.getX() + step,
                prevApproximation.getX() + step * prevRightSideFunctionValue);
    } // РАСЧЕТ НАЧАЛЬНОГО ПРИБЛИЖЕНИЯ ЯВНЫМ МЕТОДОМ АДАМСА ДЛЯ НОВОГО ЗНАЧЕНИЯ РЕШЕНИЯ ОДУ
    public Point2D evaluateAndCorrect(Point2D prevApproximation, Point2D currentApproximation, double step)
    {
        Vector prevRightSideFunctionArguments = new Vector(new double[]{
                prevApproximation.getX(), prevApproximation.getY()}, 2);
        Vector currRightSideFunctionArguments = new Vector(new double[]{
                currentApproximation.getX(), currentApproximation.getY()}, 2);
        double prevRightSideFunctionValue = this.rightSideFunction.calculatePoint(prevRightSideFunctionArguments).getY();
        double currRightSideFunctionValue = this.rightSideFunction.calculatePoint(currRightSideFunctionArguments).getY();
        double nextApproximationValue = prevApproximation.getY() + (step / 2) *
                (currRightSideFunctionValue + prevRightSideFunctionValue);
        return new Point2D(currentApproximation.getX(), nextApproximationValue);
    } // ВЫЧИСЛЕНИЕ И КОРРЕКТИРОВКА ПРИБЛИЖЕНИЯ ДЛЯ НОВОГО ЗНАЧЕНИЯ РЕШЕНИЯ
    public void implicitAdamsMethod(String pathToFileWithParameters) throws IOException {
        if (!this.isParameterOfMethodUpload(List.of("step of method", "right border")))
        {
            super.updateVariablesList(List.of("step of method", "right border"));
            this.setParametersTable(pathToFileWithParameters);
        }
        double stepOfMethod = this.parametersTable.get("StepOfMethod".toLowerCase());
        double rightBorder = this.parametersTable.get("RightBorder".toLowerCase());
        Stack<Point2D> functionApproximations = new Stack<>();
        functionApproximations.push(this.initialCondition.clonePoint());
        for (double step = functionApproximations.peek().getX(); step < rightBorder; step += stepOfMethod)
        {
            Point2D prevApproximation = functionApproximations.peek();
            Point2D currentApproximation = this.explicitAdamsMethod(prevApproximation, stepOfMethod);
            Point2D nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            do {
                currentApproximation = nextApproximation.clonePoint();
                nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            } while (Math.abs(nextApproximation.getY() - currentApproximation.getY()) >= this.epsilon);
            functionApproximations.push(nextApproximation);
        }
        this.solutionFunction = new MathFunctionOperations(new ArrayList<>(functionApproximations));
        this.solutionFunction.writePointsInFile("solutionFunctionPointsOutput.txt");
    }
}
