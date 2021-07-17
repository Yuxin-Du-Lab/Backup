import java.math.BigInteger;

public class Trig implements Base {
    private boolean isSine;
    private Factor factor;

    public Trig(boolean isSineParameter, Factor factorIn) {
        factor = factorIn;
        isSine = isSineParameter;
    }

    @Override
    public String toString() {
        String factorString = factor.check();
        if (factorString.equals("")) {
            factorString = "1";
        }
        if (factor instanceof Expression) {
            if (isSine) {
                return "sin((" + factorString + "))";
            } else {
                return "cos((" + factorString + "))";
            }
        } else {
            if (isSine) {
                return "sin(" + factorString + ")";
            } else {
                return "cos(" + factorString + ")";
            }
        }

    }

    @Override
    public Expression diff() {
        Expression newExpression = new Expression(false);
        Term newTem = new Term(false, new Constant("1", false));
        if (isSine) {
            Factor factor1 = new Power(new Trig(false, factor), BigInteger.ONE, false);
            newTem.addFactors(factor1);

            Object object = factor.diff();
            Factor factor2 = null;
            if (object instanceof Expression) {
                factor2 = (Factor) object;
            } else if (object instanceof Term) {
                Expression expression = new Expression(false);
                expression.addTerms((Term) object);
                factor2 = (Factor) expression;
            }
            newTem.addFactors(factor2);
            newExpression.addTerms(newTem);
            return newExpression;
        } else {
            Factor factor1 = new Power(new Trig(true, factor), BigInteger.ONE, true);
            newTem.addFactors(factor1);

            Object object = factor.diff();
            Factor factor2 = null;
            if (object instanceof Expression) {
                factor2 = (Factor) object;
            } else if (object instanceof Term) {
                Expression expression = new Expression(false);
                expression.addTerms((Term) object);
                factor2 = (Factor) expression;
            }
            newTem.addFactors(factor2);
            newExpression.addTerms(newTem);
            return newExpression;
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
        return toString();
    }
}
