// generated with ast extension for cup
// version 0.8
// 6/5/2024 11:51:11


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Matched {

    private PrintOption PrintOption;

    public PrintStatement (PrintOption PrintOption) {
        this.PrintOption=PrintOption;
        if(PrintOption!=null) PrintOption.setParent(this);
    }

    public PrintOption getPrintOption() {
        return PrintOption;
    }

    public void setPrintOption(PrintOption PrintOption) {
        this.PrintOption=PrintOption;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(PrintOption!=null) PrintOption.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PrintOption!=null) PrintOption.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PrintOption!=null) PrintOption.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(PrintOption!=null)
            buffer.append(PrintOption.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
