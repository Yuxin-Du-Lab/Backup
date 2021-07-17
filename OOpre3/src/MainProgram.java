import java.util.ArrayList;
import java.util.Scanner;

public class MainProgram {
    public static void addInList(ArrayList<PlaceCase> placeCases, String origin, String place) {
        boolean findPlace = false;
        for (PlaceCase loop : placeCases) {
            if (loop.getPlace().equals(place)) {
                PostBox newBox = new PostBox(origin);
                loop.addPostBox(newBox);
                findPlace = true;
            }
        }
        if (!findPlace) {
            PlaceCase newPlace = new PlaceCase(place);
            PostBox newBox = new PostBox(origin);
            newPlace.addPostBox(newBox);
            placeCases.add(newPlace);
        }
    }

    public static void distribute(ArrayList<PlaceCase> placeCases, String origin) {
        String[] buffer = origin.split("-");
        String place = buffer[buffer.length - 1];
        addInList(placeCases, origin, place);
    }

    public static void load(ArrayList<PlaceCase> placeCases, Scanner scan) {
        // load information
        while (scan.hasNextLine()) {
            String buffer = scan.nextLine();
            if (buffer.equals("END_OF_INFORMATION")) {
                break;
            } else {
                String[] processed = buffer.split("\\, | |,");
                for (int i = 0; i < processed.length; i++) {
                    String origin = processed[i];
                    distribute(placeCases, origin);
                }
            }
        }
    }

    public static void checkPlaces(ArrayList<PlaceCase> placeCases) {
        for (PlaceCase loop : placeCases) {
            System.out.println(">>>" + loop.getPlace());
            loop.checkList();
        }
    }

    public static void operateShell(Scanner scan, ArrayList<PlaceCase> placeCases) {
        // load operations
        while (scan.hasNextLine()) {
            String buffer = scan.nextLine();
            String[] processed = buffer.split("\\ ");
            String op = processed[0];
            String place = processed[processed.length - 1];
            String operation = buffer.replace(" " + place, "");
            boolean placeExist = false;
            for (PlaceCase loop : placeCases) {
                if (loop.getPlace().equals(place) & loop.getBoxNumber() != 0) {
                    loop.operate(operation);
                    placeExist = true;
                }
            }
            if (!placeExist & !op.equals("del")) {
                System.out.println("no place exists");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<PlaceCase> placeCases = new ArrayList<>();
        load(placeCases, scan);
        //PlaceCase boxCase = new PlaceCase(scan);
        //checkPlaces(placeCases);
        operateShell(scan, placeCases);
    }
}
