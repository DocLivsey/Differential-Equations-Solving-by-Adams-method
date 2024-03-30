import LinearAlgebra.Vector;
import OtherThings.*;
import LinearAlgebra.*;

import java.io.*;
import java.util.*;

public class DifferentialEquation extends NumericalBase {
    protected MathImplicitFunctionOperations rightSideFunction; // F(x, y)
    protected MathFunctionOperations solutionFunction; // y(x)
    public DifferentialEquation(String pathToParametersFile, String pathToPoints3D, MathImplicitFunction function,
                                ArrayList<PointMultiD> points3D, MathImplicitFunctionOperations rightSideFunction)
            throws IOException, ReflectiveOperationException {
        if (pathToParametersFile != null) {
            super.setFields(pathToParametersFile);
            this.setFields(pathToParametersFile);
        }
        if (rightSideFunction != null) {
            this.rightSideFunction = rightSideFunction;
        } else {
            this.rightSideFunction = new MathImplicitFunctionOperations(
                    pathToParametersFile, pathToPoints3D, points3D, 2, function);
        }
    }
    public MathImplicitFunctionOperations getRightSideFunction() {
        return rightSideFunction;
    }
    public MathFunctionOperations getSolutionFunction() {
        return solutionFunction;
    }
    public void setRightSideFunction(MathImplicitFunctionOperations rightSideFunction) {
        this.rightSideFunction = rightSideFunction;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public CauchyProblemSolving setTheCauchyProblem(String pathToParametersFile)
            throws IOException, ReflectiveOperationException {
        return new CauchyProblemSolving(pathToParametersFile, this.rightSideFunction, null);
    }
    public CauchyProblemSolving setTheCauchyProblem(Point2D initialCondition)
            throws IOException, ReflectiveOperationException {
        return new CauchyProblemSolving(null, this.rightSideFunction, initialCondition);
    }
    protected static class System extends NumericalBase {
        protected ArrayList<DifferentialEquation> differentialEquationsSystem;
        public System(ArrayList<DifferentialEquation> differentialEquationsSystem,
                                           String pathToParametersFile) throws IOException, ReflectiveOperationException {
            if (pathToParametersFile != null) {
                super.setFields(pathToParametersFile);
                this.setFields(pathToParametersFile);
            }
            this.differentialEquationsSystem = differentialEquationsSystem;
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
