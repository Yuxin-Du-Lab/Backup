import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.format.UmlClassModelInteraction;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {
    private final Map<String, MyClass> id2class = new HashMap<>();
    private final Map<String, MyInterface> id2interface = new HashMap<>();
    private final HashMap<String, MyAssociation> id2association = new HashMap<>();
    private final Map<String, String> name2id4class = new HashMap<>();
    private final ArrayList<String> classDuplicatedName = new ArrayList<>();

    private final HashSet<UmlClassOrInterface> buffer4rule002 = new HashSet<>();
    private final HashSet<UmlClassOrInterface> elementInCircles = new HashSet<>();

    private final HashSet<UmlClassOrInterface> buffer4rule003 = new HashSet<>();
    private final HashSet<UmlClassOrInterface> duplicatedList4rule003 = new HashSet<>();

    private final HashSet<UmlInterface> buffer4rule004 = new HashSet<>();
    private final HashSet<UmlClass> repeatRealization4rule004 = new HashSet<>();

    private final HashSet<UmlElement> elementsOfClassModel = new HashSet<>();

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
        distributeAssociation();
        //check();
    }

    private void parsing(UmlElement element) {
        elementsOfClassModel.add(element);
        if (element instanceof UmlClass) {
            MyClass myClass = new MyClass((UmlClass) element, this);
            id2class.put(element.getId(), myClass);
            if (name2id4class.containsKey(element.getName())) {
                classDuplicatedName.add(element.getName());
            } else {
                name2id4class.put(element.getName(), element.getId());
            }
        } else if (element instanceof UmlInterface) {
            MyInterface myInterface = new MyInterface((UmlInterface) element, this);
            id2interface.put(element.getId(), myInterface);
        } else if (element instanceof UmlAssociation) {
            MyAssociation myAssociation = new MyAssociation((UmlAssociation) element, this);
            id2association.put(element.getId(), myAssociation);
        } else if (element instanceof UmlAttribute) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.addAttribute((UmlAttribute) element);
            }
            MyInterface myInterface = id2interface.get(element.getParentId());
            if (myInterface != null) {
                myInterface.addAttribute((UmlAttribute) element);
            }
        } else if (element instanceof UmlOperation) {
            MyClass myClass = id2class.get(element.getParentId());
            MyInterface myInterface = id2interface.get(element.getParentId());
            if (myClass != null) {
                tmpOperation = myClass.addOperation((UmlOperation) element);
            } else if (myInterface != null) {
                tmpOperation = myInterface.addOperation((UmlOperation) element);
            }
        } else if (element instanceof UmlParameter) {
            if (tmpOperation != null && element.getParentId().equals(tmpOperation.getId())) {
                tmpOperation.addParameter((UmlParameter) element);
            }
        } else if (element instanceof UmlGeneralization) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.setUmlGeneralization((UmlGeneralization) element);
            }
            MyInterface myInterface = id2interface.get(element.getParentId());
            if (myInterface != null) {
                myInterface.addUmlGeneralization((UmlGeneralization) element);
            }
        } else if (element instanceof UmlInterfaceRealization) {
            MyClass myClass = id2class.get(element.getParentId());
            if (myClass != null) {
                myClass.addInterfaceRealization((UmlInterfaceRealization) element);
            }
        } else if (element instanceof UmlAssociationEnd) {
            MyAssociation myAssociation = id2association.get(element.getParentId());
            if (myAssociation != null) {
                myAssociation.addAssociationEnd((UmlAssociationEnd) element);
            }
        } else {
            elementsOfClassModel.remove(element);
        }
    }

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

    public void checkForUml001() throws UmlRule001Exception {
        HashSet<AttributeClassInformation> list = new HashSet<>();
        for (MyClass scan : id2class.values()) {
            list.addAll(scan.checkForUml001());
        }
        if (!list.isEmpty()) {
            throw new UmlRule001Exception(list);
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        // scan class
        for (MyClass myClass : id2class.values()) {
            dfsClass(myClass);
        }
        buffer4rule002.clear();
        for (MyInterface myInterface : id2interface.values()) {
            dfsInterface(myInterface);
        }
        if (!elementInCircles.isEmpty()) {
            throw new UmlRule002Exception(elementInCircles);
        }
    }

    public void checkForUml003() throws UmlRule003Exception {
        // class should be ok
        // check interface
        for (MyInterface myInterface : id2interface.values()) {
            if (myInterface.scanLink4rule003()) {
                duplicatedList4rule003.add(myInterface.getOrigin());
            }
            buffer4rule003.clear();
        }
        if (!duplicatedList4rule003.isEmpty()) {
            throw new UmlRule003Exception(duplicatedList4rule003);
        }
    }

    public boolean addGeneralization4rule003(UmlClassOrInterface umlClassOrInterface) {
        if (buffer4rule003.contains(umlClassOrInterface)) {
            return true;
        } else {
            buffer4rule003.add(umlClassOrInterface);
            return false;
        }
    }

    public void checkForUml004() throws UmlRule004Exception {
        for (MyClass myClass : id2class.values()) {
            if (myClass.scanRealization()) {
                repeatRealization4rule004.add(myClass.getOrigin());
            }
            buffer4rule004.clear();
        }
        if (!repeatRealization4rule004.isEmpty()) {
            throw new UmlRule004Exception(repeatRealization4rule004);
        }
    }

    public boolean addInterfaceRealized(MyInterface myInterface) {
        HashSet<UmlInterface> interfaceTree = myInterface.getExpendsTree();
        for (UmlInterface leaf : interfaceTree) {
            if (buffer4rule004.contains(leaf)) {
                return true;
            }
        }
        buffer4rule004.add(myInterface.getOrigin());
        return false;
    }

    public void checkForUml005() throws UmlRule005Exception {
        for (UmlElement element : elementsOfClassModel) {
            checkClassModelName(element);
        }
    }

    private void checkClassModelName(UmlElement element) throws UmlRule005Exception {
        if (element instanceof UmlGeneralization
                || element instanceof UmlAssociation
                || element instanceof UmlInterfaceRealization
                || element instanceof UmlAssociationEnd) {
            return;
        }
        if (element instanceof UmlParameter) {
            if (((UmlParameter) element).getDirection() == Direction.RETURN) {
                return;
            }
        }
        if (element.getName() == null) {
            throw new UmlRule005Exception();
        }
    }

    public void checkForUml006() throws UmlRule006Exception {
        for (MyInterface myInterface : id2interface.values()) {
            if (myInterface.checkAttributesVisibility()) {
                throw new UmlRule006Exception();
            }
        }
    }

    private void dfsClass(MyClass myClass) {
        UmlClassOrInterface umlClassOrInterface = myClass.getOrigin();
        buffer4rule002.add(umlClassOrInterface);
        MyClass father = myClass.getFather();
        if (father != null) {
            if (buffer4rule002.contains(father.getOrigin())) {
                elementInCircles.addAll(getCircleQueue(father.getOrigin()));
            } else {
                dfsClass(father);
            }
        }
        buffer4rule002.remove(umlClassOrInterface);
    }

    private void dfsInterface(MyInterface myInterface) {
        UmlClassOrInterface umlClassOrInterface = myInterface.getOrigin();
        buffer4rule002.add(umlClassOrInterface);
        HashMap<String, MyInterface> id2father = myInterface.getFathers();
        if (id2father != null) {
            for (MyInterface scan : id2father.values()) {
                if (buffer4rule002.contains(scan.getOrigin())) {
                    elementInCircles.addAll(getCircleQueue(scan.getOrigin()));
                } else {
                    dfsInterface(scan);
                }
            }
        }
        buffer4rule002.remove(umlClassOrInterface);
    }

    private HashSet<UmlClassOrInterface> getCircleQueue(UmlClassOrInterface point) {
        HashSet<UmlClassOrInterface> circleList = new HashSet<>();
        boolean findHead = false;
        for (UmlClassOrInterface scan : buffer4rule002) {
            if (scan.equals(point)) {
                findHead = true;
            }
            if (findHead) {
                circleList.add(scan);
            }
        }
        return circleList;
    }

    private void distributeAssociation() {
        for (MyAssociation loop : id2association.values()) {
            String classId = loop.getParentId();
            MyClass myClass = id2class.get(classId);
            if (myClass != null) {
                myClass.addAssociation(loop);
            }
        }
    }

    public HashMap<String, MyAssociation> getAssociationMap() {
        return id2association;
    }

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
        return new ArrayList<>(myClass.getInterfaceRealized().values());
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
