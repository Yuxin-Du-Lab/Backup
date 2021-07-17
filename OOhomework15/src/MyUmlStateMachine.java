import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlStateMachine {
    private final UmlStateMachine umlStateMachine;
    private final Map<String, MyUmlRegion> id2region = new HashMap<>();

    public MyUmlStateMachine(UmlStateMachine umlStateMachine) {
        this.umlStateMachine = umlStateMachine;
    }

    public void addRegion(UmlRegion umlRegion) {
        MyUmlRegion myUmlRegion = new MyUmlRegion(umlRegion);
        id2region.put(umlRegion.getId(), myUmlRegion);
    }

    public void checkForUml008() throws UmlRule008Exception {
        for (MyUmlRegion region : id2region.values()) {
            region.checkForUml008();
        }
    }

    public MyUmlRegion getMyRegion(String regionId) {
        return id2region.get(regionId);
    }

    public int getStateNum() {
        int stateNum = 0;
        for (MyUmlRegion region : id2region.values()) {
            stateNum += region.getStateNum();
        }
        return stateNum;
    }

    public int getSubsequentStateCount(String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        /* one state machine, one region */
        int cnt = 0;
        for (MyUmlRegion region : id2region.values()) {
            // loop only once
            cnt += region.getSubsequentStateCount(umlStateMachine.getName(), stateName);
        }
        return cnt;
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateNotFoundException,
            StateDuplicatedException,
            TransitionNotFoundException {
        List<String> names = new ArrayList<>();
        for (MyUmlRegion region : id2region.values()) {
            // loop only once
            names.addAll(
                    region.getTransitionTrigger(
                            stateMachineName, sourceStateName, targetStateName));
        }
        return names;
    }

    public void check() {
        System.out.println("check state machine:" + umlStateMachine.getName());
        for (MyUmlRegion region : id2region.values()) {
            region.check();
        }
    }

}
