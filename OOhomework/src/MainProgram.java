import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) {
        //scan in expression
        Scanner scan = new Scanner(System.in);
        String buffer = scan.nextLine();

        //delete space
        buffer = buffer.replaceAll("\\s*", "");

        //check negative
        buffer = buffer.replaceAll(Macro.getOverNegativeStandard(), "-");
        buffer = buffer.replaceAll(Macro.getDoubleNegativeStandard(), "+");

        buffer = buffer.replaceAll(Macro.getOverNegativeStandard(), "-");
        buffer = buffer.replaceAll(Macro.getDoubleNegativeStandard(), "+");
        //load in expression
        Expression expression = new Expression(buffer);

        //derivative
        expression.derivative();

        // output result
        System.out.println(expression.toString());

        //output details
        System.out.println("<<<<<Details>>>>>");
        System.out.println("processed:\n" + buffer);
        System.out.println("check list:");
        expression.checkExpression();
    }
}
