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
