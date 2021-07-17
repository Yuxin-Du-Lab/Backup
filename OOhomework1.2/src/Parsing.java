import java.util.regex.Matcher;

public class Parsing {
    private int point = 0;
    private int length;
    private boolean finish = false;

    public Object getExpression(String line) {
        length = line.length();
        boolean negative = false;
        Expression expression = new Expression(false);
        getBlank(line);
        if (line.charAt(point) == '+') {
            negative = false;
            point++;
            getBlank(line);
        } else if (line.charAt(point) == '-') {
            point++;
            negative = true;
            getBlank(line);
        }
        Term newTerm = (Term) getTerm(negative, line);
        expression.addTerms(newTerm);
        getBlank(line);

        while (point < length) {
            if (line.charAt(point) == '+') {
                point++;
                negative = false;
                getBlank(line);
            } else if (line.charAt(point) == '-') {
                point++;
                negative = true;
                getBlank(line);
            }
            newTerm = (Term) getTerm(negative, line);
            expression.addTerms(newTerm);
            getBlank(line);
        }
        return expression;
    }

    private void getBlank(String line) {
        if (point >= length) {
            finish = true;
            return;
        }
        while (line.charAt(point) == ' ' | line.charAt(point) == '\t') {
            point++;
            if (point >= length) {
                finish = true;
                return;
            }
        }
    }

    private Object getTerm(boolean negativeIn, String line) {
        boolean negative = negativeIn;
        if (line.charAt(point) == '+') {
            point++;
            getBlank(line);
        } else if (line.charAt(point) == '-') {
            point++;
            negative = !negative;
            getBlank(line);
        }
        if (line.charAt(point) == '+') {
            point++;
            getBlank(line);
        } else if (line.charAt(point) == '-') {
            point++;
            negative = !negative;
            getBlank(line);
        }
        Term term = new Term(negative, new Constant("1"));
        Factor factor = getFactor(line.substring(point));
        term.addFactors(factor);
        getBlank(line);
        if (point >= length) {
            return term;
        }
        while (line.charAt(point) == '*') {
            point++;
            getBlank(line);
            factor = getFactor(line.substring(point));
            term.addFactors(factor);
            getBlank(line);
            if (point >= length) {
                break;
            }
        }
        return term;
    }

    private Factor getFactor(String line) {
        Factor factor = new Factor(false);
        Matcher constantM = Macro.getSingedIntPattern().matcher(line);
        Matcher powerM = Macro.getPowerPattern().matcher(line);
        int head;
        int tail;
        if (constantM.lookingAt()) {
            head = constantM.start();
            tail = constantM.end();
            factor = new Constant(line.substring(head, tail));
            point += constantM.end();
        } else if (powerM.lookingAt()) {
            head = powerM.start();
            tail = powerM.end();
            factor = new Power(line.substring(head, tail), false);
            point += powerM.end();
        } else {
            tail = matchPair(line);
            Parsing parsing = new Parsing();
            factor = (Factor) parsing.getExpression(line.substring(1, tail));
            point += tail + 1;
        }
        return factor;
    }

    private int matchPair(String leftString) {
        int point = 0;
        int stack = 0;
        for (int mark = 0; mark < leftString.length(); mark++) {
            if (leftString.charAt(mark) == '(') {
                stack++;
            } else if (leftString.charAt(mark) == ')') {
                stack--;
                point = mark;
            }
            if (stack == 0) {
                break;
            }
        }
        return point;
    }

}
