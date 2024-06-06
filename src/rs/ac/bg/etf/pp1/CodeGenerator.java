package rs.ac.bg.etf.pp1;

import java.util.HashMap;
import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPc;
	private Stack<Integer> forEachStack = new Stack<Integer>();
	private Stack<Integer> findReplaceStack = new Stack<Integer>();
	int enterFindReplace = 0;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(MethodVoidType method) {
		if ("main".equalsIgnoreCase(method.getMethName())) {
			mainPc = Code.pc;
		}
	   	method.obj.setAdr(Code.pc);
	   	
	   	//dohvatanje argumenata i lokalnih varijabli
	   	
	   	SyntaxNode methodNode = method.getParent();
	   	
	   	VarCounter varCnt = new VarCounter();
	   	methodNode.traverseTopDown(varCnt);
	   	
	   	FormParamCounter formParamCnt = new FormParamCounter();
	   	methodNode.traverseTopDown(formParamCnt);
	   	
	   	
	   	//generisanje ulaza u funkciju
	   	Code.put(Code.enter);
	   	Code.put(formParamCnt.getCount());
	   	Code.put(formParamCnt.getCount()+varCnt.getCount());
	   	
	}
	
	public void visit(MethodWithType method) {
		method.obj.setAdr(Code.pc);
		
	   	//dohvatanje argumenata i lokalnih varijabli
	   	
	   	SyntaxNode methodNode = method.getParent();
	   	
	   	VarCounter varCnt = new VarCounter();
	   	methodNode.traverseTopDown(varCnt);
	   	
	   	FormParamCounter formParamCnt = new FormParamCounter();
	   	methodNode.traverseTopDown(formParamCnt);
	   	
	   	
	   	//generisanje ulaza u funkciju
	   	Code.put(Code.enter);
	   	Code.put(formParamCnt.getCount());
	   	Code.put(formParamCnt.getCount()+varCnt.getCount());
	}
	
	public void visit(MethodDecl decl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//*********** obradjivanje matched statement-a
	
	public void visit(ReadStatement statement) {
		//kada se obradjuje expr stavlja se na stek, ovde je vec na steku
		//dodajemo width na stek i pozivamo instrukciju
		if (statement.getDesignator() instanceof VarDesignator) {
			if (statement.getDesignator().obj.getType() == Tab.intType) {
				Code.put(Code.read);
				Code.store(statement.getDesignator().obj);
			} else if (statement.getDesignator().obj.getType() == ExtendedTab.boolType) {
				Code.put(Code.read);
				Code.store(statement.getDesignator().obj);
				System.out.println("bool!");
				//Code.put(Code.bread);
				//Code.put(Code.pop);
			} else {
				Code.put(Code.bread);
				Code.store(statement.getDesignator().obj);
				Code.put(Code.bread);
				Code.put(Code.pop);
			}
		} else {
			if (statement.getDesignator() instanceof VarArrayDesignator) {
				Code.put(Code.pop);
				VarArrayDesignator des = (VarArrayDesignator) statement.getDesignator();
				SyntaxNode index = des.getDesignatorArray();
				DesignatorArray elem = (DesignatorArray) des.getDesignatorArray();
				Code.load(statement.getDesignator().obj);
				index.traverseBottomUp(this);
				if (elem.obj.getType() == Tab.intType) {
					Code.put(Code.read);
					Code.store(elem.obj);
				} else if (elem.obj.getType() == ExtendedTab.boolType) {
					Code.put(Code.read);
					Code.store(elem.obj);
					System.out.println("bool!");
				} else {
					Code.put(Code.bread);
					Code.store(elem.obj);
					Code.put(Code.bread);
					Code.put(Code.pop);
				}
			}
		}
	}
	
	public void visit(PlainExpr plainPrint) {
		//kada se obradjuje expr stavlja se na stek, ovde je vec na steku
		//dodajemo width na stek i pozivamo instrukciju
		if (plainPrint.getExpr().struct == Tab.intType) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else if (plainPrint.getExpr().struct == ExtendedTab.boolType) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ExprWithNum printNum) {
		//kada se obradjuje expr stavlja se na stek, ovde je vec na steku, samo dodamo number iz smene
		//dodajemo width na stek i pozivamo instrukciju
		if (printNum.getExpr().struct == Tab.intType) {
			Code.loadConst(printNum.getN1());
			Code.put(Code.print);
		} else if (printNum.getExpr().struct == ExtendedTab.boolType) {
			Code.loadConst(printNum.getN1());
			Code.put(Code.print);
		} else {
			Code.loadConst(printNum.getN1());
			Code.put(Code.bprint);
		}
	}
	
	public void visit(FindAnyStatement statement) {
		Code.put(Code.pop); // ocisti stek
		int forward = 0, back = 0;

		//promenljiva za index
		Obj con = Tab.insert(Obj.Var, "arrIndex", Tab.intType);
		//con.setLevel(0);
		Code.loadConst(0);
		Code.store(con);
		back = Code.pc;
		
		//load elem[index]
		Code.load(statement.getDesignator1().obj); //ucitaj adresu niza
		//ucitaj index
		Code.load(con);
		//ucitaj elem
		if (statement.getDesignator1().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.baload);			
		} else {
			Code.put(Code.aload);	
		}
		
		//load expr
		SyntaxNode expr = statement.getExpr();
		expr.traverseBottomUp(this);
		forward = Code.pc;
		
		Code.putFalseJump(Code.ne, 0);
		
		//jne ako nisu jednaki predji na sledeci + povecaj negde index
		Code.load(con);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(con);
		
		
		//arraylength
		Code.load(statement.getDesignator1().obj); //ucitaj adresu niza za arraylength
		Code.put(Code.arraylength);
		
		Code.load(con);
		
		Code.putFalseJump(Code.le, back);
		//ako nema vise elem stavi false
		Code.loadConst(0);
		Code.store(statement.getDesignator().obj);
		int endFindAny = Code.pc;
		Code.putJump(0);
		
		//ako su isti ostavi true na steku i sacuvaj
		Code.fixup(forward+1);
		Code.loadConst(1);
		Code.store(statement.getDesignator().obj); 
		Code.fixup(endFindAny+1);
	}
	
	public void visit(ForeachStatement statement) {
		Code.fixup(forEachStack.pop()+1); //preskoci naredbe pre
		
		int out = 0, top = 0;
		
		Obj con = Tab.insert(Obj.Var, "arrIndex", Tab.intType);
		Obj ident = statement.getForEachIdent().obj;
		Code.load(ident);

		//con.setLevel(0);
		Code.loadConst(0);
		Code.store(con);
	
		Code.loadConst(0);
        Code.store(ident);
        
        top = Code.pc;
		
		Code.load(con);
		Code.load(statement.getDesignator().obj);
		Code.put(Code.arraylength);
		
		out = Code.pc;
		Code.putFalseJump(Code.lt, 0);
		
		//ucitaj tek elem u pom
		Code.load(statement.getDesignator().obj);
		Code.load(con);
		if (statement.getDesignator().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.baload);			
		} else {
			Code.put(Code.aload);	
		}
		//store temp var
		Code.store(ident);
		
		Code.load(con);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(con);
		SyntaxNode stmt = statement.getMatched();
		stmt.traverseBottomUp(this);
		
		Code.putJump(top);
		
		Code.fixup(out+1);
		Code.store(ident);
	}
	
	public void visit(FindReplaceStatement statement) {
		Code.fixup(findReplaceStack.pop()+1);
		
		int out = 0, top = 0;
		
		//allocate array for saving
		Code.load(statement.getDesignator1().obj);
		Code.put(Code.arraylength);
    	Code.put(Code.newarray);
    	Code.put(Code.const_1);
    	Code.store(statement.getDesignator().obj);
		
		Obj con = Tab.insert(Obj.Var, "arrIndex", Tab.intType);
		Obj ident = statement.getFindReplaceIdent().obj;
		Code.load(ident);

		con.setLevel(0);
		Code.loadConst(0);
		Code.store(con);
	
		Code.loadConst(0);
        Code.store(ident);
        
        top = Code.pc;
        
		Code.load(con);
		Code.load(statement.getDesignator1().obj);
		Code.put(Code.arraylength);
		
		out = Code.pc;
		Code.putFalseJump(Code.lt, 0); //check more elem
		
		int equal = 0, nequal = 0, skip = 0;
		
		// check ident and expr 1
		//ucitaj tek elem
		Code.load(statement.getDesignator1().obj);
		Code.load(con);
		if (statement.getDesignator1().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.baload);			
		} else {
			Code.put(Code.aload);	
		}
		SyntaxNode ex1 = statement.getExpr();
		ex1.traverseBottomUp(this);
		
		equal = Code.pc;
		Code.putFalseJump(Code.ne,0);
		
		// not equal just copy elem
		Code.load(statement.getDesignator().obj); //adr and index for store
		Code.load(con);
		//load value
		Code.load(statement.getDesignator1().obj);
		Code.load(con);
		if (statement.getDesignator1().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.baload);			
		} else {
			Code.put(Code.aload);	
		}
		if (statement.getDesignator1().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.bastore);			
		} else {
			Code.put(Code.astore);	
		}
		//preskoci kod za equal
		skip = Code.pc;
		Code.putJump(0);
		
		//equal, assign expr 2
		Code.fixup(equal+1);
		
		Code.load(statement.getDesignator().obj); //adr and index for store
		Code.load(con);
		
		SyntaxNode ex2 = statement.getExpr2();
		ex2.traverseBottomUp(this);
		
		if (statement.getDesignator1().obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(Code.bastore);			
		} else {
			Code.put(Code.astore);	
		}
		
		Code.fixup(skip+1);
		//inc index
		Code.load(con);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(con);
		
		Code.putJump(top); //go back to top
		
		Code.fixup(out+1);
		Code.store(ident);
		
		enterFindReplace = 0;
	}
	
	public void visit(ReturnWithExprStatement statement) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(PlainReturnStatement statement) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//*********** obradjivanje designator statement-a
	
	public void visit(AssignDesignator assignement) {
		//kada se obradjuje expr vrednost se stavi na expr stack, ovde je samo pokupiumo sa steka i sacuvamo na expr steku
		if (assignement.getDesignator() instanceof VarArrayDesignator) {
			VarArrayDesignator des = (VarArrayDesignator) assignement.getDesignator();
			Code.put(Code.pop);
			Code.put(Code.pop);
			Code.load(des.obj); //adresa
			
			SyntaxNode index= des.getDesignatorArray();
			index.traverseBottomUp(this); //indeks

			SyntaxNode ex = assignement.getExpr();
			ex.traverseBottomUp(this); //value
			
			Code.store(des.getDesignatorArray().obj);
		} else {
			Code.store(assignement.getDesignator().obj);	
		}
	}
	
	public void visit(ProcCall procCall) {
		if (procCall.getDesignator().obj.getName().contentEquals("chr") || procCall.getDesignator().obj.getName().contentEquals("ord")) {
			Code.put(Code.pop);
		} else if (procCall.getDesignator().obj.getName().contentEquals("len")) {
			Code.put(Code.pop);
		} else {
		//racunanje offset-a
		  Obj funObj = procCall.getDesignator().obj;
		  int offset = funObj.getAdr() - Code.pc;
		
		  //poziv funkcije
		  Code.put(Code.call);
		  Code.put2(offset);
		
		  if (procCall.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		  }
		}
	}
	
	public void visit(IncrementDes increment) {
		if (increment.getDesignator() instanceof VarArrayDesignator) {
			VarArrayDesignator designator = (VarArrayDesignator) increment.getDesignator();
						
			Code.put(Code.pop);
			Code.load(designator.obj);
			
			SyntaxNode index = designator.getDesignatorArray();
			index.traverseBottomUp(this);
			
			Code.put(Code.dup2);
		
			Code.load(designator.getDesignatorArray().obj);
			
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(designator.getDesignatorArray().obj);
		} else {
			Code.put(Code.const_1);
			Code.put(Code.add);
			Code.store(increment.getDesignator().obj);
		}
	}
	
	public void visit(DecrementDes decrement) {
		if (decrement.getDesignator() instanceof VarArrayDesignator) {
			VarArrayDesignator designator = (VarArrayDesignator) decrement.getDesignator();
			
			Code.put(Code.pop);
			Code.load(designator.obj);
			
			SyntaxNode index = designator.getDesignatorArray();
			index.traverseBottomUp(this);
			
			Code.put(Code.dup2);
		
			Code.load(designator.getDesignatorArray().obj);
			
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(designator.getDesignatorArray().obj);
		} else {
			Code.put(Code.const_1);
			Code.put(Code.sub);
			Code.store(decrement.getDesignator().obj);			
		}
	}
	
	//********* obradjivanje faktora
	
	public void visit(NumConst num) {
		Obj con = Tab.insert(Obj.Con, "$", num.struct);
		con.setLevel(0);
		con.setAdr(num.getN1());
		//stavi na expr stack
		Code.load(con);
	}
	
	public void visit(CharConst ch) {
		Obj con = Tab.insert(Obj.Con, "$", ch.struct);
		con.setLevel(0);
		con.setAdr(ch.getC1());
		Code.load(con);
	}
	
	
	public void visit(BoolConst b) {
		Obj con = Tab.insert(Obj.Con, "$", b.struct);
		con.setLevel(0);
		if (b.getB1().contentEquals("true")) {
			con.setAdr(1);
		} else if (b.getB1().contentEquals("false")) {
			con.setAdr(0);
		}
		Code.load(con);
	}
	
	public void visit(ArrayVar array) {
	    	Code.put(Code.newarray);
	    	Code.put(Code.const_1);
	    	//save array address?
	}
	
	public void visit(ProcCallFactor procCall) {
		if (procCall.getDesignator().obj.getName().contentEquals("len")) {
			Code.put(Code.arraylength);
		} else if (!(procCall.getDesignator().obj.getName().contentEquals("chr") || procCall.getDesignator().obj.getName().contentEquals("ord"))) {
		  //racunanje offset-a
		  Obj funObj = procCall.getDesignator().obj;
		  int offset = funObj.getAdr() - Code.pc;
		
		  //poziv funkcije
		  Code.put(Code.call);
		  Code.put2(offset);
		}
	}

	//******** obradjivanje designator-a
	
	public void visit(VarDesignator designator) {
		SyntaxNode parent = designator.getParent();
		if (ForeachStatement.class == parent.getClass()) {
			forEachStack.push(Code.pc);
			Code.putJump(0);
		} else if(FindReplaceStatement.class == parent.getClass()) {			
			if (enterFindReplace == 0) {
				findReplaceStack.push(Code.pc);
				Code.putJump(0);
				enterFindReplace = 1;
			}
			//Code.load(designator.obj);
		} else if (AssignDesignator.class != parent.getClass() && ProcCall.class != parent.getClass() && ReadStatement.class != parent.getClass() && FindAnyStatement.class != parent.getClass()) {
			//ako je samo promenljiva u izrazima, proveravamo u kom kontekstu se koristi
			Code.load(designator.obj);
		}
	}
	
	public void visit(VarArrayDesignator designator) {
		SyntaxNode parent = designator.getParent();
		if (AssignDesignator.class != parent.getClass() && ProcCall.class != parent.getClass() && ReadStatement.class != parent.getClass() && 
				IncrementDes.class != parent.getClass() && DecrementDes.class != parent.getClass()) {
			//ako je samo promenljiva u izrazima, proveravamo u kom kontekstu se koristi
			Code.put(Code.pop);

			Code.load(designator.obj);
			
			SyntaxNode index = designator.getDesignatorArray();
			index.traverseBottomUp(this);
		
			Code.load(designator.getDesignatorArray().obj);
		} 
	}	
	
	//*********** obradjivanje izraza
	public void visit(AddopTermExpr addExpr) {
		if (addExpr.getAddop() instanceof PlusAddop) {
			Code.put(Code.add);
		} else if (addExpr.getAddop() instanceof MinusAddop) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(SingleTermExprSigned signedExpr) {
		Code.put(Code.neg);
	}
	
	public void visit(MulTerm mulTerm) {
		if (mulTerm.getMulop() instanceof MultiplyOp) {
			Code.put(Code.mul);
		} else if (mulTerm.getMulop() instanceof DivideOp) {
			Code.put(Code.div);
		} else if (mulTerm.getMulop() instanceof RemainingOp) {
			Code.put(Code.rem);
		}
	}	
}
