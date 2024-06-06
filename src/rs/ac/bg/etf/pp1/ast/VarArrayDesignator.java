// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:12


package rs.ac.bg.etf.pp1.ast;

public class VarArrayDesignator extends Designator {

    private String desName;
    private DesignatorArray DesignatorArray;

    public VarArrayDesignator (String desName, DesignatorArray DesignatorArray) {
        this.desName=desName;
        this.DesignatorArray=DesignatorArray;
        if(DesignatorArray!=null) DesignatorArray.setParent(this);
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName=desName;
    }

    public DesignatorArray getDesignatorArray() {
        return DesignatorArray;
    }

    public void setDesignatorArray(DesignatorArray DesignatorArray) {
        this.DesignatorArray=DesignatorArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorArray!=null) DesignatorArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorArray!=null) DesignatorArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorArray!=null) DesignatorArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarArrayDesignator(\n");

        buffer.append(" "+tab+desName);
        buffer.append("\n");

        if(DesignatorArray!=null)
            buffer.append(DesignatorArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarArrayDesignator]");
        return buffer.toString();
    }
}
