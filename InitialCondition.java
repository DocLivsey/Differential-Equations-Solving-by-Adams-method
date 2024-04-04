import LinearAlgebra.*;
import OtherThings.PrettyOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class InitialCondition extends NumericalBase {
    protected double x;
    protected Vector y;
    public InitialCondition(double x, Vector y)
    { this.x = x; this.y = y; }
    public InitialCondition(String point, int pointDimension)
    {
        this.y = new Vector(pointDimension);
        this.setInitConditionFromString(point, pointDimension);
    }
    public double getX() {
        return x;
    }
    public Vector getVectorY() {
        return y;
    }
    public double getY(int index) {
        return y.getItem(index);
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setVectorY(Vector y) {
        this.y = y;
    }
    public void setY(int index, double y) {
        this.y.setItem(index, y);
    }
    @Override
    public String toString()
    { return this.x + ";" + this.y; }
    @Override
    public boolean equals(Object obj)
    { return super.equals(obj); }
    public InitialCondition cloneInitCondition()
    { return new InitialCondition(this.getX(), this.getVectorY()); }
    public void setInitConditionFromString(String initConditionStr, int initConditionDimension)
    {
        String[] splitPoint = initConditionStr.trim().split("[\\s,]+");
        if (splitPoint.length == initConditionDimension + 1)
        {
            for (int i = 0; i < initConditionDimension; i++)
                this.setY(i, Double.parseDouble(splitPoint[i + 1]));
            this.x = Double.parseDouble(splitPoint[0]);
        }
        else if (splitPoint.length == initConditionDimension)
        {
            for (int i = 0; i < splitPoint.length; i++)
                this.setY(i, Double.parseDouble(splitPoint[i]));
            this.x = Double.NaN;
        }
        else
            throw new RuntimeException(PrettyOutput.ERROR +
                    "Ошибка! Неверно введена размерность точки или неверно введена сама точка" + PrettyOutput.RESET);
    }
    public static InitialCondition readInitConditionFromFile(String pathToFile, int dimension) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
        String initConditionStr = reader.readLine();
        return new InitialCondition(initConditionStr, dimension);
    }
    public boolean hasNanValues() {
        return Double.isNaN(this.getX()) || this.getVectorY().isNanVector();
    }
    public void print()
    {
        System.out.println(PrettyOutput.HEADER_OUTPUT + "Начальное значение размерностью: " +
                (this.getVectorY().getVectorSize()) + PrettyOutput.RESET);
        System.out.println("(" + this + ")");
    }
}
