import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.format.UmlGeneralInteraction;
import com.oocourse.uml3.models.common.MessageSort;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;

import java.util.List;
import java.util.Map;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private final MyUmlClassModelInteraction myClassModel;
    private final MyUmlStateChartInteraction myStateChart;
    private final MyUmlCollaborationInteraction myCollaboration;

    public MyUmlGeneralInteraction(UmlElement... elements) {
        myClassModel = new MyUmlClassModelInteraction(elements);
        myStateChart = new MyUmlStateChartInteraction(elements);
        myCollaboration = new MyUmlCollaborationInteraction(elements);
    }

    /* Rules Check */
    public void checkForUml001() throws UmlRule001Exception {
        myClassModel.checkForUml001();
    }

    public void checkForUml002() throws UmlRule002Exception {
        myClassModel.checkForUml002();
    }

    public void checkForUml003() throws UmlRule003Exception {
        myClassModel.checkForUml003();
    }

    public void checkForUml004() throws UmlRule004Exception {
        myClassModel.checkForUml004();
    }

    public void checkForUml005() throws UmlRule005Exception {
        myClassModel.checkForUml005();
    }

    public void checkForUml006() throws UmlRule006Exception {
        myClassModel.checkForUml006();
    }

    public void checkForUml007() throws UmlRule007Exception {
        myCollaboration.checkForUml007();
    }

    public void checkForUml008() throws UmlRule008Exception {
        myStateChart.checkForUml008();
    }

    /* Class Model */
    public int getClassCount() {
        return myClassModel.getClassCount();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getClassOperationCount(className);
    }

    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getClassAttributeCount(className);
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getClassOperationVisibility(className, operationName);
    }

    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return myClassModel.getClassAttributeVisibility(className, attributeName);
    }

    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        return myClassModel.getClassAttributeType(className, attributeName);
    }

    public List<OperationParamInformation> getClassOperationParamType(
            String className, String operationName
    ) throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        return myClassModel.getClassOperationParamType(className, operationName);
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getClassAssociatedClassList(className);
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getTopParentClass(className);
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getImplementInterfaceList(className);
    }

    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return myClassModel.getInformationNotHidden(className);
    }

    /* State Chart */
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return myStateChart.getStateCount(stateMachineName);
    }

    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return myStateChart.getSubsequentStateCount(stateMachineName, stateName);
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName
    )
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        return myStateChart.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }

    /* Collaboration */
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return myCollaboration.getParticipantCount(interactionName);
    }

    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myCollaboration.getIncomingMessageCount(interactionName, lifelineName);
    }

    public int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return myCollaboration.getSentMessageCount(interactionName, lifelineName, sort);
    }
}