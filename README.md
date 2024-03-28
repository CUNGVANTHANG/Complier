# 1. Thành viên nhóm

| STT | Họ và tên | Mã sinh viên |
| :--: | :--: | :--: |
| 1 | Cung Văn Thắng | 21020939 |
| 2 | Phạm Tuấn Anh | 20020631 |
| 3 | Lê Ngọc Ánh | 20020166 |

# 2. Văn phạm phi ngữ cảnh

## 2.1. Ký hiệu

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

## 2.2. Cấu trúc

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

## 2.3. Văn phạm phi ngữ cảnh

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

# 3. Xây dựng bộ phân tích từ vựng 
## 3.1. Viết từ đầu
### 3.1.1. Phân tích từ vựng

**Cách chạy chương trình:**

**Bước 1:** Thay đổi đường dẫn file path `main.upl`

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/bf6de091-a3a6-42ec-911c-5bbf59de926f)

**Bước 2:** Chạy chương trình `Run`


- **Test case 1:**

```
begin
	int isTrue123;
	print(a);
	do {
		if x > a then {
			a = b;
		}
	} while(isTrue123)
end
```

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/dd159780-c74a-44a6-ab1c-b479e73771c3)

- **Test case 2:**

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

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/f865bd86-325c-4f67-b5eb-ae4f333bea6b)

- **Test case 3:**

```
begin
	int x;
	int y=x+1;
	/*	comments
	cho nhiều dòng
	*/
	
	bool a;//comment cho một dòng
	if x>a then{
		int c=1;
	}else{
		y=x;
		x=x+1;
	}
	print(a);
	if x>=a then{
	x=x+1;
}
	bool x=a==b;
	do{
		int b=1;
		b=b*10;
		a=(b+10)*b;
	}while(a>1);
	print(a+1);
end
```

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/ef82002a-5c53-41ad-a023-f5bcc4a2eff0)

## 3.1.3. Phân tích cú pháp



## 3.2. Sử dụng công cụ JFlex
