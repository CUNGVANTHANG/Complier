package upl;

%%

%type int
%line
%column
%char
%class Scanner
%unicode
%ignorecase
%state COMMENT
%{
    private int errorLine = 1;
    private int errorColumn = 1;
%}

%eofval{
    System.out.println("EOF"); return 1;
%eofval}

%{
    private void printErrorLocation() {
        System.out.println("\nError at line " + errorLine + ", column " + errorColumn);
    }

    boolean standaloneInt = true;

        // Biến trạng thái để theo dõi xem từ khóa "bool" có đứng một mình hay không
    boolean standaloneBool = true;

    // Biến trạng thái để theo dõi xem từ khóa "if" có đứng một mình hay không
    boolean standaloneIf = true;

    // Biến trạng thái để theo dõi xem từ khóa "then" có đứng một mình hay không
    boolean standaloneThen = true;

    // Biến trạng thái để theo dõi xem từ khóa "else" có đứng một mình hay không
    boolean standaloneElse = true;

    // Biến trạng thái để theo dõi xem từ khóa "do" có đứng một mình hay không
    boolean standaloneDo = true;

    // Biến trạng thái để theo dõi xem từ khóa "while" có đứng một mình hay không
    boolean standaloneWhile = true;

    // Biến trạng thái để theo dõi xem từ khóa "print" có đứng một mình hay không
    boolean standalonePrint = true;


%}

%%

// Mô tả các regular expressions

// Quy tắc cho từ khóa "int" khi đứng một mình
<YYINITIAL> "int" { standaloneInt = true;  System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "bool" { standaloneBool = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "if" { standaloneIf = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "then" { standaloneThen = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "else" { standaloneElse = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "do" { standaloneDo = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "while" { standaloneWhile = true; System.out.println("KEYWORD: " + yytext()); }
<YYINITIAL> "print" { standalonePrint = true; System.out.println("KEYWORD: " + yytext()); }

// Quy tắc cho identifier
<YYINITIAL> [a-zA-Z_][a-zA-Z0-9_]* { 

    if (standaloneInt || standaloneBool || standaloneIf || standaloneThen || standaloneElse || standaloneDo ||  standaloneWhile || standalonePrint)  System.out.println("Identifier: " + yytext()); 
    
}

// Quy tắc cho identifier không hợp lệ
// <YYINITIAL> [a-zA-Z][0-9]+[a-zA-Z] {
//     printErrorLocation();
// }



"begin" {System.out.println("BEGIN"); errorColumn += yytext().length(); return 0;}
"end" {System.out.println("END");errorColumn += yytext().length(); return 0;}

"=" { System.out.println("Equals"); errorColumn += yytext().length();return 0; }
">=" { System.out.println("Greater than or equal to");errorColumn += yytext().length(); return 0; }
">" { System.out.println("Greater than");errorColumn += yytext().length(); return 0; }
"<=" { System.out.println("Less than or equal to");errorColumn += yytext().length(); return 0; }
"<" { System.out.println("Less than"); errorColumn += yytext().length();return 0; }
"==" { System.out.println("Equal to"); errorColumn += yytext().length();return 0; }
"+" { System.out.println("Plus"); errorColumn += yytext().length(); return 0;}
"*" { System.out.println("Times"); errorColumn += yytext().length(); return 0;}
[0-9]+ { System.out.println("Integral Number: " + yytext()); errorColumn += yytext().length(); return 0;}
";" { System.out.println("Semicolon"); errorColumn += yytext().length();  return 0; }
"(" { System.out.println("Open parenthesis"); errorColumn += yytext().length(); return 0; }
")" { System.out.println("Close parenthesis"); errorColumn += yytext().length(); return 0; }
"{" { System.out.println("Open brace"); errorColumn += yytext().length(); return 0; }
"}" { System.out.println("Close brace"); errorColumn += yytext().length(); return 0; }
[/][*][^*]*[*]+([^*/][^*]*[*]+)*[/]       {System.out.println("Multiline comment"); }
[/][*]                                    { printErrorLocation(); }
"//".* {
    System.out.println("Comment 1 dòng: " + yytext());
    errorLine++;
    errorColumn = 1; // Reset errorColumn khi gặp dấu xuống dòng
    return 0;
}
[ \t\r\f] { errorColumn++; } // Ignore whitespace
\n { errorLine++; errorColumn = 1; } // Handle newline
. {
    printErrorLocation();
    System.out.println("Illegal character: " + yytext());
    return 0;
}

