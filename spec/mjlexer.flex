
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"break"     { return new_symbol(sym.BREAK, yytext()); }
"else"      { return new_symbol(sym.ELSE, yytext()); }
"const"     { return new_symbol(sym.CONST, yytext()); }
"if"        { return new_symbol(sym.IF, yytext()); }
"while"     { return new_symbol(sym.WHILE, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"new"       { return new_symbol(sym.NEW, yytext()); }
"read"      { return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"continue"  { return new_symbol(sym.CONTINUE, yytext()); }
"foreach"   { return new_symbol(sym.FOREACH, yytext()); }
"findAny"   { return new_symbol(sym.FINDANY, yytext()); }
"findAndReplace"  { return new_symbol(sym.FINDREPLACE, yytext()); }

"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-"         { return new_symbol(sym.MINUS, yytext()); }
"*"         { return new_symbol(sym.MUL, yytext()); }
"/"         { return new_symbol(sym.DIV, yytext()); }
"%"         { return new_symbol(sym.REM, yytext()); }
"++"        { return new_symbol(sym.INC, yytext()); }
"--"        { return new_symbol(sym.DEC, yytext()); }
"||"        { return new_symbol(sym.OR, yytext()); }
"&&"        { return new_symbol(sym.AND, yytext()); }
"=="        { return new_symbol(sym.CMPEQUAL, yytext()); }  
"!="        { return new_symbol(sym.DIFF, yytext()); }
">"         { return new_symbol(sym.GRT, yytext()); }
">="        { return new_symbol(sym.GRTEQ, yytext()); }
"<"         { return new_symbol(sym.LESS, yytext()); }
"<="        { return new_symbol(sym.LESSEQ, yytext()); }
"=>"        { return new_symbol(sym.FORARROW, yytext()); }
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }

";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"."         { return new_symbol(sym.DOT, yytext()); }

"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"["         { return new_symbol(sym.LSQUARE, yytext()); }
"]"         { return new_symbol(sym.RSQUARE, yytext()); }

"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

"'"[\040-\176]"'" {return new_symbol (sym.CHAR, new Character (yytext().charAt(1))); }

"true" | "false" { return new_symbol(sym.BOOL, yytext()); }

[0-9]+  { return new_symbol(sym.NUMBER,Integer.parseInt(yytext())); }
([a-zA-Z])[a-zA-Z0-9_]* {return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + " u koloni " + yycolumn); }










