import java.util.ArrayList;

public class Term {
    private Constant constant;
    private ArrayList<Power> powers = new ArrayList<>();
    private ArrayList<Expression> expressions = new ArrayList<>();

    public Constant getConstant() {
        return constant;
    }

    public Term(boolean negativeIn, Constant constantIn) {
        if (negativeIn) {
            constant = constantIn;
            constant.update(new Constant("-1"));
        } else {
            constant = constantIn;
        }
    }

    public void addFactors(Factor factor) {
        if (factor.getType().equals("Constant")) {
            addConstant((Constant) factor);
        } else if (factor.getType().equals("Power")) {
            addPower((Power) factor);
        } else if (factor.getType().equals("Expression")) {
            //here, is the bug of clone
            expressions.add((Expression) factor);
        }
    }

    private void addPower(Power newPower) {
        boolean notFind = true;
        for (Power loop : powers) {
            if (loop.sameBase(newPower)) {
                loop.update(newPower.getExp());
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
        //here will turn to bug
        for (Expression loop : expressions) {
            if (false) {
                constant.update(loop.getTerms().get(0).constant);
                loop.getTerms().get(0).constant.setOne();
            }
        }
        String line = constant.check();
        if (line.equals("0*")) {
            return "";
        }
        for (Power loop : powers) {
            line += loop.check();
        }
        for (Expression loop : expressions) {
            if (loop.getTermNum() == 1) {
                line += loop.check() + "*";
            } else {
                line += "(";
                line += loop.check();
                line += ")*";
            }

        }
        if (line.equals("")) {
            return "1+";
        } else if (line.equals("-")) {
            return "-1+";
        }
        return line.substring(0, line.length() - 1) + "+";
    }

    public boolean like(Term newTerm) {
        if (expressions.size() > 0 | newTerm.expressions.size() > 0) {
            return false;
        }
        if (powers.size() != newTerm.powers.size()) {
            return false;
        }
        if (powers.size() == 0 & newTerm.powers.size() == 0) {
            return true;
        }
        for (Power loop : powers) {
            boolean match = false;
            for (Power inLoop : newTerm.powers) {
                if (loop.equal(inLoop)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    public void combine(Term newTerm) {
        //newTerm is likeTerm
        constant.combine(newTerm.constant);
    }

    public Expression diff() {
        //loop every factor
        Expression expression = new Expression(false);
        for (int i = 0; i < powers.size(); i++) {
            Term diffTerm = powers.get(i).diff();
            diffTerm.constant.update(constant);
            for (int j = 0; j < powers.size(); j++) {
                if (i != j) {
                    diffTerm.addFactors(powers.get(j));
                }
            }
            for (int j = 0; j < expressions.size(); j++) {
                diffTerm.addFactors(expressions.get(j));
            }
            expression.addTerms(diffTerm);
        }
        for (int i = 0; i < expressions.size(); i++) {
            Term diffTerm = new Term(false, new Constant("1"));
            diffTerm.addFactors(expressions.get(i).diff());
            diffTerm.constant.update(constant);
            for (int j = 0; j < powers.size(); j++) {
                diffTerm.addFactors(powers.get(j));
            }
            for (int j = 0; j < expressions.size(); j++) {
                if (i != j) {
                    diffTerm.addFactors(expressions.get(j));
                }
            }
            expression.addTerms(diffTerm);
        }
        return expression;
    }

    public void combinePowers() {
        for (int i = 0; i < powers.size(); i++) {
            for (int j = 0; j < powers.size(); j++) {
                if (i == j) {
                    continue;
                } else {
                    if (powers.get(i).like(powers.get(j))) {
                        powers.get(i).update(powers.get(j).getExp());
                        powers.remove(j);
                        j--;
                    }
                }
            }
        }
    }
}
