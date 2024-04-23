import MathModule.*;
import MathModule.LinearAlgebra.*;
import OtherThings.PrettyOutput;

import java.io.*;
import java.util.*;

public class DifferentialEquation extends NumericalBase {

    protected MathImplicitFunctionOperations rightSideFunction; // F(x, y)
    protected MathFunctionOperations solutionFunction; // y(x)
    protected boolean isEquationFromSystem = false;
    protected Integer equationsIndex;
    public DifferentialEquation(String pathToParametersFile, String pathToPointsMD, MathImplicitFunction function,
                                ArrayList<PointMultiD> pointsMD, MathImplicitFunctionOperations rightSideFunction,
                                boolean isEquationFromSystem, Integer equationsIndex, int dimension) throws Exception {
        if (pathToParametersFile != null) {
            super.setFields(pathToParametersFile);
            this.setFields(pathToParametersFile);
        } if (!isEquationFromSystem) {
            if (equationsIndex != null) {
                this.isEquationFromSystem = true;
                this.equationsIndex = equationsIndex;
                throw new RuntimeException(PrettyOutput.ERROR + "Может это уравнение из системы?" + PrettyOutput.RESET);
            }
            this.equationsIndex = 0;
        } else {
            if (equationsIndex == null) {
                this.isEquationFromSystem = false;
                this.equationsIndex = 0;
                throw new RuntimeException(PrettyOutput.ERROR + "Может это уравнение НЕ из системы?" + PrettyOutput.RESET);
            }
            this.isEquationFromSystem = true;
            this.equationsIndex = equationsIndex;
        } if (rightSideFunction != null) {
            this.rightSideFunction = rightSideFunction;
        } else {
            this.rightSideFunction = new MathImplicitFunctionOperations(
                    pathToParametersFile, pathToPointsMD, pointsMD, isEquationFromSystem ? dimension : 2, function);
        }
    }
    public MathImplicitFunctionOperations getRightSideFunction() {
        return rightSideFunction;
    }
    public MathFunctionOperations getSolutionFunction() {
        return solutionFunction;
    }
    public Integer getEquationsIndex() {
        return equationsIndex;
    }
    public boolean isEquationFromSystem() {
        return isEquationFromSystem;
    }
    public void setRightSideFunction(MathImplicitFunctionOperations rightSideFunction) {
        this.rightSideFunction = rightSideFunction;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public void setEquationsIndex(Integer equationsIndex) {
        this.equationsIndex = equationsIndex;
    }
    public void setEquationFromSystem(boolean equationFromSystem) {
        isEquationFromSystem = equationFromSystem;
    }
    public CauchyProblemSolving_OLD setTheCauchyProblem(String pathToParametersFile) throws Exception {
        return new CauchyProblemSolving_OLD(pathToParametersFile, this.rightSideFunction, null,
                this.isEquationFromSystem, this.equationsIndex, this.rightSideFunction.getDimension());
    }
    public CauchyProblemSolving_OLD setTheCauchyProblem(InitialCondition initialCondition) throws Exception {
        return new CauchyProblemSolving_OLD(null, this.rightSideFunction, initialCondition,
                this.isEquationFromSystem, this.equationsIndex, this.rightSideFunction.getDimension());
    }
    protected static class EquationsSystem extends NumericalBase {
        protected ArrayList<DifferentialEquation> differentialEquationsSystem;
        public EquationsSystem(ArrayList<DifferentialEquation> differentialEquationsSystem,
                               String pathToParametersFile) throws IOException, ReflectiveOperationException {
            if (pathToParametersFile != null) {
                super.setFields(pathToParametersFile);
                this.setFields(pathToParametersFile);
            }
            this.differentialEquationsSystem = differentialEquationsSystem;
            for (int index = 0; index < this.differentialEquationsSystem.size(); index++)
                this.differentialEquationsSystem.get(index).setEquationsIndex(index);
            this.differentialEquationsSystem.removeAll(Collections.singleton(null));
        }
        public ArrayList<DifferentialEquation> getDifferentialEquationsSystem() {
            return differentialEquationsSystem;
        }
        public DifferentialEquation getEquation(int index) {
            return this.differentialEquationsSystem.get(index);
        }
        public void setDifferentialEquationsSystem(ArrayList<DifferentialEquation> differentialEquationsSystem) {
            this.differentialEquationsSystem = differentialEquationsSystem;
        }
        public void setDifferentialEquation(int index, DifferentialEquation differentialEquation) {
            this.differentialEquationsSystem.set(index, differentialEquation);
        }
        public void addDifferentialEquation(DifferentialEquation differentialEquation) {
            this.differentialEquationsSystem.add(differentialEquation);
        }
        public void removeDifferentialEquation(DifferentialEquation differentialEquation) {
            this.differentialEquationsSystem.remove(differentialEquation);
        }
        public void removeDifferentialEquation(int index) {
            this.differentialEquationsSystem.remove(index);
        }
    }
}
