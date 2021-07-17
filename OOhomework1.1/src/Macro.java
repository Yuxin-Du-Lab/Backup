import java.util.regex.Pattern;

public class Macro {
    private static final String SPACE = "([ \t])*";
    //private static final String singedInt = "([+-])?[0-9]+";
    // no pre-zero
    private static final String singedInt = "(([+-]?)(0|([1-9][0-9]*)))";
    private static final String INDEX = "([*]{2}" + SPACE + singedInt + ")";

    private static final String headSIGNED = "^[+\\- \t]*";
    private static final String NEGATIVE = "-";
    private static final String PAIR = "\\((.*)?\\)";
    private static final String continueTerm = "^" + SPACE + "[*]";

    private static final String X = "([x](" + SPACE + INDEX + ")?)";
    private static final String SINE ="(" +  "sin" + SPACE + "[(]" + SPACE + "[x]" + SPACE + "[)](" + SPACE + INDEX + ")?)";
    private static final String COSINE ="(" +  "cos" + SPACE + "[(]" + SPACE + "[x]" + SPACE + "[)](" + SPACE + INDEX + ")?)";
    private static final String POWER = "((" + X + ")|(" + SINE + ")|(" + COSINE + "))";
    private static final String FACTOR = "((" + POWER + ")|(" + singedInt +"))";



    private static final String TERM0 = "(([+-]" + SPACE + ")?" + FACTOR + ")";
    private static final String TERM = TERM0 + "(" + SPACE + "[*]" + SPACE + FACTOR + ")*";

    public static Pattern sinePattern = Pattern.compile(SINE);
    public static Pattern cosinePattern = Pattern.compile(COSINE);
    public static Pattern powerPattern = Pattern.compile(POWER);
    public static Pattern singedIntPattern = Pattern.compile(singedInt);
    public static Pattern factorPattern = Pattern.compile(FACTOR);
    public static Pattern indexPattern = Pattern.compile(INDEX);
    public static Pattern headSignedPattern = Pattern.compile(headSIGNED);
    public static Pattern negativePattern = Pattern.compile(NEGATIVE);
    public static Pattern termPattern = Pattern.compile(TERM);
    public static Pattern pairPattern = Pattern.compile(PAIR);
    public static Pattern continuePattern = Pattern.compile(continueTerm);

    public static void printMacro() {
        System.out.println(">>>X:" + X);
        System.out.println(">>>SINE:" + SINE);
        System.out.println(">>>COSINE:" + COSINE);
        System.out.println(">>>FACTOR:" + FACTOR);
        System.out.println(">>>POWER:" + POWER);
        System.out.println(">>>TERM0:" + TERM0);
        System.out.println(">>>TERM:" + TERM);
    }
}

/*
([+-])?[0-9]{1,}             带符号的整数

sin\(x\)(\**([+-])?[0-9]{1,})?  sin(x)[**带符号的整数]
 */