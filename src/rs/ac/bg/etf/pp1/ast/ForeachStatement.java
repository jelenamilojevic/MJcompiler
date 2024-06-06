// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class ForeachStatement extends Matched {

    private Designator Designator;
    private ForEachIdent ForEachIdent;
    private Matched Matched;

    public ForeachStatement (Designator Designator, ForEachIdent ForEachIdent, Matched Matched) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ForEachIdent=ForEachIdent;
        if(ForEachIdent!=null) ForEachIdent.setParent(this);
        this.Matched=Matched;
        if(Matched!=null) Matched.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ForEachIdent getForEachIdent() {
        return ForEachIdent;
    }

    public void setForEachIdent(ForEachIdent ForEachIdent) {
        this.ForEachIdent=ForEachIdent;
    }

    public Matched getMatched() {
        return Matched;
    }

    public void setMatched(Matched Matched) {
        this.Matched=Matched;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ForEachIdent!=null) ForEachIdent.accept(visitor);
        if(Matched!=null) Matched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ForEachIdent!=null) ForEachIdent.traverseTopDown(visitor);
        if(Matched!=null) Matched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ForEachIdent!=null) ForEachIdent.traverseBottomUp(visitor);
        if(Matched!=null) Matched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForeachStatement(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForEachIdent!=null)
            buffer.append(ForEachIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Matched!=null)
            buffer.append(Matched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForeachStatement]");
        return buffer.toString();
    }
}
