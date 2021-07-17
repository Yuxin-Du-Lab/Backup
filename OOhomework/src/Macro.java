import java.util.regex.Pattern;

public class Macro {
    private static String itemStandard = "([+-])?((([+-])?[0-9]{1,})|" +
            "(x(\\*{2}([+-])?[0-9]{1,})?))(\\*((([+-])?" +
            "[0-9]{1,})|(x(\\*{2}([+-])?[0-9]{1,})?))){0,}";
    private static Pattern itemPattern = Pattern.compile(itemStandard);

    private static String powerStandard = "x(\\*{2}([+-])?[0-9]{1,})?";
    private static Pattern powerPattern = Pattern.compile(powerStandard);

    private static String constantStandard = "([+-])?[0-9]{1,}";
    private static Pattern constantPattern = Pattern.compile(constantStandard);

    private static String doubleNegativeStandard = "--";
    private static String overNegativeStandard = "-\\+";

    public static Pattern getItemPattern() {
        return itemPattern;
    }

    public static Pattern getPowerPattern() {
        return powerPattern;
    }

    public static Pattern getConstantPattern() {
        return constantPattern;
    }

    public static String getDoubleNegativeStandard() {
        return doubleNegativeStandard;
    }

    public static String getPowerStandard() {
        return powerStandard;
    }

    public static String getOverNegativeStandard() { return overNegativeStandard; }
}

/*
([+-])?[0-9]{1,}                                        带符号的整数
([+-])?(0|([1-9][0-9]{0,}))                             带符号无前导0整数
([+-])?(([1-9][0-9]{0,}))                               带符号无0整数

\\*{2}([+-])?[0-9]{1,}                                  指数
\\*{2}([+-])?(0|([1-9][0-9]{0,}))                       *
\\*{2}([+-])?(([1-9][0-9]{0,}))                         **

x(\\*{2}([+-])?[0-9]{1,})?                             幂函数
x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?                   *
x(\\*{2}([+-])?(([1-9][0-9]{0,})))?                     **

([+-])?[0-9]{1,}                                        常数因子
([+-])?(0|([1-9][0-9]{0,}))                             *
([+-])?(([1-9][0-9]{0,}))                               **

x(\\*{2}([+-])?[0-9]{1,})?                              变量因子
x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?                   *
x(\\*{2}([+-])?(([1-9][0-9]{0,})))?                     **


(([+-])?[0-9]{1,})|(x(\\*{2}([+-])?[0-9]{1,})?)         因子 = 常数因子|变量因子
(([+-])?(0|([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?)   *
(([+-])?(([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(([1-9][0-9]{0,})))?)       **

([+-])?因子(\\*因子){0,}                                    项
因子 = ((([+-])?[0-9]{1,})|(x(\\*{2}([+-])?[0-9]{1,})?))
项 = ([+-])?((([+-])?[0-9]{1,})|(x(\\*{2}([+-])?
[0-9]{1,})?))(\\*((([+-])?[0-9]{1,})|(x(\\*{2}([+-])?[0-9]{1,})?))){0,}

因子 = (([+-])?(0|([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?)
项 = ([+-])?((([+-])?(0|([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?))
(\\*((([+-])?(0|([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(0|([1-9][0-9]{0,})))?))){0,}

因子 = (([+-])?(([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(([1-9][0-9]{0,})))?)
项 = ([+-])?((([+-])?(([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(([1-9][0-9]{0,})))?))
(\\*((([+-])?(([1-9][0-9]{0,})))|(x(\\*{2}([+-])?(([1-9][0-9]{0,})))?))){0,}

表达式 = ([+-])?项(([+-])项){0,}
 */