// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class FindReplaceStatement extends Matched {

    private Designator Designator;
    private Assignop Assignop;
    private Designator Designator1;
    private Expr Expr;
    private FindReplaceIdent FindReplaceIdent;
    private Expr Expr2;

    public FindReplaceStatement (Designator Designator, Assignop Assignop, Designator Designator1, Expr Expr, FindReplaceIdent FindReplaceIdent, Expr Expr2) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.Assignop=Assignop;
        if(Assignop!=null) Assignop.setParent(this);
        this.Designator1=Designator1;
        if(Designator1!=null) Designator1.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.FindReplaceIdent=FindReplaceIdent;
        if(FindReplaceIdent!=null) FindReplaceIdent.setParent(this);
        this.Expr2=Expr2;
        if(Expr2!=null) Expr2.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public Assignop getAssignop() {
        return Assignop;
    }

    public void setAssignop(Assignop Assignop) {
        this.Assignop=Assignop;
    }

    public Designator getDesignator1() {
        return Designator1;
    }

    public void setDesignator1(Designator Designator1) {
        this.Designator1=Designator1;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public FindReplaceIdent getFindReplaceIdent() {
        return FindReplaceIdent;
    }

    public void setFindReplaceIdent(FindReplaceIdent FindReplaceIdent) {
        this.FindReplaceIdent=FindReplaceIdent;
    }

    public Expr getExpr2() {
        return Expr2;
    }

    public void setExpr2(Expr Expr2) {
        this.Expr2=Expr2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(Assignop!=null) Assignop.accept(visitor);
        if(Designator1!=null) Designator1.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(FindReplaceIdent!=null) FindReplaceIdent.accept(visitor);
        if(Expr2!=null) Expr2.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(Assignop!=null) Assignop.traverseTopDown(visitor);
        if(Designator1!=null) Designator1.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(FindReplaceIdent!=null) FindReplaceIdent.traverseTopDown(visitor);
        if(Expr2!=null) Expr2.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(Assignop!=null) Assignop.traverseBottomUp(visitor);
        if(Designator1!=null) Designator1.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(FindReplaceIdent!=null) FindReplaceIdent.traverseBottomUp(visitor);
        if(Expr2!=null) Expr2.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FindReplaceStatement(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Assignop!=null)
            buffer.append(Assignop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Designator1!=null)
            buffer.append(Designator1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindReplaceIdent!=null)
            buffer.append(FindReplaceIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr2!=null)
            buffer.append(Expr2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FindReplaceStatement]");
        return buffer.toString();
    }
}
