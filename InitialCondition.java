import MathModule.*;
import MathModule.LinearAlgebra.Vector;
import OtherThings.PrettyOutput;

import java.io.*;
import java.util.*;

public class InitialCondition extends NumericalBase {
    protected double x;
    protected Vector y;
    public InitialCondition(String pathToInitConditionInputFile, String stringPoint, Double x, Vector y,
                            Integer pointDimension) throws IOException, ReflectiveOperationException {
        if (pointDimension != null) {
            if (pathToInitConditionInputFile != null && !pathToInitConditionInputFile.isBlank()) {
                this.x = readInitConditionFromFile(pathToInitConditionInputFile, pointDimension).getX();
                this.y = readInitConditionFromFile(pathToInitConditionInputFile, pointDimension).getVectorY();
            } else if (stringPoint != null && !stringPoint.isBlank() && this.y == null) {
                this.y = new Vector();
                this.setInitConditionFromString(stringPoint, pointDimension);
            }
        } if (pointDimension == null && this.y == null) {
            this.x = Objects.requireNonNullElse(x, Double.NaN);
            if (y != null) {
                this.y = y;
            } else this.y = new Vector();
        }
    }
    public InitialCondition(double x, Vector y) throws ReflectiveOperationException, IOException {
        this(null, null, x, y, null);
    }
    public InitialCondition(String pathToFile, int pointDimension) throws ReflectiveOperationException, IOException {
        this(pathToFile, null, null, null, pointDimension);
    }
    public InitialCondition(int pointDimension, String point) throws ReflectiveOperationException, IOException {
        this(null, point, null, null, pointDimension);
    }
    public InitialCondition() throws ReflectiveOperationException, IOException {
        this(null, null, null, null, null);
    }
    public double getX() {
        return x;
    }
    public Vector getVectorY() {
        return y;
    }
    public double getY(int index) {
        return y.getElementAt(index);
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setVectorY(Vector y) {
        this.y = y;
    }
    public void setY(int index, double y) {
        this.y.setElementAt(y, index);
    }
    public void addY(double y) {
        this.y.addElement(y);
    }
    @Override
    public String toString()
    { return this.x + ";" + this.y; }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        InitialCondition condition = (InitialCondition) obj;
        return this.x == condition.getX() && this.y.equals(condition.getVectorY());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
    public InitialCondition copy() throws ReflectiveOperationException, IOException {
        return new InitialCondition(this.getX(), this.getVectorY());
    }
    public void setInitConditionFromString(String initConditionStr, int initConditionDimension)
    {
        String[] splitPoint = initConditionStr.trim().split("[\\s,]+");
        if (splitPoint.length == initConditionDimension + 1)
        {
            for (int i = 0; i < initConditionDimension; i++)
                this.addY(Double.parseDouble(splitPoint[i + 1]));
            this.x = Double.parseDouble(splitPoint[0]);
        }
        else if (splitPoint.length == initConditionDimension)
        {
            for (String string : splitPoint)
                this.addY(Double.parseDouble(string));
            this.x = Double.NaN;
        }
        else
            throw new RuntimeException(PrettyOutput.ERROR +
                    "Ошибка! Неверно введена размерность точки или неверно введена сама точка" + PrettyOutput.RESET);
    }
    public static InitialCondition readInitConditionFromFile(String pathToFile, int dimension)
            throws IOException, ReflectiveOperationException {
        BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
        String initConditionStr = reader.readLine();
        return new InitialCondition(dimension, initConditionStr);
    }
    public boolean hasNanValues() {
        return Double.isNaN(this.getX()) || this.getVectorY().isNanVector();
    }
    public Vector convertToVector() throws ReflectiveOperationException, IOException {
        Vector convert = this.getVectorY().copy();
        convert.addElementAt(this.getX(), 0);
        return convert;
    }
    public void print()
    {
        System.out.println(PrettyOutput.HEADER_OUTPUT + "Начальное значение размерностью: " +
                (this.getVectorY().getVectorSize()) + PrettyOutput.RESET);
        System.out.println("(" + this + ")");
    }
    public Vector getInitConditionAsVector() throws ReflectiveOperationException, IOException {
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        doubleArrayList.add(this.x);
        doubleArrayList.addAll(this.y.getVector());
        return new Vector(doubleArrayList);
    }
}
