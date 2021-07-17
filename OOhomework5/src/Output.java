import com.oocourse.TimableOutput;

public class Output {
    public static void init() {
        // please MUST initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();

    }

    public static void print(String line) throws Exception {
        // print something
        TimableOutput.println(line);
    }
}
