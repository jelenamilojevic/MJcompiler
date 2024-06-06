// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:12


package rs.ac.bg.etf.pp1.ast;

public class VarDesignator extends Designator {

    private String desName;

    public VarDesignator (String desName) {
        this.desName=desName;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName=desName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDesignator(\n");

        buffer.append(" "+tab+desName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDesignator]");
        return buffer.toString();
    }
}
