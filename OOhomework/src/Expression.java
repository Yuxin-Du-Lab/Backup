import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Expression {
    private ArrayList<Term> expression = new ArrayList<>();

    public void addItem(Term theTerm) {
        boolean find = false;
        for (Term loop : expression) {
            if (loop.getIndex().compareTo(theTerm.getIndex()) == 0) {
                //like terms
                loop.updateConstant(theTerm.getConstant());
                find = true;
            }
        }
        if (!find) {
            expression.add(theTerm);
        }
    }

    public Expression(String line) {
        //match item
        //System.out.println("^match item^");
        Matcher itemMatcher = Macro.getItemPattern().matcher(line);
        while (itemMatcher.find()) {
            int head = itemMatcher.start();
            int tail = itemMatcher.end();
            //System.out.println(line.substring(head, tail));
            Term theTerm = new Term(line.substring(head, tail));
            addItem(theTerm);
        }
    }

    public void checkExpression() {
        for (Term loop : expression) {
            System.out.print(loop.getConstant().toString() +
                    "*x^" + loop.getIndex().toString() + " ");
        }
    }

    public void derivative() {
        for (Term loop : expression) {
            loop.derivative();
        }
    }

    @Override
    public String toString() {
        String finalExpression = "";
        boolean head = true;
        for (Term loop : expression) {
            if (head) {
                if (loop.getConstant().compareTo(BigInteger.ZERO) != 0) {
                    finalExpression = finalExpression + loop.toString();
                    head = false;
                }
            } else {
                if (loop.getConstant().compareTo(BigInteger.ZERO) > 0) {
                    finalExpression = finalExpression + "+" + loop.toString();
                } else {
                    finalExpression = finalExpression + loop.toString();
                }
            }

        }
        if (finalExpression.equals("")) {
            return "0";
        } else {
            return finalExpression;
        }
    }
}
