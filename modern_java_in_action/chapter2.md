## 동작 파라미터화 코드 전달하기 

* 동작 파라미터화 란?
	* 어떻게 실행할 것인지 결정하지 않은 코드 블럭 
		* 나중에 프로그램에서 호출 (호출은 나중으로 미뤄짐 )
	* 시시각각 변하는 사용자 요구사항에 대해서 효과적으로 대응하면서도 엔지니어링적인 비용이 최소화되게 대응
		* 공통된 부분의 추상화가 필요 => 람다 

* 수행 가능 기능 
	* 리스트의 모든 요소에 대해서 "어떤 동작"을 수행 
		=> filter/ sort/ distinct 
	* 리스트 관련 작업을 끝낸 다음에 "어떤 다른 동작"을 수행 가능
		=> `Runnable` thread 생성으로 afterlogic 수행 
	* 에러가 발생하면 "정해진 어떤 다른 동작" 수행 
		=> `Optional.orElseThrow(RuntimeException::new)`

#### | 변화하는 요구사항에 대응 
* 반복되는 코드 추상화 
* 색을 파라미터화 = `filterApplesByColor(inventory, GREEN)`
* 가능한 모든 속성으로 필터링 = `filterApples(inventory, GREEN, 0 , true)`

#### | 동작 파라미터화 
* 변화하는 요구사항에 좀 더 유연하게 대응 
> predicate   
- 참또는 거짓을 반환하는 함수 
```java
	public interface Predicate {
	
		boolean test (Object object);
	}

	// 예상 구현식 
	Predicate<T> predicateExample = new Predicate<T>() {  
	    @Override  
	    public boolean test(T t) {  
	        return t.equals("111");  
	    }  
	};
	
	// 람다로 작성
	Predicate<T> example = (t -> t.eqauls("111"));
	```

- predicate 가 중요한 이유는 ? 
	- 람다에서 stream API 는 Predicate / Function/Comparator/Supplier/Consumer 를 인자로 받는 경우가 많다. 
	
```java
public interface Stream<T> extends BaseStream<T, Stream<T>> {

	  Stream<T> filter(Predicate<? super T> var1);
}
```



⭐Stratey Pattern (전략 패턴)
* 각 알고리즘은 캡슐화하는 알고리즘 패밀리를 정의해두고 런타임에 알고리즘을 선택 
* 객체가 하는 `행위를 전략`으로 만들고 `동적으로 수정이 필요한 경우 전략`으로 바꾸는 것만으로 행위의 수정이 가능하도록 만듬. (유연하게 확장)
* 사용 이유
	* SOLID 원칙 중 OCP(open closed principle)에 위배되지 않게 구성 
		* OCP : 확장에는 열려 있으나 변경에는 닫혀 있어야 한다는 원칙 (ex. JDBC 관계)
			* `확장` : 새로운 타입을 추가함으로써 새로운 긴으을 추가(구현)
				* 확장에 열려있다. => 새로운 타입(클래스, 모듈, 함수)를 추가함으로써 기능을 확장 
			* `변경` : 확장이 발생했을 때 상위 레벨이 영향을 미치지 않아야 하는 것
				* 변경 했을때 닫혀있다 => 확장 발생시 해당 코드 호출 쪽에서는 변경 발생 X 
		* 구현 방법
			* `확장 될 것과 변경` 될 것을 엄격히 구분
			* 두 모듈이 만나는 지점에 `인터페이스를 정의`
			* `구현에 대한 의존`보다는 `정의된 인터페이스를 의존` 하도록 코드 작성
			* 변경이 발생하는 부분을 `추상화` 하여 분리 
			

✅ 예시 ![[Screen Shot 2022-09-02 at 7.31.50 PM.png]]

* strategy : 인터페이스나 추상 클래스로 외부에서 동일한 방식으로 알고리즘을 호출 방법 명시
* ConcreateStrategy 1,2,3 : strategy pattern에 명시한 알고리즘을 실제로 구현
* context : 
	* 스트래티지 패턴을 이용하는 역할 수행 
	* 필요에 따라 동적으로 구체적인 전략을 바꿀수 있도록 setter 제공 

