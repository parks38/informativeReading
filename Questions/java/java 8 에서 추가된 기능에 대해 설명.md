----

#### | 답변 
자바 8은 기존의 프로그램에 새로운 개념과 기능을 제공해 더 효과적이고 간결하게 구현 가능하도록 도와준다. 
자바 8 에는 `람다, Stream API, 함수형 프로그래밍, Optional, 그리고 인터페이스의 디폴트 메서드` 기능들이 추가 되었다. 



#### | 정리

-   자바8는 기존의 프로그램에 새로운 개념과 기능을 제공해 더 효과적이고 간결하게 구현 가능
-   멀티코어 프로세서 온전히 활용
-   `Stream` 개념 일부는 컬렉션에서 가져온 것으로 스트림의 인수를 병렬로 처리할수 있어 가독성 좋은 코드 구현 가능
-   `default method` 통해 기존 인터페스 구현 클래스를 변경하지 않고 인터페이스 수정 가능
-   `Optional` null 처리 방법과 패턴 매칭 활용 기법 추가

 #java8 에 추가된 새로운 것들 : 
	-   Lambda 표현식
	-   Functional 인터페이스
	-   Steam
	-   Optional
	-   인터페이스의 Default method
	-   날짜 관련 클래스들 추가
	-   병렬 배열 정렬
	-   StringJoiner

#### | java 기능들 진화 
자바에서 가장 큰 변화는 java8 에서 시작함
-   lambda EX. SORT
-   멀티코어 CPU 대중화 (하드웨어 변화)
    -   이전 :
        -   java 1.0: 스레드, 락, 메모리 모델 지원
        -   java5 : thread pool / concurrent collection
        -   java 7 : 병렬 실행 (fork/ join framework)
    -   java8:
        -   stream API
            -   데이터베이스 질의 언어에서 고수준 언어로 동작 구현 에서 최적의 저수준 실행 방법을 선태하는 방법으로 동작
            -   멀티코어 CPU (에러 발생) 보다 비용이 비싼 `SYNCHORNIZED` 사용 x
        -   메서드에 코드를 전달하는 기법
            -   메서드 참조와 람다
            -   간결한 방법으로 동작 (BEHAVIOR PARAMETERIZATION) 구현
        -   인터페이스의 디폴트 메서드
        -   `FUNCATIONAL PROGRAMMING`
    -   java 9 : reactive programming (병렬 실행 기법) RxJava

> Stream Process

-   한번에 한개씩 만들어지는 연속적인 데이터 항목들의 모음
-   스트림 동작 방법
    -   여러 행의 스트림을 입력 받아 여러 행의 스트림을 출력
    -   자동차 생산 공장과 같이 한개씩 처리하지만 작업장에서는 동시에 작업 처리
    -   ⭐ (데이터 베이스 질의 처럼) 고수준으로 추상화해서 스트림으로 처리
    -   스트림 파이프 라인 이용 여러 CPU 코어에 쉽게 할당
    -   (공짜) 병렬성

-   stream api -> paralellStream() 이용해 stream 객체 생성해서 병렬 처리
    -   대량의 데이터 병렬 처리 코드 작성

> BEHAVIORAL PARAMETERIZATION 로 메서드 코드 전달

-   코드 일부를 API 로 전달하는 기능 
-   아직은 어떻게 실행할 것인지 결정하지 않은 코드 블럭을 의미한다.
-   즉, 코드 블럭의 실행은 나중으로 미뤄진다.
-   결과적으로 코드 블럭에 따라 메서드의 동작이 파라미터화된다.

> 병렬성과 공유 가변 데이터

-   동시에 안전하게 실행하기 위해 `공유된 가변 데이터` ( `SHARED MULTIPLE DATA`) 에 접근하지 않아야 함.
    -   순수 함수
    -   부작용 없는 함수
    -   상태 없는 함수
    
-   함수형 프로그래밍
    -   공유되지 않은 가변 데이터를 다른 메서드에 전달
    -   `프로그램이 실행되는 동안 ㅋㅁ포넌트 간의 상화 작용이 일어나지 않는다.`

- 명령형 프로그래밍    
    -   일련의 가변 상태로 프로그램 정의
    -   공유되지않은 가변 데이터 요구사항이란 인수를 결과로 변환하는 기능과 관련 -> 함수가 정해진 기능만 수행하여 다른 부작용을 일으키지 않음

> 자바 함수

-   의미 : 수학적인 함수처럼 사용되며 부작용을 일으키지 않는 함수
-   멀티코어에서 `stream` 병렬 프로그래밍
* 메서드와 람다를 일급 시민으로
	* 일급 시민 이란?
	    -   변수 `variable` 나 데이터에 할당 가능
	    -   객체의 인자로 `parameter` 넘길수 있어야 함
	    -   객체의 리턴값(`return value`) 으로 리턴 가능해야 함.

> 함수형 프로그래밍 유용한 아이디어

-   `Optional<T>` - NullPointer 예외 방지
-   (구조적) 패턴 매칭 기법
    -   switch 확장하여 데이터 형식 분류와 분석을 한번에 수행

> default method

interface 안에 구현된 메소드를 추가해야 할 때 `default` 키워드를 붙여준다.

```
default method를 만든이유? 

'하위 호환성' 때문이다. 많은 사람이 사용하고 있는 인터페이스에 새로운 메소드를 추가해야 할 때 기존 방식대로 추가하면 이미 사용하고 있는 사람들은 전부 오류가 발생하고 수정해야하는 일이 발생한다. 이럴 때 사용하는 것이 default 메소드다.+ Lambda 표현식을 사용하기 위해서 만들어졌다.(lambda 표현식을 사용하려면 메소드가 1개여야 하는데 그 이외의 메소드는 default method로 구성되게 만들면 되기 때문이다)
```

> #StringJoiner

: 순차적으로 나열되는 문자열 사이에 특정 문자열을 넣어줘야 할 때 사용한다. (prefix, suffix 설정도 가능)

**문자열 처리 class**
	-   String
	-   StringBuilder
	-   StringBuffer
	-   Formatter
	-   **StringJoiner**

#### | ETC
* [[Stream의 NPE]]
---
[footnote]
https://medium.com/@inhyuck/java-8%EC%97%90-%EC%B6%94%EA%B0%80%EB%90%9C-%EA%B2%83%EB%93%A4-8c66023cbbae
