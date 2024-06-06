package rs.ac.bg.etf.pp1;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	int printCallCount = 0;
	int varDeclCount = 0;
	int methVarDeclCount = 0;
	int constVarDeclCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	boolean breakDetected = false;
	boolean continueDetected = false;
	int actParamsCount = 0;
	int nVars;
	LinkedList<OneIdent> declList = new LinkedList<OneIdent>();
	LinkedList<MultiIdentDecl> multiDeclList = new LinkedList<MultiIdentDecl>();
	LinkedList<SingleConstDecl> constList = new LinkedList<SingleConstDecl>();
	LinkedList<SingleMethIdent> singleMethodVars = new LinkedList<SingleMethIdent>();
	LinkedList<MultiMethIdent> multiMethodVars = new LinkedList<MultiMethIdent>();
	LinkedList<Expr> actParamList = new LinkedList<Expr>();

	Logger log = Logger.getLogger(getClass());


// ************* metode za greske *******
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0 ) 
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean passed() {
		return !errorDetected;
	}

	// ********************************
	//******** visit metode ***********
	
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName() , Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program prog) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(prog.getProgName().obj);
		Tab.closeScope();
	}
	
    // *********************************
	// visit za konstante i promenljive
	
	//provera tipa, dodavanje globalnih promenljivih
	
	protected boolean alreadyDeclared(SyntaxNode node, String id) {
		if (Tab.currentScope.findSymbol(id) == null) {
			return false;
		} else {
			report_error("Simbol sa imenom "+ id + " je vec definisan!",node);
			return true;			
		}
	}
	
	public void visit(Type type) {
	  Obj typeNode = Tab.find(type.getTypeName());
	  if (typeNode == Tab.noObj) {
		  report_error("Nije pronadjen tip " + type.getTypeName()+ " u tabeli simbola", null);
		  type.struct = Tab.noType;
	  } else {
		  if (Obj.Type == typeNode.getKind()) {
			  type.struct = typeNode.getType();
		  } else {
			  report_error("Greska: Ime "+ type.getTypeName()+ " ne predstavlja tip!", type);
			  type.struct = Tab.noType;
		  }
	  }
	}
	
	public void visit(GlobalVar gVar) {
		Type type = gVar.getType();
		while (!declList.isEmpty()) {
			OneIdent decl = declList.removeFirst();
			SingleVarDecl singleDecl= decl.getSingleVarDecl();
			if (!alreadyDeclared(singleDecl, singleDecl.getVName())) {
				if (singleDecl.getArrayDecl() instanceof WithArrayDecl) {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), new Struct(Struct.Array, type.struct));
					//report_info("Pronadjena deklaracija globalne promenljive "+ singleDecl.getVName()+ " niza tipa "+ type.getTypeName(),decl);
				} else {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), type.struct);
					//report_info("Pronadjena deklaracija globalne promenljive "+ singleDecl.getVName()+ " tipa "+ type.getTypeName(),decl);
				}
			}
		}
		
		while (!multiDeclList.isEmpty()) {
			MultiIdentDecl decl = multiDeclList.removeFirst();
			SingleVarDecl singleDecl = decl.getSingleVarDecl();
			if (!alreadyDeclared(singleDecl, singleDecl.getVName())) {
				if (singleDecl.getArrayDecl() instanceof WithArrayDecl) {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), new Struct(Struct.Array, type.struct));
					//report_info("Pronadjena deklaracija globalne promenljive "+ singleDecl.getVName()+ " niza tipa "+ type.getTypeName(),decl);
				} else {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), type.struct);
					//report_info("Pronadjena deklaracija globalne promenljive "+ singleDecl.getVName()+ " tipa "+ type.getTypeName(),decl);
				}
			}
		}
	}
	
	public void visit(OneIdent decl) {
		varDeclCount++;
		declList.add(decl);
	}
	
	public void visit(MultiIdentDecl decl) {
		varDeclCount++;
		multiDeclList.add(decl);
	}

	
	//*********** globalne konstante
	public void visit(GlobalConst gConst) {
		Type type = gConst.getType();
		while (!constList.isEmpty()) {
			SingleConstDecl decl = constList.removeFirst();
			if (!alreadyDeclared(decl, decl.getConstName())) {
				if (type.struct.equals(decl.getConstFactor().struct)) {
					decl.obj = Tab.insert(Obj.Con, decl.getConstName(), type.struct);
					if (decl.getConstFactor() instanceof NumberConst) {
						NumberConst num = (NumberConst) decl.getConstFactor();
						decl.obj.setAdr(num.getN1());
					} else if (decl.getConstFactor() instanceof CharacterConst) {
						CharacterConst ch = (CharacterConst) decl.getConstFactor();
						decl.obj.setAdr(ch.getC1());
					} else if( decl.getConstFactor() instanceof BooleanConst) {
						BooleanConst b = (BooleanConst) decl.getConstFactor();
						if (b.getB1().contentEquals("true")) {
							decl.obj.setAdr(1);
						} else {
							decl.obj.setAdr(0);
						}
					}
					//report_info("Pronadjena deklaracija globalne konstante "+ decl.getConstName() + " tipa "+ type.getTypeName(),decl);
				} else {
					report_error("Greska: pogresan tip vrednosti u deklaraciji konstante na liniji "+gConst.getLine(),null);
				}
			}
		}
	}
	
	
	public void visit(SingleConstDecl sConst) {
		constVarDeclCount++;
        constList.add(sConst);
	}
	
	public void visit(NumberConst numConst) {
		numConst.struct = Tab.intType;
	}

	public void visit(CharacterConst charConst) {
		charConst.struct = Tab.charType;
	}
	
	public void visit(BooleanConst boolConst) {
		boolConst.struct = ExtendedTab.boolType;
	}

	//********* lokalne promenljive metoda
	
	public void visit(MethodVarDecl decl) {
		Type type = decl.getType();
		while (!singleMethodVars.isEmpty()) {
			SingleMethIdent singleDecl = singleMethodVars.removeFirst();
			if (!alreadyDeclared(singleDecl, singleDecl.getVName())) {
				if (singleDecl.getArrayDecl() instanceof WithArrayDecl) {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), new Struct(Struct.Array, type.struct));
					//report_info("Pronadjena deklaracija lokalne promenljive "+ singleDecl.getVName()+ " niza tipa "+ type.getTypeName(),decl);
				} else {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), type.struct);
					//report_info("Pronadjena deklaracija lokalne promenljive "+ singleDecl.getVName()+ " tipa "+ type.getTypeName(),decl);
				}
			}
		}
		
		while (!multiMethodVars.isEmpty()) {
			MultiMethIdent singleDecl = multiMethodVars.removeFirst();
			if (!alreadyDeclared(singleDecl, singleDecl.getVName())) {
				if (singleDecl.getArrayDecl() instanceof WithArrayDecl) {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), new Struct(Struct.Array, type.struct));
					//report_info("Pronadjena deklaracija lokalne promenljive "+ singleDecl.getVName()+ " niza tipa "+ type.getTypeName(),decl);
				} else {
					singleDecl.obj = Tab.insert(Obj.Var, singleDecl.getVName(), type.struct);
					//report_info("Pronadjena deklaracija lokalne promenljive "+ singleDecl.getVName()+ " tipa "+ type.getTypeName(),decl);
				}
			}
		}
	}
	
	
	public void visit(SingleMethIdent decl) {
		methVarDeclCount++;
		singleMethodVars.add(decl);
	}
	
	public void visit(MultiMethIdent decl) {
		methVarDeclCount++;
		multiMethodVars.add(decl);
	}
	//********************************
	//*******Obradjivanje metoda
	public void visit(MethodWithType method) {
		currentMethod = Tab.insert(Obj.Meth, method.getMethName(), method.getType().struct);
		method.obj = currentMethod;
		Tab.openScope();
		if (method.getMethName().contentEquals("main")) {
			report_error("Greska na liniji:"+method.getLine()+", metoda main mora biti void tipa! ",null);
		} else {
			//report_info("Obradjuje se funkcija "+method.getMethName(), method);	
			method.obj = Tab.noObj;
		}
	}
	
	public void visit(MethodVoidType method) {
		currentMethod = Tab.insert(Obj.Meth, method.getMethName(), Tab.noType);
		method.obj = currentMethod;
		Tab.openScope();
		//report_info("Obradjuje se funkcija "+method.getMethName(), method);
	}
	
	public void visit(MethodDecl methDecl) {
		if (!returnFound && currentMethod.getType()!= Tab.noType) {
			report_error("Greska na liniji "+methDecl.getLine()+" : funkcija "+currentMethod.getName()+" nema return iskaz!",null);
		}
		
		Obj obj = methDecl.getMethodTypeName().obj;
		if (obj.getName().contentEquals("main") && obj.getLevel()!=0) {
			//proveri broj parametara
			report_error("Greska na liniji "+methDecl.getLine()+" : funkcija main ne sme da ima argumente!",null);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		returnFound = false;
		currentMethod = null;
	}
	//********Poziv metoda
	public void visit(ProcCall procCall) {
		Obj proc = procCall.getDesignator().obj;
		ExtendedSymbolTableVisitor visitor = new ExtendedSymbolTableVisitor();
		visitor.visitObjNode(proc);
		
		if (proc.getKind() == Obj.Meth) {
			report_info("Pronadjen poziv funkcije "+proc.getName()+" na liniji "+procCall.getLine(),null);
			System.out.println(visitor.getOutput());
			procCall.struct = proc.getType();
		} else {
			report_error("Greska na liniji "+procCall.getLine()+" : ime "+proc.getName()+" nije funkcija!", null);
			procCall.struct = Tab.noType;
		}
		
		//provera parametara		
		if (proc.getLevel() != actParamsCount) {
			report_error("Greska na liniji: "+procCall.getLine()+", pogresan broj parametara u pozivu metode "+proc.getName(), null);
		} else if (actParamsCount !=0 ) {
			Object[] locals = proc.getLocalSymbols().toArray(); //lista argumenata i tipova
			//lista actPars
			for (int i = 0; i< proc.getLevel(); i++) {
			   Expr ex = actParamList.removeFirst();
			   Obj var = (Obj) locals[i];
			   if (procCall.getDesignator().obj.getName().contentEquals("len")) {
				   if (ex.struct.getKind() != Struct.Array) {
						report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (procCall.getDesignator().obj.getName().contentEquals("chr")) {
				   if (ex.struct.getKind() != Struct.Int) {
							report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (procCall.getDesignator().obj.getName().contentEquals("ord")) {
				   if (ex.struct.getKind() != Struct.Char) {
							report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (!(var.getType().equals(ex.struct))) {
					report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
			   }
			}
		}
		actParamsCount = 0;
	}
	
	public void visit(ProcCallFactor procCall) {
		Obj proc = procCall.getDesignator().obj;
		ExtendedSymbolTableVisitor visitor = new ExtendedSymbolTableVisitor();
		visitor.visitObjNode(proc);
		
		if (proc.getKind() == Obj.Meth) {
			if (proc.getType() == Tab.noType) {
				report_error("Greska na liniji "+procCall.getLine()+" : ime "+proc.getName()+" nema povratnu vrednost!", null);
				procCall.struct = Tab.noType;
			} else {
				report_info("Pronadjen poziv funkcije "+proc.getName()+" na liniji "+procCall.getLine(),null);
				System.out.println(visitor.getOutput());
				procCall.struct = proc.getType();	
			}
		} else {
			report_error("Greska na liniji "+procCall.getLine()+" : ime "+proc.getName()+" nije funkcija!", null);
			procCall.struct = Tab.noType;
		}
		
		//provera parametara
		if (proc.getLevel() != actParamsCount) {
			report_error("Greska na liniji: "+procCall.getLine()+", pogresan broj parametara u pozivu metode "+proc.getName(), null);
		} else if (actParamsCount !=0 ) {
			Object[] locals = proc.getLocalSymbols().toArray(); //lista argumenata i tipova
			//lista actPars
			for (int i = 0; i< proc.getLevel(); i++) {
			   Expr ex = actParamList.removeFirst();
			   Obj var = (Obj) locals[i];
			   if (procCall.getDesignator().obj.getName().contentEquals("len")) {
				   if (ex.struct.getKind() != Struct.Array) {
						report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (procCall.getDesignator().obj.getName().contentEquals("chr")) {
				   if (ex.struct.getKind() != Struct.Int) {
							report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (procCall.getDesignator().obj.getName().contentEquals("ord")) {
				   if (ex.struct.getKind() != Struct.Char) {
							report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
				   }
			   } else if (!(var.getType().equals(ex.struct))) {
					report_error("Greska na liniji: "+procCall.getLine()+", pogresan tip parametra "+(i+1)+ " u pozivu metode "+proc.getName(), null);
			   }
			}
		}

		actParamsCount = 0;
	}
	
	public void visit(FormalParamDecl formParam) {
		if (!alreadyDeclared(formParam, formParam.getParamName())) {
			if (formParam.getFormParamArray() instanceof FormParamWithArray) {
				formParam.obj = Tab.insert(Obj.Var, formParam.getParamName(), new Struct(Struct.Array, formParam.getType().struct));
				//report_info("Pronadjena deklaracija formalnog parametra "+ formParam.getParamName()+ " niza tipa "+ formParam.getType().getTypeName(),formParam);
				currentMethod.setLevel(currentMethod.getLevel()+1);
			} else {
				formParam.obj = Tab.insert(Obj.Var, formParam.getParamName(), formParam.getType().struct);
				//report_info("Pronadjena deklaracija formalnog parametra "+ formParam.getParamName()+ " tipa "+ formParam.getType().getTypeName(),formParam);
				currentMethod.setLevel(currentMethod.getLevel()+1);
			}
		}
	}
	
	//************provera parametara pri pozivu metode
	public void visit(SingleParamExpr paramExpr) {
		paramExpr.struct = paramExpr.getExpr().struct;
		actParamsCount++;
		actParamList.add(paramExpr.getExpr());
	}
	
	public void visit(MultiParamExpr paramExpr) {
		paramExpr.struct = paramExpr.getExpr().struct;
		actParamsCount++;
		actParamList.add(paramExpr.getExpr());
	}
	
	//************ obradjivanje return naredbi i provera
	public void visit(ReturnWithExprStatement returnExpr) {
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			   report_error("Greska na liniji " + returnExpr.getLine()+" : tip izraza se ne slaze sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		} 
		
	}
	
	public void visit(PlainReturnStatement statement) {
		//add checks?
	}
	
	public void visit(AssignDesignator assignment) {
		Designator des = assignment.getDesignator();
		Expr expr = assignment.getExpr();

		if (assignment.getDesignator() instanceof VarArrayDesignator) {
			if (!expr.struct.assignableTo(des.obj.getType().getElemType())) {
				report_error("Greska na liniji "+ assignment.getLine() + " : nekompatibilni tipovi u dodeli vrednosti! ", null);
			}
		} else if (!expr.struct.assignableTo(des.obj.getType())) {
			report_error("Greska na liniji "+ assignment.getLine() + " : nekompatibilni tipovi u dodeli vrednosti! ", null);
		}
	}
	
	public void visit(IncrementDes incDes) {
		if (incDes.getDesignator() instanceof VarDesignator) {
			if (!(incDes.getDesignator().obj.getType().getKind() == Struct.Int)) {
				report_error("Greska na liniji "+ incDes.getLine() + " : podatak mora biti int za inc operaciju! ", null);
				incDes.struct = Tab.noType;
			} else {
				incDes.struct = incDes.getDesignator().obj.getType();
			}
		} else if (incDes.getDesignator() instanceof VarArrayDesignator) {
			VarArrayDesignator designator = (VarArrayDesignator) incDes.getDesignator();
			if (designator.getDesignatorArray().obj.getKind() != Obj.Elem) {
				report_error("Greska na liniji "+ incDes.getLine() + " : podatak je nije deklarisan kao niz! ", null);
				incDes.struct = Tab.noType;
			}else if (!(designator.getDesignatorArray().obj.getType().getKind() == Struct.Int)) {
				report_error("Greska na liniji "+ incDes.getLine() + " : podatak mora biti int za inc operaciju! ", null);
				incDes.struct = Tab.noType;
			} else {
			 incDes.struct = incDes.getDesignator().obj.getType().getElemType();
			}
		}
	}
	
	public void visit(DecrementDes decDes) {
		if (decDes.getDesignator() instanceof VarDesignator) {
			if (!(decDes.getDesignator().obj.getType().getKind() == Struct.Int)) {
				report_error("Greska na liniji "+ decDes.getLine() + " : podatak mora biti int za dec operaciju! ", null);
				decDes.struct = Tab.noType;
			} else {
				decDes.struct = decDes.getDesignator().obj.getType();
			}
		} else if (decDes.getDesignator() instanceof VarArrayDesignator) {
			VarArrayDesignator designator = (VarArrayDesignator) decDes.getDesignator();
			if (designator.getDesignatorArray().obj.getKind() != Obj.Elem) {
				report_error("Greska na liniji "+ decDes.getLine() + " : podatak je nije deklarisan kao niz! ", null);
				decDes.struct = Tab.noType;
			} else if (!(designator.getDesignatorArray().obj.getType().getKind() == Struct.Int)) {
				report_error("Greska na liniji "+ decDes.getLine() + " : podatak mora biti int za dec operaciju! ", null);
				decDes.struct = Tab.noType;
			} else {
			    decDes.struct = decDes.getDesignator().obj.getType().getElemType();
			}
		}
	}
	
	//*********obradjivanje izraza i provera
	
	public void visit(SimpleTerm term) {
		term.struct = term.getFactor().struct;
	}
	
	public void visit(SingleTermExpr expr) {
		expr.struct = expr.getTerm().struct;
	}
	
	public void visit(SingleTermExprSigned expr) {
		if (!(expr.getTerm().struct.getKind() == Struct.Int)) {
			report_error("Greska na liniji "+expr.getLine()+" izraz mora biti tipa int!", null);
			expr.struct = Tab.noType;
		} else {
			expr.struct = expr.getTerm().struct;			
		}
	}
	
	public void visit(AddopTermExpr expr) {
		Struct termExpr  = expr.getExpr().struct;
		Struct term = expr.getTerm().struct;
		
		if (termExpr.equals(term) && termExpr == Tab.intType) {
			expr.struct = termExpr;
		} else {
			report_error("Greska na liniji "+expr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje!",null);
			expr.struct = Tab.noType;
		}
		
	}
	
	public void visit(MulTerm mulTerm) {
		Struct t1 = mulTerm.getTerm().struct;
		Struct t2 = mulTerm.getFactor().struct;
		
		if (t1.equals(t2) && t2 == Tab.intType) {
			mulTerm.struct = t2;
		} else {
			report_error("Greska na liniji "+mulTerm.getLine()+" : nekompatibilni tipovi u izrazu za mnozenje!",null);
			mulTerm.struct = Tab.noType;
		}
	}
	
	public void visit(CondWithRelop condFactor) {
		Expr ex = condFactor.getExpr();
		Expr ex1 = condFactor.getExpr1();
		
		if (ex1.struct.getKind() == Struct.Array || ex.struct.getKind() == Struct.Array) {
		    if (!(condFactor.getRelop() instanceof CmpEqualOp || condFactor.getRelop() instanceof DiffOp)) {
		    	report_error("Greska na liniji: "+condFactor.getLine()+", nizovi se mogu porediti samo sa  == i != ",null);
		    	condFactor.struct = Tab.noType;
		    }	
		}
		
		if (!(ex.struct.compatibleWith(ex1.struct))) {
			report_error("Greska: nakompatibilni tipovi pri proveri uslova na liniji "+ condFactor.getLine(),null);
			condFactor.struct = Tab.noType;
		} else {
			condFactor.struct = ex.struct;
		}
	}
	
	public void visit(CondWithoutRelop condFactor) {
		condFactor.struct = condFactor.getExpr().struct;
	}
	
	//*******************************
	
	//********koriscenje prom, designatori
	public void visit(VarDesignator designator) {
		Obj obj = Tab.find(designator.getDesName());
		if (obj == Tab.noObj) {
			report_error("Greska na liniji "+ designator.getLine()+" : ime "+ designator.getDesName()+ " nije deklarisano!",null);
		}
		designator.obj = obj;
		ExtendedSymbolTableVisitor visitor = new ExtendedSymbolTableVisitor();
		visitor.visitObjNode(designator.obj);
		if (designator.obj.getKind() == Obj.Con) {
			report_info("Pronadjeno koriscenje globalne konstante "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
		} else if (designator.obj.getKind() == Obj.Var && designator.obj.getLevel() == 1 && designator.obj.getAdr() < currentMethod.getLevel()) {
			report_info("Pronadjeno koriscenje formalnog parametra "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
		} else if (designator.obj.getKind() == Obj.Var && designator.obj.getLevel() == 1) {
			report_info("Pronadjeno koriscenje lokalne promenljive "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
		} else if (designator.obj.getKind() == Obj.Var && designator.obj.getLevel() == 0) {
			report_info("Pronadjeno koriscenje globalne promenljive "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
		}
	}
	
	public void visit(VarArrayDesignator designator) {
		Obj obj = Tab.find(designator.getDesName());
		if (obj == Tab.noObj) {
			report_error("Greska na liniji "+ designator.getLine()+" : ime "+ designator.getDesName()+ " nije deklarisano!",null);
		}
		
		if (obj.getType().getKind() != Struct.Array) {
			report_error("Greska na liniji "+ designator.getLine()+" : promenljiva "+ designator.getDesName()+ " nije niz!",null);
		}
		designator.obj = obj;
		designator.getDesignatorArray().obj = new Obj(Obj.Elem, designator.getDesName(), designator.obj.getType().getElemType());
		
		ExtendedSymbolTableVisitor visitor = new ExtendedSymbolTableVisitor();
		visitor.visitObjNode(designator.obj);
		
		if (obj.getAdr() < currentMethod.getLevel()) {
			report_info("Pronadjeno koriscenje formalnog parametra "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
		}
		report_info("Pronadjen pristup elementu niza "+visitor.getOutput()+" na liniji "+ designator.getLine(), null);
	}
	
	public void visit(ArrayIndexDes index) {
		if (index.getExpr().struct.getKind() != Struct.Int) {
			report_error("Greska na liniji: "+ index.getLine()+ " indeks niza mora biti int tipa!", null);
		}
		index.obj = Tab.noObj;
	}
	
	//***********obradjivanje faktora
	public void visit(NumConst cnst) {
		cnst.struct = Tab.intType;
	}
	
	public void visit(CharConst cnst) {
		cnst.struct = Tab.charType;
	}
	
	public void visit(BoolConst cnst) {
		cnst.struct = ExtendedTab.boolType;
	}
	
	public void visit(ParenExpr fact) {
		fact.struct = fact.getExpr().struct;
	}
	
	public void visit(ArrayVar var) {
		if (var.getExpr().struct.getKind() != Struct.Int) {
			report_error("Greska: pri alociranju novog niza na liniji "+var.getLine()+", izraz u zagradama mora biti int tipa!",null);
			var.struct = Tab.noType;
		} else {
		  Struct s = new Struct(Struct.Array, var.getType().struct);
		  var.struct = s;
		}
	}
	
	public void visit(Var var) {
		if (var.getDesignator() instanceof VarArrayDesignator) {
			var.struct = var.getDesignator().obj.getType().getElemType();
		} else if (var.getDesignator() instanceof VarDesignator) {
			var.struct = var.getDesignator().obj.getType();
		}
	}
	
	public void visit(ReadStatement statement) {
		if (statement.getDesignator().obj.getType().getKind() == Obj.Var) {
			if (statement.getDesignator() instanceof VarDesignator) {
				int kind = statement.getDesignator().obj.getType().getKind();
				if (!(kind == Struct.Int || kind == Struct.Char || kind == Struct.Bool)) {
					report_error("Greska na liniji:"+statement.getLine()+", promenljiva mora biti int, char ili bool!",null);
				}
			} else if (statement.getDesignator() instanceof VarArrayDesignator) {
				VarArrayDesignator designator = (VarArrayDesignator) statement.getDesignator();
				int kind = designator.getDesignatorArray().obj.getType().getKind();
				if (!(kind == Struct.Int || kind == Struct.Char || kind == Struct.Bool)) {
					report_error("Greska na liniji:"+statement.getLine()+", promenljiva mora biti int, char ili bool!",null);
				}
			}
		}
	}

	public void visit(PrintStatement statement) {
		printCallCount++;
	}
	
	public void visit(PlainExpr plainPrint) {
		if (plainPrint.getExpr().struct != null) {
			int kind = plainPrint.getExpr().struct.getKind();
			if(!(kind == Struct.Int || kind == Struct.Char || kind == Struct.Bool)) {
				report_error("Greska na liniji:"+plainPrint.getLine()+", los tip izraza u naredbi print!",null);
			}
		}
	}
	
	public void visit(ExprWithNum printNum) {
		if (printNum.getExpr().struct != null) {
			int kind = printNum.getExpr().struct.getKind();
			if(!(kind == Struct.Int || kind == Struct.Char || kind == Struct.Bool)) {
				report_error("Greska na liniji:"+printNum.getLine()+", los tip izraza u naredbi print!",null);
			}
		}
	}
	
	public void visit(BreakStatement statement) {
		//System.out.println(statement.getParent());
		breakDetected = true;
	}
	
	public void visit(ContinueStatement statement) {
		continueDetected = true;
	}
	
	public void visit(MatchedStatement statement) {
		//System.out.println(statement.getCondition());
	}
	
	public void visit(FindAnyStatement statement) {
		//levi designator je bool
		Designator d = statement.getDesignator();
		if (!(d.obj.getType().getKind() == Struct.Bool || d.obj.getType().getElemType().getKind() == Struct.Bool)) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva dodele findAny mora biti bool!",null);
		}
		//desni designator niz
		Designator d1 = statement.getDesignator1();
		if ((statement.getDesignator1() instanceof VarArrayDesignator) &&((VarArrayDesignator)d1).getDesignatorArray() != null) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u findAny mora biti niz!",null);
		} else if (!(d1.obj.getType().getKind() == Struct.Array)) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u findAny mora biti niz!",null);
		} else {
		//expr vrednost koja se trazi u elementima niza, tip isti ako elem niza
			Expr expr = statement.getExpr();
			if (!(d1.obj.getType().getElemType() == expr.struct)) {
			    report_error("Greska na liniji:"+statement.getLine()+", elementi niza i vrednost izraza nisu istog tipa",null);
			}
		}
	}
	
	public void visit(WhileStatement statement) {
		
	}
	
	public void visit(ForeachStatement statement) {
		Designator des = statement.getDesignator();
		if ((statement.getDesignator() instanceof VarArrayDesignator) &&((VarArrayDesignator)des).getDesignatorArray() != null) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u foreach mora biti niz!",null);
		} else if (!(statement.getDesignator().obj.getType().getKind() == Struct.Array)) {
			report_error("Greska na liniji "+statement.getLine()+" promenljiva "+statement.getDesignator().obj.getName()+" mora biti niz!",null);	
		}
		if (!(statement.getDesignator().obj.getType().getElemType()==null)) {
			ForEachIdent ident = statement.getForEachIdent();
			Obj obj = Tab.find(ident.getName());
			if (obj == Tab.noObj) {
				report_error("Greska na liniji "+ statement.getLine()+" : ime "+ident.getName()+ " nije deklarisano!",null);
			} else if (!(statement.getDesignator().obj.getType().getElemType().getKind() == obj.getType().getKind())) {
				report_error("Greska na liniji:"+statement.getLine()+", niz i promenljiva se ne slazu po tipu!",null);
			}
			ident.obj = obj;
		}
	}
	
	public void visit(FindReplaceStatement statement) {
		//levi designator je bool
		Designator d = statement.getDesignator();
		if ((statement.getDesignator() instanceof VarArrayDesignator) &&((VarArrayDesignator)d).getDesignatorArray() != null) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u findAndReplace mora biti niz!",null);
		} else if (!(d.obj.getType().getKind() == Struct.Array)) {
			report_error("Greska na liniji:"+statement.getLine()+", promenljiva dodele findAndReplace mora biti niz!",null);
		} else {
			//desni designator niz
			Designator d1 = statement.getDesignator1();
			if ((statement.getDesignator1() instanceof VarArrayDesignator) &&((VarArrayDesignator)d1).getDesignatorArray() != null) {
				report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u findAndReplace mora biti niz!",null);
			} else if (!(d1.obj.getType().getKind() == Struct.Array)) {
				report_error("Greska na liniji:"+statement.getLine()+", promenljiva koja se koristi u findAndReplace mora biti niz!",null);
			} else {
				//expr vrednost koja se trazi u elementima niza, tip isti ako elem niza
				if (d.obj.getType().getElemType().getKind() != d1.obj.getType().getElemType().getKind()) {
					report_error("Greska na liniji:"+statement.getLine()+", oba niza u findAndReplace moraju biti istog tipa!",null);
				} else {
					FindReplaceIdent ident = statement.getFindReplaceIdent();
					Obj obj = Tab.find(ident.getName());
					if (obj == Tab.noObj) {
						report_error("Greska na liniji "+ statement.getLine()+" : ime "+ident.getName()+ " nije deklarisano!",null);
					} else if((obj.getType().getKind() != d1.obj.getType().getElemType().getKind()) || obj.getType().getKind() != d.obj.getType().getElemType().getKind()) {
						report_error("Greska na liniji:"+statement.getLine()+", tekuci element u findAndReplace moraju biti istog tipa kao nizovi!",null);
					}
					ident.obj = obj;
				}
				Expr expr = statement.getExpr();
				if (!(d1.obj.getType().getElemType() == expr.struct)) {
					report_error("Greska na liniji:"+statement.getLine()+", elementi niza i vrednost prvog izraza nisu istog tipa",null);
				}

				Expr expr2 = statement.getExpr2();
				if (!(d1.obj.getType().getElemType() == expr2.struct)) {
					report_error("Greska na liniji:"+statement.getLine()+", elementi niza i vrednost drugog izraza nisu istog tipa",null);
				}
			}
		}
	}
	
}
