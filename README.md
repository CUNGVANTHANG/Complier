# 1. Thành viên nhóm

| STT |   Họ và tên    | Mã sinh viên |
|:---:|:--------------:|:------------:|
|  1  | Cung Văn Thắng |   21020939   |
|  2  | Phạm Tuấn Anh  |   20020631   |
|  3  |  Lê Ngọc Ánh   |   20020166   |

# 2. Văn phạm phi ngữ cảnh

```
Program          -> 'begin' StatementList 'end'

StatementList    -> Statement ';' StatementList | ε

Statement        -> Declaration | Assignment | ConditionalStatement | LoopStatement | PrintStatement

Declaration      -> Type Identifier | Type Identifier '=' Expression

Assignment       -> Identifier '=' Expression

Expression       -> Term | Expression '+' Term | Expression ROP Term

Term             -> Factor | Term '*' Factor

Factor           -> Identifier | Number | '(' Expression ')'

ConditionalStatement -> 'if' Expression 'then' '{' Statement '}' | 'if' Expression 'then' '{' Statement '}' 'else' '{' Statement '}'

LoopStatement    -> 'do' '{' Statement '}' while' Expression

PrintStatement   -> 'print' '(' Expression ')'

Type             -> int | bool
```

# 3. Xây dựng bộ phân tích từ vựng

## 3.1. Phân tích từ vựng


