import java.math.BigInteger;

public class ConstantFactor extends Factor {
    ConstantFactor() {

    }

    ConstantFactor(String str) {
        SignHasZeroInteger signHasZeroInteger = new SignHasZeroInteger(str);
        this.setCoefficient(signHasZeroInteger.getInteger());
        this.setDegree(new BigInteger("0"));
    }

    public String getregex() {
        return new SignHasZeroInteger().getregex();
    }
}
