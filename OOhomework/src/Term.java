import java.math.BigInteger;
import java.util.regex.Matcher;

public class Term {
    private BigInteger constant;
    private BigInteger index;

    public Term(String item) {
        boolean negative;
        //define negative
        String unsignedItem;
        if (item.charAt(0) == '-') {
            negative = true;
            unsignedItem = item.substring(1);
        } else {
            negative = false;
            unsignedItem = item;
        }
        //System.out.println("negative:" + negative);

        //match index
        index = BigInteger.ZERO;
        //System.out.println("unsigned item match index:" + unsignedItem);
        Matcher powerMatcher = Macro.getPowerPattern().matcher(unsignedItem);
        while (powerMatcher.find()) {
            int head = powerMatcher.start();
            int tail = powerMatcher.end();
            //System.out.println(head + "->" + tail);
            //System.out.println(unsignedItem.substring(head,tail));
            if ((tail - head) == 1) {
                //only x
                index = index.add(BigInteger.ONE);
            } else {
                BigInteger theIndex = new BigInteger(unsignedItem.substring(head + 3, tail));
                index = index.add(theIndex);
            }
        }
        //System.out.println("index=" + index);
        //clear index part
        unsignedItem = unsignedItem.replaceAll(Macro.getPowerStandard(), "");
        //System.out.println("new unsignedItem String" + unsignedItem);

        //match constant
        constant = BigInteger.ONE;
        Matcher constantMatcher = Macro.getConstantPattern().matcher(unsignedItem);
        while (constantMatcher.find()) {
            int head = constantMatcher.start();
            int tail = constantMatcher.end();
            BigInteger theConstant = new BigInteger(unsignedItem.substring(head, tail));
            constant = constant.multiply(theConstant);
        }
        if (negative) {
            constant = constant.multiply(new BigInteger("-1"));
        }
        //System.out.println("index=" + index.toString());
        //System.out.println("constant=" + constant.toString());
    }

    public BigInteger getIndex() {
        return index;
    }

    public BigInteger getConstant() {
        return constant;
    }

    public void updateConstant(BigInteger newConstant) {
        constant = constant.add(newConstant);
    }

    public void derivative() {
        if (index.compareTo(BigInteger.ZERO) == 0) {
            constant = BigInteger.ZERO;
        } else {
            constant = constant.multiply(index);
            index = index.subtract(BigInteger.ONE);
        }
    }

    @Override
    public String toString() {
        if (this.constant.compareTo(BigInteger.ZERO) == 0) {
            return "";
        } else if (this.index.compareTo(BigInteger.ZERO) == 0) {
            return this.constant.toString();
        } else if (this.constant.compareTo(BigInteger.ONE) == 0
                && this.index.compareTo(BigInteger.ONE) == 0) {
            return "x";
        } else if (this.constant.compareTo(BigInteger.ONE) == 0
                && this.index.compareTo(BigInteger.ONE) != 0) {
            return "x**" + this.index.toString();
        } else if (this.constant.compareTo(new BigInteger("-1")) == 0
                && this.index.compareTo(BigInteger.ONE) == 0) {
            return "-x";
        } else if (this.constant.compareTo(new BigInteger("-1")) == 0
                && this.index.compareTo(BigInteger.ONE) != 0) {
            return "-x**" + this.index.toString();
        } else if (this.index.compareTo(BigInteger.ONE) == 0) {
            return this.constant.toString() + "*x";
        } else {
            return this.constant.toString() + "*x**" + this.index.toString();
        }
    }
}
