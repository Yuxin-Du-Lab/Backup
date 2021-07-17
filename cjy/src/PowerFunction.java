import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerFunction extends Factor {
    PowerFunction() {

    }

    PowerFunction(String str) {
        Pattern pattern = Pattern.compile(new Exponent().getregex());
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            this.setDegree(new Exponent(matcher.group()).getInteger());
            this.setCoefficient(new BigInteger("1"));
        }
        else {
            this.setDegree(new BigInteger("1"));
            this.setCoefficient(new BigInteger("1"));
        }
    }

    public String getregex() {
        return "x" + "(" + new BlankItem().getregex() + new Exponent().getregex() + ")?";
    }
}
