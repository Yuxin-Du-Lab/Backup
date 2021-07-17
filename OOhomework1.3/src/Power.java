import java.math.BigInteger;

public class Power extends Factor {
    private BigInteger exp;
    private Base base;

    public Power(Base baseIn, BigInteger expIn, boolean negative) {
        super(negative);
        base = baseIn;
        exp = expIn;
    }

    public boolean sameBase(Power newPower) {
        if (newPower.base.equal(base)) {
            return true;
        } else {
            return false;
        }
    }

    public void update(BigInteger newExp) {
        exp = exp.add(newExp);
        //exp += newExp;
    }

    public String check() {
        if (exp.equals(BigInteger.ONE)) {
            return base.check();
        } else if (exp.equals(BigInteger.ZERO)) {
            return "";
        }
        return base.check() + "**" + exp.toString();
    }

    public String getType() {
        return "Power";
    }

    public Term diff() {
        Constant constant = new Constant(exp.toString(), false);
        Power power = new Power(base, exp.subtract(BigInteger.ONE), false);

        Factor factor = (Factor) base.diff();
        Term term = new Term(false, constant);
        if (factor.getNegative()) {
            term.getConstant().update(new Constant("-1", false));
        }
        term.addFactors(power);
        term.addFactors(factor);
        return term;
    }

    public BigInteger getExp() {
        return exp;
    }

    public Base getBase() {
        return base;
    }

    public boolean equal(Power newPower) {
        if (newPower.getBase().toString().equals(base.toString())
                & newPower.getExp().compareTo(exp) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean like(Power newPower) {
        if (newPower.getBase().toString().equals(base.toString())) {
            return true;
        } else {
            return false;
        }
    }
}
