// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class MultiMethIdentList extends MethIdentArray {

    private MultiMethIdent MultiMethIdent;
    private MethIdentArray MethIdentArray;

    public MultiMethIdentList (MultiMethIdent MultiMethIdent, MethIdentArray MethIdentArray) {
        this.MultiMethIdent=MultiMethIdent;
        if(MultiMethIdent!=null) MultiMethIdent.setParent(this);
        this.MethIdentArray=MethIdentArray;
        if(MethIdentArray!=null) MethIdentArray.setParent(this);
    }

    public MultiMethIdent getMultiMethIdent() {
        return MultiMethIdent;
    }

    public void setMultiMethIdent(MultiMethIdent MultiMethIdent) {
        this.MultiMethIdent=MultiMethIdent;
    }

    public MethIdentArray getMethIdentArray() {
        return MethIdentArray;
    }

    public void setMethIdentArray(MethIdentArray MethIdentArray) {
        this.MethIdentArray=MethIdentArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MultiMethIdent!=null) MultiMethIdent.accept(visitor);
        if(MethIdentArray!=null) MethIdentArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MultiMethIdent!=null) MultiMethIdent.traverseTopDown(visitor);
        if(MethIdentArray!=null) MethIdentArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MultiMethIdent!=null) MultiMethIdent.traverseBottomUp(visitor);
        if(MethIdentArray!=null) MethIdentArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultiMethIdentList(\n");

        if(MultiMethIdent!=null)
            buffer.append(MultiMethIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethIdentArray!=null)
            buffer.append(MethIdentArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultiMethIdentList]");
        return buffer.toString();
    }
}
