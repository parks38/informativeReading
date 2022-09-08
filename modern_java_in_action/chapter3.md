#### | 람다란 무엇? 
`동작 파라미터화`를 이용해 변화하는 요구사항에 효과적으로 대응하는 코드 구현하여 더 유연하고 재사용할 수 있는 코드 생성 

- 이전에 익명 클래스로 구현할 수 있었으나 간결하지 않음. 
- 익명 클래스 처럼 이름 없는 함수이며 메서드를 인수로 전달할 수 있어 표현식이 비슷 

* `람다 표현식`
	* 메서드로 전달할 수 있는 익명 함수 단수화 한 것 (이름은 없지만 파라미터 리스트, 바디, 반환 형식을 가지며 예외 던질수 있음)
	* ⭐ 동작 파라미터를 통해 익명 클래스 판에 박힌 코드를 구현할 필요 X 

##### [예시] Comparator 클래스

> 익명 클래스 구현 
```java
Comparator<Integer> rank = new Comparator<Integer>() {
	public int compare(Integer a1, Integer a2) {
		return a1.compareTo(a2);
	}
}
```
> 람다 구현
```java
Comparator<Integer> rank 
	= (Integer a1, Integer a2) -> a1.compareTo(a2);
```

* 파라미터 리스트 : Comparator 의 compare 메서드
* 화살표 : 파라미터 리스트와 바디 구분
* 람다 바디 : 반환값 표기 

> 기본 문법 

(parameter) -> expression
(parameter) -> { statemet; }  // block style 

> 람다 예제 
*  boolean 표현식 : Predicate 
* 객체 생성 : Supplier 
* 객체에서 소비 : Consumer, BiConsumer <A, B>
* 객체에서 선택/추출: Function 
* 두 값의 합 비교 : IntBinaryOperator
* 두 객체 비교 : Comparator, BiFunction, ToIntBiFunction


#### | 함수형 인터페이스 (@FunctionalInterface)
* 정의 : *하나의 추상 메서드만을 지정 하고 함수형 인터페이스의 추상 메서드는 람다 표현식의 시그니처를 묘사한다.

	* 추상 메서드를 즉석으로 제공하며 람다 표현식 `전체가 함수형 인터페이스의 인스턴스로 취급` (함수형 인터페이스 구현한 클래스의 인스턴스) 
* 선언시 실제 함수형 인터페이스가 아니라면 에러 발생
		* Multiple nonoverriding abstract methods found in interface 
* `JAVA.UTIL.FUNCTION`
	* Predicate, Function, Supplier, Consumer, BinaryOperator

> Predicate 

 => test 추상 메서드 정의하며 T의 객체의 인수로 받아 boolean 반환 
```java
public interface Predicate<T> {
	boolean test(T t);
}

//example
Predicate<String> nonEmptyString = (String s) -> !s.isEmpty();
```

> Consumer 

=> 제네릭 형식 T 객체를 받아 void 반환하는 `accept` 추상 메서드 정의 
* 인수로 받아 동작을 수행 
```java
@FunctionalInterface
public interface Consumer<T> {
	void accept(T t);
}

public <T> void forEach (List<T> list, Consumer<T> c {
	for (T t: list) c.accept(t);
})
```

> Function 

=> T 인수를 받아 제네릭 형식 R 객체를 반환 / 추상 메서드 `apply` 정의
```java
@FunctionalInterface
public interface Function<T, R> {
	R apply(T t);
}

List<Integer> l = map(
	Arrays.asList("lamdas", "in", "action"),
	(String s) -> s.length() // Function 의 apply method 구현 
)
```

[기본형 특화]
* Boxing (기본 > 참조)
* Unboxing (참조 > 기본)
* Autoboxing : (박싱 언박싱 자동 구현)
	* 비용 소비 
	* 박싱한 값은 메모리를 더 소비하며 기본형을 가져올 때도 메로리를 탐색하는 과정 필요

* java8 오토박싱 동작을 피할수 있도록 특별한 버전의 함수형 인터페이스 제공
	* IntPredicate / DoublePredicate/ IntConsumer/ LongBinaryOperator/IntFunction등..

> ETC 

```java
public interface Comparator<T> {
	int compare(T o1, T o2);
}

public interface Runnable {
	void run();
}

// java.awt.event.ActionListener
public interface ActionListener extends EventListener {
	void actionPerformed(ActionEvent e);
}

// java.util.concurrent.Callable
public interface Callable<V> {
	V call() throws Excpetion;
}

// java.security.PrivilegedAction
public interface PrivilegedAction<T> {
	T run();
}

```

