## Aspect Oriented Programming
* 로직 주입
* 횡단 관심사 (cross-cutting concern) + 핵심 관심사 = 코드'
  - ⭐횡단 관심사는 모두 사라지고 오직 핵심 관심사만 남음
  - 추가 개발과 유지보수 관점 편한 코드
  - 단일 책임 원칙 (SRP) 적용
* 종류
  - @Around
  - @Before
  - @After
  - @AfterReturning : 메서드 정상 종료 후
  - @AfterThrowing : 메서드에서 예외가 발생하면서 종료 된 후

* 애노테이션
  - @Aspect : 이 클래스는 AOP 사용
  - @Before : 대상 메서드 실행 전 메서드 실행

* 핵심
  - 인터페이스 기반
  - 프록시 기반
  - 런타임 기반

* 용어
  - Aspect : 관점
    - 여러개의 advice 와 여러개의 pointcut 결합체의 의미
    - When + Where + what
  - Advisor: 조언자
    - 어디서, 언제, 무엇을
    - 스프링 AOP 에서만 사용
  - Advice : = When, What
    - pointcut 적용할때 언제라는 개념까지 포함
    - Poincut 을 언제 무엇을 적용할지 정의 메서드 
  - JoinPoint : 
    - 연결 가능한 지점
    - Pointcut 은 JointPoint 의 붑ㄴ 집합
    - Aspcect 적용 가능한 모든 지점
  - Pointcut :  = Where
    - aspect 적용하는 위치 지정자
    - 타겟 클래스의 타켓 메서드 지정자
 
 
 ## PSA (Portable Service Abstraction)
 * 일관성 있는 서비스 추상화
 * 서비스 추상화 ex. JDBC
  - DB 관계없이 어댑터 패턴을 활용하여 다수의 인터페이스로 제어할수 있게 함.
  - ORM, Cache, Transaction
