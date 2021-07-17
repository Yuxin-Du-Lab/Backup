import java.util.regex.Matcher;

public class Power {
    public int exp;
    public Base base;

    public Power(String powerString) {
        Matcher indexMatcher = Macro.indexPattern.matcher(powerString);
        String[] buffer = powerString.split("\\*");
        if (indexMatcher.find()) {
            String expString = buffer[buffer.length-1];
            exp = Integer.parseInt(expString);
        } else {
            exp = 1;
        }
        Matcher sineMatcher = Macro.sinePattern.matcher(buffer[0]);
        Matcher cosineMatcher = Macro.cosinePattern.matcher(buffer[0]);
        if (sineMatcher.find() ) {
            base = new Trig(true);
        } else if (cosineMatcher.find()) {
            base = new Trig(false);
        } else {
            base = new X();
        }
    }

    public int diff() {
        exp--;
        base.diff();
        return exp + 1;
    }

    public boolean sameBase(Power newPower) {
        if (newPower.base.equal(base)) {
            return true;
        } else {
            return false;
        }
    }

    public void update(int newExp) {
        exp += newExp;
    }

    public String check() {
        return base.check() + "**" + Integer.toString(exp);
    }

}
