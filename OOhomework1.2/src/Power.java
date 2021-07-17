import java.math.BigInteger;
import java.util.regex.Matcher;

public class Power extends Factor {
    private BigInteger exp;
    private Base base;

    public Power(String powerStringIn, boolean negative) {
        super(negative);
        String powerString = powerStringIn.replaceAll("\\s*", "");
        Matcher indexMatcher = Macro.getIndexPattern().matcher(powerString);
        String[] buffer = powerString.split("\\*");
        if (indexMatcher.find()) {
            String expString = buffer[buffer.length - 1];
            exp = new BigInteger(expString);
            //exp = Integer.parseInt(expString);
        } else {
            exp = BigInteger.ONE;
            //exp = 1;
        }
        Matcher sineMatcher = Macro.getSinePattern().matcher(buffer[0]);
        Matcher cosineMatcher = Macro.getCosinePattern().matcher(buffer[0]);
        if (sineMatcher.find()) {
            base = new Trig(true);
        } else if (cosineMatcher.find()) {
            base = new Trig(false);
        } else {
            base = new X();
        }
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
            return base.check() + "*";
        } else if (exp.equals(BigInteger.ZERO)) {
            return "";
        }
        return base.check() + "**" + exp.toString() + "*";
        //return base.check() + "**" + Integer.toString(exp);
    }

    public String getType() {
        return "Power";
    }

    public Term diff() {
        //Constant constant = new Constant(Integer.toString(exp));
        Constant constant = new Constant(exp.toString());
        //Power power = new Power(base.toString() + "**" + Integer.toString(exp - 1), false);
        Power power = new Power(base.toString() + "**"
                + exp.subtract(BigInteger.ONE).toString(), false);

        Factor factor = base.diff();
        Term term = new Term(false, constant);
        if (factor.getNegative()) {
            term.getConstant().update(new Constant("-1"));
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
