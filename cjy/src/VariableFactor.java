public class VariableFactor extends Factor {
    public VariableFactor() {

    }

    public VariableFactor(String str) {
        PowerFunction powerFunction = new PowerFunction(str);
        this.setCoefficient(powerFunction.getCoefficient());
        this.setDegree(powerFunction.getDegree());
    }

    public String getregex() {
        return new PowerFunction().getregex();
    }
}
