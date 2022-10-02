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

![[Pasted image 20220929142556.png]]

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
