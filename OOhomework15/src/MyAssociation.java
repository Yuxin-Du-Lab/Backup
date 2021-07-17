import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;

public class MyAssociation {
    private final UmlAssociation umlAssociation;
    private final MyUmlClassModelInteraction myUmlClassModelInteraction;
    private final UmlAssociationEnd[] ends = new UmlAssociationEnd[2];
    private int index = 0;

    public MyAssociation(
            UmlAssociation umlAssociation,
            MyUmlClassModelInteraction myUmlClassModelInteraction) {
        this.umlAssociation = umlAssociation;
        this.myUmlClassModelInteraction = myUmlClassModelInteraction;
    }

    public void addAssociationEnd(UmlAssociationEnd umlAssociationEnd) {
        ends[index++] = umlAssociationEnd;
    }

    public String getName() {
        return umlAssociation.getName();
    }

    public void check() {
        System.out.println("Association name:" + umlAssociation.getName());
        for (int i = 0; i < 2; i++) {
            UmlAssociationEnd umlAssociationEnd = ends[i];
            System.out.println("End:" + umlAssociationEnd.getName());
        }
    }

    public boolean isSelfLink() {
        return ends[0].getReference().equals(ends[1].getReference());
    }

    public String getAssociated(String end1referenceId) {
        // input class id, return associated class id
        if (isSelfLink()) {
            return end1referenceId;
        }
        if (ends[0].getReference().equals(end1referenceId)) {
            // ends[0] is end1
            return ends[1].getReference();
        }
        if (ends[1].getReference().equals(end1referenceId)) {
            return ends[0].getReference();
        }
        return null;
    }

    public String getId() {
        return umlAssociation.getId();
    }

    public String getParentId() {
        return umlAssociation.getParentId();
    }

    public UmlAssociationEnd[] getEnds() {
        return ends;
    }
}
