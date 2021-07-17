import java.util.Scanner;

public class MainProgress {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        try {
            Parsing parsing = new Parsing();
            Expression expression = (Expression) parsing.getExpression(line, false);

            Expression diff = expression.diff();
            String buffer = diff.check();

            //Parsing parsing1 = new Parsing();
            //Expression expression1 = (Expression) parsing1.getExpression(buffer,false);

            //String output = expression1.check();
            System.out.println(buffer);

            //System.out.println(">>>before\n" + buffer);
        } catch (Exception e) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }
}
