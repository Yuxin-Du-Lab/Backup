import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;

import java.util.HashMap;
import java.util.Map;

public class MyAssociation {
    private final UmlAssociation umlAssociation;
    private final MyUmlClassModelInteraction myUmlClassModelInteraction;
    private final Map<String, UmlAssociationEnd> referenceId2associationEnd = new HashMap<>();

    public MyAssociation(
            UmlAssociation umlAssociation,
            MyUmlClassModelInteraction myUmlClassModelInteraction) {
        this.umlAssociation = umlAssociation;
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
    }

    public void addAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        referenceId2associationEnd.put(umlAssociationEnd.getReference(), umlAssociationEnd);
    }

    public String getName() {
        return umlAssociation.getName();
    }

    public void check() {
        System.out.println("Association name:" + umlAssociation.getName());
        for (UmlAssociationEnd umlAssociationEnd: referenceId2associationEnd.values()) {
            System.out.println("End:" + umlAssociationEnd.getName());
        }
    }

    public String getAssociated(String end1) {
        if (referenceId2associationEnd.containsKey(end1)) {
            for (String id: referenceId2associationEnd.keySet()) {
                if (!id.equals(end1)) {
                    return id;
                }
            }
            return end1;    // self associate
        }
        return null;
    }
}
