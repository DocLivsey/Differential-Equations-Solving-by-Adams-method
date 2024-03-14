public class DifferentialEquation {
    protected MathFunctionOperations rightSideFunction; // F(x, y)
    protected MathFunctionOperations solutionFunction; // y(x)
    public DifferentialEquation (MathFunctionOperations rightSideFunction)
    { this.rightSideFunction = rightSideFunction; }
    public MathFunctionOperations getRightSideFunction() {
        return rightSideFunction;
    }
    public MathFunctionOperations getSolutionFunction() {
        return solutionFunction;
    }
    public void setRightSideFunction(MathFunctionOperations F) {
        this.rightSideFunction = F;
    }
    public void setSolutionFunction(MathFunctionOperations solutionFunction) {
        this.solutionFunction = solutionFunction;
    }
    public void AdamsMethod()
    {

    }
}
