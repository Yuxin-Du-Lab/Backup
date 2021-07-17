import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Person {
    private final int deathOrRecoveryTime;
    private final double baseDeath;
    private final double immune;
    private final int isolation;

    private final int id;
    private final int age;
    private final Map<Integer, Person> id2acquaintances = new HashMap<>();
    private final Map<Integer, Person> id2remoteFriend = new HashMap<>();
    private final Coordinates point;
    private State state;
    private int infectedTime = 0;
    private boolean isIsolated = false;

    private final int paintSize = 10;

    public Person(int id,
                  int x, int y,
                  int deathOrRecoveryTime,
                  double baseDeath,
                  double immune,
                  int isolation) {
        this.id = id;
        this.age = (int) (90 * getN());

        this.point = new Coordinates(x, y);
        this.state = State.HEALTH;
        this.deathOrRecoveryTime = deathOrRecoveryTime;
        this.baseDeath = baseDeath;
        this.immune = immune;
        this.isolation = isolation;
    }

    public void addAcquaintances(Person person) {
        id2acquaintances.put(person.getId(), person);
    }

    public void addRemoteLink(Person person) {
        id2remoteFriend.put(person.getId(), person);
    }

    public int getId() {
        return id;
    }

    public double getDistance(Person newPerson) {
        return point.getDistance(newPerson.getPoint());
    }

    public Coordinates getPoint() {
        return this.point;
    }

    public boolean equal(Person person) {
        return person.getId() == id;
    }

    public void paintShortLinks(Graphics graphics, HashMap<Coordinates, Coordinates> point2point4infectWay) {
        for (Person friend : id2acquaintances.values()) {
            Coordinates friendPoint = friend.getPoint();
            Coordinates check = point2point4infectWay.get(friendPoint);
            if (check != null && check.equal(point)) {
                graphics.setColor(Color.RED);
            } else {
                graphics.setColor(Color.BLACK);
            }
            if (friend.isIsolated || isIsolated) {
                graphics.setColor(Color.WHITE);
            }
            graphics.drawLine(point.getX() * 20 + paintSize / 2, point.getY() * 20 + paintSize / 2,
                    friendPoint.getX() * 20 + paintSize / 2, friendPoint.getY() * 20 + paintSize / 2);
        }
    }

    public void paintRemoteLinks(Graphics graphics, HashMap<Coordinates, Coordinates> point2point4infectWay) {
        for (Person friend : id2remoteFriend.values()) {
            Coordinates friendPoint = friend.getPoint();
            Coordinates check = point2point4infectWay.get(friendPoint);
            if (check != null && check.equal(point)) {
                graphics.setColor(Color.RED);
            } else {
                graphics.setColor(Color.BLACK);
            }
            if (friend.isIsolated() || isIsolated) {
                graphics.setColor(Color.WHITE);
            }
            graphics.drawLine(point.getX() * 20 + paintSize / 2, point.getY() * 20 + paintSize / 2,
                    friendPoint.getX() * 20 + paintSize / 2, friendPoint.getY() * 20 + paintSize / 2);
        }
    }

    public void paintPerson(Graphics graphics) {
        if (state == State.HEALTH) {
            graphics.setColor(Color.BLACK);
        } else if (state == State.INFECTED) {
            graphics.setColor(Color.RED);
        } else if (state == State.RECOVERY) {
            graphics.setColor(Color.GREEN);
        } else if (state == State.DEATH) {
            graphics.setColor(Color.WHITE);
        } else if (state == State.SEVERE) {
            graphics.setColor(Color.magenta);
        }
        if (isIsolated) {
            graphics.setColor(Color.BLUE);
        }

        graphics.drawOval(point.getX() * 20, point.getY() * 20, paintSize, paintSize);
        graphics.fillOval(point.getX() * 20, point.getY() * 20, paintSize, paintSize);
    }

    public ArrayList<Person> getLinkedList() {
        ArrayList<Person> list = new ArrayList<>(id2acquaintances.values());
        list.addAll(id2remoteFriend.values());
        return list;
    }

    public State infected() {
        if (state == State.HEALTH) {
            state = State.INFECTED;
        } else if (state == State.RECOVERY) {
            Random random = new Random();
            if (random.nextDouble() % 1 >= immune) {
                state = State.INFECTED;
            }
        }
        return state;
    }

    public State timeGo(BufferedWriter log) throws IOException {
        if (state == State.INFECTED || state == State.SEVERE) {
            infectedTime++;
        }
        if ((state == State.INFECTED || state == State.SEVERE) && infectedTime >= isolation) {
            isIsolated = true;
        }
        if (infectedTime == deathOrRecoveryTime / 2) {
            Random random = new Random();
            if (random.nextDouble() % 1 < getTurnedDeath() / 0.8) {
                state = State.SEVERE;
            }
        }
        if (infectedTime >= deathOrRecoveryTime) {
            if (state == State.INFECTED) {
                state = State.RECOVERY;
            }
            if (state == State.SEVERE) {
                state = State.DEATH;
            }
            isIsolated = false;
            infectedTime = 0;
        }
        return state;
    }

    public State getState() {
        return state;
    }

    private double getN() {
        Random random = new Random();
        double x = random.nextDouble() % 1;
        double pi = 3.1415926535;
        return 1 / (Math.sqrt(2.0 * pi)) * Math.exp(-0.5 * x * x);
    }

    public double getTurnedDeath() {
        double newDeath;
        if (age < 40) {
            newDeath = baseDeath;
        } else if (age < 50) {
            newDeath = baseDeath * 2;
        } else if (age < 60) {
            newDeath = baseDeath * 6;
        } else if (age < 70) {
            newDeath = baseDeath * 18;
        } else {
            newDeath = baseDeath * 74;
        }
        System.out.println(toString() + "\tage:\t" + age + "\tnew death:\t" + newDeath % 1);
        return newDeath % 1;
        //return baseDeath;
    }

    public boolean isIsolated() {
        return isIsolated;
    }

    @Override
    public String toString() {
        return "Person:\tID" + id + " | " + point.toString();
    }
}