#### | 함수 디스크립터 
* 함수형 인터페이스의 추상 메서드 시그니처(signature) 는 람다 표현식의 시그니처를 가르킴 
	* `Function Descriptor` : 람다 표현식 시그니처 서술 메서드
	* 표현식 유효성 검사
		* 변수에 할당
		* @ FuncationalInterface 추상 메서드와 같은 시그니처를 갖는다 
		* 함수형 인터페이스를 인수로 받는 메서드로 전달
			* 인수 (argument = 전달인자/값)
			* 인자 (parameter = 매개변수)
```java
// 1 & 2 인수 
plusNumber(1,2);

// a & b = 인자 
function plusNumber(a,b) {
  return a + b;
}
```

	
#### | 실행 어라운드 패턴 (execute around pattern)
* 자원 할당, 자원정리 (recurrent pattern(순환 패턴)은 자원을 열고 닫는다)
	* ex] database 작업 
	
-> 이와 같은 경우 `호출 방식이 고정`이기 때문에 변화하는 부분에 대한 `동작 파라미터화를 구현하면 간결`해 진다. 

> 실행 어라운드 패턴을 적용한 유연해 지는 과정

*1단계 : 동작 파라미터화 
```java
	public String processFile() throws IOException {
		try (BufferedReader br = 
			new BufferedReader(new FileReader("data.txt"))) {
			return br.readLine();
		}	
	}
```

*2단계: 함수형 인터페이스를 이용해 동작 전달
```java 
public static String processFile(Process p) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return p.process(br);
    }
}

@FunctionalInterface
interface Process {
    String process(BufferedReader br) throws IOException;
}
```

*3단계 : 동작 실행 
```java
public static String processFile(Process p) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return p.process(br);
    }
}
```

*4단계: 람다 전달
```java
String result = 
	processFile((BufferedReader br) -> br.readLine() + br.readLine());
```
* try-with-resource : 자원을 명시적으로 닫을 필요가 없기에 간결한 코드 구현 
* 유연성과 재사용성 추가

#### | 형식 검사, 형식 추론, 제약 
* *context 이용해서 람다의 형식 추론

> 형식 검사 

* 기대형식 (type expected) = 대상 형식 (target expected)
	* 파라미터나 람다 할당 변수가 기대되는 표현식

```java
// 1. context 확인 
filter(inventory, (Apple a) -> a.getWeight() > 150);

// 2. 대상 형식 
filter(inventory, Predicate<Apple> p)

// 3. 대상 형식의 추상 메서드? 
boolean test(Apple apple)

// 4. Apple 인수로 받아 boolean 반환하는 test 메서드
Apple -> boolean 

// 5. 함수 디스크립터는 Apple -> boolean 이므로 람다의 시그니처와 일치
// 람다도 Apple 을 인수로 받아 boolean 반환하므로 코드 형식 검사 성공적 
```

* `대상 형식 (target typing)` 특징 때문에 같은 람다 표현식이라도 호환되는 추상 메서드를 가진 다른 함수형 인터페이스로 사용 될수 있음. 

* `Callable`, `PriviledgedAction` 
	=> 인수를  받지 않고 제네릭 형식 T를 반환하는 함수를 정의 한다. 

```java
	Callable<Integer> c = () -> 42;  
	PrivilegedAction<Integer> p = () -> 42;
```

> 형식 추론 

* 할당문 콘텍스트, 메서드 호출 콘텍스트(파라미터, 반환값), 형변환 (cast) 콘텍스트 등으로 람다 표현식의 형식을 추론 가능 
* 콘텍스트 (대상 형식)을 이용해 람다 표현식과 함수형 인터페이스를 추론 
	* *함수 디스크터를 함수 형식을 통해 컴파일러는 람다의 시그니처 추론 가능
```java
	Comparator<Apple> c =
		(a1, a2) -> a1.getWeight().compareTo(a2.getWeight()); // 형식 추론 
```

> capturing lambda (람다 캡쳐링)

* 자유 변수 (free variable) (파라미터로 넘긴 변수가 아닌 외부 정의된 변수) 활용
* 제약! 인스턴스 변수와 정적 변수를 자유롭게 캡쳐 할 수 있으나 `지역 변수는 명시적으로 final 선언`이 되어 있어야 함. 
	* 한번만 할당할 수 있는 지역 변수를 캡쳐할 수 있다. 

```java 
int portNumer = 1337;

Runnable r = () -> System.out.println(portNumber);
// final 처럼 취급되거나 선언 되어야 함.

portNumber = 31337;
```

