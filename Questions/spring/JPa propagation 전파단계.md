----
#### | JPA Propagation 

JPA `@Transactional`은 중요한 옵션 두가지가 있다. 
==propagation==은 세션의 트랜잭션을 어떻게 이용할지에 대한 설정이다. 
ex. REQUIRED, SUPPORTS, MANDATORY, NEVER, NOT_SUPPORTED, REQUIRES_NEW, NESTED
==isolation==은 JPA 상에서 DB Isolation 을 지정할수 있다. 
ex. DEFAULT, READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE

> propagation 

propagation 은 트랜잭션의 영역, 바운도리를 지정하기 위한 설정이다. 

| 종료         | 트랜잭션 존재                                                                   | 트랜잭션 미존재                                         | 비고                                                              |
| ------------ | ------------------------------------------------------------------------------- | ------------------------------------------------------- | ----------------------------------------------------------------- |
| required     | 기존 트랜잭션 이용                                                              | 신규 트랜잭션 생성                                      | 기본설정                                                          |
| support      | 기본 트랜잭션 이용                                                              | 트랜잭션 없이 수행                                      |                                                                   |
| mandatory    | 기존 트랜잭션 이용                                                              | Exception 발생                                          | 꼭 이전 트랜잭션이 있어야 하는 경우                               |
| never        | exception 발생                                                                  | 정상 수행                                               | 트랜잭션 없을때만 작업이 진행 되어야 하는 경우                    |
| not_support  | 트랜잭션이 종료될때까지 대기한 후 트랜잭션이 종료되고 나면 실행                 | 트랜잭션 없이 로직 수행                                 | 기존 트랜잭션에 영향을 주면 안될 경우                             |
| requires_new | 현재 트랜잭션이 종룓될때까지 대기한 후 새로운 트랜잭션을 생성하고 실행하려 할때 | 신규 트랜잭션을 생성하고 로직을 실행                    | 이전 트랜잭션과 구분하여 새로운 트랜잭션으로만 처리가 필요할 경우 |
| nested       | 현재 트랜잭션에 save point 를 걸고 이후 트랜잭션을 수행                         | required 와 동일하게 신규 트랜잭션을 생성하고 로직 수행 | DBMS 틀성에 따라 지원 혹은 미지원                                 |




#### | Spring Propagation

-   Consistent programming model across different transaction APIs such as Java Transaction API (JTA), JDBC, Hibernate, Java Persistence API (JPA), and Java Data Objects (JDO).

스프링의 declarative transaction management (선언적 트랜잭션 관리)는 스프링 AOP (aspect oriented programming) 로 인해 가능하며 보일러 플레이트 방식으로 사용 가능하다. 

스프링 프래임 워크의 선언적 트랜잭션 관리는 개별 메소드 레벻까지 트랜잭션 동작을 지정할수 있다는 점에서 EJB CMT 와 유사하다. 차이점이라고 한다면 ==1) EJB 에 없는 롤백 규칙을 선언할 수있으며 2)AOP 로 트랜색션 동작을 커스텀할 수 있고 3) EJB 에서는 지원하는 고급 어플리케이션 서버나 원격 호출간 트랜잭션 컨텍스트 전파는 지원하지 않는다.==

트랜잭션의 범위는 하나 이상의 DAO 를 사용하는 Service layer 과 연관이 있으며 트랜잭션이 컴포넌트끼리 서비스 레이어 트랜잭션 범위 내에서 전달한다. 

![[Pasted image 20221213091013.png]]

> 기본 개념

클래스에 `@Transactional` 을 달고 설정에 `@EnableTransactionManagement` 를 추가한다. 
트랜잭션 지원은 ==AOP 프록시를 통해 활성화==되며
트랜잭션 어드바이스는 ==메타데이터(XML, 에노테이션 기반)==으로 구동된다
`트랜잭션 메타데이터`를 가지고 만든 AOP 프록시는 `TransactionInterceptor`와 적당한 `TransactionManager`
구현체를 사용해 호출하고 트랜잭션을 실행한다. 

==스프링 AOP 는 AOP 섹션에서 다루고 있다==
`TransactionInterceptor`는 명령형과 반응형 프로그래밍 모델에 따라 트랜잭션을 관리하며
메소드 리턴 타임을 검사해 적당한 트랜잭션 관리 방식을 감지한다. 
- 명령형 => `org.springframework.transaction.PlatformTransactionManager`
	- ==@Transactional== 은 `PlatformTransactionManger`이 관리하는데 스레드에 바인딩된 트랜잭션으로 동작하며 스레드 내 모든 데이터 접근 연산에 트랜잭션을 노출한다. 
- 반응형 => `ReactiveTransactionManager`
	- 반응형 트랜잭션은 스레드 로컬 대신 `리액터 컨텍스트`를 사용하며 모든 데이터 연산은 ==리액티브 파이프라인 내 리액터 컨텍스트에서 실행==한다. 

![[Pasted image 20221213093111.png]]




To configure the transaction propagation strategy for EJB components, Java EE defines the
@TransactionAttributea annotation. Since Java EE 7, even non-EJB components can now be
enrolled in a transactional context if they are augmented with the @Transactionalb annotation.
In Spring, transaction propagation (like any other transaction properties) is configurable via
the @Transactionalc annotation.

Table 6.6: Transaction propagation strategies
| propagation   | Java EE | Spring | Description                                                                                                                                                        |
| ------------- | ------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| REQUIRED      | yes     | yes    | This is the default propagation strategy, and it only starts  a transaction if and only if the current thread is not already associated with a transaction context |
| REQUIRES_NEW  | yes     | yes    | Any currently running transaction context is suspended=and replaced by a new transaction                                                                           |
| NOT_SUPPORTED | yes     | yes    |  Any currently running transaction context is suspended, and the current method is run outside of a transaction                                                                                                                                                                  |
| MANDATORY     | yes     | yes    | The current method runs only if the current thread is already associated with a transaction context                                                                                                                                                                   |
| NESTED        | no      | yes    | The current method is executed within a nested transaction if the current thread is already associated with a transaction. Otherwise, a new transaction is started.                                                                                                                                                                   |
| NEVER         | no      | yes    | The current method must always run outside of a transaction context, and, if the current thread is

----
[참고]
https://stackoverflow.com/questions/8490852/spring-transactional-isolation-propagation
https://oingdaddy.tistory.com/28
https://n1tjrgns.tistory.com/266


[propagation/isolation]
https://devocean.sk.com/blog/techBoardDetail.do?ID=163799
