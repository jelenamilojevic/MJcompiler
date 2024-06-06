// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarList extends GlobalConstDeclList {

    private GlobalConstDeclList GlobalConstDeclList;
    private GlobalVarDecl GlobalVarDecl;

    public GlobalVarList (GlobalConstDeclList GlobalConstDeclList, GlobalVarDecl GlobalVarDecl) {
        this.GlobalConstDeclList=GlobalConstDeclList;
        if(GlobalConstDeclList!=null) GlobalConstDeclList.setParent(this);
        this.GlobalVarDecl=GlobalVarDecl;
        if(GlobalVarDecl!=null) GlobalVarDecl.setParent(this);
    }

    public GlobalConstDeclList getGlobalConstDeclList() {
        return GlobalConstDeclList;
    }

    public void setGlobalConstDeclList(GlobalConstDeclList GlobalConstDeclList) {
        this.GlobalConstDeclList=GlobalConstDeclList;
    }

    public GlobalVarDecl getGlobalVarDecl() {
        return GlobalVarDecl;
    }

    public void setGlobalVarDecl(GlobalVarDecl GlobalVarDecl) {
        this.GlobalVarDecl=GlobalVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalConstDeclList!=null) GlobalConstDeclList.accept(visitor);
        if(GlobalVarDecl!=null) GlobalVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalConstDeclList!=null) GlobalConstDeclList.traverseTopDown(visitor);
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalConstDeclList!=null) GlobalConstDeclList.traverseBottomUp(visitor);
        if(GlobalVarDecl!=null) GlobalVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarList(\n");

        if(GlobalConstDeclList!=null)
            buffer.append(GlobalConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarDecl!=null)
            buffer.append(GlobalVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarList]");
        return buffer.toString();
    }
}
