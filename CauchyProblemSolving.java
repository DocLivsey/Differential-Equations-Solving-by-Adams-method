import LinearAlgebra.*;
import LinearAlgebra.Vector;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.io.*;
import java.util.*;

public class CauchyProblemSolving extends DifferentialEquation {
    protected final HashMap<String, Double> parametersTable = new HashMap<>();
    protected Point2D initialCondition; // y(x0) = y0 - The Cauchy`s problem
    public CauchyProblemSolving(String pathToParametersFile, String pathToPoints3D, MathImplicitFunction function,
                                ArrayList<PointMultiD> points3D, Point2D initialCondition,
                                MathImplicitFunctionOperations rightSideFunction)
            throws IOException, ReflectiveOperationException {
        super(pathToParametersFile, pathToPoints3D, function, points3D, rightSideFunction);
        super.updateVariablesList(List.of("step of method", "x0", "y0"));
        if (pathToParametersFile != null) {
            this.setInitialCondition(pathToParametersFile);
        } else if (this.initialCondition == null || this.initialCondition.isNanPoint()) {
            if (initialCondition != null && !initialCondition.isNanPoint())
                this.initialCondition = initialCondition;
            else
                throw new RuntimeException(PrettyOutput.ERROR +
                        "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
        }
    }
    public CauchyProblemSolving(String pathToParametersFile, String pathToPoints3D, Point2D initialCondition,
                                MathImplicitFunction function) throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, pathToPoints3D, function, null, initialCondition, null);
    }
    public CauchyProblemSolving(String pathToPoints3D, Point2D initialCondition, MathImplicitFunction function)
            throws IOException, ReflectiveOperationException {
        this(null, pathToPoints3D, initialCondition, function);
    }
    public CauchyProblemSolving(String pathToParametersFile, ArrayList<PointMultiD> points3D, Point2D initialCondition,
                                MathImplicitFunction function) throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, null, function, points3D, initialCondition, null);
    }
    public CauchyProblemSolving(ArrayList<PointMultiD> points3D, Point2D initialCondition, MathImplicitFunction function)
            throws IOException, ReflectiveOperationException {
        this(null, points3D, initialCondition, function);
    }
    public CauchyProblemSolving(String pathToParametersFile, MathImplicitFunctionOperations rightSideFunction,
                                Point2D initialCondition) throws IOException, ReflectiveOperationException {
        this(pathToParametersFile, null, null, null, initialCondition, rightSideFunction);
    }
    public CauchyProblemSolving(Point2D initialCondition, MathImplicitFunctionOperations rightSideFunction)
            throws IOException, ReflectiveOperationException {
        this(null, rightSideFunction, initialCondition);
    }
    public HashMap<String, Double> getParametersTable() {
        return parametersTable;
    }
    public Point2D getInitialCondition() {
        return initialCondition;
    }
    public void setParametersTable(String pathToParametersFile) throws IOException {
        this.parametersTable.putAll(getVariablesTable(pathToParametersFile));
    }
    public void setInitialCondition(Point2D initialCondition) {
        this.initialCondition = initialCondition;
    }
    public void setInitialCondition(String pathToParametersFile) throws IOException {
        HashMap<String, Double> initConditionCoordinatesTable = FileParser.SettingsParser.getParametersTable(pathToParametersFile);
        double x0 = initConditionCoordinatesTable.get("x0");
        double y0 = initConditionCoordinatesTable.get("y0");
        this.initialCondition = new Point2D(x0, y0);
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
    } // РАСЧЕТ НАЧАЛЬНОГО ПРИБЛИЖЕНИЯ ЯВНЫМ МЕТОДОМ АДАМСА ДЛЯ НЕЯВНОГО МЕТОДА АДАМСА
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
    public void implicitAdamsMethod(String pathToFileWithParameters, String pathToSolutionsPointsOutput) throws IOException {
        if (!this.isParameterOfMethodUpload(List.of("step of method", "right border")))
        {
            super.updateVariablesList(List.of("step of method", "right border"));
            this.setParametersTable(pathToFileWithParameters);
        }
        int maxIterationsCount = 1000;
        double stepOfMethod = this.parametersTable.get("StepOfMethod".toLowerCase());
        double rightBorder = this.parametersTable.get("RightBorder".toLowerCase());
        Stack<Point2D> functionApproximations = new Stack<>();
        functionApproximations.push(this.initialCondition.clonePoint());
        for (double step = functionApproximations.peek().getX(); step < rightBorder; step += stepOfMethod)
        {
            Point2D prevApproximation = functionApproximations.peek();
            Point2D currentApproximation = this.explicitAdamsMethod(prevApproximation, stepOfMethod);
            Point2D nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            int iterations = 0;
            do { // УТОЧНЕНИЕ ПРБЛИЖЕНИЯ
                iterations++;
                currentApproximation = nextApproximation.clonePoint();
                nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            } while (Math.abs(nextApproximation.getY() - currentApproximation.getY()) >= super.epsilon
                    && iterations < maxIterationsCount);
            functionApproximations.push(nextApproximation);
        }
        this.solutionFunction = new MathFunctionOperations(new ArrayList<>(functionApproximations));
        if (pathToSolutionsPointsOutput != null)
            this.writeSolutionsPointsInFile(pathToSolutionsPointsOutput);
    }
    public void writeSolutionsPointsInFile(String pathToFile) throws IOException {
        if (this.solutionFunction != null)
            this.solutionFunction.writePointsInFile(pathToFile);
        else
            throw new RuntimeException(
                    PrettyOutput.ERROR + "Решение не найдено либо не было записано" + PrettyOutput.RESET);
    }
    public static class SystemSolving extends EquationsSystem {
        protected final HashMap<String, Double> parametersTable = new HashMap<>();
        protected InitialCondition initialConditions;
        public SystemSolving(ArrayList<DifferentialEquation> differentialEquationsSystem, String pathToParametersFile,
                             InitialCondition initialConditions) throws IOException, ReflectiveOperationException {
            super(differentialEquationsSystem, pathToParametersFile);
            super.updateVariablesList(List.of("step of method", "x0", "y0"));
            if (pathToParametersFile != null) {
                this.setInitialConditions(pathToParametersFile);
            } if (this.initialConditions == null || this.initialConditions.hasNanValues()) {
                if (initialConditions != null && !initialConditions.hasNanValues())
                    this.initialConditions = initialConditions;
                else
                    throw new RuntimeException(PrettyOutput.ERROR +
                            "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
            }
        }
        public SystemSolving(ArrayList<DifferentialEquation> differentialEquationsSystem,
                             InitialCondition initialConditions) throws IOException, ReflectiveOperationException {
            this(differentialEquationsSystem, null, initialConditions);
        }
        public SystemSolving(DifferentialEquation differentialEquation1, DifferentialEquation differentialEquation2,
                             DifferentialEquation differentialEquation3, String pathToParametersFile,
                             InitialCondition initialConditions) throws IOException, ReflectiveOperationException {
            this(new ArrayList<>(List.of(differentialEquation1, differentialEquation2, differentialEquation3)),
                    pathToParametersFile, initialConditions);
        }
        public SystemSolving(ArrayList<MathImplicitFunction> mathImplicitFunctions, InitialCondition initialConditions,
                             String pathToParametersFile) throws IOException, ReflectiveOperationException {
            this(new ArrayList<>(){{
                int dimension = mathImplicitFunctions.size() + 1;
                for (int index = 0; index < dimension - 1; index++) {
                    ArrayList<PointMultiD> points = new ArrayList<>(List.of(new PointMultiD(new Vector(new double[]{
                            initialConditions.getX(), initialConditions.getY(index)}, 3), Double.NaN)));
                    this.add(new DifferentialEquation(pathToParametersFile, null,
                            mathImplicitFunctions.get(index), points, null));
                }
            }}, pathToParametersFile, initialConditions);
        }
        public InitialCondition getInitialConditions() {
            return initialConditions;
        }
        public void setInitialConditions(InitialCondition initialConditions) {
            this.initialConditions = initialConditions;
        }
        public void setInitialConditions(String pathToParametersFile) throws IOException {
            HashMap<String, Double> initConditionCoordinatesTable = FileParser.SettingsParser.getParametersTable(pathToParametersFile);
        }
        public void implicitAdamsMethod(String pathToFileWithParameters, String pathToSolutionsPointsOutput)
                throws IOException, ReflectiveOperationException {
            int index = 0;
            System.out.println(this.initialConditions);
            for (var differentialEquation : this.differentialEquationsSystem)
            {
                Point2D initCondition = new Point2D(this.initialConditions.getX(), this.initialConditions.getY(index++));
                CauchyProblemSolving cauchyProblem = differentialEquation.setTheCauchyProblem(initCondition);
                System.out.println(pathToSolutionsPointsOutput + index);
                cauchyProblem.implicitAdamsMethod(
                        pathToFileWithParameters, pathToSolutionsPointsOutput + index);
            }
        }
    }
}
