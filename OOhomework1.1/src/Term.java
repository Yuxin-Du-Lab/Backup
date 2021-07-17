import java.util.ArrayList;
import java.util.regex.Matcher;

public class Term {
    private boolean negative = false;
    public Constant constant;
    public ArrayList<Power> powers = new ArrayList<>();
    public ArrayList<Expression> expressions = new ArrayList<>();

    public Term(String termString) {
        termString = signed(termString);
        if (negative) {
            constant = new Constant("-1");
        } else {
            constant = new Constant("1");
        }
        termString = pair(termString);
        Matcher factorMatcher = Macro.factorPattern.matcher(termString);
        while (factorMatcher.find()) {
            String factor = termString.substring(factorMatcher.start(), factorMatcher.end());
            System.out.println(">>>matched factor:" + factor);
            classified(factor);
        }
    }

    public String pair(String buffer) {
        Matcher pairMatcher = Macro.pairPattern.matcher(buffer);
        while (pairMatcher.find()) {
            int head = pairMatcher.start();
            int tail = pairMatcher.end();
            if (buffer.charAt(tail - 1) == '(') {
                buffer = pair(buffer.substring(tail));
            } else if (buffer.charAt(tail - 1)==')') {
                System.out.println(">>match expression:" + buffer.substring(head, tail));
                Expression newExpression = new Expression(buffer.substring(head + 1, tail - 1));
                expressions.add(newExpression);
                buffer = buffer.substring(0,head) + buffer.substring(tail);
            }
        }
        return buffer;
    }

    private String signed(String termString) {
        Matcher signedMatcher = Macro.headSignedPattern.matcher(termString);
        if (signedMatcher.find()) {
            int tail = signedMatcher.end();
            String  signedString = termString.substring(0,tail);
            //update termString
            termString = termString.substring(tail);
            //match "-" and define term signed
            Matcher negativeMatcher = Macro.negativePattern.matcher(signedString);
            while (negativeMatcher.find()) {
                negative = !negative;
            }
        }
        System.out.println(">>>after matched signed:" + termString);
        return termString;
    }

    private void classified(String factor) {
        Matcher constantMatcher = Macro.singedIntPattern.matcher(factor);
        Matcher powerMatcher = Macro.powerPattern.matcher(factor);
        if (powerMatcher.find()) {
            Power newPower = new Power(factor);
            addPower(newPower);
        } else if (constantMatcher.find()) {
            Constant newConstant = new Constant(factor);
            addConstant(newConstant);
        } else {
            System.out.println(">>>not match in constant & power");
        }
    }

    private void addPower(Power newPower) {
        boolean notFind = true;
        for (Power loop: powers) {
            if (loop.sameBase(newPower)) {
                loop.update(newPower.exp);
                notFind = false;
                break;
            }
        }
        if (notFind) {
            powers.add(newPower);
        }
    }

    private void addConstant(Constant newConstant) {
        constant.update(newConstant);
    }

    public String check() {
        String line = constant.check();
        for (Power loop: powers) {
            line += "*" +  loop.check();
        }
        for (Expression loop: expressions) {
            line += "*" + loop.check();
        }
        return line;
    }

    public boolean like(Term newTerm) {
        boolean isLikeTerm = true;
        for (Power newLoop: newTerm.powers) {
            boolean findSamePower = false;
            for (Power loop: powers) {
                if (loop.base.toString().equals(newLoop.base.toString()) & loop.exp==newLoop.exp) {     //same base
                    findSamePower = true;
                }
            }
            if (!findSamePower) {
                isLikeTerm = false;
                break;
            }
        }
        return isLikeTerm;
    }

    public void combine(Term newTerm) {
        //newTerm is likeTerm
        constant.combine(newTerm.constant);
    }
}
