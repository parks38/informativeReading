## Chapter 1 
---
자바에서 가장 큰 변화는 java8 에서 시작함
- lambda EX. SORT 
- 멀티코어 CPU 대중화 (하드웨어 변화)
	- 이전 : 
		- java 1.0: 스레드, 락, 메모리 모델 지원 
		- java5 : thread pool / concurrent collection
		- java 7 : 병렬 실행 (fork/ join framework)
	- java8: 
		- stream API
			- 데이터베이스 질의 언어에서 고수준 언어로 동작 구현 에서 최적의 저수준 실행 방법을 선태하는 방법으로 동작 
			- 멀티코어 CPU (에러 발생) 보다 비용이 비싼 `SYNCHORNIZED` 사용 x  
		- 메서드에 코드를 전달하는 기법
			- 메서드 참조와 람다 
			- 간결한 방법으로 동작 (BEHAVIOR PARAMETERIZATION) 구현 
		- 인터페이스의 <U>디폴트 메서드 </U>
		- `FUNCATIONAL PROGRAMMING`
	- java 9 : reactive programming (병렬 실행 기법)  RxJava


#### | Stream Process 
* 한번에  한개씩 만들어지는 연속적인 데이터 항목들의 모음 
* 스트림 동작 방법 
	* 여러 행의 스트림을 입력 받아 여러 행의 스트림을 출력 
	* 자동차 생산 공장과 같이 한개씩 처리하지만 작업장에서는 동시에 작업 처리 
	* ⭐ (데이터 베이스 질의 처럼) 고수준으로 추상화해서 스트림으로 처리 
	* 스트림 파이프 라인 이용 여러 CPU 코어에 쉽게 할당 
	* (공짜) 병렬성 

- stream api -> paralellStream() 이용해 stream 객체 생성해서 병렬 처리 
	- 대량의 데이터 병렬 처리 코드 작성 
```java
public class ParallelReduceMinMax {
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(4, 2, 8, 1, 9, 6, 7, 3, 5);
 
        // 최대 값 구하기 - 병렬
        int max = intList.parallelStream().reduce(1, Integer::max);
        System.out.printf("최대값 : %s\n", max);
 
        // 최소 값 구하기 - 병렬
        int min = intList.parallelStream().reduce(1, Integer::min);
        System.out.printf("최소값 : %s\n", min);
    }
}
```
- 내부 확인
	- 몇개의 스레드와 어떤 이름으로 반영 되는지 확인 
```java
package org.example.streamparallel;
 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
public class InsideParallelStream {
 
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 
        // 스트림 내부의 쓰레드 값을 구함.
        intList.parallelStream().forEach(value -> {
            // 현재 쓰레드 이름을 구함.
            String threadName = Thread.currentThread().getName();
 
            /* 스레드 이름과 데이터 값을 출력한다. */
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.printf(currentTime.format(formatter) + " -> Thread Name : %s, Stream Value : %s\n", threadName, value);
 
            // 시간 확인을 위해 2초간 sleep 함
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {}
        });
    }
}
```

- 순차처리 스트림/ 병렬처리 스트림 비교 
```java
public class ParallelExample { 
	public static void main(String[] args) { 
		
		List<String> list =
			 Arrays.asList("홍길동","신용권","뷰티풀","김거거","누누누");
		long start = 0, end = 0; //속도 비교를 위해 
		
		//순차처리 start = System.currentTimeMillis(); 
		Stream<String> stream = list.stream(); 
		stream.forEach(ParallelExample::print); 
		end = System.currentTimeMillis(); 
		System.out.println(end-start); //56~ 61 정도 나옴.
		
		//병렬처리 start = System.currentTimeMillis(); 
		Stream<String> parallelStream = list.parallelStream();
		parallelStream.forEach(ParallelExample::print); 
		end = System.currentTimeMillis(); 
		System.out.println(end-start); //5~6 정도 나옴 
	} 
	
	public static void print(String str) { 
		System.out.println(str+ " : "+Thread.currentThread().getName());
	} 
}
```

#### | BEHAVIORAL PARAMETERIZATION 로 메서드 코드 전달
* 코드 일부를  API 로 전달하는 기능
Pasted image 20220825130059.png
 => https://dev-kani.tistory.com/36 

