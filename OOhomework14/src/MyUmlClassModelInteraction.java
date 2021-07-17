import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.OperationParamInformation;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.format.UmlClassModelInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {
    private final Map<String, MyClass> id2class = new HashMap<>();
    private final Map<String, MyInterface> id2interface = new HashMap<>();
    private final Map<String, MyAssociation> id2association = new HashMap<>();
    private final Map<String, String> name2id4class = new HashMap<>();
    private final ArrayList<String> classDuplicatedName = new ArrayList<>();

    private MyOperation tmpOperation = null;

    private static final String INT_TYPE = "int";
    private static final String BYTE_TYPE = "byte";
    private static final String SHORT_TYPE = "short";
    private static final String LONG_TYPE = "long";
    private static final String FLOAT_TYPE = "float";
    private static final String DOUBLE_TYPE = "double";
    private static final String CHAR_TYPE = "char";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String STRING_TYPE = "String";

    /**
     * 构造函数的逻辑为将 elements 数组内的各个 UML 类图元素传入 UmlInteraction 类,以备后续解析。
     */
    public MyUmlClassModelInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            //System.out.println("StateChart---" + element.toString());
            parsing(element);
        }
        //check();
    }

    private void parsing(UmlElement element) {
        if (element instanceof UmlClass) {
            MyClass myClass = new MyClass((UmlClass) element, this);
            id2class.put(element.getId(), myClass);
            if (name2id4class.containsKey(element.getName())) {
                //System.out.println(">>>classDuplicated" + element.getName());
                classDuplicatedName.add(element.getName());
            } else {
                name2id4class.put(element.getName(), element.getId());
            }
            //System.out.println(">>>build class:" + element.getName());
        } else if (element instanceof UmlInterface) {
            MyInterface myInterface = new MyInterface((UmlInterface) element, this);
            id2interface.put(element.getId(), myInterface);
            //System.out.println(">>>build interface:" + element.getName());
        } else if (element instanceof UmlAssociation) {
            MyAssociation myAssociation = new MyAssociation((UmlAssociation) element, this);
            id2association.put(element.getId(), myAssociation);
            //System.out.println(">>>build association:" + element.getName());
        } else if (element instanceof UmlAttribute) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.addAttribute((UmlAttribute) element);
                //System.out.println(">>>add element:"
                // + element.getName() + " at " + myClass.getName());
            }
        } else if (element instanceof UmlOperation) {
            MyClass myClass = id2class.get(element.getParentId());
            MyInterface myInterface = id2interface.get(element.getParentId());
            /* here may exist some problem */
            if (myClass != null) {
                tmpOperation = myClass.addOperation((UmlOperation) element);
                //System.out.println(">>>add op:" + element.getName() + " at " + myClass.getName());
                return;
            }
            if (myInterface != null) {
                tmpOperation = myInterface.addOperation((UmlOperation) element);
                //System.out.println(">>>add op:"
                // + element.getName() + " at " + myInterface.getName());
            }
        } else if (element instanceof UmlParameter) {
            if (tmpOperation != null && element.getParentId().equals(tmpOperation.getId())) {
                tmpOperation.addParameter((UmlParameter) element);
                //System.out.println(">>>add parameter:"
                // + element.getName() + " in " + tmpOperation.getName());
            }
            //System.out.println(">>>ERROR1 par's operation not found");
            /* here may exist some problem */
        } else if (element instanceof UmlGeneralization) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.setUmlGeneralization((UmlGeneralization) element);
                return;
            }
            MyInterface myInterface = id2interface.get(element.getParentId());
            if (myInterface != null) {
                myInterface.addUmlGeneralization((UmlGeneralization) element);
            }
        } else if (element instanceof UmlInterfaceRealization) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.addInterfaceRealization((UmlInterfaceRealization) element);
                //System.out.println(">>>add interfaceRealization:"
                // + element.getName() + " at " + myClass.getName());
            }
            //System.out.println(">>>ERROR2 interface's realization not found");
        } else if (element instanceof UmlAssociationEnd) {
            MyAssociation myAssociation = id2association.get(element.getParentId());
            if (myAssociation != null) {
                myAssociation.addAssociationEnd((UmlAssociationEnd) element);
                //System.out.println(">>>add Association End:"
                // + element.getName() + " at " + myAssociation.getName());
            }
        }
    }

    /*
        private void check() {
            System.out.println(">>>Check!");
            for (MyClass myClass : id2class.values()) {
                myClass.check();
            }
            for (MyInterface myInterface : id2interface.values()) {
                myInterface.check();
            }
            for (MyAssociation myAssociation : id2association.values()) {
                myAssociation.check();
            }
        }
    */
    public int getClassCount() {
        return id2class.size();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.getOperationCount();
    }

    public int getClassAttributeCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.getAttributeCount();
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.getOperationVisibility(operationName);
    }

    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        MyClass myClass = name2class(className);
        Visibility visibility = myClass.getAttributeVisibility(className, attributeName);
        if (visibility == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return visibility;
    }

    public String getClassAttributeType(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException, AttributeWrongTypeException {
        MyClass myClass = name2class(className);
        String type = myClass.getAttributeType(className, attributeName);
        //System.out.println(">>>we get type: " + type);
        if (type == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        type = type.replaceAll("\"", "");
        if (isBaseType(type)) {
            return type;
        }
        if (isReferenceType(type)) {
            String id = type.substring(6, type.length() - 1);
            MyClass myClass1 = id2class.get(id);
            MyInterface myInterface1 = id2interface.get(id);
            if (myClass1 != null) {
                return myClass1.getName();
            }
            if (myInterface1 != null) {
                return myInterface1.getName();
            }
        }
        throw new AttributeWrongTypeException(className, attributeName);
    }

    public List<OperationParamInformation> getClassOperationParamType(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.getOperationParamType(className, operationName);
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return new ArrayList<>(myClass.getAssociatedClassList().values());
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.getTopFather().getName();
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return new ArrayList<>(myClass.getInterfaceRealization().values());
    }

    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        MyClass myClass = name2class(className);
        return myClass.checkAttributeHidden();
    }

    public boolean isBaseType(String type) {
        boolean bool1 = type.equals(INT_TYPE)
                || type.equals(BYTE_TYPE)
                || type.equals(SHORT_TYPE)
                || type.equals(LONG_TYPE)
                || type.equals(FLOAT_TYPE);
        boolean bool2 = type.equals(DOUBLE_TYPE)
                || type.equals(CHAR_TYPE)
                || type.equals(BOOLEAN_TYPE)
                || type.equals(STRING_TYPE);
        return bool1 || bool2;

    }

    public boolean isReferenceType(String typeCode) {
        if (typeCode.equals("")) {
            return false;
        }
        String id = typeCode.substring(6, typeCode.length() - 1);
        return id2class.containsKey(id) || hasInterface(id);
    }

    private boolean hasInterface(String type) {
        for (MyInterface myInterface : id2interface.values()) {
            if (myInterface.getName().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> getAssociateClasses(String end1) {
        Map<String, String> id2name4associatedClass = new HashMap<>();
        for (MyAssociation myAssociation : id2association.values()) {
            String associatedId = myAssociation.getAssociated(end1);
            if (associatedId != null) {
                MyClass myClass = id2class.get(associatedId);
                if (myClass != null) {
                    id2name4associatedClass.put(associatedId, myClass.getName());
                }
                /*
                MyInterface myInterface = id2interface.get(associatedId);
                if (myInterface != null) {
                    id2name4associatedClass.put(associatedId, myInterface.getName());
                }
                 */
            }
        }
        return id2name4associatedClass;
    }

    public MyClass getMyClass(String id) {
        return id2class.get(id);
    }

    public MyInterface getMyInterface(String id) {
        return id2interface.get(id);
    }

    private MyClass name2class(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        String idOfClass = name2id4class.get(className);
        if (idOfClass == null) {
            throw new ClassNotFoundException(className);
        }
        if (classDuplicatedName.contains(className)) {
            throw new ClassDuplicatedException(className);
        }
        return id2class.get(idOfClass);
    }
}
