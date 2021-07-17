import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction {
    private final UmlInteraction umlInteraction;
    private final Map<String, UmlMessage> id2message = new HashMap<>();

    private final Map<String, UmlLifeline> id2lifeline = new HashMap<>();
    private final Map<String, String> name2id4lifeline = new HashMap<>();
    private final List<String> duplicatedNameOfLifeline = new ArrayList<>();

    private final Map<String, UmlEndpoint> id2endpoint = new HashMap<>();

    public MyUmlInteraction(UmlInteraction umlInteraction) {
        this.umlInteraction = umlInteraction;
    }

    public void addMessage(UmlMessage umlMessage) {
        id2message.put(umlMessage.getId(), umlMessage);
    }

    public void addLifeline(UmlLifeline umlLifeline) {
        id2lifeline.put(umlLifeline.getId(), umlLifeline);
        String name = umlLifeline.getName();
        if (name2id4lifeline.containsKey(name)) {
            duplicatedNameOfLifeline.add(name);
        } else {
            name2id4lifeline.put(name, umlLifeline.getId());
        }
    }

    public void addEndpoint(UmlEndpoint umlEndpoint) {
        id2endpoint.put(umlEndpoint.getId(), umlEndpoint);
    }

    public int getLifelineCnt() {
        return id2lifeline.size();
    }

    public int getIncomingMessageCount(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        UmlLifeline umlLifeline = getUmlLifeline(lifelineName);
        return countMessageIn(umlLifeline.getId());
    }

    public int getSentMessageCount(String lifelineName, MessageSort sort)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        UmlLifeline umlLifeline = getUmlLifeline(lifelineName);
        return countMessageOutType(umlLifeline.getId(), sort);
    }

    private UmlLifeline getUmlLifeline(String lifelineName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (duplicatedNameOfLifeline.contains(lifelineName)) {
            throw new LifelineDuplicatedException(umlInteraction.getName(), lifelineName);
        }
        UmlLifeline umlLifeline = id2lifeline.get(
                name2id4lifeline.get(lifelineName));
        if (umlLifeline == null) {
            throw new LifelineNotFoundException(umlInteraction.getName(), lifelineName);
        }
        return umlLifeline;
    }

    private int countMessageIn(String lifelineId) {
        int inCnt = 0;
        for (UmlMessage message : id2message.values()) {
            String targetId = message.getTarget();
            if (targetId.equals(lifelineId)) {
                inCnt++;
            }
        }
        return inCnt;
    }

    private int countMessageOutType(String lifelineId, MessageSort sort) {
        int cnt = 0;
        for (UmlMessage message : id2message.values()) {
            String sourceId = message.getSource();
            if (sourceId.equals(lifelineId) && message.getMessageSort() == sort) {
                cnt++;
            }
        }
        return cnt;
    }
}
