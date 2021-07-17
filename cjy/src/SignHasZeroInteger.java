import java.math.BigInteger;

public class SignHasZeroInteger extends HasZeroInteger {
    public SignHasZeroInteger() {

    }

    public SignHasZeroInteger(String str) {
        String str1 = (str.charAt(0) == '-' ? str.substring(1) : str);
        BigInteger integer = new BigInteger(str1);
        if (str.charAt(0) == '-') {
            integer = integer.negate();
        }
        this.setInteger(integer);
    }

    public String getregex() {
        return new Signal().getregex() + "?" + new HasZeroInteger().getregex();
    }
}