* 인스턴스 변수 => 힙 저장 / 지역 변수 => 스택 저장 
* 람다는 지역변수에 바로 접근할 수 있다는 가정하에 람다가 스레드에 실행된다면 할당한 `스레드가 사라져서 변수 할당이 해제되었는데도 람다를 실행하는 스레드에서는 해당 변수를 접근`하려고 할수 있다. 
* 원래 변수에 접근을 허용하는것이 아닌 `자유 지역 변수의 복사본`을 제공 
* 복사본은 값이 바뀌지 않아 한번만 값을 할당해야한다는 제약. 

#### | 메서드 참조 (method reference)
* 기존 메서드 정의를 재활용해 람다를 전달 
* 메서드 참조 통해 재사용성 높이자
	* `명시적으로 메서드를 참조함으로써 가독성을 높임`
		* 단 하나의 메소드만을 호출하는 경우 해당 표현식에서 불필요한 매개변수를 제거하고 사용.
		* (::) 

| 메서드 참조 유형   | 예                     | 같은 기능 람다                                       |
| ------------------ | ---------------------- | ---------------------------------------------------- |
| 정적               | Integer::parseInt      | str -> Integer.parseInt(str)                         |
| 한정적(인스턴스)   | Instant.now()::isAfter | Instant then = Instant.now <br> t -> then.isAfter(t) |
| 비한정적(인스턴스) | String::toLowerCase    | str -> str.toLowerCase()                             |
| 클래스 생성자      | TreeMap<K,V>::new      | () -> new TreeMap<K,V>()                             |
| 배열 생성자        | int[]::new             | len -> new int[len]                                                     |

> 유형

* 정적 메서드 가리키는 참조
* 인스턴스 메서드 참조
	* 수신 객체를 특정하는 한정적(bound) 인스턴스 참조
		* 정적 참조와 비슷하며 함수 객체가 받는 인수와 참조되는 메서드가 받는 인수가 같음
	* 수신 객체를 특정하지 않는 비한정적(unbound) 인스턴스 참조
		* 함수 객체를 적용하는 시점에서 수신 객체 알려줌
		* 수신 객체 전달용 매개변수가 매개변수 목록의 첫번쨰로 추가되며 그 뒤로는 참조되는 메서드 선언에 정의된 매개변수들이 뒤따름.
		* 스트림 파이프라인에서의 `매핑과 필터` 함수에 쓰임
* 생성자 가르키는 참조 (팩터리 객체로 사용)
	* 클래스 생성자 가르키는 참조
	* 배열 생성자 가르키는 참조 


> 람다 메서드 참조 

*1단계 코드 전달
2단계 익명 클래스 사용
3단계 람다 표현식 사용 
	- 람다 표현식 시그니처 정의; 
		- 오직 하나의 추상 메서드를 정의 하는 인터페이스 의 시그니처 (함수 디스크립터)  
4단계 메서드 참조 사용 

#### | Default Method
* Comparator, Predicate, Function 은 디폴트 메서드 제공 
* 여러 개의 람다 표현식을 조합해서 복잡한 람다 표현식 제공 
	* 디폴트 메서드는 추상 메서드가 아니므로 (추가로 메서드를 제공한다는) 함수형 인터페이스 정의에 어긋나지 않음. 

>  Comparator 조합 : 역정렬 

```java
inventory.sort(
	comparing(Apple::getWeight)
	.reversed()
	.thenComparing(Apple::getCountry)
);
```

> Predicate 조합 
* negate, and, or  조합 
```java 
Predicate<Apple> redAndHeavyAppleOrGreen = 
	redApple.and(apple -> apple.getWeight() > 150)
			.or(apple -> GREEN.equals(a.getColor()));
```

> Function 조합 
* andThen, compose 
```java
Function<Integer, Integer> h = f.andThen(g);  // g(f(X))
Function<Integer, Integer> h = f.compoase(g);  // f(g(x))
```


#### | 정리 
* `람다 표현식` 은 익명 함수의 일종 => 이름 없고 파라미터 리스트, 바디, 반환 형식을 가지며 예외를 던짐
* @FunctionalInterface : 하나의 추상 메서드만을 정의 
	* 추상 메서드를 즉석으로 제공 가능
	* `람다 표현식 전체가 함수형 인터페이스의 인스턴스로 취급`
	* 종류 : Predicate, Function, Supplier, Consumer, BiOperator
	* 박싱 피할수 있는 기본 특화 인터페이스 제공 : IntPredicate, IntToLongFunction
* 실행 어라운드 패턴 - 람다를 활용하여 유연성과 재사용성 추가로 획득
* 람다 표현식의 기대 형식 = 대상 형식 이라고 함. 
* 메서드 참조를 이용하여 메서드 구현을 재사용하고 전달 
* default method : Comparator, Predicate, Function (조합 사용 가능)
