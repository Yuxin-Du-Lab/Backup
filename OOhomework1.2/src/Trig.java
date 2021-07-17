public class Trig implements Base {
    private boolean isSine;

    public Trig(boolean isSineParameter) {
        isSine = isSineParameter;
    }

    @Override
    public String toString() {
        if (isSine) {
            return "sin(x)";
        } else {
            return "cos(x)";
        }
    }

    @Override
    public Factor diff() {
        if (isSine) {
            return new Power("cos(x)", false);
        } else {
            return new Power("sin(x)", true);  //-1!!
        }
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
        if (isSine) {
            return "sin(x)";
        } else {
            return "cos(x)";
        }
    }
}
