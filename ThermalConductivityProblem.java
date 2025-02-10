import MathModule.*;
import MathModule.LinearAlgebra.*;
import MathModule.LinearAlgebra.Vector;
import OtherThings.Pair;
import OtherThings.PrettyOutput;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class ThermalConductivityProblem extends NumericalBase {
    protected MathImplicitFunctionOperations thermalConductivityFunction;
    protected Double thermalConductivityCoefficient;
    //protected MathFunction thermalConductivityCoefficient;

    protected MathFunction initialCondition;
    protected HashMap<String, Pair<Double, MathFunction>> neumannsBoundaryCondition = new HashMap<>(){{
        put("leftBoundary", null);
        put("rightBoundary", null);
    }};

    public enum GridMode {
        COMPACT, EXPAND,
    }

    protected ThermalConductivityProblem(String pathToSettingsFile, String pathToInitialConditionFile, String pathToLeftBoarderFile,
                                         String pathToRightBoarderFile, Double thermalConductivityCoefficient, Double leftBorder,
                                         Double rightBorder, Pair<Double, MathFunction> leftBoundaryCondition,
                                         Pair<Double, MathFunction> rightBoundaryCondition, MathFunction initialCondition,
                                         MathFunction leftBoundary, MathFunction rightBoundary)
            throws IOException, ReflectiveOperationException {
        if (pathToSettingsFile != null) {
            super.setFields(pathToSettingsFile);
            this.setFields(pathToSettingsFile);
        } if (pathToInitialConditionFile != null && this.initialCondition == null) {
            throw new IllegalArgumentException(PrettyOutput.ANSI_RED_BACKGROUND +
                    "Метод чтения с файла пока не реализован" + PrettyOutput.RESET);
        } else this.initialCondition = Objects.requireNonNullElseGet(initialCondition, () -> (x) -> new Point2D());
        if (pathToLeftBoarderFile != null && this.neumannsBoundaryCondition.get("leftBoundary") == null) {
            throw new IllegalArgumentException(PrettyOutput.ANSI_RED_BACKGROUND +
                    "Метод чтения с файла пока не реализован" + PrettyOutput.RESET);
        } else if (leftBoundaryCondition != null) {
            this.neumannsBoundaryCondition.put("leftBoundary", leftBoundaryCondition);
        } else if (leftBoundary != null && leftBorder != null) {
            this.neumannsBoundaryCondition.put("leftBoundary", new Pair<>(leftBorder, leftBoundary));
        } else {
            throw new IllegalArgumentException(PrettyOutput.ANSI_RED_BACKGROUND +
                    "Невозможно решить задачу без граничных условий" + PrettyOutput.RESET);
        } if (pathToRightBoarderFile != null && this.neumannsBoundaryCondition.get("rightBoundary") == null) {
            throw new IllegalArgumentException(PrettyOutput.ANSI_RED_BACKGROUND +
                    "Метод чтения с файла пока не реализован" + PrettyOutput.RESET);
        } else if (rightBoundaryCondition != null) {
            this.neumannsBoundaryCondition.put("rightBoundary", rightBoundaryCondition);
        } else if (rightBoundary != null && rightBorder != null) {
            this.neumannsBoundaryCondition.put("rightBoundary", new Pair<>(rightBorder, rightBoundary));
        } else {
            throw new IllegalArgumentException(PrettyOutput.ANSI_RED_BACKGROUND +
                    "Невозможно решить задачу без граничных условий" + PrettyOutput.RESET);
        } if (thermalConductivityCoefficient != null && this.thermalConductivityCoefficient == null) {
            this.thermalConductivityCoefficient = thermalConductivityCoefficient;
        } else this.thermalConductivityCoefficient = 1.0;
    }
    public ThermalConductivityProblem(String pathToSettingsFile, Double thermalConductivityCoefficient,
                                      MathFunction initialCondition, Pair<Double, MathFunction> leftBoundaryCondition,
                                      Pair<Double, MathFunction> rightBoundaryCondition)
            throws IOException, ReflectiveOperationException {
        this(pathToSettingsFile, null, null, null,
                thermalConductivityCoefficient, null, null, leftBoundaryCondition,
                rightBoundaryCondition, initialCondition, null, null);
    }
    public ThermalConductivityProblem(Double thermalConductivityCoefficient, MathFunction initialCondition,
                                      Pair<Double, MathFunction> leftBoundaryCondition,
                                      Pair<Double, MathFunction> rightBoundaryCondition)
            throws IOException, ReflectiveOperationException {
        this(null, thermalConductivityCoefficient, initialCondition,
                leftBoundaryCondition, rightBoundaryCondition);
    }
    public ThermalConductivityProblem(String pathToSettingsFile, Double thermalConductivityCoefficient,
                                      Double leftBorder, Double rightBorder, MathFunction initialCondition,
                                      MathFunction leftBoundary, MathFunction rightBoundary)
            throws IOException, ReflectiveOperationException {
        this(pathToSettingsFile, null, null, null,
                thermalConductivityCoefficient, leftBorder, rightBorder, null,
                null, initialCondition, leftBoundary, rightBoundary);
    }
    public ThermalConductivityProblem(Double thermalConductivityCoefficient, Double leftBorder, Double rightBorder,
                                      MathFunction initialCondition, MathFunction leftBoundary, MathFunction rightBoundary)
            throws IOException, ReflectiveOperationException {
        this(null, thermalConductivityCoefficient, leftBorder, rightBorder,
                initialCondition, leftBoundary, rightBoundary);
    }

    public MathImplicitFunctionOperations getThermalConductivityFunction() {
        return thermalConductivityFunction;
    }
    public void setThermalConductivityFunction(MathImplicitFunctionOperations thermalConductivityFunction) {
        this.thermalConductivityFunction = thermalConductivityFunction;
    }

    public Double getThermalConductivityCoefficient() {
        return thermalConductivityCoefficient;
    }
    public void setThermalConductivityCoefficient(Double thermalConductivityCoefficient) {
        this.thermalConductivityCoefficient = thermalConductivityCoefficient;
    }

    public MathFunction getInitialCondition() {
        return initialCondition;
    }
    public void setInitialCondition(MathFunction initialCondition) {
        this.initialCondition = initialCondition;
    }

    public HashMap<String, Pair<Double, MathFunction>> getNeumannsBoundaryCondition() {
        return neumannsBoundaryCondition;
    }
    public void setNeumannsBoundaryCondition(HashMap<String, Pair<Double, MathFunction>> neumannsBoundaryCondition) {
        this.neumannsBoundaryCondition = neumannsBoundaryCondition;
    }

    public Pair<Double, MathFunction> getLeftBoundaryCondition() {
        return neumannsBoundaryCondition.get("leftBoundary");
    }
    public void setLeftBoundaryCondition(Pair<Double, MathFunction> leftBoundaryCondition) {
        this.neumannsBoundaryCondition.put("leftBoundary", leftBoundaryCondition);
    }

    public MathFunction getLeftBoundary() {
        return neumannsBoundaryCondition.get("leftBoundary").getSecond();
    }
    public void setLeftBoundary(MathFunction leftBoundary) {
        this.neumannsBoundaryCondition.get("leftBoundary").setSecond(leftBoundary);
    }

    public Double getLeftBorder() {
        return neumannsBoundaryCondition.get("leftBoundary").getFirst();
    }
    public void setLeftBorder(Double leftBorder) {
        this.neumannsBoundaryCondition.get("leftBoundary").setFirst(leftBorder);
    }

    public Pair<Double, MathFunction> getRightBoundaryCondition() {
        return neumannsBoundaryCondition.get("rightBoundary");
    }
    public void setRightBoundaryCondition(Pair<Double, MathFunction> rightBoundaryCondition) {
        this.neumannsBoundaryCondition.put("rightBoundary", rightBoundaryCondition);
    }

    public MathFunction getRightBoundary() {
        return neumannsBoundaryCondition.get("rightBoundary").getSecond();
    }
    public void setRightBoundary(MathFunction rightBoundary) {
        this.neumannsBoundaryCondition.get("rightBoundary").setSecond(rightBoundary);
    }

    public Double getRightBorder() {
        return neumannsBoundaryCondition.get("rightBoundary").getFirst();
    }
    public void setRightBorder(Double rightBorder) {
        this.neumannsBoundaryCondition.get("rightBoundary").setFirst(rightBorder);
    }

    public boolean isStability(double delta, double tau) {
        return tau <= (delta * delta) / (2 * thermalConductivityCoefficient);
    }

    private MathFunction conditionsApproximation(double initValue, double delta, MathFunction condition) {
        return (args) -> new Point2D(args.get(0),
                delta * condition.function(new ArrayList<>(List.of(args.get(0)))).getY() + initValue);
    }

    private MathFunction leftConditionApproximation(double first, double second, double delta) {
        return (args) -> new Point2D(args.get(0), -(2 * delta * getLeftBoundary()
                        .function(new ArrayList<>(List.of(args.get(0)))).getY() + second - 4 * first) / 3);
    }

    private MathFunction rightConditionApproximation(double first, double second, double delta) {
        return (args) -> new Point2D(args.get(0), (2 * delta * getRightBoundary()
                .function(new ArrayList<>(List.of(args.get(0)))).getY() - second + 4 * first) / 3);
    }

    public Pair<Double, Double> calculateSteps(int spatialGridDense, int timeGridDense) {
        double delta = (getLeftBorder() + getRightBorder()) / spatialGridDense;
        double tau = (getRightBorder() - getLeftBorder()) / timeGridDense;
        return new Pair<>(delta, tau);
    }

    public Pair<Double, Double> gridAutoChanging(int spatialGridDense, int timeGridDense, GridMode gridMode) {
        double delta = calculateSteps(spatialGridDense, timeGridDense).getFirst();
        double tau = calculateSteps(spatialGridDense, timeGridDense).getSecond();
        double timeLimit = (getRightBorder() - getLeftBorder());
        if (!isStability(delta, tau)) {
            if (gridMode == GridMode.COMPACT) {
                delta = Math.sqrt(2 * tau * thermalConductivityCoefficient);
                if (delta > timeLimit) {
                    delta = timeLimit;
                }
            } else if (gridMode == GridMode.EXPAND) {
                tau = (delta * delta) / (2 * thermalConductivityCoefficient);
                if (tau > timeLimit) {
                    tau = timeLimit;
                }
            }
        }
        return new Pair<>(delta, tau);
    }

    public Matrix fillInitialLayers(int spatialGridDense, int timeGridDense, GridMode gridMode)
            throws ReflectiveOperationException, IOException {
        double delta = gridAutoChanging(spatialGridDense, timeGridDense, gridMode).getFirst();
        double tau = gridAutoChanging(spatialGridDense, timeGridDense, gridMode).getSecond();
        double timeLimit = (getRightBorder() - getLeftBorder());
        Vector timeCoordinates = new Vector();
        Vector spatialCoordinates = new Vector();
        Matrix grid = new Matrix();
        for (double initT = 0; initT <= timeLimit; initT += tau) {
            ArrayList<Double> arrayList = new ArrayList<>();
            if (initT == 0) {
                for (double initX = getLeftBorder(); initX <= getRightBorder(); initX += delta) {
                    arrayList.add(initialCondition.function(new ArrayList<>(List.of(initX))).getY());
                    spatialCoordinates.addElement(initX);
                }
            } else {
                int firstCol = 0;
                int lastCol = grid.getColumns() - 1;
                IntStream.range(0, grid.getColumns()).forEach(index -> arrayList.add(Double.NaN));
                /*setLeftBoundary(conditionsApproximation(getLeftBoundary()
                        .function(new ArrayList<>(List.of(initT))).getY(), delta, getLeftBoundary()));
                setRightBoundary(conditionsApproximation(getRightBoundary()
                        .function(new ArrayList<>(List.of(initT))).getY(), delta, getRightBoundary()));
                arrayList.set(firstCol, getLeftBoundary().function(new ArrayList<>(List.of(initT))).getY());
                arrayList.set(lastCol, getRightBoundary().function(new ArrayList<>(List.of(initT))).getY());*/
            }
            grid.addRow(arrayList);
            timeCoordinates.addElement(initT);
        }
        spatialCoordinates
                .writeVectorInFile("txt_files/spatialCoordinatesOutputFile.txt", "");
        timeCoordinates.toMatrix()
                .writeMatrixInFile("txt_files/timeCoordinatesOutputFile.txt", "");
        return grid;
    }

    public void solveProblem(String pathToFileWithParameters, String pathToOutputFile)
            throws ReflectiveOperationException, IOException {
        if (!this.isParameterOfMethodUpload(List.of("time grid dense", "x grid dense"))) {
            this.updateVariablesList(List.of("time grid dense", "x grid dense"));
            this.setParametersTable(pathToFileWithParameters);
        }
        GridMode gridMode = GridMode.EXPAND;
        int spatialGridDense = this.parametersTable.get("xGridDense".toLowerCase()).intValue();
        int timeGridDense = this.parametersTable.get("TimeGridDense".toLowerCase()).intValue();
        double delta = gridAutoChanging(spatialGridDense, timeGridDense, gridMode).getFirst();
        double tau = gridAutoChanging(spatialGridDense, timeGridDense, gridMode).getSecond();
        System.out.println("delta = " + delta + " tau = " + tau);
        /*setLeftBoundary(conditionsApproximation(getLeftBoundary()
                .function(new ArrayList<>(List.of(getLeftBorder()))).getY(), delta, getLeftBoundary()));
        setRightBoundary(conditionsApproximation(getRightBoundary()
                .function(new ArrayList<>(List.of(getLeftBorder()))).getY(), delta, getRightBoundary()));*/

        Matrix grid = fillInitialLayers(spatialGridDense, timeGridDense, gridMode);
        IntStream.range(0, grid.getRows() - 1).forEach(
                        rowIndex -> {
                            IntStream.range(1, grid.getColumns() - 1).forEach(
                                    colIndex -> {
                                        int lastIndex = colIndex - 1, nextIndex = colIndex + 1;
                                        double coefficient = Math.pow(thermalConductivityCoefficient, 2) * tau;
                                        double last = grid.getElementAt(rowIndex, lastIndex);
                                        double current = grid.getElementAt(rowIndex, colIndex);
                                        double next = grid.getElementAt(rowIndex, nextIndex);
                                        double newApproximationOnNextLayer = current + coefficient *
                                                (next - 2 * current + last) / Math.pow(delta, 2);
                                        grid.setElementAt(newApproximationOnNextLayer, rowIndex + 1, colIndex);
                                    });
                            int left = 0, right = grid.getColumns() - 1;
                            int temporalRowIndex = rowIndex + 1;
                            if (Double.isNaN(grid.getElementAt(temporalRowIndex, left))) {
                                double first = grid.getElementAt(temporalRowIndex, 1);
                                double second = grid.getElementAt(temporalRowIndex, 2);
                                double newApproximationOnLeft = leftConditionApproximation(first, second, delta)
                                        .function(new ArrayList<>(List.of(tau * rowIndex))).getY();
                                grid.setElementAt(newApproximationOnLeft, temporalRowIndex, left);
                            } if (Double.isNaN(grid.getElementAt(temporalRowIndex, right))) {
                                double first = grid.getElementAt(temporalRowIndex, right - 1);
                                double second = grid.getElementAt(temporalRowIndex, right - 2);
                                double newApproximationOnLeft = leftConditionApproximation(first, second, delta)
                                        .function(new ArrayList<>(List.of(tau * rowIndex))).getY();
                                grid.setElementAt(newApproximationOnLeft, temporalRowIndex, right);
                            }
                        });
        grid.writeMatrixInFile(pathToOutputFile, "");
    }
}
