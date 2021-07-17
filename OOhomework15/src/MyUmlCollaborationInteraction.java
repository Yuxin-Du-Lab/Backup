import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.elements.UmlMessage;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlInteraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlCollaborationInteraction implements UmlCollaborationInteraction {
    private final Map<String, MyUmlInteraction> id2interaction = new HashMap<>();
    private final Map<String, String> name2id4interaction = new HashMap<>();
    private final List<String> duplicatedNameOfInteraction = new ArrayList<>();
    private final HashMap<String, UmlAttribute> id2attribute = new HashMap<>();

    private final List<UmlMessage> messageBuffer = new ArrayList<>();
    private final List<UmlLifeline> lifelineBuffer = new ArrayList<>();
    private final List<UmlEndpoint> endpointBuffer = new ArrayList<>();

    public MyUmlCollaborationInteraction(UmlElement... elements) {
        for (UmlElement umlElement : elements) {
            //System.out.println("Collaboration---" + umlElement.toString());
            parsing(umlElement);
        }
        sortLifelineBuffer();
        sortMessageBuffer();
        sortEndpointBuffer();
    }

    private void parsing(UmlElement element) {
        if (element instanceof UmlInteraction) {
            MyUmlInteraction myUmlInteraction = new MyUmlInteraction((UmlInteraction) element);
            id2interaction.put(element.getId(), myUmlInteraction);
            String name = element.getName();
            if (name2id4interaction.containsKey(name)) {
                duplicatedNameOfInteraction.add(name);
            } else {
                name2id4interaction.put(name, element.getId());
            }
        } else if (element instanceof UmlMessage) {
            messageBuffer.add((UmlMessage) element);
        } else if (element instanceof UmlLifeline) {
            lifelineBuffer.add((UmlLifeline) element);
        } else if (element instanceof UmlEndpoint) {
            /* do not know how to use UmlEndPoint */
            endpointBuffer.add((UmlEndpoint) element);
        } else if (element instanceof UmlAttribute) {
            id2attribute.put(element.getId(), (UmlAttribute) element);
        }
    }

    private void sortMessageBuffer() {
        if (messageBuffer.size() == 0) {
            return;
        }
        for (UmlMessage umlMessage : messageBuffer) {
            String parentId = umlMessage.getParentId();
            MyUmlInteraction myUmlInteraction = id2interaction.get(parentId);
            if (myUmlInteraction != null) {
                myUmlInteraction.addMessage(umlMessage);
            }
        }
        messageBuffer.clear();
    }

    private void sortLifelineBuffer() {
        if (lifelineBuffer.size() == 0) {
            return;
        }
        for (UmlLifeline umlLifeline : lifelineBuffer) {
            String parentId = umlLifeline.getParentId();
            MyUmlInteraction myUmlInteraction = id2interaction.get(parentId);
            if (myUmlInteraction != null) {
                myUmlInteraction.addLifeline(umlLifeline);
            }
        }
        //lifelineBuffer.clear();
    }

    private void sortEndpointBuffer() {
        if (endpointBuffer.size() == 0) {
            return;
        }
        for (UmlEndpoint endpoint : endpointBuffer) {
            String parentId = endpoint.getParentId();
            MyUmlInteraction myUmlInteraction = id2interaction.get(parentId);
            if (myUmlInteraction != null) {
                myUmlInteraction.addEndpoint(endpoint);
            }
        }
        endpointBuffer.clear();
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getLifelineCnt();
    }

    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getIncomingMessageCount(lifelineName);
    }

    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        MyUmlInteraction myUmlInteraction = getInteraction(interactionName);
        return myUmlInteraction.getSentMessageCount(lifelineName, sort);
    }

    private MyUmlInteraction getInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (duplicatedNameOfInteraction.contains(interactionName)) {
            throw new InteractionDuplicatedException(interactionName);
        }
        MyUmlInteraction myUmlInteraction = id2interaction.get(
                name2id4interaction.get(interactionName));
        if (myUmlInteraction == null) {
            throw new InteractionNotFoundException(interactionName);
        }
        return myUmlInteraction;
    }

    public void checkForUml007() throws UmlRule007Exception {
        for (UmlLifeline lifeline : lifelineBuffer) {
            if (lifeline.getRepresent() == null) {
                throw new UmlRule007Exception();
            }
            if (!id2attribute.containsKey(lifeline.getRepresent())) {
                throw new UmlRule007Exception();
            }
        }
    }
}
