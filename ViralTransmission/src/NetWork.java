import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NetWork {
    private final int rankSqrt;
    private final double r;   //short linked threshold
    private final int stepNum;
    private final int initNum;
    private final int deathOrRecoveryTime;
    private final double deathP;
    private final double immune;
    private final int isolation;

    private int networkSize = 0;
    private final Map<Integer, Person> id2person = new HashMap<>();
    private final Map<Integer, Person> id2infected = new HashMap<>();
    private final HashMap<Coordinates, Coordinates> point2point4infectWay = new HashMap<>();
    private BufferedWriter log = null;

    private final double infectionP;

    public NetWork(int rankSqrt,
                   double r,
                   int stepNum,
                   double infectionP,
                   int initNum,
                   int deathOrRecoveryTime,
                   double deathP,
                   double immune,
                   int isolation)
            throws InterruptedException {
        this.rankSqrt = rankSqrt;
        this.r = r;
        this.stepNum = stepNum;
        this.infectionP = infectionP;
        this.initNum = initNum;
        this.deathOrRecoveryTime = deathOrRecoveryTime;
        this.deathP = deathP;
        this.immune = immune;
        this.isolation = isolation;
        try {
            log = new BufferedWriter(new FileWriter("Network Running Log.txt"));
            /**
             * core steps here
             */
            buildNetwork();
            virusInitial();
            int step;
            for (step = 0; step < stepNum; step++) {
                virusStep(step);
            }
            information(step);
            log.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    private void virusInitial() throws IOException, InterruptedException {
        System.out.println("Infect initial...");
        log.write("Infect Initial:");
        log.newLine();
        log.write("Infect Initial Num--->" + initNum);
        log.newLine();
        Random random = new Random();
        for (int i = 0; i < initNum; i++) {
            int infectedId = Math.abs(random.nextInt()) % (networkSize - 1);
            Person infectedPerson = id2person.get(infectedId);
            infectedPerson.infected();
            id2infected.put(infectedId, infectedPerson);
            log.write("INFECTED:" + infectedPerson.toString());
            log.newLine();
        }
        log.write("Infect Initial End");
        log.newLine();
        draw();
    }

    private void virusStep(int step) throws InterruptedException, IOException {
        log.write("STEP-------" + step);
        log.newLine();
        System.out.println("STEP-------" + step);
        Random random = new Random();
        Map<Integer, Person> buffer = new HashMap<>();
        ArrayList<Integer> removeBuffer = new ArrayList<>();
        for (Person person : id2infected.values()) {
            for (Person friend : person.getLinkedList()) {
                if (person.isIsolated()) {
                    break;
                }
                if (friend.isIsolated()) {
                    continue;
                }
                if (random.nextDouble() % 1 < infectionP) {
                    State state = friend.infected();
                    if (state == State.INFECTED || state == State.SEVERE) {
                        if (state == State.SEVERE && random.nextDouble() % 1 < 0.5) {
                            continue;
                        }
                        buffer.put(friend.getId(), friend);
                        point2point4infectWay.put(person.getPoint(), friend.getPoint());
                        point2point4infectWay.put(friend.getPoint(), person.getPoint());
                    }
                }
            }
            State state = person.timeGo(log);
            if (state == State.RECOVERY || state == State.DEATH) {
                removeBuffer.add(person.getId());
            }
        }
        for (Integer id : removeBuffer) {
            id2infected.remove(id);
        }
        id2infected.putAll(buffer);
        draw();
        information(step);
    }

    private void buildNetwork() throws IOException, InterruptedException {
        System.out.println("Network Building...");
        buildPeople();
        for (Person loop : id2person.values()) {
            buildLinks(loop);
        }
        draw();
        System.out.println("Build Network End");
    }

    private void buildPeople() throws IOException {
        System.out.println("People building...");
        for (int i = 0; i < rankSqrt; i++) {
            for (int j = 0; j < rankSqrt; j++) {
                Person person = new Person(networkSize, i, j, deathOrRecoveryTime, deathP, immune, isolation);
                //log.write(person.toString());
                //log.newLine();
                id2person.put(networkSize, person);
                networkSize++;
            }
        }
        System.out.println("Build People End");
    }

    private void buildLinks(Person corePerson) throws IOException {
        System.out.println("Acquaintances building...");
        for (Person loop : id2person.values()) {
            if (corePerson.equal(loop)) {
                continue;
            }
            if (corePerson.getDistance(loop) <= r) {
                corePerson.addAcquaintances(loop);
                //log.write("Link Build:\t" + corePerson.toString() + "---" + loop.toString());
                //log.newLine();
            }
            if (wouldLink(corePerson.getDistance(loop))) {
                corePerson.addRemoteLink(loop);
            }
        }
        System.out.println("Build Acquaintances End");
    }

    private boolean wouldLink(double distance) {
        double q = 2;
        double constNum = 0.01;
        double threshold = constNum / Math.pow(distance, q);
        Random random = new Random();
        double newNum = random.nextDouble() % 1;
        //return false;
        return newNum < threshold;
    }

    public void draw() throws InterruptedException {
        if (false) {
            return;
        }
        System.out.println("Painting...");
        JFrame frame = new JFrame("Frame");
        frame.setSize(2000, 2000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = buildPanel(frame);
        frame.setContentPane(panel);
        System.out.println("Paint end");
        Thread.sleep(1000); // avoid next step is going
    }

    private JPanel buildPanel(Frame frame) {
        return new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);
                /**
                 * painting here
                 */
                drawDetail(graphics);
            }
        };
    }

    public void drawDetail(Graphics graphics) {
        //graphics.drawOval(10, 10, 10, 10);
        // draw people
        for (Person person : id2person.values()) {
            person.paintPerson(graphics);
        }
        // draw short links
        for (Person person : id2person.values()) {
            person.paintShortLinks(graphics, point2point4infectWay);
        }
        for (Person person : id2person.values()) {
            person.paintRemoteLinks(graphics, point2point4infectWay);
        }
    }

    private void information(int step) throws IOException {
        int liveTotalNum = 0;
        int infectedLiveNum = 0;
        int recoveryNum = 0;
        int isolatedNum = 0;
        int severe = 0;
        int death = 0;
        for (int i=0; i<networkSize; i++) {
            Person person = id2person.get(i);
            if (person.getState() == State.RECOVERY) {
                liveTotalNum++;
                recoveryNum++;
            } else if (person.getState() == State.INFECTED) {
                liveTotalNum++;
                infectedLiveNum++;
            } else if (person.getState() == State.DEATH) {
                death++;
            } else if (person.getState() == State.HEALTH) {
                liveTotalNum++;
            } else if (person.isIsolated()) {
                liveTotalNum++;
                infectedLiveNum++;
                isolatedNum++;
            } else if (person.getState() == State.SEVERE) {
                liveTotalNum++;
                infectedLiveNum++;
                severe++;
            }
        }
        System.out.println("TOTAL NUM:\t" + networkSize);
        System.out.println("Live Still:\t" + liveTotalNum);
        System.out.println("Live Infected:\t" + infectedLiveNum);
        System.out.println("Live Recovery:\t" + recoveryNum);
        System.out.println("Death Total:\t" + death);

        log.write("Live Still:\t" + liveTotalNum);
        log.newLine();
        log.write("Live Infected:\t" + infectedLiveNum);
        log.newLine();
        log.write("Live Severe:\t" + severe);
        log.newLine();
        log.write("Live Isolated:\t" + isolatedNum);
        log.newLine();
        log.write("Live Recovery:\t" + recoveryNum);
        log.newLine();
        log.write("Death Total:\t" + death);
        log.newLine();
    }
}
