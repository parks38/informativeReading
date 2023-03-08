1주차
Identitiy vs. Equality 
	- ▶ Lombok - @EqualsAndHashCode(callSuper=false)

- 웹 서버 (Apache, Nginx) 는 OSI 7계층 중 어디서 동작하는가? 
	- ▶ 웹 서버 소프트웨어(Apache, Nginx)의 서버 간 라우팅 기능은 OSI 7계층 중 어디 서 작동하는지 설명해보세요. 
	- ▶ Nginx 란?▶ Load Balancing 이란?
Java 8 에 추가된 기능들
	N+1 문제는 무엇이고 이것이 발생하는 이유와 해결 방법은?
	✅ N + 1 문제란 무엇인가?
	✅ N + 1 문제는 어떤 이유로 발생하는가?
	✅ 해결 방법은 무엇인가?
	▶ Join Fetch 문제점?
	▶ FetchType 으로 해결할 수 없는가?
	
JPA 를 사용한다면 그 이유는?
	Ø 장점/단점
	Ø Mybatis 장단점

callByReference vs. callByValue
▶ Java 호출 방식?
▶ C/C++ 와 Java 의 호출 방식 비교

interface 와 추상 클래스 
✅ 추상 클래스와 인터페이스의 공통점 
✅ 추상 클래스와 인터페이스의 차이점 
▶다형성 - 추상 클래스 연관

TCP 3/4 way handshake 
▶TCP 4-way handshake TIME-WAIT

spring bean? 생성과 의존성을 주입 과정 
	Ø 스프링 빈의 생성 과정 
	Ø 의존성 주입 과정 
▶ IoC
▶ 생성자 주입과, 필드 주입 혹은 Setter 주입 간에 의존성 주입 과정에 차이가 나는 이 유 
▶ 스프링 빈 라이프 사이클을 압축시키기 위해 생성자 주입을 통해 빈 생성과 초기화 를 동시에 진행하면 되지 않을까? 
▶ 자바 빈과의 차이 

bean/component annotation 과 그 둘의 차이점 
JPA 영속성 컨텍스트의 이점 (5가지)
MSA 란 무엇이고 장단점은? 
▶ Monolothic vs. MSA

객체지향에 대해서 설명해 주세요 
▶  [객체 지향 특성] 
▶ 절차지향 vs. 객체 지향 이란? 

Spring DI/IoC ? 
✅ DI 란? 
✅ IoC 란? (inversion of control : 제어의 역전) 
▶ 생성자 주입을 사용해야 하는 이유? 
▶ Spring Container 란? 
⭐ BeanFactory vs. ApplicationContext? 


2주차
Index
Discussion Topic
JIT 컴파일러
	- 실행 엔진 
	- interpretor
JIT 과 AOT 컴파일
Spring과 Spring Boot의 차이
DI 종류와 차이점
	- 관련 어노테이션 
JPA의 장점
RESTful
GET과 POST 메소드의 차이점
세션과 쿠키의 차이
	- 쿠키와 세션을 사용하는 이유
	- 캐시와의 차이점
DB 트랜잭션과 그 동작과정
	- ACID 
DB 락
	- 비관적 락/ 낙관적 락 
트랜잭션 격리 수준 (4) 
REST API와 그 CRUD
	- SOAP 
	- GraphQL
Spring AOP
	- 관련 애노테이션 
JPA Save 동작 방법
	- persist()/merge()
	- Detach 상태 
Mvc 
	- 처리 순서
	- Spring mvc 
	- dispatcherServlet 

3주차
CheckedException vs unCheckedException. 스프링 트랜잭션 추상화에서 rollback
대상은 무엇일까?
Spring MVC가 contrller 1개로 여러개 요청 처리 하는 방법
쿠키는 클라이언트 어느 부분에 저장되는가?
	- 웹 브라우저 별 쿠키 저장 위치
	- ☑ 세션 저장 위치
IOC Container 역할
	- 제어의 역전 IoC(Inversion of Control)란? 
	- IoC 컨테이너 종류: BeanFactory와 ApplicationContext
POJO란? spring framwork에서 POJO는 무엇인가?
	- POJO의 장점
스레드와 프로세스의 차이
	- Program/process/thread
	- 단일 프로세스/멀티 프로그램이/멀티 태스킹/ 멀티 스레딩/ 멀티 프로세싱
	- WAS와 멀티스레드
Thread Pool
	- 단순하게 Thread를 사용하는 전략
	- 스레드 풀의 구성 요소/ 작업 처리 순서 /특징
	- 스레드 풀 설정이 필요한 이유
	- Java 의 thread pool vs. tomcat 의 Thread pool 
	- Spring Boot와 Thread Pool
	
JPA의 동시성 제어 메커니즘 : 비관적 락과 낙관적 락
	✅ JPA의 동시성 제어 메커니즘과 DB 트랜잭션 격리 수준의 차이

4주차 
Servlet filter 와 spring interceptor 의 차이? 
	- Dispatch servlet 까지 전달 
	- 인터셉터(Interceptor)와 AOP?
	- 용도 
Filter 와 Interceptor 예외 처리 방식
	-  errorController
	- @ExceptionHandler /  @ControllerAdvice
JVM 메모리 구조 
	- Method/Class
	- JVM Stack
	- Heap 
자바 코드의 실행 과정 (JVM) 
	- 심볼릭 레퍼런스/ GC
	- 기본 자료형/ 네트워크 바이트 오더 
Reflection 
	- 장단점
	- 사용되는 곳 
	- JPA의 Entity에 기본 생성자가 필요한 이유 
string과 stringBuffer? stringBuilder 차이? 
배열 안 중복 제거하는 방법? 
	- 종류
	- Java8 update
SOLID (5대 원칙) 에 대해서 설명 


https://sigridjin.medium.com/java-stream-api%EB%8A%94-%EC%99%9C-for-loop%EB%B3%B4%EB%8B%A4-%EB%8A%90%EB%A6%B4%EA%B9%8C-50dec4b9974b
