import com.oocourse.spec1.main.Runner;

public class Test {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class);
        runner.run();
    }
}
