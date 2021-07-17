import java.math.BigInteger;
import java.util.regex.Matcher;

public class Parsing {
    private int point = 0;
    private int length;

    public Object getExpression(String line, boolean originNegaive) {
        length = line.length();
        if (length == 0) {
            exit();
        }
        boolean negative = originNegaive;
        Expression expression = new Expression(originNegaive);
        //blank
        getBlank(line);
        //[+-]? blank
        if (line.charAt(point) == '+') {
            negative = false;
            point++;
            getBlank(line);
        } else if (line.charAt(point) == '-') {
            point++;
            negative = true;
            getBlank(line);
        }
        //Term
        Term newTerm = (Term) getTerm(negative, line);
        expression.addTerms(newTerm);
        //blank
        getBlank(line);

        while (point < length) {
            //[+-]? blank
            if (line.charAt(point) == '+') {
                point++;
                negative = false;
                getBlank(line);
            } else if (line.charAt(point) == '-') {
                point++;
                negative = true;
                getBlank(line);
            } else {
                exit();
            }
            //Term
            newTerm = (Term) getTerm(negative, line);
            expression.addTerms(newTerm);
            //blank
            getBlank(line);
        }

        return expression;
    }

    private void getBlank(String line) {
        if (point >= length) {
            return;
        }
        while (line.charAt(point) == ' ' | line.charAt(point) == '\t') {
            point++;
            if (point >= length) {
                return;
            }
        }
    }

    private Object getTerm(boolean negativeIn, String line) {
        boolean negative = negativeIn;
        //[+-]? blank
        if (line.charAt(point) == '+') {
            point++;
            getBlank(line);
        } else if (line.charAt(point) == '-') {
            point++;
            negative = !negative;
            getBlank(line);
        }

        Term term = new Term(negative, new Constant("1", false));
        //factor
        Factor factor = getFactor(line);
        term.addFactors(factor);
        getBlank(line);
        if (point >= length) {
            return term;
        }
        //*
        while (line.charAt(point) == '*') {
            point++;
            //blank
            getBlank(line);
            //factor
            factor = getFactor(line);
            term.addFactors(factor);
            getBlank(line);
            if (point >= length) {
                break;
            }
        }
        return term;
    }
    
    private Factor getFactor(String line) {
        Factor factor = null;
        boolean extraNegative = false;
        int head;
        int tail;
        /*
        try {
            Matcher powerN = Macro.getPowerPattern().matcher(line.substring(point + 1));
            if ((line.charAt(point) == '+' | line.charAt(point) == '-')
                    & (powerN.lookingAt() | line.charAt(point + 1) == '(')) {
                if (line.charAt(point) == '-') {
                    extraNegative = true;
                }
                point++;
            }
        } catch (Exception ignored) {
            //do nothing
        }*/

        Matcher constantM = Macro.getSingedIntPattern().matcher(line.substring(point));
        Matcher powerM = Macro.getPowerPattern().matcher(line.substring(point));
        if (constantM.lookingAt()) {
            //constant
            head = point;
            tail = point + constantM.end();
            factor = new Constant(line.substring(head, tail), extraNegative);
            point = tail;
        } else if (line.charAt(point) == '(') {
            //expression
            tail = matchPair(line.substring(point)) + point;
            Parsing parsing = new Parsing();
            factor = (Factor) parsing.getExpression(line.substring(point + 1, tail), extraNegative);
            point = tail + 1;
        } else if (powerM.lookingAt()) {
            //power
            Matcher sinM = Macro.getSinePattern().matcher(line.substring(point));
            Matcher cosM = Macro.getCosinePattern().matcher(line.substring(point));
            if (line.charAt(point) == 'x') {
                point++;
                getBlank(line);
                BigInteger index = getIndex(line);
                factor = new Power(new X(), index, extraNegative);
            } else if (sinM.lookingAt()) {
                //sin
                factor = getTrigPower(line, true, extraNegative);
            } else if (cosM.lookingAt()) {
                //cos
                factor = getTrigPower(line, false, extraNegative);
            }
        } else {
            exit();
        }
        if (factor == null) {
            exit();
        }
        return factor;
    }

    private Factor getTrigPower(String line, boolean isSine, boolean negative) {
        point += 3;
        getBlank(line);
        if (line.charAt(point) == '(') {
            point++;
            getBlank(line);
            final Factor insideFactor = getFactor(line);
            getBlank(line);
            if (line.charAt(point) == ')') {
                point++;
            } else {
                exit();
            }
            getBlank(line);
            BigInteger index = getIndex(line);
            Factor factor = new Power(new Trig(isSine, insideFactor), index, negative);
            return factor;
        } else {
            exit();
        }
        return null;
    }

    private BigInteger getIndex(String line) {
        BigInteger index = new BigInteger("1");
        if (point + 2 >= length) {
            return index;
        } else if (line.substring(point, point + 2).equals("**")) {
            point += 2;
            getBlank(line);
            index = getInt(line);
        }
        if (index.compareTo(new BigInteger("50")) > 0
                | index.compareTo(new BigInteger("-50")) < 0) {
            exit();
        }
        return index;
    }

    private BigInteger getInt(String line) {
        Matcher intM = Macro.getSingedIntPattern().matcher(line.substring(point));
        if (intM.lookingAt()) {
            int tail = intM.end() + point;
            String intString = line.substring(point, tail);
            BigInteger index = new BigInteger(intString);
            point = tail;
            return index;
        } else {
            exit();
        }
        return null;
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
        if (stack != 0) {
            exit();
        }
        return point;
    }

    private void exit() {
        System.out.println("WRONG FORMAT!");
        System.exit(0);
    }
}
