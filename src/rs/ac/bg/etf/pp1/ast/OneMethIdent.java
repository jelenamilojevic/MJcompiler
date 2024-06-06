// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class OneMethIdent extends MethIdentArray {

    private SingleMethIdent SingleMethIdent;

    public OneMethIdent (SingleMethIdent SingleMethIdent) {
        this.SingleMethIdent=SingleMethIdent;
        if(SingleMethIdent!=null) SingleMethIdent.setParent(this);
    }

    public SingleMethIdent getSingleMethIdent() {
        return SingleMethIdent;
    }

    public void setSingleMethIdent(SingleMethIdent SingleMethIdent) {
        this.SingleMethIdent=SingleMethIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleMethIdent!=null) SingleMethIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleMethIdent!=null) SingleMethIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleMethIdent!=null) SingleMethIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneMethIdent(\n");

        if(SingleMethIdent!=null)
            buffer.append(SingleMethIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneMethIdent]");
        return buffer.toString();
    }
}
