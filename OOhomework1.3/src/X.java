import java.math.BigInteger;

public class X implements Base {
    @Override
    public String toString() {
        return "x";
    }

    @Override
    public Factor diff() {
        return new Power(new X(), BigInteger.ZERO, false);
    }

    @Override
    public boolean equal(Base newBase) {
        if (newBase.toString().equals(toString())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String check() {
        return "x";
    }
}
