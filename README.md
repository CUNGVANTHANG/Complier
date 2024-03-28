## 1. Thành viên nhóm

| STT | Họ và tên | Mã sinh viên |
| :--: | :--: | :--: |
| 1 | Cung Văn Thắng | 21020939 |
| 2 | Phạm Tuấn Anh | 20020631 |
| 3 | Lê Ngọc Ánh | 20020166 |

## 2. Văn phạm phi ngữ cảnh

### 1. Ký hiệu

- `S`: Biểu tượng bắt đầu (start symbol)
- `<stmtList>`: Danh sách các câu lệnh
- `<stmt>`: Câu lệnh
- `<Type>`: Kiểu dữ liệu
- `<Declaration>`: Khai báo biến
- `<Assignment>`: Gán giá trị cho biến
- `<Expr>`: Biểu thức
- `<Term>`: Hạng tử
- `<Factor>`: Nhân tử
- `<Primary>`: Đơn vị cơ bản
- `<RelOp>`: Toán tử quan hệ
- `<AddOp>`: Toán tử cộng trừ
- `<MulOp>`: Toán tử nhân chia
- `T`: Tập hợp các ký hiệu (token)

### 2. Cấu trúc

- Cấu trúc chương trình: `begin <stmtList> end`
- Danh sách câu lệnh: có thể rỗng hoặc gồm nhiều câu lệnh nối tiếp nhau bằng dấu chấm phẩy `;`
- Câu lệnh:
    - `if <Expr> then { <stmtList> }`: Câu lệnh điều kiện if-then
    - `if <Expr> then { <stmtList> } else { <stmtList> }`: Câu lệnh điều kiện if-then-else
    - `do { <stmtList> } while (<Expr>)`: Vòng lặp do-while
    - `<Assignment>`: Gán giá trị cho biến
    - `<Declaration>`: Khai báo biến
    - `print "(" <Expr> ")"`: In ra giá trị của biểu thức
- Khai báo biến: `<Type> <identifier>`
- Gán giá trị: `<identifier> = <Expr>`
- Biểu thức:
    - `<Term> <RelOp> <Term>`: Biểu thức so sánh
    - `<Term>`: Biểu thức toán học
- Hạng tử:
    - `<Factor> <AddOp> <Term>`: Biểu thức toán học
    - `<Factor>`: Nhân tử
- Nhân tử:
    - `<Primary> <MulOp> <Factor>`: Biểu thức toán học
    - `<Primary>`: Đơn vị cơ bản
- Đơn vị cơ bản:
    - `identifier`: Biến
    - `integer`: Số nguyên
    - `(" <Expr> ")`: Biểu thức trong ngoặc

### 3. Văn phạm phi ngữ cảnh

```
S -> begin <stmtList> end

T = {identifier}, {keyword}, {integer}, {boolean}, {operator}, "do", "while", "if", "then", "else", "print"

<stmtList> -> epsilon | <stmt> ; <stmtList>

<stmt> ->
  "if" <Expr> "then" { <stmtList> }
  | "if" <Expr> "then" { <stmtList> } "else" { <stmtList> }
  | "do" { <stmtList> } "while" "(" <Expr> ")"
  | <Assignment>
  | <Declaration>
  | "print" "(" <Expr> ")"

<Type> -> "int" | "bool"

<Declaration> -> <Type> <identifier> 

<Assignment> -> <identifier> = <Expr>  

<Expr> -> <Term> <RelOp> <Term> | <Term>  

<RelOp> -> "<" | ">" | "<=" | ">=" | "==" | "!="

<Term> -> <Factor> <AddOp> <Term> | <Factor>

<AddOp> -> "+" | "-"

<Factor> -> <Primary> <MulOp> <Factor> | <Primary>

<MulOp> -> "*" | "/"

<Primary> -> <identifier> | <integer> | "(" <Expr> ")"

{identifier} -> ^[a-zA-Z]+[0-9]*  

{integer} -> [0-9]+               

{keyword} = "do" | "while" | "if" | "then" | "else" | "print"

{operator} = "+" | "-" | "*" | "/" | "<" | ">" | "<=" | ">=" | "==" | "!="

```

## 3. Xây dựng bộ phân tích từ vựng 
### 1. Viết từ đầu

- **Test case 1: Phân tích từ vựng**

```
begin
	int x;
	int y=x+1;

    bool a;
	if x>a then{
		int c=1;
	}else{
		y=x;
		x=x+1;
	}
end
```

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/205dc43e-a6b2-46e3-9f40-9154f48e51e1)

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/d4344cb5-ac13-4b63-bfcb-80c0427523c4)




### 2. Sử dụng công cụ JFlex
