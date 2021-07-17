import java.util.Scanner;

public class MainProgress {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Parsing parsing1 = new Parsing();
        Expression expression1 = (Expression) parsing1.getExpression(line);


        Parsing parsing2 = new Parsing();
        String line1 = expression1.check();
        Expression expression2 = (Expression) parsing2.getExpression(line1);


        Expression diff = expression2.diff();
        Parsing parsing3 = new Parsing();
        Expression expression3 = (Expression) parsing3.getExpression(diff.check());
        String buffer = expression3.check();
        buffer = buffer.replaceAll("\\+-","-");
        System.out.println(buffer);
        /*
        System.out.println(">>>Details");
        System.out.println(">>>parsing1:\n" + expression1.check());
        System.out.println(">>>parsing2:\n" + expression2.check());
        System.out.println(">>>diff:\n" + diff.check());
        System.out.println(">>>parsing3:\n" + expression3.check());
        */
    }
}
