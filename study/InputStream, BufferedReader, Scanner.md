----

> Stream 

* 데이터가 들어온 순서대로 흐르는 `단방향 통로` => 데이터의 흐름 
	* `입력스트립`, `출력스트림` 으로 나뉨 
* Stream 을 통해 byte/byte[]  사용
* 동기적, blocking 방식 동작 
	=> 데이터를 읽거나 쓰기 위해 스트림에 요청하면 스트림은 다시 데이터를 읽거나 쓸 수 있을때까지 다른 작업을 하지 않고 기다린다. 
	- 닫아주지 않으면 메모리 누수 발생
		- 예외처리 필요! 


> InputStream / OutputStream

Q. `inputStream` 과 `System.in` 이 무엇일까?
- System.in 은 InputStream 타입의 정적 필드
- System.in 변수는 표준 입력 스트림으로 console 명령줄 인수를 입력 받을 수 잇다. 

```java
import java.io.IOException;
import java.io.InputStream;
 
public class Input_Test {
 
	public static void main(String[] args) throws IOException {
		
		InputStream inputstream = System.in;
		int a = inputstream.read();
		System.out.println(a);
	
	}
}
```

* inputStream 의 특징 
	* 인코딩 형식의 10진수로 변슈에 저장 
	* 1 byte 만 읽음

=> 컴퓨터의 데이터는 바이트 단위 데이터로 구성되어 잇고 데이터를 저장하던 전달하던 컴퓨터 바이트 단위로 데이터가 저장됨
* InputStream의 입력 메소드 read() 는 1 바이트 단위로 읽어 들이고 문자가 2 byte 이상 구성되어 있는 인코딩 사용 경우 1 byte 만 읽고 나머지는 스트림에 남아있기 때문에 인코딩 값을 10진수로 변환한 값이 출력됨. 

> Scanner(system.in) 그리고 InputStreamReader(System.in)

Scanner(system.in) 은 입력 바이트 스트림인 InputStream 을 통해 표준을 입력 받음. 

```java
import java.io.IoException;
import java.io.InputStream;
import java.util.Scanner;

public class Input {
	public static void main(String[] args) throws IOException {
		InputStream inputstream = System.in;
		Scanner scan = new Scanner(inputstream);
		
		int a = scan.nextInt();
	}
}

```

왜 Scanner 안에 InputStream 이 들어갈까?

![[Pasted image 20230405091019.png]]

* Scanner 생성자가 Overloading 
```java
public Scanner(InputStream source) {
   this(new InputStreamReader(source), WHITESPACE_PATTERN);
}
```

> InputStreamReader 

![[Pasted image 20230405091444.png]]

➡️ InputStream 과 어떻게 다른가?
* InputStream 의 특징을 해결하기 위고 `온전히 문자를 읽어들이기 위헤` 확장한 것
	* 입력받은 데이터는 int 형으로 저장되는데 10진수의 utf-16으로 저장
	* 1 BYTE 만 읽음 

⭐ 바이트 단위로 읽어 드리는 문자단위로 데이터로 변환시키는 중개자 역할! 


> BufferedReader

![[Pasted image 20230405091523.png]]


```java 
InputStream inputstream = System.in;
InputStreamReader sr = new InputStreamReader(inputstream);
BufferedReader br = new BufferedReader(sr);
```

1.  기본적으로 바이트 스트림인 InputStream 을 통해 바이트 단위로 데이터를 입력 받음 
2.  입력 데이터를 char 형태로 처리하기 위해 중개자 역할인 문자스트림 InputStreamReader 로 감싸줌 

➡️ BufferedReader 필요한 이유? 
* Scanner -> InputStreamReader : 문자를 처리함 (문자열이 아님!)
* 문자열을 입력하고 싶다면 매번 배열을 선언해야 하는 단점이 남아있음
* Buffer를 통해 입력받은 문자를 쌓아둔 뒤 한번에 문자열 처럼 처리 
	* readLine() 을 통해 한줄 전체를 읽어 char 배열 생성 필요 없이 String으로 return 
	* ⭐ 문자를 모아둔 후 보내니 속도가 빠르고 별다른 정규식 검사 없으니 더 빠름. 
  ![[Pasted image 20230405091853.png]]


* Byte Type = InputStream
* Char Type = InputStreamReader
* Char Type 의 직렬화 = BufferedReader


###### ✅ 정리

* InputStream 은 바이트 단위로 데이터를 처리한다. 또한 System.in 의 타입도 InputStream 이다.
* InputStreamReader 은 문자(character) 단위로 데이터를 처리할 수 있도록 돕는다. InputStream 의 데이터를 문자로 변환하는 중개 역할을 한다.
* BufferedReader 은 스트림에 버퍼를 두어 문자를 버퍼에 일정 정도 저장해둔 뒤 한 번에 보낸다.


-----
[참고]
https://st-lab.tistory.com/41
https://javanitto.tistory.com/11