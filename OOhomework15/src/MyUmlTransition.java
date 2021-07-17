import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlTransition {
    private final UmlTransition umlTransition;
    private final Map<String, UmlEvent> id2event = new HashMap<>();

    public MyUmlTransition(UmlTransition umlTransition) {
        this.umlTransition = umlTransition;
    }

    public void addEvent(UmlEvent umlEvent) {
        id2event.put(umlEvent.getId(), umlEvent);
    }

    public String getSource() {
        return umlTransition.getSource();
    }

    public String getTarget() {
        return umlTransition.getTarget();
    }

    public String getName() {
        return umlTransition.getName();
    }

    public List<String> getTriggers() {
        List<String> names = new ArrayList<>();
        for (UmlEvent event : id2event.values()) {
            names.add(event.getName());
        }
        return names;
    }

    public int getTriggersSize() {
        return id2event.size();
    }

    public UmlTransition getOrigin() {
        return umlTransition;
    }

    public void check() {
        System.out.println("check transition:" + umlTransition.getName());
        for (UmlEvent event : id2event.values()) {
            System.out.println("event:" + event.getName());
        }
    }
}
