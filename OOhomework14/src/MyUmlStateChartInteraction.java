import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlTransition;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private final Map<String, MyUmlStateMachine> id2stateMachine = new HashMap<>();
    private final Map<String, String> name2id4stateMachine = new HashMap<>();
    private final List<String> duplicatedName4stateMachine = new ArrayList<>();

    private final Map<String, UmlRegion> id2region = new HashMap<>();
    private final Map<String, UmlPseudostate> id2pseudostate = new HashMap<>();
    private final Map<String, UmlState> id2state = new HashMap<>();
    private final Map<String, UmlFinalState> id2finalState = new HashMap<>();
    private final Map<String, UmlTransition> id2transition = new HashMap<>();
    private final Map<String, UmlEvent> id2event = new HashMap<>();

    public MyUmlStateChartInteraction(UmlElement... elements) {
        for (UmlElement umlElement : elements) {
            //System.out.println("StateChart---" + umlElement.toString());
            parsing(umlElement);
        }
        sortRegionBuffer();
        sortPseudostateBuffer();
        sortStateBuffer();
        sortFinalStateBuffer();
        sortTransitionBuffer();
        sortEventBuffer();
        clearBuffer();
        //check();
    }

    private void parsing(UmlElement element) {
        if (element instanceof UmlStateMachine) {
            MyUmlStateMachine myUmlStateMachine = new MyUmlStateMachine((UmlStateMachine) element);
            id2stateMachine.put(element.getId(), myUmlStateMachine);
            String name = element.getName();
            if (name2id4stateMachine.containsKey(name)) {
                duplicatedName4stateMachine.add(name);
            } else {
                name2id4stateMachine.put(name, element.getId());
            }
        } else if (element instanceof UmlRegion) {
            id2region.put(element.getId(), (UmlRegion) element);
        } else if (element instanceof UmlPseudostate) {
            id2pseudostate.put(element.getId(), (UmlPseudostate) element);
        } else if (element instanceof UmlState) {
            id2state.put(element.getId(), (UmlState) element);
        } else if (element instanceof UmlFinalState) {
            id2finalState.put(element.getId(), (UmlFinalState) element);
        } else if (element instanceof UmlTransition) {
            id2transition.put(element.getId(), (UmlTransition) element);
        } else if (element instanceof UmlEvent) {
            id2event.put(element.getId(), (UmlEvent) element);
        }
    }

    private void sortRegionBuffer() {
        if (id2region.isEmpty()) {
            return;
        }
        for (UmlRegion region : id2region.values()) {
            MyUmlStateMachine stateMachine = id2stateMachine.get(region.getParentId());
            if (stateMachine != null) {
                stateMachine.addRegion(region);
            }
        }
    }

    private void sortPseudostateBuffer() {
        if (id2pseudostate.isEmpty()) {
            return;
        }
        for (UmlPseudostate pseudostate : id2pseudostate.values()) {
            String parentId4region = pseudostate.getParentId();
            MyUmlStateMachine myUmlStateMachine = region2machine(parentId4region);
            if (myUmlStateMachine != null) {
                myUmlStateMachine.getMyRegion(parentId4region).addPseudostate(pseudostate);
            }
        }
    }

    private void sortStateBuffer() {
        if (id2state.isEmpty()) {
            return;
        }
        for (UmlState state : id2state.values()) {
            String parentId4region = state.getParentId();
            MyUmlStateMachine myUmlStateMachine = region2machine(parentId4region);
            if (myUmlStateMachine != null) {
                myUmlStateMachine.getMyRegion(parentId4region).addState(state);
            }
        }
    }

    private void sortFinalStateBuffer() {
        if (id2finalState.isEmpty()) {
            return;
        }
        for (UmlFinalState finalState : id2finalState.values()) {
            String parentId4region = finalState.getParentId();
            MyUmlStateMachine myUmlStateMachine = region2machine(parentId4region);
            if (myUmlStateMachine != null) {
                myUmlStateMachine.getMyRegion(parentId4region).addFinalState(finalState);
            }
        }
    }

    private void sortTransitionBuffer() {
        if (id2transition.isEmpty()) {
            return;
        }
        for (UmlTransition transition : id2transition.values()) {
            String parentId4region = transition.getParentId();
            MyUmlStateMachine myUmlStateMachine = region2machine(parentId4region);
            if (myUmlStateMachine != null) {
                myUmlStateMachine.getMyRegion(parentId4region).addTransition(transition);
            }
        }
    }

    private void sortEventBuffer() {
        if (id2event.isEmpty()) {
            return;
        }
        for (UmlEvent event : id2event.values()) {
            String parentId4transition = event.getParentId();
            UmlTransition transition = id2transition.get(parentId4transition);
            if (transition != null) {
                String parentId4region = transition.getParentId();
                MyUmlStateMachine myUmlStateMachine = region2machine(parentId4region);
                if (myUmlStateMachine != null) {
                    myUmlStateMachine.
                            getMyRegion(parentId4region).
                            getTransition(parentId4transition).
                            addEvent(event);
                }
            }
        }
    }

    private MyUmlStateMachine region2machine(String regionId) {
        UmlRegion umlRegion = id2region.get(regionId);
        if (umlRegion != null) {
            String parentId4stateMachine = umlRegion.getParentId();
            return id2stateMachine.get(parentId4stateMachine);
        }
        return null;
    }

    private void clearBuffer() {
        id2state.clear();
        id2transition.clear();
        id2finalState.clear();
        id2pseudostate.clear();
        id2event.clear();
        id2region.clear();
    }

    private void check() {
        System.out.println(">>>checking...");
        for (MyUmlStateMachine stateMachine : id2stateMachine.values()) {
            stateMachine.check();
        }
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MyUmlStateMachine myUmlStateMachine = getMyUmlStateMachine(stateMachineName);
        return myUmlStateMachine.getStateNum();
    }

    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MyUmlStateMachine stateMachine = getMyUmlStateMachine(stateMachineName);
        return stateMachine.getSubsequentStateCount(stateName);
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName
    )
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        MyUmlStateMachine myUmlStateMachine = getMyUmlStateMachine(stateMachineName);
        return myUmlStateMachine.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }

    private MyUmlStateMachine getMyUmlStateMachine(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (duplicatedName4stateMachine.contains(stateMachineName)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        MyUmlStateMachine stateMachine = id2stateMachine.get(
                name2id4stateMachine.get(stateMachineName));
        if (stateMachine == null) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        return stateMachine;
    }
}
