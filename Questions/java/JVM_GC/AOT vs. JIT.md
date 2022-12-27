----
#### 🌈 JIT

==실행을 위해 적시==에 TypeScript를 컴파일합니다.
-   브라우저에서 컴파일됩니다.
-   각 파일은 별도로 컴파일됩니다.
-   코드를 변경 한 후 브라우저 페이지를 다시로드하기 전에 빌드 할 필요가 없습니다.
-   지역 개발에 적합합니다.

#### 🌈 AOT

==빌드 단계==에서 TypeScript를 컴파일합니다.
-   시스템 자체에서 명령 줄을 통해 컴파일됩니다 (빠른 속도).
-   모든 코드는 함께 컴파일되어 스크립트에 HTML / CSS를 삽입합니다.
-   컴파일러를 배포 할 필요가 없습니다 (Angular 크기의 절반).
-   더 안전한 원본 소스는 공개되지 않습니다.
-   프로덕션 빌드에 적합합니다.

![[Pasted image 20221226142941.png]]

#### > Compile 
컴파일은 Java 와 Python 같이 고수준 언어의 소스코드를 기계언어로 변경하는 것을 의미한다. 
컴파일러의 목적은 일관된 실행 파일을 생성하는 것이며 소스코드가 빠르고 안전하게 실행되는 것이다. 

컴파일러는 다양한 최적화를 기계어 생성하며 수행한다. 
ex. constant inlining, loop unrolling, partial evaltion 

> c1과 c2 compiler? 

Java Hotspot VM 안의 메이저 컴플라이어는 c1 complier 과 c2 complier 이다. 
애플리케이션이 실행 될 때 JVM 은 JIT 컴파일러를 통해 핫코드를 간소화 시킨다. 
코드가 자주 효율적이지 않게 작성되어 있기 때문에 
JIT 컴파일러는 C1 & C2 컴파일러 스레드를 사용해서 코드 효율성을 최적화 시킨다. 
C1/C2 컴파일러가 사용하는 메모리 공간은 "Code Cache" 라고 불린다. 

JIT 컴파일러가 애플리케이션 실행 시 `클라이언트 JIT`는 코드 컴파일을 시작한다. 
`server JIT`는 특정 타임에만 실행되는데 서버 JIT 가 시간은 더 걸리지만 클라이언트 JIT 보다 성능은 좋다. 
JDK 는 클라이언트와 서버 JIT 컴파일러 를 사용해서 코드를 (streamline)간소화 한다. 
 1) 클라이언트 JIT 컴파일러 코드 컴파일
 2) 충분한 데이터 확보 되면 ... 서버 JIT 컴파일러 작동 
-- JVM 의 계층화된 컴파일링 : ==C1 -> client // C2 -> server ==

Q. JVM 은 OAT 를 사용하는가? 
AOT 는 실행하는 동안 원시 코드를 ==동적으로 생성하며 공유 데이터 캐시==에서 생성된 AOT 코드를 캐시하고
메소드를 실행하는 후속 JVM 은 JIT  컴파일 원시 코드에서 발생하는 성능 저하 없이 
공유 데이터 캐시에서 AOT 코드를 로드하고 사용 할 수 있다. 

> 📌 C1 Compiler 

빠르고 가볍게 최적화된 바이트 코드 컴파일러 실행 
ex. some value numbering, inlining, class analysis 
✅ JIT 컴파일러 클라이언트가 사용하는 스레드 

[사용]
* GFC oriented SSA "high" IR  => CGG 지향적인 SSA와 고수준의 중간 표현
* machine oriented "low" IR => 기계 지향적인 저수준 IR
* 선형 스캔 레지스터 할당 및 템플렛 스타일 코드 생성기 

> 📌 C2 Compiler 

✅ JIT 컴파일러 서버가 사용하는 스레드 

노드의 바다 SSA 와 IR 을 사용하는 고도로 최적화된 바이트 코드 컴파일러이며 IR 은 동일환 종류의 기계별 IR로 낮아진다. 
- 그래프 색칠 레지스터 할당자
- 색상은 로컬, 글러벌, 인수 레지스터와 스택을 포함한 기계의 상태이다. 
* global value numbering, conditional constant type propagation, constant folding, global code motion, algebraic identities, method inlining (aggressive, optimistic, and/or multi-morphic), intrinsic replacement, loop transformations (unswitching, unrolling), array range check elimination, and others.

> compiler 실행 시간

* compiler strategy : JIT / AOT 

자바 소스 코드는 처음 바이트 코드로 변환 이후 네이티브 코드로 변환된다. 
무거운 최적화는 JIT 컴파일 동안에 실행된다. 

#### > JIT 컴파일러 (동적 컴파일러)를 어떻게 최적화 하는가? 

![[Pasted image 20221227172610.png]]

✅ compilation tier
- c1 : level 1-3
- c2 : level 4 

	0: 인터프리트된 코드
	1: 단순 C1 컴파일된 코드
	2: 제한된 C1 컴파일된 코드
	3: 전체 C1 컴파일된 코드
	 🔺 전형적인 코드는 3레벨인 전체 C1 컴파일된 코드로 처음 컴파일 
	4: C2 컴파일된 코드

✅ Profiling 
jvm 은 해당 코드블록이 어떤 컴파일러를 사용할지 [1] 코드 사용 빈도 [2] complexity/time consuming 토대로 결정한다. 
- 기본적으로 C1 컴파일러 사용 but! level 4 도달하면 C2 사용 
- C2 가 더 최적화된 코드 생성
- JVM 에서 해당 코드를 `code cache` 에 저장할지 결정 
- 모든 코드를 level 4로 컴파일 하지 않으며 comple 한 작업을 담당 코드가 아니라면 자주 쓰여도 `code cache`에 넣지 않음 
	=> 이점이 별로 없다. 
	 - code cache 는 크기가 제한되어 있다. 


[참고]
c1,c2 이용한 최적화
https://kotlinworld.com/307
https://www.cogentinfo.com/blog/jvm-c1-c2-compiler-thread-high-cpu-consumption
https://shirohoo.github.io/backend/java/2022-07-16-aot-vs-jit-in-java/
