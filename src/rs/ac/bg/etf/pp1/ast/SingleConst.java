// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class SingleConst extends ConstDecl {

    private SingleConstDecl SingleConstDecl;

    public SingleConst (SingleConstDecl SingleConstDecl) {
        this.SingleConstDecl=SingleConstDecl;
        if(SingleConstDecl!=null) SingleConstDecl.setParent(this);
    }

    public SingleConstDecl getSingleConstDecl() {
        return SingleConstDecl;
    }

    public void setSingleConstDecl(SingleConstDecl SingleConstDecl) {
        this.SingleConstDecl=SingleConstDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SingleConstDecl!=null) SingleConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleConstDecl!=null) SingleConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleConstDecl!=null) SingleConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleConst(\n");

        if(SingleConstDecl!=null)
            buffer.append(SingleConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleConst]");
        return buffer.toString();
    }
}
