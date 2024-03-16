import OtherThings.*;
import LinearAlgebra.*;
import LinearAlgebra.Vector;

import java.io.IOException;
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
    public void AdamsMethod(String pathToFileWithArguments) throws IOException {
        double stepOfMethod = 0.5;
        double halfOfStep = stepOfMethod / 2;
        double rightBorder = this.initialCondition.getX() + 10;
        Stack<Point2D> functionApproximations = new Stack<>();
        functionApproximations.push(this.initialCondition.clonePoint());

        for (double step = functionApproximations.peek().getX(); step < rightBorder; step += stepOfMethod)
        {
            Point2D nextApproximation = new Point2D(step, functionApproximations.peek().getY()
                    + this.rightSideFunction.calculatePoint(new Vector(new double[]{
                            step, functionApproximations.peek().getY()}, 2)).getY());
            functionApproximations.push(nextApproximation);
        }
        this.solutionFunction = new MathFunctionOperations(new ArrayList<>(functionApproximations));
        this.solutionFunction.writePointsInFile("solutionFunctionPointsOutput.txt");
    }
}
