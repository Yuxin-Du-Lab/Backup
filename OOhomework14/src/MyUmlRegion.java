import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlRegion {
    private final UmlRegion umlRegion;
    private final Map<String, UmlPseudostate> id2pseudostate = new HashMap<>();
    private final Map<String, UmlState> id2state = new HashMap<>();
    private final Map<String, String> name2id4state = new HashMap<>();
    private final List<String> duplicatedName4state = new ArrayList<>();

    private final Map<String, UmlFinalState> id2finalState = new HashMap<>();
    private final Map<String, MyUmlTransition> id2transition = new HashMap<>();

    private final List<String> touchedId4state = new ArrayList<>();

    public MyUmlRegion(UmlRegion umlRegion) {
        this.umlRegion = umlRegion;
    }

    public void addPseudostate(UmlPseudostate umlPseudostate) {
        id2pseudostate.put(umlPseudostate.getId(), umlPseudostate);
    }

    public void addState(UmlState umlState) {
        id2state.put(umlState.getId(), umlState);
        String name = umlState.getName();
        if (name2id4state.containsKey(name)) {
            duplicatedName4state.add(name);
        } else {
            name2id4state.put(name, umlState.getId());
        }
    }

    public void addFinalState(UmlFinalState umlFinalState) {
        id2finalState.put(umlFinalState.getId(), umlFinalState);
    }

    public void addTransition(UmlTransition umlTransition) {
        MyUmlTransition myUmlTransition = new MyUmlTransition(umlTransition);
        id2transition.put(umlTransition.getId(), myUmlTransition);
    }

    public MyUmlTransition getTransition(String transitionId) {
        return id2transition.get(transitionId);
    }

    public int getStateNum() {
        return id2state.size() + id2pseudostate.size() + id2finalState.size();
    }

    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        UmlState headState = getState(stateMachineName, stateName);
        touchedId4state.clear();
        return bfsState(headState);
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateNotFoundException,
            StateDuplicatedException,
            TransitionNotFoundException {
        UmlState sourceState = getState(stateMachineName, sourceStateName);
        UmlState targetState = getState(stateMachineName, targetStateName);
        List<String> findList = new ArrayList<>();
        for (MyUmlTransition transition : id2transition.values()) {
            if (sourceState.getId().equals(transition.getSource())
                    && targetState.getId().equals(transition.getTarget())) {
                findList.addAll(transition.getTriggers());
            }
        }
        if (findList.isEmpty()) {
            throw new TransitionNotFoundException(
                    stateMachineName, sourceStateName, targetStateName);
        }
        return findList;
    }

    private int bfsState(UmlState state) {
        int nextLayerStateNum = 0;
        for (MyUmlTransition transition : id2transition.values()) {
            if (transition.getSource().equals(state.getId())) {
                String targetId = transition.getTarget();
                if (touchedId4state.contains(targetId)) {
                    continue;
                }
                if (id2finalState.containsKey(targetId)) {
                    // touched final state
                    touchedId4state.add(targetId);
                    nextLayerStateNum++;
                }
                if (id2state.containsKey(targetId)) {
                    UmlState nextState = id2state.get(targetId);
                    touchedId4state.add(targetId);
                    nextLayerStateNum += 1 + bfsState(nextState);
                }
            }
        }
        return nextLayerStateNum;
    }

    private UmlState getState(String stateMachineName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (duplicatedName4state.contains(stateName)) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        UmlState state = id2state.get(name2id4state.get(stateName));
        if (state == null) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        return state;
    }

    public void check() {
        System.out.println("check region:" + umlRegion.getName());
        for (UmlPseudostate pseudostate : id2pseudostate.values()) {
            System.out.println("pseudostate:" + pseudostate.getName());
        }
        for (UmlState state : id2state.values()) {
            System.out.println("state:" + state.getName());
        }
        for (UmlFinalState finalState : id2finalState.values()) {
            System.out.println("final state:" + finalState.getName());
        }
        for (MyUmlTransition transition : id2transition.values()) {
            transition.check();
        }
    }
}
