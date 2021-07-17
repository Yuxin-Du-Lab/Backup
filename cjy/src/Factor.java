import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factor {
    private BigInteger degree;
    private BigInteger coefficient;

    public Factor() {

    }

    public Factor(String str) {
        Pattern pattern = Pattern.compile(new VariableFactor().getregex());
        Matcher matcher = pattern.matcher(str);
        boolean find = matcher.find();
        if (!find) {
            ConstantFactor constantFactor = new ConstantFactor(str);
            this.degree = constantFactor.getDegree();
            this.coefficient = constantFactor.getCoefficient();
        }
        else {
            VariableFactor variableFactor = new VariableFactor(str);
            this.degree = variableFactor.getDegree();
            this.coefficient = variableFactor.getCoefficient();
        }
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public BigInteger getDegree() {
        return degree;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void setDegree(BigInteger degree) {
        this.degree = degree;
    }

    public String getregex() {
        return "((" + new ConstantFactor().getregex() + ")|(" +
                new VariableFactor().getregex() + "))";
    }
}
