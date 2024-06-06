// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:12


package rs.ac.bg.etf.pp1.ast;

public class RemainingOp extends Mulop {

    public RemainingOp () {
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
        buffer.append("RemainingOp(\n");

        buffer.append(tab);
        buffer.append(") [RemainingOp]");
        return buffer.toString();
    }
}
