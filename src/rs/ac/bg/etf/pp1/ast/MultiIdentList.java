// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class MultiIdentList extends VarDecl {

    private MultiIdent MultiIdent;
    private VarDecl VarDecl;

    public MultiIdentList (MultiIdent MultiIdent, VarDecl VarDecl) {
        this.MultiIdent=MultiIdent;
        if(MultiIdent!=null) MultiIdent.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public MultiIdent getMultiIdent() {
        return MultiIdent;
    }

    public void setMultiIdent(MultiIdent MultiIdent) {
        this.MultiIdent=MultiIdent;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MultiIdent!=null) MultiIdent.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MultiIdent!=null) MultiIdent.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MultiIdent!=null) MultiIdent.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultiIdentList(\n");

        if(MultiIdent!=null)
            buffer.append(MultiIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultiIdentList]");
        return buffer.toString();
    }
}
