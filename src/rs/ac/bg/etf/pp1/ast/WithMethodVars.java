// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class WithMethodVars extends MethodVarDeclList {

    private MethodVarDeclList MethodVarDeclList;
    private MethodVarDecl MethodVarDecl;

    public WithMethodVars (MethodVarDeclList MethodVarDeclList, MethodVarDecl MethodVarDecl) {
        this.MethodVarDeclList=MethodVarDeclList;
        if(MethodVarDeclList!=null) MethodVarDeclList.setParent(this);
        this.MethodVarDecl=MethodVarDecl;
        if(MethodVarDecl!=null) MethodVarDecl.setParent(this);
    }

    public MethodVarDeclList getMethodVarDeclList() {
        return MethodVarDeclList;
    }

    public void setMethodVarDeclList(MethodVarDeclList MethodVarDeclList) {
        this.MethodVarDeclList=MethodVarDeclList;
    }

    public MethodVarDecl getMethodVarDecl() {
        return MethodVarDecl;
    }

    public void setMethodVarDecl(MethodVarDecl MethodVarDecl) {
        this.MethodVarDecl=MethodVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodVarDeclList!=null) MethodVarDeclList.accept(visitor);
        if(MethodVarDecl!=null) MethodVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodVarDeclList!=null) MethodVarDeclList.traverseTopDown(visitor);
        if(MethodVarDecl!=null) MethodVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodVarDeclList!=null) MethodVarDeclList.traverseBottomUp(visitor);
        if(MethodVarDecl!=null) MethodVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WithMethodVars(\n");

        if(MethodVarDeclList!=null)
            buffer.append(MethodVarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodVarDecl!=null)
            buffer.append(MethodVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WithMethodVars]");
        return buffer.toString();
    }
}
