import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item extends Factor {
    public Item() {

    }

    public Item(BigInteger exp, BigInteger coe) {
        this.setCoefficient(coe);
        this.setDegree(exp);
    }

    public Item(String str) {
        Pattern pattern = Pattern.compile(new Factor().getregex());
        Matcher matcher = pattern.matcher(str);
        BigInteger coefficient = new BigInteger("1");
        BigInteger degree = new BigInteger("0");
        boolean minus = true;
        while (matcher.find()) {
            minus = (minus & matcher.start() > 0);
            String str1 = matcher.group();
            Factor factor = new Factor(str1);
            coefficient = coefficient.multiply(factor.getCoefficient());
            degree = degree.add(factor.getDegree());
        }
        this.setDegree(degree);
        if (str.charAt(0) == '-' && minus) {
            this.setCoefficient(coefficient.negate());
        } else {
            this.setCoefficient(coefficient);
        }
    }

    public String getregex() {
        return "(" + new Signal().getregex() + new BlankItem().getregex() + ")?" +
            new Factor().getregex() + new BlankItem().getregex() +
                "(" + new BlankItem().getregex() +
            "\\*" + new BlankItem().getregex() + new Factor().getregex() + ")*";
    }

    public String toString() {
        if (this.getCoefficient().equals(new BigInteger("0"))) {
            return "";
        }
        else if (this.getDegree().equals(new BigInteger("0"))) {
            return this.getCoefficient().toString();
        }
        else if (this.getCoefficient().equals(new BigInteger("1")) &&
                 this.getDegree().equals(new BigInteger("1"))) {
            return "x";
        }
        else if (this.getCoefficient().equals(new BigInteger("1")) &&
                 !this.getDegree().equals(new BigInteger("1"))) {
            return "x**" + this.getDegree();
        }
        else if (this.getCoefficient().equals(new BigInteger("-1")) &&
                 this.getDegree().equals(new BigInteger("1"))) {
            return "-x";
        }
        else if (this.getCoefficient().equals(new BigInteger("-1")) &&
                 !this.getDegree().equals(new BigInteger("1"))) {
            return "-x**" + this.getDegree();
        }
        else if (this.getDegree().equals(new BigInteger("1"))) {
            return this.getCoefficient() + "*x";
        }
        else {
            return this.getCoefficient() + "*x**" + this.getDegree();
        }
    }
}
