public class Trig implements Base {
    public boolean isSine;
    public Expression expression = new Expression("x");

    public Trig(boolean isSineParameter) {
        isSine = isSineParameter;
    }

    @Override
    public String toString() {
        if (isSine) {
            return "sin";
        } else {
            return "cos";
        }
    }

    @Override
    public int diff() {
        isSine = !isSine;
        if( isSine ) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equal(Base newBase) {
        if ( newBase.toString().equals(toString()) ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String check() {
        if (isSine) {
            return "sin(x)";
        } else {
            return "cos(x)";
        }
    }
}
