중간 연산 
* 한 스트림을 다른 스트림으로 변환
* 스트림 파이프 라인 구성  (여러 연산 연결) 
* 스트림 요소를 소비하지 않음. 
* filter/ map

최종 연산 
* 스트림 요소를 소비해 최종 결과 도출 
* 파이프라인을 최적화하며 계산 과정 짧게 생략 
* forEach, findFirst, count, reduce 

### Collector ?
함수형 프로그래밍은 '무엇'을 원하는지 직접 명시ㅎ며
Collector 인터페이스는 스트림의 요소를 어떤 식으로 도출할지 지정한다. 


장점 
=> 높은 조합성과 재사용성 
ex. collect 를 통해 유연한 방식으로 정의 

* collect 는 리듀싱 연산 이용해 스트림의 각 요소를 방문하며 컬렉터 작업
* 함수를 요소로 변환시 컬렉터를 적용하며 최종 결과를 저장하는 자료구조에 값을 누적함. 
* Collectors utiliy class는 자주 사용하는 collector instance 를 쉽게 생성할수 있는 정적 팩토리 메서드 제공. 

[제공 기능]
* 스트림 요소를 하나의 값으로 리듀스하고 요약
* 요소 그룹화
* 요소 분할[^partitioning]

#### | 리듀싱과 요약
컬렉터로 스트림의 모든 항목을 하나의 결과로 합침. 
트리를 구성하는 다수준 맵, 합계를 가리키는 단순한 정수 등 다양한 형식으로 결과 도출 

> counting 

```java
public static <T> Collector<T, ?, Long> counting() {
	return reducing(0L, e -> 1L, Long::sum);
}
```

* 스트림의 최댓값과 최솟값 찾는 방법 
`Collectors.maxBy`  / `Collectors.minBy`

* Comparator 을 인수로 받음. 
```java
	Comparator<Dish> dishCaloriesComparator =
		Comparator.comparingInt(Dish::getCalories);
	
	Optional<Dish> mostCalorieDish = 
		menu.stream()
			.collect(maxBy(dishCaloriesComparator));
```

> 요약 (summarization) 연산

: 스트림의 객체의 숫자 필드의 합계나 평균등을 반환하는 연산에도 리듀싱 기능이 자주 사용 

단순 합계 이외 평균값 계산 등의 연산도 제공
*  Collectors.summingInt / summingLong / summingDouble
* Collectors.averagingInt / averagingLong / averagingDouble 
* summarizingInt / summarizinLong / summarizingDouble
* LongSummaryStatistics, DoubleStatistics 

> 문자열 연결 

* `joining` : toString 하여 모든 문자열 chaining 하여 반환 
	* Stringbuilder 이용하여 문자열 생성 

> 범용 리듀싱 요약 연산 

`Collectors.reducing`
* 범용 리듀싱 대신 특화된 컬렉터를 편의성을 위해 사용하지만 모든 컬렉터는 reducing 팩토리 메서드로 정의 가능하다. 
```java
public static <T,U> Collector<T,?,U> 
	reducing(U identity, Function<? super T,? extends U> mapper, 
					BinaryOperator<U> op)
					// 1. 시작값 혹은 반환값 
					// 2. 변환 함수 (항등 함수 = identity function)
					// 3. 두 항목의 하나의 값으로 합하는 BinaryOperator 
```

reducing()은 다중 연산과 함께 사용할때 좋다. => groupingBy() / paritioningBy()

✔️ collect vs. reduce 
collect  도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드이고 
reduce 는 두 값을 하나로 도출하는 불변형 연산ㅇ디ㅏ. 
reduce 를 잘못 사용하면 여러 스레드가 동시에 같은 데이터 구조를 고치려들면 망가져
리듀싱 연산을 병렬로 수행할 수 없다. (매번 새로운 리스트 할당하면 성능 저하)
*collect 메서드로 리듀싱 연산을 가변 컨테이너 관련 작업을 화며 병렬성을 확보하는것이 바람직하다. 

[컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행 가능]
* 제네릭 와일드 카드 사용
	* 누적자 형식을 알려주지 않음으로 형식의 자유로움을 더함. 

