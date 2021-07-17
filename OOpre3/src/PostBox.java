import java.util.regex.Matcher;

public class PostBox implements Comparable<PostBox> {
    private String unprocessed;

    private String username;
    private String originName;
    private String usernameCategory = " ";
    private int cateNumber = 0;

    private String domain;
    private String domainCategory;

    //private String[] times = new String[4];
    //private final int timeSize;

    private String year;
    private String month;
    private String day;
    private String hour = null;
    private String minute = null;
    private String second = null;

    public void setUsernameCategory(String pattern) {
        usernameCategory += pattern;
        cateNumber++;
    }

    public PostBox(String origin) {
        unprocessed = origin;
        // match date
        String address = matchDate(origin);

        //depart with @
        String[] processed0 = address.split("@");
        originName = processed0[0];
        username = originName.toLowerCase();
        domain = processed0[1].split("\\-")[0];
        domainCategory = domain.split("\\.")[0];
    }

    private void matchTime(String originTimeString) {
        try {
            Matcher matcherTime = Macro.getpTime().matcher(originTimeString);
            matcherTime.find();
            int head = matcherTime.start();
            int tail = matcherTime.end();
            String timeString = originTimeString.substring(head, tail);
            //processed2: ss:mimi:hh
            String[] buffer = timeString.split(":");
            int timeSize = buffer.length;
            hour = buffer[timeSize - 1];
            minute = buffer[timeSize - 2];
            second = buffer[timeSize - 3];
        } catch (Exception e) {
            //do noting
        }
    }

    private String matchDate(String origin) {
        Matcher matcherDate = Macro.getpDate().matcher(origin);
        matcherDate.find();
        int head = matcherDate.start();
        int tail = matcherDate.end();
        String buffer = origin.substring(head, tail);
        String[] processed = buffer.split("-");
        year = processed[2];
        month = processed[1];
        day = processed[0];
        String timeString = origin.substring(0, head);
        matchTime(timeString);
        return origin.substring(tail + 1);
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        String timeLine = year + "-" + month + "-" + day;
        if (hour != null) {
            timeLine += "-" + hour;
        }
        if (minute != null) {
            timeLine += ":" + minute;
        }
        if (second != null) {
            timeLine += ":" + second;
        }
        return timeLine;
    }

    public String getDate() {
        return year + "-" + month + "-" + day;
    }

    public String getDomain() {
        return domain;
    }

    public String toString() {
        //form: username@domain time
        return username + "@" + domain + " " + getTime();
    }

    @Override
    public int compareTo(PostBox demo) {
        if (this.getUsername().compareTo(demo.getUsername()) > 0) {
            return 1;
        } else if (this.getUsername().compareTo(demo.getUsername()) == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    //search
    public String getDomainCategory() {
        return domainCategory;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getSecond() {
        return second;
    }

    public String getOriginName() {
        return originName;
    }

    public String getUsernameCategory() {
        return cateNumber + usernameCategory;
    }
}
