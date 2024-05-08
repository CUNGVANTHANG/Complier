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

**Bước 1:** Thay đổi đường dẫn file path `main.upl`

<img src="https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/bf6de091-a3a6-42ec-911c-5bbf59de926f" width="800px">

**Bước 2:** Chạy chương trình `Run`

**Thử với các test case sau:**

- **Test case 1: Kiểm tra identifier**

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

- **Test case 2: Kiểm tra if then else**

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

- **Test case 3: Kiểm tra comment**

```
begin
	int x;
	int y=x+1;
	/*	comments
	cho nhiều dòng
	*/
	
	int xyz;

	/* 
		abcdefghjkl
	/*

	// comment o day

	/*
	
end

```

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/9e296fc6-9f0c-4ddd-904a-3d17ba6c8469)

- **Test case 4: Kiểm tra keyword có bị nhầm với identifier**

```
begin
	int aaaaa233;
	int awhile;
	int aint;
	intA 122
	/ 
end
```

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/a3152b69-3827-419e-a929-eaf892431920)

- **Test case 5: Ví dụ trong file**

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

## 3.2. Thông báo lỗi

**Bước 1:** Thay đổi đường dẫn file path `main.upl`

<img src="https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/25993f57-3aab-4b50-89be-66b3ba5ec368" width="800px">

**Bước 2:** Chạy chương trình `Run`

**Thử với các test case sau:**

- **Test case 1: Chưa khai báo biến**

```
begin
	if a > b then {
		a =b
	}
end
```

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/9ee6f7f1-f8f0-42ad-82fd-cfe628f7b5bd)

- **Test case 2: Không có `begin`**

```

	if a > b then {
		a =b
	}

end
```

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/cb22e249-1941-4e7c-a9a9-b5f6d5d5426b)

- **Test case 3: Sai cú pháp**

```
begin
	int x = 5;
	int y = 5;
	/*	comments
	cho nhiều dòng
	*/
	
	bool a;
	if x>y then 
		int c=1;
	}

/*
	do {
		print(y);
	} while(x == y);
	
end
```

_Kết quả:_

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/fe68bdaa-a1ab-42e4-9a1a-2383d1fdc96b)

- **Test case 4: Đúng cú pháp**

```
begin
	int x = 5;
	int y = 5;
	/*	comments
	cho nhiều dòng
	*/
	
	bool a;//comment cho một dòng
	if x>y then {
		int c=1;
	}

	do {
		print(y);
	} while(x == y);
	
end
```

_Kết quả:_ Không in ra lỗi

- **Test case 5: Ví dụ trong file**

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

![image](https://github.com/CUNGVANTHANG/BAITAP_NHOM/assets/96326479/ef124ed9-0025-4551-b448-a7101c60dadd)
