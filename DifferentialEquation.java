import LinearAlgebra.Vector;
import OtherThings.*;
import LinearAlgebra.*;

import java.io.*;
import java.util.*;

public class DifferentialEquation {
    protected MathImplicitFunctionOperations rightSideFunction; // F(x, y)
    protected MathFunctionOperations solutionFunction; // y(x)
    protected Point2D initialCondition; // y(x0) = y0 - The Cauchy`s problem
    public DifferentialEquation (MathImplicitFunctionOperations rightSideFunction, Point2D initialCondition)
    {
        if (initialCondition.isNullPoint())
            throw new RuntimeException(PrettyOutput.ERROR +
                    "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
        this.rightSideFunction = rightSideFunction;
        this.initialCondition = initialCondition;
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
    public void setRightSideFunction(MathImplicitFunctionOperations rightSideFunction) {
        this.rightSideFunction = rightSideFunction;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public void setInitialCondition(Point2D initialCondition) {
        this.initialCondition = initialCondition;
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
        double nextApproximationValue = currentApproximation.getY() + (step / 2) *
                (currRightSideFunctionValue + prevRightSideFunctionValue);
        return new Point2D(currentApproximation.getX(), nextApproximationValue);
    } // ВЫЧИСЛЕНИЕ И КОРРЕКТИРОВКА ПРИБЛИЖЕНИЯ ДЛЯ НОВОГО ЗНАЧЕНИЯ РЕШЕНИЯ
    public void implicitAdamsMethod(String pathToFileWithParameters) throws IOException {
        double stepOfMethod = 1;
        double halfOfStep = stepOfMethod / 2;
        double stepForCalculation = stepOfMethod;
        double rightBorder = this.initialCondition.getX() + 5;
        Stack<Point2D> functionApproximations = new Stack<>();
        functionApproximations.push(this.initialCondition.clonePoint());

        for (double step = functionApproximations.peek().getX(); step < rightBorder; step += stepForCalculation)
        {
            Point2D prevApproximation = functionApproximations.peek();
            Point2D currentApproximation = this.explicitAdamsMethod(prevApproximation, stepForCalculation);
            /*for (int i = 0; i < 3; i++) {
                currentApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            } // ИТЕРАЦИОННЫЙ ПРОЦЕСС ВЫЧИСЛЕНИЯ И КОРРЕКТИРОВКИ*/
            Point2D nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepForCalculation);
            functionApproximations.push(nextApproximation);
        }
        this.solutionFunction = new MathFunctionOperations(new ArrayList<>(functionApproximations));
        this.solutionFunction.writePointsInFile("solutionFunctionPointsOutput.txt");
    }
}