#### | 익명 클래스 사용
* 익명 클래스 란? 
	* 프로그램에서 일시적으로 한번만 사용되고 버려지는 객체
	* 재사용 되지 않음. -> 확장성에 좋지 못함. (단발성 필요시 사용)
*  코드의 장황함 (verbosity) 로인해 나쁜 특성
* 유지보수하는데 시간이 오래걸림. 
* 코드 전달 과정에서 객체를 만들고 명시적으로 새로운 동작을 정의하는 메서드를 구현해야 한다는 것은 동일하다. 
```java
List<Apple> redApples = filterApple(inventory, new ApplePredicate() {
	public voolean test(Apple a) {
		return RED.equals(a.getColor());
	}
})
```

** 추가 
 [이펙티브 자바 3판] 
 * 아이템 42. 익명 클래스보다는 람다를 사용하라
	 - 익명 클래스 방식은 코드가 길기 떄문에 함수형 프로그래밍 (functional programming) 에 적합하지 않음. 
	 - https://madplay.github.io/post/prefer-lambdas-to-anonymous-classes
* 43. 람다 보다는 메서드 참조를 이용해라 
	* 메서드 참조 쪽이 짧고 명확하다면 메서드 참조를 쓰고, 그렇지 않을 때만 람다를 사용하라.
	* https://jinseongsoft.tistory.com/208

#### | 람다 표현식 사용 

```java
	List<Apple> result = filterApples(inventory, 
		(Apple apple) -> RED.equals(apple.getColor()));
```

#### | 리스트 형태를 추상화 

```java
public interface Predicate<T> {
	boolean test(T t);
}

// 추상화 
public static<T> List<T> filter(List<T> list, Predicate<T> p) {
	List<T> result = new ArrayList<>();
	for(T e: list) {
		if (p.test(e)) {
			result.add(e);
		}
	}
}

// example
List<Apple> redApples = filter(inventory, 
				(Apple apple) -> RED.equals(apple.getColor()));
```

[참고 자료]
- https://dev0101.tistory.com/36

#### | 실전 예제 
> Comparator 

```java
	//java.util.Comparator
	public interface Comparator<T> {
		int compare(T o1, T o2);
	}
	
	inventory.sort((Apple a1, Apple a2) -> 
					   a1.getWeight().compareTo(a2.getWeight()))
```

> Runnable 

* 자바 스레드를 통해 병렬 코드 블럭 실행 
```java
	//java.lang.Runnable
	public interface Runnable {
		void run();
	}
	
	Thread t = new Thread(new Runnable() {
		public void run() {
			sout("hello word");
		}
	});
	
	Thread t = new Thread(() -> sout("hello world"));
```

> Callable 

* `ExecutorService`
	* 태스크 제출과 실행 과정의 연관성을 끈어줌. 
	* 태스크를 스레드 풀로 보내고 결과를 Future 로 저장 (Thread/ Runnable 과 차이점)
	
```java
	//java.util.concurrent.Callable
	public interface Callable<V> {
		V call();
	}

	ExecutorService es = Executors.newCachedThreadPool();
	Future<String> threadName = es.submit(new Callable<String>() {
		@Overrid
		public String call() throws Exception {
			return Thread.currentThread().getName();
		}
	})

	// lambda 이용
	Future<String> threadName = 
		es.submit(() -> Thread.currentThread().getName());
```

> GUI Event 

* 마우스 클랙/ 문자열 위로 이동하는 이벤트에 대응하는 동작 수행하는 식
* `javaFX -> .onSetAction()` `EventHandler` 전달하며 이벤트 반응 설정 가능 
```java
	Button button = new Button("Send");
	button.setOnAction(new EventHandler<ActionEvent>() {
		public void handle (ActionEvent action) {
			label.setText("sent!!");	
		}
	})

	// lambda 변환
	button.setOnAction((ActionEvent event) -> label.setText("Sent!"));
```

##### | 정리 
* 동작 파라미터화는 
	* 메서드 내부적으로 다양한 동작을 수행할 수 있도록 코드를 메서드 인수로 전달
	* 변화하는 요구사항에 더 잘 대응하며 엔지니어링 비용 줄여줌
* 코드 전달 기법 사용시 동적울 메서드의 인수로 전달
* 자바 API => 동작으로 파라미터화 : sort, Thread, GUI 처리 가능 