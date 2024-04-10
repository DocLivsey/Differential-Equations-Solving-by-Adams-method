import MathModule.*;
import MathModule.LinearAlgebra.*;
import MathModule.LinearAlgebra.Vector;
import OtherThings.PrettyOutput;
import Parsers.FileParser;

import java.io.*;
import java.util.*;

public class CauchyProblemSolving extends DifferentialEquation {
    protected InitialCondition initialCondition; // y(x0) = y0 - The Cauchy`s problem
    public CauchyProblemSolving(String pathToParametersFile, String pathToPointsMD, ArrayList<PointMultiD> pointsMD,
                                MathImplicitFunctionOperations rightSideFunction, InitialCondition initialCondition,
                                MathImplicitFunction function, boolean isEquationFromSystem, Integer equationsIndex,
                                int dimension) throws Exception {
        super(pathToParametersFile, pathToPointsMD, function, pointsMD, rightSideFunction,
                isEquationFromSystem, equationsIndex, dimension);
        super.updateVariablesList(List.of("step of method", "x0", "y0"));
        if (pathToParametersFile != null) {
            this.setInitialCondition(pathToParametersFile);
        } else if (this.initialCondition == null || this.initialCondition.hasNanValues()) {
            if (initialCondition != null && !initialCondition.hasNanValues())
                this.initialCondition = initialCondition;
            else
                throw new RuntimeException(PrettyOutput.ERROR +
                        "ОШИБКА! Для решения задачи Коши необходимо задать начальные условия" + PrettyOutput.RESET);
        }
    }
    public CauchyProblemSolving(String pathToParametersFile, String pathToPointsMD, InitialCondition initialCondition,
                                boolean isEquationFromSystem, Integer equationsIndex, int dimension,
                                MathImplicitFunction function) throws Exception {
        this(pathToParametersFile, pathToPointsMD, null, null, initialCondition, function,
                isEquationFromSystem, equationsIndex, dimension);
    }
    public CauchyProblemSolving(String pathToParametersFile, String pathToPointsMD, InitialCondition initialCondition,
                                MathImplicitFunction function) throws Exception {
        this(pathToParametersFile, pathToPointsMD, initialCondition, false,
                null, 2, function);
    }
    public CauchyProblemSolving(String pathToPointsMD, InitialCondition initialCondition, MathImplicitFunction function)
            throws Exception {
        this(null, pathToPointsMD, initialCondition, function);
    }
    public CauchyProblemSolving(String pathToParametersFile, ArrayList<PointMultiD> pointsMD, InitialCondition initialCondition,
                                boolean isEquationFromSystem, Integer equationsIndex, int dimension,
                                MathImplicitFunction function) throws Exception {
        this(pathToParametersFile, null, pointsMD, null, initialCondition, function,
                isEquationFromSystem, equationsIndex, dimension);
    }
    public CauchyProblemSolving(String pathToParametersFile, ArrayList<PointMultiD> pointsMD, InitialCondition initialCondition,
                                MathImplicitFunction function) throws Exception {
        this(pathToParametersFile, pointsMD, initialCondition,
                false, null, 2, function);
    }
    public CauchyProblemSolving(ArrayList<PointMultiD> points3D, InitialCondition initialCondition,
                                MathImplicitFunction function) throws Exception {
        this(null, points3D, initialCondition, function);
    }
    public CauchyProblemSolving(String pathToParametersFile, MathImplicitFunctionOperations rightSideFunction,
                                InitialCondition initialCondition, boolean isEquationFromSystem, Integer equationsIndex,
                                int dimension) throws Exception {
        this(pathToParametersFile, null, null, rightSideFunction, initialCondition, null,
                isEquationFromSystem, equationsIndex, dimension);
    }
    public CauchyProblemSolving(String pathToParametersFile, MathImplicitFunctionOperations rightSideFunction,
                                InitialCondition initialCondition) throws Exception {
        this(pathToParametersFile, rightSideFunction, initialCondition,
                false, null, 2);
    }
    public CauchyProblemSolving(InitialCondition initialCondition, MathImplicitFunctionOperations rightSideFunction)
            throws Exception {
        this(null, rightSideFunction, initialCondition);
    }
    public HashMap<String, Double> getParametersTable() {
        return parametersTable;
    }
    public InitialCondition getInitialCondition() {
        return initialCondition;
    }
    public void setParametersTable(String pathToParametersFile) throws IOException {
        this.parametersTable.putAll(getVariablesTable(pathToParametersFile));
    }
    public void setInitialCondition(InitialCondition initialCondition) {
        this.initialCondition = initialCondition;
    }
    public void setInitialCondition(String pathToParametersFile) throws IOException {
        HashMap<String, Double> initConditionCoordinatesTable = FileParser.SettingsParser.getParametersTable(pathToParametersFile);
        double x0 = initConditionCoordinatesTable.get("x0");
        double y0 = initConditionCoordinatesTable.get("y0");
        this.initialCondition = new InitialCondition(x0, new Vector(new double[]{y0}, 1));
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
        functionApproximations.push(new Point2D(
                this.initialCondition.getX(), this.initialCondition.getY(this.equationsIndex)));
        for (double step = functionApproximations.peek().getX(); step < rightBorder; step += stepOfMethod)
        {
            Point2D prevApproximation = functionApproximations.peek();
            Point2D currentApproximation = this.explicitAdamsMethod(prevApproximation, stepOfMethod);
            Point2D nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            int iterations = 0;
            do { // УТОЧНЕНИЕ ПРБЛИЖЕНИЯ
                iterations++;
                currentApproximation = nextApproximation.copy();
                nextApproximation = this.evaluateAndCorrect(prevApproximation, currentApproximation, stepOfMethod);
            } while (Math.abs(nextApproximation.getY() - currentApproximation.getY()) >= super.epsilon
                    && iterations < maxIterationsCount);
            functionApproximations.push(nextApproximation);
        }
        this.solutionFunction = new MathFunctionOperations(new ArrayList<>(functionApproximations));
        if (pathToSolutionsPointsOutput != null && !pathToSolutionsPointsOutput.isEmpty())
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
            differentialEquationsSystem.forEach(equation -> {
                try {
                    equation.setSolutionFunction(new MathFunctionOperations());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        public SystemSolving(ArrayList<DifferentialEquation> differentialEquationsSystem,
                             InitialCondition initialConditions) throws Exception {
            this(differentialEquationsSystem, null, initialConditions);
        }
        public SystemSolving(DifferentialEquation differentialEquation1, DifferentialEquation differentialEquation2,
                             DifferentialEquation differentialEquation3, String pathToParametersFile,
                             InitialCondition initialConditions) throws IOException, ReflectiveOperationException {
            this(new ArrayList<>(List.of(differentialEquation1, differentialEquation2, differentialEquation3)),
                    pathToParametersFile, initialConditions);
        }
        public SystemSolving(ArrayList<MathImplicitFunction> mathImplicitFunctions, InitialCondition initialConditions,
                             String pathToParametersFile) throws Exception {
            this(new ArrayList<>(){{
                int dimension = mathImplicitFunctions.size() + 1;
                for (int index = 0; index < dimension - 1; index++) {
                    ArrayList<PointMultiD> points = new ArrayList<>(List.of(new PointMultiD(new Vector(new double[]{
                            initialConditions.getX(), initialConditions.getY(index)}, dimension),
                            mathImplicitFunctions.get(index).function(initialConditions.convertToVector()).getY())));
                    this.add(new DifferentialEquation(pathToParametersFile, null,
                            mathImplicitFunctions.get(index), points, null,
                            true, index, dimension));
                }
            }}, pathToParametersFile, initialConditions);
        }
        public HashMap<String, Double> getParametersTable() {
            return parametersTable;
        }
        public InitialCondition getInitialConditions() {
            return initialConditions;
        }
        public void setParametersTable(String pathToParametersFile) throws IOException {
            this.parametersTable.putAll(getVariablesTable(pathToParametersFile));
        }
        public void setInitialConditions(InitialCondition initialConditions) {
            this.initialConditions = initialConditions;
        }
        public void setInitialConditions(String pathToParametersFile) throws IOException {
            HashMap<String, Double> initConditionCoordinatesTable = FileParser.SettingsParser.getParametersTable(pathToParametersFile);
        }
        public void implicitAdamsMethod(String pathToFileWithParameters, String pathToSolutionsPointsOutput)
                throws Exception {
            if (!this.isParameterOfMethodUpload(List.of("step of method", "right border")))
            {
                super.updateVariablesList(List.of("step of method", "right border"));
                this.setParametersTable(pathToFileWithParameters);
            }
            double stepOfMethod = this.parametersTable.get("StepOfMethod".toLowerCase());
            double finalRightBorder = this.parametersTable.get("RightBorder".toLowerCase());
            double localRightBorder = this.initialConditions.getX() + stepOfMethod;
            InitialCondition localInitCondition = this.initialConditions.cloneInitCondition();
            while (localRightBorder <= finalRightBorder)
            {
                for (int equationIndex = 0; equationIndex < this.differentialEquationsSystem.size(); equationIndex++)
                {
                    double finalLocalRightBorder = localRightBorder;
                    DifferentialEquation equation = this.differentialEquationsSystem.get(equationIndex);
                    equation.setEquationsIndex(equationIndex); equation.setEquationFromSystem(true);
                    CauchyProblemSolving problemSolving = equation.setTheCauchyProblem(localInitCondition);
                    File equationSettingsFile = FileParser.SettingsParser
                            .writeInSettingsFile("D:\\My Files\\All Scripts\\численные методы" +
                                    "\\Решение ОДУ и систем методом Адамса\\Differential Equations Solving",
                                    "CurrentEquationsSettings",
                                    new HashMap<>(){{
                                        this.put(FileParser.SettingsParser.Settings.PARAMETERS,
                                                "step of method = " + stepOfMethod + ";\n" +
                                                "right border = " + finalLocalRightBorder + ";\n");
                                        this.put(FileParser.SettingsParser.Settings.FIELDS,
                                                "epsilon = " + problemSolving.getEpsilon() + ";\n");
                                    }});
                    problemSolving.implicitAdamsMethod(equationSettingsFile.toString(),
                            "CurrentSolutionFunctionPointsOutput.txt");
                    this.differentialEquationsSystem.get(equationIndex).getSolutionFunction()
                            .addPoints(problemSolving.getSolutionFunction().getPoints());
                    localInitCondition.setY(equationIndex, problemSolving.getSolutionFunction()
                            .getPoint(problemSolving.getSolutionFunction().getPoints().size() - 1).getY());
                }
                localInitCondition.setX(localRightBorder);
                localRightBorder += stepOfMethod;
            }
            for (int equationIndex = 0; equationIndex < this.differentialEquationsSystem.size(); equationIndex++)
            {
                this.differentialEquationsSystem.get(equationIndex).getSolutionFunction()
                        .writePointsInFile("SolutionsFunction" + equationIndex + "PointsOutput.txt");
            }
        }

        public void systemSolving(String pathToFileWithParameters, String pathToSolutionsPointsOutput)
                throws IOException, ReflectiveOperationException {
            if (!this.isParameterOfMethodUpload(List.of("step of method", "right border")))
            {
                super.updateVariablesList(List.of("step of method", "right border"));
                this.setParametersTable(pathToFileWithParameters);
            }
            int iterations = 0;
            double stepOfMethod = this.parametersTable.get("StepOfMethod".toLowerCase());
            double finalRightBorder = this.parametersTable.get("RightBorder".toLowerCase());

        }
    }
}
