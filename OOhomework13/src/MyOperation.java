import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOperation {
    private final UmlOperation umlOperation;
    private final MyUmlInteraction myUmlInteraction;
    private final Map<String, UmlParameter> id2parameter = new HashMap<>();
    private UmlParameter returnParameter = null;

    public MyOperation(UmlOperation umlOperation, MyUmlInteraction myUmlInteraction) {
        this.umlOperation = umlOperation;
        this.myUmlInteraction = myUmlInteraction;
    }

    public void addParameter(UmlParameter umlParameter) {
        id2parameter.put(umlParameter.getId(), umlParameter);
    }

    public String getId() {
        return umlOperation.getId();
    }

    public String getName() {
        return umlOperation.getName();
    }

    public Visibility getVisibility() {
        return umlOperation.getVisibility();
    }

    public List<String> getParaList(String className, String operationName)
            throws MethodWrongTypeException {
        List<String> parameterList = new ArrayList<>();
        for (UmlParameter umlParameter : id2parameter.values()) {
            if (umlParameter.getDirection() == Direction.RETURN) {
                returnParameter = umlParameter;
            } else {
                String type = umlParameter.getType().toJsonString().replaceAll("\"", "");
                if (myUmlInteraction.isBaseType(type)) {
                    parameterList.add(type);
                } else if (myUmlInteraction.isReferenceType(type)) {
                    String id = type.substring(6, type.length() - 1);
                    MyClass myClass = myUmlInteraction.getMyClass(id);
                    if (myClass != null) {
                        parameterList.add(myClass.getName());
                    }
                    MyInterface myInterface = myUmlInteraction.getMyInterface(id);
                    if (myInterface != null) {
                        parameterList.add(myInterface.getName());
                    }
                } else {
                    throw new MethodWrongTypeException(className, operationName);
                }
            }
        }
        return parameterList;
    }

    public String getReturnType(String className, String operationName)
            throws MethodWrongTypeException {
        if (returnParameter != null) {
            String type = returnParameter.getType().toJsonString().replaceAll("\"", "");
            if (myUmlInteraction.isBaseType(type)
                    || type.equals("void")) {
                return type;
            }
            if (myUmlInteraction.isReferenceType(type)) {
                String id = type.substring(6, type.length() - 1);
                MyClass myClass = myUmlInteraction.getMyClass(id);
                if (myClass != null) {
                    return myClass.getName();
                }
                MyInterface myInterface = myUmlInteraction.getMyInterface(id);
                if (myInterface != null) {
                    return myInterface.getName();
                }
            }
            throw new MethodWrongTypeException(className, operationName);
        }
        return null;
    }

    public void check() {
        System.out.println("Operation name:" + umlOperation.getName());
        System.out.println("Parameters:");
        for (UmlParameter umlParameter : id2parameter.values()) {
            System.out.println(umlParameter.getName());
        }
    }
}
