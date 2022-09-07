#### | 람다란 무엇? 
`동작 파라미터화`를 이용해 변화하는 요구사항에 효과적으로 대응하는 코드 구현하여 더 유연하고 재사용할 수 있는 코드 생성 

- 이전에 익명 클래스로 구현할 수 있었으나 간결하지 않음. 
- 익명 클래스 처럼 이름 없는 함수이며 메서드를 인수로 전달할 수 있어 표현식이 비슷 

* `람다 표현식`
	* 메서드로 전달할 수 있는 익명 함수 단수화 한 것 (이름은 없지만 파라미터 리스트, 바디, 반환 형식을 가지며 예외 던질수 있음)
	* ⭐ 동작 파라미터를 통해 익명 클래스 판에 박힌 코드를 구현할 필요 X 

##### > [예시] Comparator 클래스

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
* 객체 생성 : Supplier (??) 
* 객체에서 소비 : Consumer, BiConsumer <A, B>
* 객체에서 선택/추출
* 두 값의 합 비교
* 두 객체 비교 : Comparator


#### | 함수형 인터페이스 (@FunctionalInterface)
* 정의 : 하나의 추상 메서드만을 정의
	* 추상 메서드를 즉석으로 제공하며 람다 표현식 `전체가 함수형 인터페이스의 인스턴스로 취급` (함수형 인터페이스 구현한 클래스의 인스턴스) 
* 선언시 실제 함수형 인터페이스가 아니라면 에러 발생
		* Multiple nonoverriding abstract methods found in interface 
* `JAVA.UTIL.FUNCTION`
	* Predicate, Function, Supplier, Consumer, BinaryOperator
```java
public interface Predicate<T> {
	boolean test(T t);
}

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

* Callable, PriviledgedAction => 인수를  받지 않고 제네릭 형식 T를 반환하는 함수를 정의 한다. 
```java
	Callable<Integer> c = () -> 42;  
	PrivilegedAction<Integer> p = () -> 42;
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

* java8
	* Predicate, Function => 박싱 동작을 피할 수 있는 IntPredicate, IntToLongFunction 
	
#### | 실행 어라운드 패턴 (execute around pattern)
* 자원 할당, 자원정리 (recurrent pattern(순환 패턴)은 자원을 열고 닫는다)
	* try-with-resource : 자원을 명시적으로 닫ㄷ을 필요가 없기에 간결한 코드 구현 
	* 유연성과 재사용성 추가
* 기대형식 (type expected) & 대상 형식 (target expected)
* 메서드 참조 통해 재사용성 높이자
	* `method reference`
		* 단 하나의 메소드만을 호출하는 경우 해당 표현식에서 불필요한 매개변수를 제거하고 사용.
		* (::) 토
* Comparator, Predicate, Function 은 디폴트 메서드 제공 
