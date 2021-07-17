
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello virus");
        int rankSqrt = 70;
        double r = 1.5;
        int steps = 20;
        double infectionP = 0.3;
        int initNum = 1;
        int deathOrRecoveryTime = 4;
        double deathP = 0.1;
        double immune = 0.8;
        int isolationTime = 10;

        NetWork netWork = new NetWork(rankSqrt, r, steps, infectionP, initNum, deathOrRecoveryTime, deathP, immune, isolationTime);

        System.out.println("System run finished");
    }
}
