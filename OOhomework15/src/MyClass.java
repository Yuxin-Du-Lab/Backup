import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.OperationParamInformation;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyClass {
    private final UmlClass umlClass;
    private final MyUmlClassModelInteraction myUmlClassModelInteraction;
    private final Map<String, UmlAttribute> id2attribute = new HashMap<>();
    private final Map<String, String> name2id4attribute = new HashMap<>();
    private final ArrayList<String> attributeDuplicatedName = new ArrayList<>();

    private final Map<String, MyOperation> id2operation = new HashMap<>();
    private final Map<String, MyAssociation> id2association = new HashMap<>();

    private final Map<String, UmlInterfaceRealization> id2interfaceRealization = new HashMap<>();
    private final HashSet<MyInterface> interfaceRealized = new HashSet<>();
    private UmlGeneralization umlGeneralization = null;
    private MyClass father = null;

    public MyClass(UmlClass umlClass, MyUmlClassModelInteraction myUmlClassModelInteraction) {
        this.umlClass = umlClass;
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
    }

    public int getOperationCount() {
        return id2operation.size();
    }

    public int getAttributeCount() {
        if (umlGeneralization != null) {
            getFather();
            int fatherAttributeCnt = father.getAttributeCount();
            return id2attribute.size() + fatherAttributeCnt;
        } else {
            return id2attribute.size();
        }
    }

    public HashSet<AttributeClassInformation> checkForUml001() {
        // Check attributes name repeat
        HashSet<AttributeClassInformation> list = new HashSet<>();
        String className = getName();
        ArrayList<String> buffer = new ArrayList<>();
        for (UmlAttribute scan : id2attribute.values()) {
            String attributeName = scan.getName();
            if (attributeName == null) {
                continue;
            }
            if (buffer.contains(attributeName)) {
                list.add(new AttributeClassInformation(attributeName, className));
            } else {
                buffer.add(attributeName);
            }
        }
        String classId = umlClass.getId();
        for (MyAssociation scan : myUmlClassModelInteraction.getAssociationMap().values()) {
            UmlAssociationEnd[] ends = scan.getEnds();
            if (ends[0].getReference().equals(classId)) {
                if (ends[1].getName() != null) {
                    if (buffer.contains(ends[1].getName())) {
                        list.add(new AttributeClassInformation(ends[1].getName(), className));
                    } else {
                        buffer.add(ends[1].getName());
                    }
                }
            }
            if (ends[1].getReference().equals(classId)) {
                if (ends[0].getName() != null) {
                    if (buffer.contains(ends[0].getName())) {
                        list.add(new AttributeClassInformation(ends[0].getName(), className));
                    } else {
                        buffer.add(ends[0].getName());
                    }
                }
            }
        }
        return list;
    }

    public boolean scanRealization() {
        getRealizations();
        for (MyInterface myInterface : interfaceRealized) {
            if (myUmlClassModelInteraction.addInterfaceRealized(myInterface)) {
                return true;
            }
        }
        getFather();
        if (father != null) {
            return father.scanRealization();
        }
        return false;
    }

    private void getRealizations() {
        if (interfaceRealized.isEmpty() && !id2interfaceRealization.isEmpty()) {
            for (UmlInterfaceRealization umlInterfaceRealization :
                    id2interfaceRealization.values()) {
                String targetInterfaceId = umlInterfaceRealization.getTarget();
                MyInterface myInterface = myUmlClassModelInteraction.
                        getMyInterface(targetInterfaceId);
                if (myInterface != null) {
                    interfaceRealized.add(myInterface);
                }
            }
        }
    }

    public void addAssociation(MyAssociation myAssociation) {
        id2association.put(myAssociation.getId(), myAssociation);
    }

    public Map<Visibility, Integer> getOperationVisibility(String operationName) {
        Map<Visibility, Integer> map = new HashMap<>();
        int publicNum = 0;
        int protectNum = 0;
        int privateNum = 0;
        int packageNum = 0;
        map.put(Visibility.PUBLIC, publicNum);
        map.put(Visibility.PROTECTED, protectNum);
        map.put(Visibility.PRIVATE, privateNum);
        map.put(Visibility.PACKAGE, packageNum);
        for (MyOperation myOperation : id2operation.values()) {
            if (myOperation.getName().equals(operationName)) {
                switch (myOperation.getVisibility()) {
                    case PUBLIC:
                        map.put(Visibility.PUBLIC, ++publicNum);
                        break;
                    case PROTECTED:
                        map.put(Visibility.PROTECTED, ++protectNum);
                        break;
                    case PRIVATE:
                        map.put(Visibility.PRIVATE, ++privateNum);
                        break;
                    case PACKAGE:
                        map.put(Visibility.PACKAGE, ++packageNum);
                        break;
                    default:
                }
            }
        }
        return map;
    }

    public Visibility getAttributeVisibility(String className, String attributeName)
            throws AttributeDuplicatedException {
        Visibility visibilityFather = null;
        if (umlGeneralization != null) {
            getFather();
            visibilityFather = father.getAttributeVisibility(className, attributeName);
        }
        if (visibilityFather != null && name2id4attribute.containsKey(attributeName)
                || attributeDuplicatedName.contains(attributeName)) {
            throw new AttributeDuplicatedException(className, attributeName);
        }
        if (visibilityFather != null) {
            return visibilityFather;
        }
        String attributeId = name2id4attribute.get(attributeName);
        if (attributeId != null) {
            return id2attribute.get(attributeId).getVisibility();
        }
        return null;
    }

    public String getAttributeType(String className, String attributeName)
            throws AttributeDuplicatedException {
        String typeFather = null;
        if (umlGeneralization != null) {
            getFather();
            typeFather = father.getAttributeType(className, attributeName);
        }
        if (typeFather != null && name2id4attribute.containsKey(attributeName)
                || attributeDuplicatedName.contains(attributeName)) {
            throw new AttributeDuplicatedException(className, attributeName);
        }
        if (typeFather != null) {
            return typeFather;
        }
        String attributeId = name2id4attribute.get(attributeName);
        if (attributeId != null) {
            UmlAttribute umlAttribute = id2attribute.get(attributeId);
            return umlAttribute.getType().toJsonString();
        }
        return null;
    }

    public List<OperationParamInformation> getOperationParamType(
            String className, String operationName)
            throws MethodWrongTypeException, MethodDuplicatedException {
        List<OperationParamInformation> operationParamInformationList = new ArrayList<>();
        for (MyOperation myOperation : id2operation.values()) {
            if (myOperation.getName().equals(operationName)) {
                List<String> parameterTypes = myOperation.getParaList(className, operationName);
                String returnType = myOperation.getReturnType(className, operationName);
                OperationParamInformation operationParamInformation =
                        new OperationParamInformation(parameterTypes, returnType);
                if (isDuplicated(operationParamInformationList, operationParamInformation)) {
                    throw new MethodDuplicatedException(className, operationName);
                }
                operationParamInformationList.add(operationParamInformation);
            }
        }
        return operationParamInformationList;
    }

    public Map<String, String> getAssociatedClassList() {
        Map<String, String> id2name4associatedClasses = new HashMap<>();
        if (umlGeneralization != null) {
            getFather();
            id2name4associatedClasses.putAll(father.getAssociatedClassList());
        }
        id2name4associatedClasses.putAll(
                myUmlClassModelInteraction.getAssociateClasses(
                        umlClass.getId()));
        return id2name4associatedClasses;
    }

    public MyClass getTopFather() {
        getFather();
        if (father == null) {
            return this;
        } else {
            return father.getTopFather();
        }
    }

    public Map<String, String> getInterfaceRealized() {
        Map<String, String> id2name4interfaceRealization = new HashMap<>();
        if (umlGeneralization != null) {
            getFather();
            id2name4interfaceRealization.putAll(father.getInterfaceRealized());
        }
        for (UmlInterfaceRealization umlInterfaceRealization : id2interfaceRealization.values()) {
            String interfaceId = umlInterfaceRealization.getTarget();
            MyInterface myInterface = myUmlClassModelInteraction.getMyInterface(interfaceId);

            //interfaceRealizationNames.addAll(myInterface.getUpCastingNames());
            id2name4interfaceRealization.putAll(myInterface.getUpCasting());
            myInterface.clearTouched();
        }
        return id2name4interfaceRealization;
    }

    public List<AttributeClassInformation> checkAttributeHidden() {
        List<AttributeClassInformation> attributeClassInformationList = new ArrayList<>();
        if (umlGeneralization != null) {
            getFather();
            attributeClassInformationList.addAll(father.checkAttributeHidden());
        }
        for (UmlAttribute umlAttribute : id2attribute.values()) {
            if (umlAttribute.getVisibility() != Visibility.PRIVATE) {
                AttributeClassInformation attributeClassInformation =
                        new AttributeClassInformation(umlAttribute.getName(), umlClass.getName());
                attributeClassInformationList.add(attributeClassInformation);
            }
        }
        return attributeClassInformationList;
    }

    public boolean isDuplicated(
            List<OperationParamInformation> operationParamInformationList,
            OperationParamInformation operationParamInformation) {
        for (OperationParamInformation loop : operationParamInformationList) {
            if (loop.equals(operationParamInformation)) {
                return true;
            }
        }
        return false;
    }

    public void addAttribute(UmlAttribute umlAttribute) {
        id2attribute.put(umlAttribute.getId(), umlAttribute);
        if (name2id4attribute.containsKey(umlAttribute.getName())) {
            attributeDuplicatedName.add(umlAttribute.getName());
        } else {
            name2id4attribute.put(umlAttribute.getName(), umlAttribute.getId());
        }
    }

    public MyOperation addOperation(UmlOperation umlOperation) {
        MyOperation myOperation = new MyOperation(umlOperation, myUmlClassModelInteraction);
        id2operation.put(umlOperation.getId(), myOperation);
        return myOperation;
    }

    public void setUmlGeneralization(UmlGeneralization umlGeneralization) {
        if (this.umlGeneralization == null) {
            this.umlGeneralization = umlGeneralization;
        }
        /* may exception */
    }

    public void addInterfaceRealization(UmlInterfaceRealization umlInterfaceRealization) {
        id2interfaceRealization.put(umlInterfaceRealization.getId(), umlInterfaceRealization);
    }

    public String getName() {
        return umlClass.getName();
    }

    public void check() {
        System.out.println("Class name:" + umlClass.getName());
        if (umlGeneralization != null) {
            System.out.println("Generalization:" + umlGeneralization.getName());
        }
        System.out.println("Interface Realizations:");
        for (UmlInterfaceRealization umlInterfaceRealization : id2interfaceRealization.values()) {
            System.out.println(umlInterfaceRealization.getName());
        }
        System.out.println("Attributes:");
        for (UmlAttribute umlAttribute : id2attribute.values()) {
            System.out.println(umlAttribute.getName());
        }
        System.out.println("Operations:");
        for (MyOperation myOperation : id2operation.values()) {
            myOperation.check();
        }
    }

    public MyClass getFather() {
        if (father == null && umlGeneralization != null) {
            String fatherId = umlGeneralization.getTarget();
            father = myUmlClassModelInteraction.getMyClass(fatherId);
        }
        return father;
    }

    public UmlClass getOrigin() {
        return umlClass;
    }

}
