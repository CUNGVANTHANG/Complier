    1      1 Keyword_Begin  
    2      2 Identifier      int
    2      6 Identifier      x
    2      7 Semicolon      
    3      2 Identifier      int
    3      6 Identifier      y
    3      7 Op_assign      
    3      8 Identifier      x
    3      9 Op_add         
    3     10 Integer             1
    3     11 Semicolon      
    8      2 Identifier      bool
    8      7 Identifier      a
    8      8 Semicolon      
    9      2 Keyword_if     
    9      5 Identifier      x
    9      6 Op_greater     
    9      7 Identifier      a
    9      9 Keyword_then   
    9     13 LeftBrace      
   10      3 Identifier      int
   10      7 Identifier      c
   10      8 Op_assign      
   10      9 Integer             1
   10     10 Semicolon      
   11      2 RightBrace     
   11      3 Keyword_else   
   11      7 LeftBrace      
   12      3 Identifier      y
   12      4 Op_assign      
   12      5 Identifier      x
   12      6 Semicolon      
   13      3 Identifier      x
   13      4 Op_assign      
   13      5 Identifier      x
   13      6 Op_add         
   13      7 Integer             1
   13      8 Semicolon      
   14      2 RightBrace     
   15      2 Keyword_print  
   15      7 LeftParen      
   15      8 Identifier      a
   15      9 RightParen     
   15     10 Semicolon      
   16      2 Keyword_if     
   16      5 Identifier      x
   16      6 Op_greaterequal
   16      8 Identifier      a
   16     10 Keyword_then   
   16     14 LeftBrace      
   17      2 Identifier      x
   17      3 Op_assign      
   17      4 Identifier      x
   17      5 Op_add         
   17      6 Integer             1
   17      7 Semicolon      
   18      1 RightBrace     
   19      2 Identifier      bool
   19      7 Identifier      x
   19      8 Op_assign      
   19      9 Identifier      a
   19     10 Op_equal       
   19     12 Identifier      b
   19     13 Semicolon      
   20      2 Keyword_do     
   20      4 LeftBrace      
   21      3 Identifier      int
   21      7 Identifier      b
   21      8 Op_assign      
   21      9 Integer             1
   21     10 Semicolon      
   22      3 Identifier      b
   22      4 Op_assign      
   22      5 Identifier      b
   22      6 Op_multiply    
   22      7 Integer            10
   22      9 Semicolon      
   23      3 Identifier      a
   23      4 Op_assign      
   23      5 LeftParen      
   23      6 Identifier      b
   23      7 Op_add         
   23      8 Integer            10
   23     10 RightParen     
   23     11 Op_multiply    
   23     12 Identifier      b
   23     13 Semicolon      
   24      2 RightBrace     
   24      3 Keyword_while  
   24      8 LeftParen      
   24      9 Identifier      a
   24     10 Op_greater     
   24     11 Integer             1
   24     12 RightParen     
   24     13 Semicolon      
   25      2 Keyword_print  
   25      7 LeftParen      
   25      8 Identifier      a
   25      9 Op_add         
   25     10 Integer             1
   25     11 RightParen     
   25     12 Semicolon      
   26      1 Keyword_End    
   27      1 End_of_input   
