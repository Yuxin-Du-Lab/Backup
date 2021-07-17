import java.util.ArrayList;

public class Expression extends Factor {
    private ArrayList<Term> terms = new ArrayList<>();

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public Expression(boolean negativeIn) {
        super(negativeIn);
    }

    public void addTerms(Term newTerm) {
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
            line += loop.check();
        }
        if (line.equals("")) {
            return "0";
        }
        line = line.substring(0, line.length() - 1);
        return line;
    }

    public String getType() {
        return "Expression";
    }

    public void combine(Expression newExpression) {
        terms.addAll(newExpression.terms);
    }

    public Expression diff() {
        Expression expression = new Expression(false);
        for (Term term : terms) {
            expression.combine(term.diff());
        }
        return expression;
    }

    public int getTermNum() {
        return terms.size();
    }
}
