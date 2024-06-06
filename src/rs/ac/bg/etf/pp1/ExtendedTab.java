package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

public class ExtendedTab extends Tab {
	public static final Struct boolType = new Struct(Struct.Bool);
	
	public static void tabInit() {
		init();
		currentScope.addToLocals(new Obj(Obj.Type, "bool",boolType));
	}

	public static void dump() {
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
		
		ExtendedSymbolTableVisitor stv = new ExtendedSymbolTableVisitor();
		
		for (Scope s = currentScope; s!=null; s = s.getOuter()) {
			s.accept(stv);
		}
		System.out.println(stv.getOutput());
	}
}
