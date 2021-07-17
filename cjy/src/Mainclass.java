import java.util.Scanner;

public class Mainclass {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        Expression expression = (new Expression(str)).derive();
        System.out.println(expression.toString());
    }
}
