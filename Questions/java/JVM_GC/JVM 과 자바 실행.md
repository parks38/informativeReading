#### 📌 목표

자바 소스 파일(.java)을 JVM으로 실행하는 과정 이해하기.

#### 📌 **학습할 것**

-   JVM이란 무엇인가
-   컴파일 하는 방법
-   실행하는 방법
-   바이트코드란 무엇인가
-   JIT 컴파일러란 무엇이며 어떻게 동작하는지
-   JVM 구성 요소
-   JDK와 JRE의 차이

#### **✔️  메소드, 클래스**

**클래스**

**:** 자바의 가장 작은 단위 메소드의 소속

⇒ 상태 (state) 와 행동 (behavior)

-   상태 : 클래스의 특성을 결정짓는 것 ex. variable (변수)
-   행동 : 메소드

**메소드**

: 어떤 값을 주고 결과를 넣어주는 것

####   **✔️ JVM (Java Virtual Machine)**

![](https://blog.kakaocdn.net/dn/m4adt/btqYus5Wvtx/khprE0WQis68LS9biv6Wek/img.png)

: 자바의 가상 머신 (스택 기반) / 자바 프로그램 실행 환경 만들어주는 소프트웨어

-   .java 파일로 생성돼있는 소스코드를 Java Compiler를 통해 .class파일로 변환한 것을, 해당 OS와의 중재자 역할로 OS가 이해할수 있도록 .class ByteCode를 해석해주는 역할.
-   자바 장점 : Window/ Linux OS에 종속되지 않고 .java 파일만 생성해주면 어떤 운영체제에든 JVM을 통해 실행 가능.
-   한정된 메모리 효율에서 최대의 성능을 내기 위한 메모리 관리를 위해 자바 가상머신이 중요하다.

#### **✔️ JVM 구성 요소**

**I. Class Loader**

: 런타임 시점에 클래스를 로딩하게 해주며 클래스의 인스턴스를 생성하면 클래스 로드를 통해 메모리에 로드 - 클래스 파일을 로드하는데 사용하는 하위 시스템

Loading

-   Bootstrap : JVM 실행 위한 핵심 클래스 로딩
-   Extension : 자바의 확장 클래스들을 로딩
-   Application : $CLASSPATH 안의 .class확장자 탐색하여 로딩

Linking

-   Verify : class파일 유효성 확인
-   Prepare : 메모리 준비 과정
-   Resolve : symbolic memory reference를 실제 레퍼런스로 교체

Initialization(초기화)

-   static 변수값 메모리 할당하고 static 블럭 실행/ 초기값 채우기

**II. Execution Engine**

: 런타임 데이터 영역에 할당된 바이트코드를 실행 / 바이트코드를 읽고 조각 별로 실행

-   Interpreter : 바이트 코드를 명령어 단위로 실행하여 한줄씩 수행. 단점 : 느리다.
-   JIT(Just in time) Compiler : Interpreter 보완; 네이티브 코드로 컴파일된 코드 캐시 보관하여 실행
-   Garbage Collector : 동적으로 할당된 메모리중 사용하지 않는 메모리 반환 - 자동 메모리 관리

**III. Runtime Data Area**

**Method Area** : 메타데이터, 상수런타임풀, 메서드에 대한 코드와 같은 클래스 구조 저장. (다른 스레드에서도 활용 가능한) 공유자원.

**Heap** : 모든 개체, 관련 인스턴스 변수 및 배열은 힙에 저장 (메모리는 여러 스레드에 걸쳐 공유)

**Java Thread (Stack)** : 로컬 변수를 저장하는 부분적인 결과

-   스레드에는 자체 JVM 스택 존재, 스레드가 생성될때 동시에 생성
-   메서드 호출시, 새 프레임 생성, 메서드 호출 프로세스가 완료되면 삭제
-   스택은 공유 X/ 스레드 세이프하다.
-   내부 : local variable array, operand stack, frame data 영역 존재
    

**Program Counter Register** : 현재 실행중인 Java가상 시스템 명령의 주소 저장

-   각 스레드에 별도의 PC 레지스터 존재

**Native Internal Thread (Stack) :** 네이티브 라이버리에 따라 네이티브 코드 명령 보관 / 자바가 아닌 다른언어로 쓰여있음.

**IV. Native Method Interface** : 프로그래밍 프레임워크

-   JVM에서 실행 중인 Java코드가 라이버리 및 네이티브 어플리케이션으로 호출 가능

**V. Native Method Libraries** : 실행 엔진에 필요한 라이버리 (C, C++) 의 모음

#### **✔️ 컴파일 하는 방법**

javac fileName.java

⭐  **javac.exe :** 자바 소스코드를 컴파일 할때 사용하는 프로그램이며

⭐  **java.exe:** 컴파일된 바이트코드를 실행할 때 사용.

소스코드(.java) —> Compiler -(bytecode: .class 변환) -> Disk -(bytecode)-> `JVM` -(기계어) -> OS

![](https://blog.kakaocdn.net/dn/cxl5gb/btqYoY5PSbH/XzlFpV9DYU6ELZCPiJxSdK/img.png)

#### **✔️ 자바 프로그램 실행 과정**

1.  프로그램 실행
2.  JVM이 OS로 부터 메모리를 할당 받고, 용도에 따라 나누어 관리
3.  자바 컴파일러(javac)가 소스 코드(.java)를 읽어 바이트 코드로 변환(.class)
4.  JVM의 Class Loader가 변환된 파일(*.class)들을 로드
5.  Execution engine(실행 엔진) 은 로딩된 class 파일을 해석
6.  해석된 파일들은 Runtime Data Area(할당 메모리)에 배치되고 실질적 수행이 이루어짐

#### **✔️ 바이트코드(.class) 란 무엇인가**

-   운영체제에 맞는 완벽한 기계어가 아닌 중간기계어
-   이 바이트코드를 실행하기 위해 JVM 필요
-   자바 컴파일로 변환되는 코드의 형태

**⭐ Java Compiler**

: .java파일로 생성돼있는 소스코드를 이진수로 구성되어있는 .class 라는 Java Byte Code로 변환 시켜줌.

기계어 : 컴퓨터가 이해할 수있는 0과 1로 이루어진 바이너리 코드

-   바이너리코드 ≠ 기계어

#### **✔️ JIT 컴파일러란 무엇이며 어떻게 동작 하는가?**

: 처음 자바는 Interpreter방식으로 모든 코드를 컴파일 하였고 그때문에 속도가 느리다는 평가를 받았다.

JIT(Just in Time) 컴파일러는 하드웨어의 발전으로 native 코드로 직접 실행하는 방식으로 CPU + OS (기계)가 직접 읽고 수행하는 방식이다.

-   네이티브 캐쉬에 한번 컴파일된 코드를 보관하기 때문에 같은 코드 반복시, 매번 기계어 코드를 새로 생성하는 방식이 아닌 이전에 만든 기계어를 재사용함으로써 속도를 빠르게 했다.
-   운영체제에 맞게 byte 실행 코드를 한번에 변환하여 실행하여 이전 Java Interpreter방식보다 성능이 10-20배 증가했다고 한다.

**⭐ Interpreter or JIT Compiler**

무조건 JIT Comipler방식이 옳은건 아니다...

: 한번만 실행되는 코드라면 Interpreter!

메소드의 반복 수행이 일정 정도를 넘는다면 JIT Compiler!

🤵🏻 Interpreter 이 그냥 커피라면 JIT는 TOP야

#### **✔️ JDK와 JRE의 차이**

![](https://blog.kakaocdn.net/dn/Aq3i8/btqYvesPyFq/HoQKmRLxfkb5f8iTYpgngk/img.png)

**JDK (Java Development Kit)**

: 자바 프로그래밍 도구

-   개발을 위한 도구 (javac, java)
-   JDK 다운시, JRE도 함께 설치 ; JDK = JRE + @

오픈소스 연동에 더 풍부한/최적화된 기능을 빠르게 구현하는 강점

- 기본 기능 제공 클래스 /자료구조 / 네트워크 / 입출력 / 예외처리 / Algorithm Library 제공

**JRE (Java Runtime Environment)**

: 컴파일된 자바 프로그램 실행하는 자바 실행 환경 - 해당 OS에 맞는 환경 설치

-   JVM이 자바 프로그램 동작시킬때 필요한 라이버리 / 실행 환경 구현

**▶️ 참고 사진**

![](https://blog.kakaocdn.net/dn/bbm3ZD/btqYAtCHj48/ktvcHgKWxH3N0qTdP4BcJk/img.png)

**📍presentation 자료 :** 

[docs.google.com/presentation/d/1zvh5k-1JonEi_s6Z8s_vqF8vs59dAwojghnuk6evqaA/edit](https://docs.google.com/presentation/d/1zvh5k-1JonEi_s6Z8s_vqF8vs59dAwojghnuk6evqaA/edit)