#### | 병렬성과 공유 가변 데이터 
* 동시에 안전하게 실행하기 위해 `공유된 가변 데이터` ( `SHARED MULTIPLE DATA`) 에 접근하지 않아야 함. 
	* 순수 함수
	* 부작용 없는 함수
	* 상태 없는 함수 

* 함수형 프로그래밍 
	* 공유되지 않은 가변 데이터를 다른 메서드에 전달 
	* `프로그램이 실행되는 동안 ㅋㅁ포넌트 간의 상화 작용이 일어나지 않는다.`
* 명령형 프로그래밍 
	* 일련의 가변 상태로 프로그램 정의
	* 공유되지않은 가변 데이터 요구사항이란 인수를 결과로 변환하는 기능과 관련 
	 -> 함수가 정해진 기능만 수행하여 다른 부작용을 일으키지 않음 


### | 자바 함수 
* 의미 : 수학적인 함수처럼 사용되며 부작용을 일으키지 않는 함수 
* 멀티코어에서 `stream` 병렬 프로그래밍 

#### > 메서드와 람다를 일급 시민으로
* 일급 시민 이란? 
	* 변수 `variable` 나 데이터에 할당 가능
	* 객체의 인자로 `parameter` 넘길수 있어야 함
	* 객체의 리턴값(`return value`) 으로 리턴 가능해야 함.

* 이급시민?
	* 메서드, 클래스 

> 메서드 참조 (method reference) = ::

 ⭐ 메서드가 더이상 이급 시민이 아닌 일급시민이 된다. 
  -  람다 문법 -> `함수를 일급값으로 넘겨주는 프로그램을 구현` 
```java
File[] hiddenFile = new File(".").listFiles(File::isHidden);
```
> 익명함수 
* predicate
	* argument 를 받아 boolean 값 반환 함수형 인터페이스
	* functional method : test()
	* boolean 을 Boolean 변환 과정이 없어 더 효율적 


#### > 메서드 전달에서 람다로
- 한번만 사용할 메서드는 따로 정의 구현 필요가 없으니 직접 넘겨줌. 
- 익명 람다 보다는 코드가 수행하는 일을 잘 설명하는 메서드를 정의하고 참조하여 활용 


### | Stream 
* stream API 는 collection API 와 다른 방식으로 데이터를 처리
* 컬렉션은 반복 과정 처이 (외부 반복)
* 스트림은 라이버리 내부에서 모든 데이터 처리 (내부 반복)

* ⭐ [컬렉션을 처리하면서 발생하는 모호하고 반복적인 커드 + 멀티코어 활용 어려움] 해결
* 컬렉션 (sql 질의 언어 동작과 비슷함) 반복 패턴 많음. 
* filtering, extractinv, grouping 병렬화 
* forking : 앞 뒷 부분을 다르게 처리하고 마지막 결과를 합쳐 정리함. 
	* 순차 : `.stream()`
	* 병렬처리 : `.parallelStream()`

> default method 
- 모듈을 정의하는 문법을 제공하여 패키지 모음을 포함하는 모듈 정의 
- 인터페이스를 쉽게 바꿀수 있는 디폴트 메서드 제공 

- Java8 이전에서도 스트림을 사용할수 있도록 `default` 메서드를 이용해 기존의 코드를 건들이지 않고 원래의 인터페이스 설계를 자유롭게 확장 
```java
default void sort(Comparator<? super E> c) {
	Collections.sort(this, c);
}
```

### | 함수형 프로그래밍 유용한 아이디어
* `Optional<T>` - NullPointer 예외 방지 
* (구조적) 패턴 매칭 기법 
	* switch 확장하여 데이터 형식 분류와 분석을 한번에 수행 


### | 정리 
* 자바8는 기존의 프로그램에 새로운 개념과 기능을 제공해 더 효과적이고 간결하게 구현 가능
* 멀티코어 프로세서 온전히 활용 
* `Stream` 개념 일부는 컬렉션에서 가져온 것으로 스트림의 인수를 병렬로 처리할수 있어 가독성 좋은 코드 구현 가능 
* `default method` 통해 기존 인터페스 구현 클래스를 변경하지 않고 인터페이스 수정 가능
* `Optional` null 처리 방법과 패턴 매칭 활용 기법 추가 

