import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyInterface {
    private final UmlInterface umlInterface;
    private final MyUmlClassModelInteraction myUmlClassModelInteraction;
    private final HashMap<String, MyOperation> id2operation = new HashMap<>();
    private final HashMap<String, MyInterface> id2father = new HashMap<>();
    private final List<UmlGeneralization> fathersBuffer = new ArrayList<>();
    private final HashSet<UmlAttribute> attributes = new HashSet<>();
    private boolean touched = false;
    private static int cnt = 0;

    public MyInterface(UmlInterface umlInterface,
                       MyUmlClassModelInteraction myUmlClassModelInteraction) {
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
        this.umlInterface = umlInterface;
    }

    public void addAttribute(UmlAttribute umlAttribute) {
        attributes.add(umlAttribute);
    }

    public boolean checkAttributesVisibility() {
        for (UmlAttribute umlAttribute : attributes) {
            if (umlAttribute.getVisibility() != Visibility.PUBLIC) {
                return true;
            }
        }
        return false;
    }

    public MyOperation addOperation(UmlOperation umlOperation) {
        MyOperation myOperation = new MyOperation(umlOperation, myUmlClassModelInteraction);
        id2operation.put(umlOperation.getId(), myOperation);
        return myOperation;
    }

    public String getName() {
        return umlInterface.getName();
    }

    public void check() {
        System.out.println("Interface name:" + umlInterface.getName());
        for (MyOperation myOperation : id2operation.values()) {
            myOperation.check();
        }
    }

    public Map<String, String> getUpCasting() {
        if (touched) {
            return new HashMap<>();
        }
        touched = true;
        getFathers();
        Map<String, String> id2name4myInterface = new HashMap<>();
        for (MyInterface father : id2father.values()) {
            id2name4myInterface.putAll(father.getUpCasting());
        }
        id2name4myInterface.put(umlInterface.getId(), umlInterface.getName());
        return id2name4myInterface;
    }

    public void clearTouched() {
        if (!touched) {
            return;
        }
        touched = false;
        getFathers();
        for (MyInterface father : id2father.values()) {
            father.clearTouched();
        }
    }

    public void addUmlGeneralization(UmlGeneralization umlGeneralization) {
        fathersBuffer.add(umlGeneralization);
    }

    public String getId() {
        return umlInterface.getId();
    }

    public HashMap<String, MyInterface> getFathers() {
        if (fathersBuffer.size() == 0) {
            return null;
        }
        for (UmlGeneralization umlGeneralization : fathersBuffer) {
            MyInterface father = myUmlClassModelInteraction.getMyInterface(
                    umlGeneralization.getTarget());
            id2father.put(father.getId(), father);
        }
        fathersBuffer.clear();
        return id2father;
    }

    public UmlInterface getOrigin() {
        return umlInterface;
    }

    public boolean scanLink4rule003() {
        getFathers();
        for (MyInterface father : id2father.values()) {
            if (myUmlClassModelInteraction.addGeneralization4rule003(father.getOrigin())) {
                // father duplicated
                return true;
            }
        }
        for (MyInterface father : id2father.values()) {
            if (father.scanLink4rule003()) {
                return true;
            }
        }
        return false;
    }

    public HashSet<UmlInterface> getExpendsTree() {
        HashSet<UmlInterface> tree = new HashSet<>();
        tree.add(getOrigin());
        for (MyInterface father : id2father.values()) {
            tree.addAll(father.getExpendsTree());
        }
        return tree;
    }
}
