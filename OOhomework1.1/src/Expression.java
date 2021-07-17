import java.util.ArrayList;
import java.util.regex.Matcher;

public class Expression {
    ArrayList<Term> terms = new ArrayList<>();

    public Expression(String expressionString) {
        Matcher termMatcher = Macro.termPattern.matcher(expressionString);
        int head = 0;
        int tail = 0;
        while (termMatcher.find(tail)) {
            tail = termMatcher.end();
            if (expressionString.charAt(tail) == '(') {
                tail = matchPair(expressionString.substring(tail));
                Matcher continueTerm = Macro.continuePattern.matcher(expressionString.substring(tail));
                if (!continueTerm.find()) {
                    head = termMatcher.start();
                    String termString = expressionString.substring(head, tail);
                    System.out.println(">>>matched term:" + termString);
                    Term newTerm = new Term(termString);
                    addInTerms(newTerm);
                }
            } else {
                head = termMatcher.start();
                String termString = expressionString.substring(head, tail);
                System.out.println(">>>matched term:" + termString);
                Term newTerm = new Term(termString);
                addInTerms(newTerm);
            }

        }
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


    private void addInTerms(Term newTerm) {
        boolean findLikeTerm = false;
        for (Term loop : terms) {
            if (loop.like(newTerm)) {
                findLikeTerm = true;
                //combine
                loop.combine(newTerm);
                break;
            }
        }
        if (!findLikeTerm) {
            terms.add(newTerm);
        }
    }

    public String check() {
        String line = "";
        for (Term loop : terms) {
            line += loop.check() + "+";
        }
        return line;
    }
}
