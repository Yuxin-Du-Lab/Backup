import java.math.BigInteger;

public class HasZeroInteger {
    private BigInteger integer;

    public HasZeroInteger() {

    }

    public HasZeroInteger(String str) {
        this.integer = new BigInteger(str);
    }

    public void setInteger(BigInteger integer) {
        this.integer = integer;
    }

    public BigInteger getInteger() {
        return integer;
    }

    public String getregex() {
        return "([0-9]+)";
    }
}
