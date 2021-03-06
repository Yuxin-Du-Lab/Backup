import java.util.regex.Pattern;

public class Macro {
    private static final String SPACE = "[ |\t]*";
    private static final String SINGED_INT = "([+-])?[0-9]+";
    private static final String INDEX = "[*]{2}" + SPACE + SINGED_INT;
    //private static final String HEAD_SIGNED = "^[+\\- \t]*";
    //private static final String NEGATIVE = "-";
    //private static final String PAIR = "\\((.*)?\\)";
    //private static final String continueTerm = "^" + SPACE + "[*]";

    private static final String X = "(x)";
    //private static final String SINE = "(" + "sin" + SPACE + "[(]" + SPACE + "[x]"
    //        + SPACE + "[)](" + SPACE + INDEX + ")?)";
    //private static final String COSINE = "(" + "cos" + SPACE + "[(]" + SPACE
    //        + "[x]" + SPACE + "[)](" + SPACE + INDEX + ")?)";
    private static final String SINE = "(sin)";
    private static final String COSINE = "(cos)";
    private static final String POWER = "(" + X + "|" + SINE + "|" + COSINE + ")";
    private static final String FACTOR = "(" + POWER + "|" + SINGED_INT + ")";

    private static final String TERM0 = "(([+-]" + SPACE + ")*" + FACTOR + ")";
    private static final String TERM = TERM0 + "(" + SPACE + "[*]" + SPACE + FACTOR + ")*";

    private static Pattern sinePattern = Pattern.compile(SINE);
    private static Pattern cosinePattern = Pattern.compile(COSINE);
    private static Pattern powerPattern = Pattern.compile(POWER);
    private static Pattern singedIntPattern = Pattern.compile(SINGED_INT);
    private static Pattern indexPattern = Pattern.compile(INDEX);

    public static void printMacro() {
        System.out.println(">>>FACTOR:" + FACTOR);
        System.out.println(">>>TERM0:" + TERM0);
        System.out.println(">>>TERM:" + TERM);
    }

    public static Pattern getSinePattern() {
        return sinePattern;
    }

    public static Pattern getCosinePattern() {
        return cosinePattern;
    }

    public static Pattern getPowerPattern() {
        return powerPattern;
    }

    public static Pattern getSingedIntPattern() {
        return singedIntPattern;
    }

    public static Pattern getIndexPattern() {
        return indexPattern;
    }
    /*
    public static Pattern getHeadSignedPattern() {
        return headSignedPattern;
    }

    public static Pattern getNegativePattern() {
        return negativePattern;
    }
     */
}

/*
([+-])?[0-9]{1,}             ??????????????????

sin\(x\)(\**([+-])?[0-9]{1,})?  sin(x)[**??????????????????]
 */