스트림 인터페이스에서 제공하는 메서드를 사용하면 코드가 더 복잡한 대신
재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의 추상화와 일반화를 가질수 있음. 
- 자동 언박싱 (autounboxing)

#### | 그룹화 
`Collectors.groupingBy`
*  분류 함수 (classification function)
	* 그룹화 함수가 반환하는 키 그리고 각 키에 대응하는 스트림의 모든 항목 리스트를 값으로 갖는 맵이 반환 됨. 

> groupingBy

다수준 그룹화를 수행할 때 함수형 프로그래밍의 장점이 더해진다. 
명령형 코드는 `다중루프`와 `조건문` 을 추가하여 가독성과 유지보수성이 떨어진다. 

![image](https://user-images.githubusercontent.com/16564373/193231922-34206276-51e1-4c7a-8a2f-c738d6eda408.png)

* 그룹화된 요소 조작 
	* Predicate 를 만족하는 키가 맵에 없는 경우는 키 자체가 맵에서 사라진다.  이럴 경우 Collectors 형식의 두번째 인수를 갖도록 groupingBy 팩토리 메서드를 오버로드해 문제를 해결함 
```java
Map<Dish.Type, List<Dish>> caloreisDishesByType = 
	menu.stream()
		.collect(groupingBy(Dish::getType,
			filtering(dish -> dish.getCalories() > 500, toList())));
```

* `filtering` 메소드는 Collectors클래스의 정적 팩토리 메서드로 프레디케이트를 인수로 받음. 각<u> 그룹의 요소와 필터링 된 요소를 재그룹화 한다. </u>
* 매핑 함수를 이용해 요소를 변환 작업 => `mapping()`
	* flapMap()으로 중복 태그 제거해 사용도 가능하다. 

#### | 다수준 그룹화 
`Collectors.groupingBy` 
* 일반적인 분류 함수와 컬렉터를 인수로 받는다
* 바깥 groupingBy 메소드에 스트림의 항목을 분류할 두번쨰 기준을 정의하는 내부 groupingBy 전달해서 두 수준으로 스트림의 항목 그룹화 가능 
```java
menu.stream().collect(
	groupingBy(Dish::getType, // 첫번쨰 수준의 분류 함수 
		groupingBy(dish -> {  // 두번째 수준의 분류 함수
			if (dish.getCalories() <= 400) return CaloricLevel.DIET;
			else if (dish.getCalories() <= 700)
				return CaloricLevel.NORMAL;
				else return CaloricLevel.FAT;
		})
	)
);
```

🔺 다수준 그룹화 연산은 다양한 수준으로 확장 가능하며 n 수준 그룹화의 결과는
			n 수준 트리 구조로 표현되는 n 수준 맵이 된다. 

![image](https://user-images.githubusercontent.com/16564373/193231974-8d4ed0a1-a945-47e1-8c72-6a80e44db9e5.png)

`groupingBy` 연산을 *버킷* 개념으로 생각할 수 있다. 
각 키나 버킷을 만들고 준비된 각각의 버킷을 서브 스트림 컬렉터로 채워나가는 것을
반복하며 n 수준 그룹화를 달성한다. 

> 서브그룹으로 데이터 수집

`groupingBy` 컬렉터의 형식은 제한 이 없으며 
두번째 인자에 `counting` 컬렉터를 이용도 가능하다.

```java
Map<Dish.Type, Long> typeCount = 
	menu.stream().collect(
		groupingBy(Dish::getType, counting())
	);
```

* `groupingBy(f)`
* `groupingBy(f, toList())`

> Collector 결과 다른 형식에 사용

그룹화 연산에서 맵의 모든 값을 `Optional` 로 감쌀 필요 없이 삭제가 가능하다. 
팩토리 메서드 `Collectors.collectingAndThen` 으로 컬렉터가 반환한 결과도 활용 가능하다. 

```java
menu.stream()
	.collect(groupingBy(Dish::getType,  // 분류 함수 
		collectingAndThen(
			maxBy(copmparingInt(Dish::getCalories)),  // 감싸진 컬렉터
			Optional::get // 변환 함수 
		)));
```
🔺 maxBy 는 Optional 형식으로 반환하는데 그 이유는 요리의 키는 맵에 추가되지 않는 것은 처음부터 존재하지 않기 때문이다. grouingBy 컬렉터는 스트림의 첫번째 요소를 그룹화 맵에 새로운 키를 lazy 하게 추가하며 리듀싱 컬렉터가 반환하는 형식을 사용하는 상황에서는 Optional 래퍼 사용이 필요하지 않다. 

팩서리 메서드 `collectingAndThen` 은 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환한다.  반환되는 컬렉터는 기존 컬렉터의 래퍼 역할을 하며 마지막 과정에서 변환 함수로 자신을 반환하는 값을 매핑한다. 
 (인수) <변환 함수> => 다른 컬렉터 반환 => 기존 컬렉터 래퍼 역할 => 변환 함수로 반환값 매핑

![image](https://user-images.githubusercontent.com/16564373/193232023-7bee775c-3b11-4aaf-9073-52122ed85e57.png)

✔️중첩 컬렉터 동작 방식 
* `groupingBy` 바깥쪽에 위치하며 스트림을 세개의 서브 스트림으로 그룹화 한다. 
* `collectingAndThen` 컬렉터로 서브 스트림을 감싸며 적용한다. 
* 이 결과를 세번째 컬렉터 `maxBy` 으로 감싼다. 
* 리듀싱 컬렉터가 서브 스트림에 연산을 수행한 결과에` collectingAndThen` 의 `Optional::get` 변환 함수 적용 
* 맵의 분류 키에 대응하는 세 값이 결과로 나온다. 

> mapping

`mapping()` 는 입력 요소를 누적하기 전에 매핑 함수를 적용해서 다양한 형식의 객체를 주어진 형식의 컬렉터에 맞게 변환하는 역할을 한다. 

> 분할 함수 (partitioning function)

`Predicate` 를 분류 함수로 사용하는 특수한 그룹화 기능 으로 Boolean 을 반환하기에 맵의 키 형식은 Boolean 이다. => 두개의 그룹으로 분류 (true/false)

* 장점 
	* 참, 거짓 두가지 요소의ㅏ 스트림 리스트를 모두 유지한다는 것이 분할의 장점 
		* `partioningBy` : 내부적으로 특수한 맵과 두개의 필드로 이루어져 간결하고 효과적 

> 소수와 비소수로 분할 

```java
public Map<Boolean, List<Integer>> partitionPrimes(int n) {
	return IntStream.rangeClosed(2, n).boxed()
				.collect(
					partitioningBy(candidate -> isPrime(candidate));	
				)
}
```

*Collectors 정적 팩토리 메서드*
| 팩토리 메서드      | 반환 형식                    |
| ------------------ | ---------------------------- |
| toList             | List< T>                     |
| toSet              | Set< T>                      |
| toCollection       | Collection< T>               |
| counting           | Long                         |
| summingInt         | Integer                      |
| averagingInt       | Double                       |
| summarizingInt     | IntSummaryStatistics         |
| joining            | String                       |
| maxBy              | Optional< t>                 |
| minBy              | Optional < t>                |
| reducing           | type produced by operation   |
| collectingAndThen | type returned by transform() |
| groupingBy         | Map<L, List< T>>             |
| partitioningBy     | Map<Boolean, List< T>>                             |

#### | Collector 인터페이스 
리듀싱 연산자(컬렉터)를 어떻게 구현할지 제공하는 메서드 집합으로 구성한다. 

```java
public interface Collector<T, A, R> {
	Supplier<A> supplier();
	BiConsumer<A, T> accumulator();
	Function<A, R> finisher();
	BinaryOperator<A> combiner();
	Set<Characteristics> characteristics();
}
```

* T : 수집될 스트림 항목의 제네릭 형식
* A : 누적자 (수집 과정에서 중간 결과를 누적하는 객체의 형식)
* R : 수집 연산 결과 객체의 형식(대게 컬렉션 형식)

```java
public class ToListCollector<t> implements Collector<T, List<T>, List<T>>
```

> characteristics

collect 메서드가 어떤 최적화(병렬화)를 이용해 리듀싱 연산을 수행할지 결정하고 돕는 집합 제공
<u> 불변 집합을 반환</u> 하며 *병렬로 리듀스 할건지 확인하고 어떤 최적화를 진행할지 제공*
- `UNORDERED` : 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향 받지 않음
- `CONCURRENT` : 다중 스레드에서 accumulator 를 동시에 호출 가능하며 병렬 리듀싱을 수행할 수 있다. UNORDERED 와 함께 설정하지 않았다면 정렬되어 있지 않은 상황에서 병렬 리듀싱 수행 
- `IDENTITY_FINISH` : identity 적용 뿐 아니라 생략 가능하다. 
	- 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다. 
	- 누적자 A -> R 안전하게 형변환 
	

```java
public Set<Characteristics> characteristics() {
	return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
}
```

`IDENTITY_FINISH` 수집 연산은 새로 구현하지 않고 같은 결과 도출 가능 (발행, 누적, 합침) 인수로 받아 오버로드하며 Collector 인터페이스의 메서드가 반환하는 함수와 같은 기능 수행 가능하다. 



> supplier

빈 결과로 이루어진 supplier 를 반환해야 한다. 
수집과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수이며
`ToListCollector` 처럼 누적자를 반환하는 컬렉터에서 
<u> 누적자가 비어있는 스트림의 수집 과정의 결과</u>가 될 수 있다. 

```java
public Supplier<List<T>> supplier() {
	return () -> new ArrayList<T>();
	// return ArrayList::new;
}
```

> accumulator 

리듀싱 연산을 수행하는 함수를 반환하며 
스트림에서 n 번째 요소를 탐색할때 두 인수 [누적자, n 번째 요소 함수]에 적용한다. 

```java
public BiConsumer<List<T>, T> accumulator() {
	return (list, item) -> list.add(item);
	// return List::add;
}
```

> finisher 

스트림 탐색을 끝내고 누적자 객체를 최종 결과로 반환하며 누적 과정을 끝낼때 까지
호출할 함수를 반환한다. 누적자 객체가 이미 최종 결과인 경우도 있다. 

```java
public Function <List<T>, List<T>> finisher() {
	return Function.identity();
}
```

위 세가지 (supplier, finisher, accumulator) 으로 순차적 스트림 리듀싱 기능을 수행이 가능하며 다른 중간 연산과 파이프라인을 구성할수 있게 *게으른 특성* 과 *병렬 실행* 도 고려해야해 스트림 리듀싱 기능 구현은 복잡하다. 

![image](https://user-images.githubusercontent.com/16564373/193232080-890098f7-83b5-41aa-b56f-877dccab82bf.png)

> combiner 

스트림의 서로 다른 서브 파트를 병렬로 처리할 때 누적자 처리 결과를 정의한다. 
스트림의 두번째 서브 파트에 항목 리스트를 첫번쨰 서브 파트 결과 리스트를 뒤에 추가하면 된다

```java
public BinaryOperator<List<T>> combiner() {
	return list1, list2 -> {
		list1.addAll(list2);
		return list1;	
	}
}
```

![image](https://user-images.githubusercontent.com/16564373/193232118-4252bb4d-63e0-4e67-9c5c-a89b47fb64d7.png)

*병렬 리듀싱 수행 과정*
* 스트림을 재귀적으로 분할한다. (병렬 수행 속도는 순차 수행 속도보다 느려져서 병렬 수행의 효과가 상쇄된다.) 프로세싱 코어의 개수를 초과하는 병렬 작업은 효율적이지 않음. 
* 서브스트림의 각 요소에 리듀싱 연산을 순차적으로 적용해 병렬로 처리 가능하다. 
* 마지막에 combiner 메서드가 반환하는 함수로 모든 부분결과를 쌍으로 합쳐 <u> 분할된 모든 서브스트림의 결과를 합치면 연산 완료이다. </u>

