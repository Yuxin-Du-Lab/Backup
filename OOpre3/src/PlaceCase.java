import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;

public class PlaceCase {
    private final ArrayList<PostBox> postBoxes = new ArrayList();
    private final String place;
    private int boxNumber;

    public PlaceCase(String newPlace) {
        place = newPlace;
        boxNumber = 0;
    }

    public void addPostBox(PostBox newBox) {
        postBoxes.add(newBox);
        defineUsernamePattern(newBox);
        Collections.sort(postBoxes);
        boxNumber++;
    }

    private void defineUsernamePattern(PostBox postBox) {
        final Matcher matcherA = Macro.getpA().matcher(postBox.getOriginName());
        final Matcher matcherB = Macro.getpB().matcher(postBox.getOriginName());
        final Matcher matcherC = Macro.getpC().matcher(postBox.getOriginName().toLowerCase());
        final Matcher matcherD = Macro.getpD().matcher(postBox.getOriginName());
        final Matcher matcherE = Macro.getpE().matcher(postBox.getOriginName());

        if (matcherA.find()) {
            postBox.setUsernameCategory("A");
        }
        if (matcherB.find()) {
            postBox.setUsernameCategory("B");
        }
        if (matcherC.find()) {
            postBox.setUsernameCategory("C");
        }
        if (matcherD.find()) {
            postBox.setUsernameCategory("D");
        }
        if (matcherE.find()) {
            postBox.setUsernameCategory("E");
        }
    }

    private void search(String op, String username) {
        // search for the user
        boolean find = false;
        PostBox target = null;
        for (PostBox loop : postBoxes) {
            if (username.equals(loop.getUsername())) {
                find = true;
                target = loop;
                break;
            }
        }
        if (!find) {
            //System.out.println("no " + username + " exists");
            System.out.println("no username exists");
            return;
        }

        // operate op
        if (op.equals("qdtype")) {
            System.out.println(target.getDomainCategory());
        } else if (op.equals("qyear")) {
            System.out.println(target.getYear());
        } else if (op.equals("qmonth")) {
            System.out.println(target.getMonth());
        } else if (op.equals("qday")) {
            System.out.println(target.getDay());
        } else if (op.equals("qhour")) {
            System.out.println(target.getHour());
        } else if (op.equals("qminute")) {
            System.out.println(target.getMinute());
        } else if (op.equals("qsec")) {
            System.out.println(target.getSecond());
        } else if (op.equals("qutype")) {
            System.out.println(target.getUsernameCategory());
        } else if (op.equals("qutime")) {
            System.out.println(target.getDate());
        }
    }

    private void listTime(String timeLine) {
        String[] processed = timeLine.split("-");
        String yearTarget = processed[0];
        String monthTarget = processed[1];
        String dayTarget = processed[2];
        boolean find = false;
        for (PostBox loop : postBoxes) {
            if (loop.getYear().equals(yearTarget)
                    & loop.getMonth().equals(monthTarget)
                    & loop.getDay().equals(dayTarget)) {
                System.out.println(loop.toString());
                find = true;
            }
        }
        if (!find) {
            System.out.println("no email exists");
        }
    }

    private void deleteUser(String username) {
        for (PostBox loop : postBoxes) {
            if (loop.getUsername().equals(username)) {
                postBoxes.remove(loop);
                boxNumber--;
                break;
            }
        }
    }

    private void deleteTime(String timeLine) {
        String[] processed = timeLine.split("-");
        String yearTarget = processed[0];
        String monthTarget = processed[1];
        String dayTarget = processed[2];
        //iterator
        Iterator<PostBox> iter = postBoxes.iterator();
        while (iter.hasNext()) {
            PostBox item = iter.next();
            if (item.getYear().equals(yearTarget)
                    & item.getMonth().equals(monthTarget)
                    & item.getDay().equals(dayTarget)) {
                iter.remove();
                boxNumber--;
            }
        }
        //checkList();
    }

    public void operate(String operation) {
        String buffer = operation;
        String[] processed = buffer.split("\\ ");
        //System.out.println(processed.length);
        if (processed[0].equals("qutime")
                & processed[1].equals("all")
                & processed.length == 3) {
            // list time shot
            listTime(processed[2]);
        } else if (processed[0].equals("del")) {
            //delete part
            if (processed[1].equals("all")
                    & processed.length == 3) {
                deleteTime(processed[2]);
            } else {
                deleteUser(processed[1]);
            }
        } else {
            // ordinary search
            String op = processed[0];
            String username = processed[1];
            search(op, username);
        }
    }

    public void checkList() {
        //check:
        System.out.println(">>>Checking List:");
        for (PostBox loop : postBoxes) {
            System.out.println(loop.toString());
        }
        System.out.println(">>>..");
    }

    public String getPlace() {
        return place;
    }

    public int getBoxNumber() {
        return boxNumber;
    }
}
