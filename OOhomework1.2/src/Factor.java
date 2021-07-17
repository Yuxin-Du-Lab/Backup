public class Factor {
    private boolean negative = false;

    public boolean getNegative() {
        return negative;
    }

    public Factor(boolean negativeIn) {
        negative = negativeIn;
    }

    public String getType() {
        return "Factor";
    }

    public Object diff() {
        return new Term(false, new Constant("1"));
    }
}
