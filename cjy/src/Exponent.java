import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exponent extends HasZeroInteger {
    public Exponent() {

    }

    public Exponent(String str) {
        Pattern pattern = Pattern.compile(new SignHasZeroInteger().getregex());
        Matcher matcher = pattern.matcher(str);
        matcher.find();
        this.setInteger(new SignHasZeroInteger(matcher.group()).getInteger());
    }

    public String getregex() {
        return "\\*\\*" + new BlankItem().getregex() + new SignHasZeroInteger().getregex();
    }
}
