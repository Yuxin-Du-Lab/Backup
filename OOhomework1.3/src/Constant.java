import java.math.BigInteger;

public class Constant extends Factor {
    private BigInteger value;

    public Constant(String constantString, boolean negative) {
        super(negative);
        value = new BigInteger(constantString);
    }

    public Term diff() {
        return new Term(false, new Constant("0", false));
    }

    public void update(Constant newConstant) {
        value = value.multiply(newConstant.value);
    }

    public String check() {
        if (value.equals(BigInteger.ONE)) {
            return "";
        }
        return value.toString();
    }

    public void combine(Constant newConstant) {
        value = value.add(newConstant.value);
        //value += newConstant.value;
    }

    public String getType() {
        return "Constant";
    }
}
