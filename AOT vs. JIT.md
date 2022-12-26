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


> 📌 C1 Compiler 

빠르고 가볍게 최적화된 바이트 코드 컴파일러 실행 
ex. some value numbering, inlining, class analysis 
[사용]
* GFC oriented SSA "high" IR  => CGG 지향적인 SSA와 고수준의 중간 표현
* machine oriented "low" IR => 기계 지향적인 저수준 IR
* 선형 스캔 레지스터 할당 및 템플렛 스타일 코드 생성기 

> 📌 C2 Compiler 

노드의 바다 SSA 와 IR 을 사용하는 고도로 최적화된 바이트 코드 컴파일러이며 IR 은 동일환 종류의 기계별 IR로 낮아진다. 
- 그래프 색칠 레지스터 할당자
- 색상은 로컬, 글러벌, 인수 레지스터와 스택을 포함한 기계의 상태이다. 
* global value numbering, conditional constant type propagation, constant folding, global code motion, algebraic identities, method inlining (aggressive, optimistic, and/or multi-morphic), intrinsic replacement, loop transformations (unswitching, unrolling), array range check elimination, and others.

> compiler 실행 시간

* compiler strategy : JIT / AOT 

자바 소스 코드는 처음 바이트 코드로 변환 이후 네이티브 코드로 변환된다. 
무거운 최적화는 JIT 컴파일 동안에 실행된다. 

#### > JIT 컴파일러 (동적 컴파일러)를 어떻게 최적화 하는가? 
오류



[참고]
c1,c2 이용한 최적화
https://kotlinworld.com/307
https://www.cogentinfo.com/blog/jvm-c1-c2-compiler-thread-high-cpu-consumption

