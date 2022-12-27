----
  
> JIT 컴파일러 위치? 

✔️ Execution Engine(실행 엔진)
: 런타임 데이터 영역에 ==할당된 바이트코드를 실행 / 바이트코드를 읽고 조각 별로 실행==

![[Pasted image 20221226113806.png]]

[참고]
.java 확장자로 소스 코드를 작성하면 ==JVM 이 이해 할 수 있는 코드로 컴파일(compile)==을 해주고 
컴파일이 완료 되면 JVM 이 해석 가능한 `byte code`로 작성된 `.class` 확장자 클래스가 생성된다. 
클래스 파일은 어플리케이션 동작할때 ==메모리에 적재되어 JVM 실행 엔진에 의해 수행==된다. 

-   Interpreter : 바이트 코드를 명령어 단위로 실행하여 한줄씩 수행. 단점 : 느리다.
-   JIT(Just in time) Compiler : Interpreter 보완; 네이티브 코드로 컴파일된 코드 캐시 보관하여 실행
-   Garbage Collector : 동적으로 할당된 메모리중 사용하지 않는 메모리 반환 - 자동 메모리 관리

>  JIT 컴파일러란 무엇이고, 이것은 왜 필요할까요?

✔️ JIT 컴파일러 
: 처음 자바는 Interpreter방식으로 모든 코드를 컴파일 하였고 그때문에 속도가 느리다는 평가를 받았다.
JIT(Just in Time) 컴파일러는 ==하드웨어의 발전으로 native 코드로 직접 실행하는 방식==으로` CPU + OS (기계)`가 직접 읽고 수행하는 방식이다.

-   네이티브 캐쉬에 한번 컴파일된 코드를 보관하기 때문에 같은 코드 반복시, 
     매번 기계어 코드를 새로 생성하는 방식이 아닌 이전에 만든 ==기계어를 재사용함으로써 속도를 빠르==게 했다.
-   운영체제에 맞게 byte 실행 코드를 한번에 변환하여 실행하여 이전 Java Interpreter방식보다 성능이 10-20배 증가했다고 한다.

> ⭐ Interpreter or JIT Compiler

무조건 JIT Comipler방식이 옳은건 아니다...
: 한번만 실행되는 코드라면 Interpreter!
메소드의 반복 수행이 일정 정도를 넘는다면 JIT Compiler!

	🤵🏻 Interpreter 이 그냥 커피라면 JIT는 TOP야

#### | 키워드

-   `컴파일러` : `.java` 확장자로 저장된 소스 코드를 ==JVM이 이해할 수 있는 코드==로 컴파일 한다. 
-   `인터프리터` : "Write once Run Anywhere"
	- 이식성이 높은 언어의 이유 
	- 플랫폼에 맞게 인터프리터가 바이트 코드를 실행한다. (Window, Mac, Linux)

	![[Pasted image 20221226133225.png]]
JVM 인터프리터는 ==런타임에 바이트 코드를 하나씩== 읽어드리는 방식으로 C, C++와 같이 미리 컴파일하여 기계어로 변경되는 언어에 비해 속도가 느리다. 
- `JIT Compiler`
	- ==자주 실행되는 바이트 코드 영역을 런타임 중에 기계어로 컴파일==해서 사용한다.  

#### | 꼬리질문

>  AOT 컴파일은 무엇일까요?

`Ahead Of Time` 컴파일러 
목표 시스템의 기계어와 무관하게 중간 언어 형태로 배포된 후 인터프리터나 JIT 컴파일러 등 기계어 변역을 통해
실행되는 중간 언어를 ==미리 목표 시스템의 기계어로 변역==하는 방식
JIT 가 런타임에 컴파일하기에 발생하는 성능 이슈가 생기지 않고 ==네이티브 성능을 낼 수 ==있다.

AOT 와 JIT 는 정반대의 개념이다! 

[[AOT vs. JIT]]

| 비교        | AOT                                                          | JIT                                                              |
| ----------- | ------------------------------------------------------------ | ---------------------------------------------------------------- |
| 컴파일 방식 | GraalVM 지원 빌드타임에 바이트 코드를 기계어로 정적 컴파일링 | Java HotspotVM 기본설정으로 런타임에 바이트 코드를 기계어로 변환 (동적 컴파일링)|
| 속도        | 빠르다                                                       | 첫 러닝타임에 모두 컴파일해야 하기에 느리다                      |
| 컴파일 타임 | Build Time                                                   | RunTime before display                                           |
| 사이즈      | JIT 의 half bundle size (optimized)                          | bundler size higher                                              |
|             | production                                                   | development                                                      |
| 에러 체크   | 빌드타임에 오류 확인이 가능하다                              | 애플리케이션이 화면에 띄울때 확인 가능하다.                                                                 |
 


>  C1 컴파일러와 C2 컴파일러는 무엇일까요?

Java Hotspot VM 안의 메이저 컴플라이어로 c1 과 c2 가 있다.
JVM 실행 될 때 JIT 컴파일러를 통해 핫코드를 간소화 시키는데 이 안에 C1 & C2 스레드가 있고 코드 효율성 최적화를 시킨다. 

⭐ 이 2개는 역할이 어떻게 다른가요?
C1 은 클라이언트 JIT 로 level 1-3 compilation tier(수행 빈도와 복잡도 레벨)을 담당하고
빠르고 가볍게 최적화된 바이트 코드 컴파일러를 실행한다. 

C2는 서버 JIT가 사용하는 스레드로 고도로 최적화된 바이트 코드 컴파일러로 level4 를 수행하며 
C2는 더 오래걸리지만 가 더 최적화된 코드를 생성하며 필요 시에만 동작한다. 

> 컴파일 과정에서 컴파일러가 최적화해주는 것들은 무엇무엇이 있을까요?



#### | 검색 키워드

-   `JVM 컴파일러 최적화`
-   `hotspot compiler optimization`

#### | 내용

-   `+연산을 StringBuilder로 변환해주기`
-   `반복문 펼쳐주기`
-   `탈출분석`
-   `인라이닝`
-   `Etc...`

💡 컴파일러가 최적화해주는 것들이 정말 많습니다. 잘 찾아보세요.


![[Pasted image 20221226131057.png]]

[참고자료]
https://junhyunny.github.io/information/java/jvm-execution-engine/
JIT vs. AOT 
https://shirohoo.github.io/backend/java/2022-07-16-aot-vs-jit-in-java/
https://www.cesarsotovalero.net/blog/aot-vs-jit-compilation-in-java.html
http://daplus.net/angular-angular%EC%9D%98-jit-just-in-time-vs-aot-ahead-of-time-%EC%BB%B4%ED%8C%8C%EC%9D%BC/


https://heewon26.tistory.com/207
