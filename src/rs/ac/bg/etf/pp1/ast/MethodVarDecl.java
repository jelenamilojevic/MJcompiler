// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class MethodVarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private MethIdentArray MethIdentArray;

    public MethodVarDecl (Type Type, MethIdentArray MethIdentArray) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.MethIdentArray=MethIdentArray;
        if(MethIdentArray!=null) MethIdentArray.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public MethIdentArray getMethIdentArray() {
        return MethIdentArray;
    }

    public void setMethIdentArray(MethIdentArray MethIdentArray) {
        this.MethIdentArray=MethIdentArray;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(MethIdentArray!=null) MethIdentArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(MethIdentArray!=null) MethIdentArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(MethIdentArray!=null) MethIdentArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethIdentArray!=null)
            buffer.append(MethIdentArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodVarDecl]");
        return buffer.toString();
    }
}
