----
**Transaction이란 한 문장으로 정의해 보면 데이터베이스의 상태를 변화시키기 위해서 수행하는 작업의 단위를 의미합니다.**

###### ✅Spring에서 제공하는 트랜잭션

1.  개발식 트랜잭션
    -   트랜잭션 매니저에서 트랜잭션을 얻어오는 방법
    -   가독성을 떨어뜨리고, 휴먼에러가 유발될 수 있으므로 잘 사용하지 않음.
2.  선언적 트랜잭션 (어노테이션)
    -   스프링의 AOP를 적극적으로 체감가능
    -   xml에서 AOP 설정으로 트랜잭션을 선언하는 방법
    -   어노테이션 사용방법(@Transactional)


스프링에서는 `선언적 트랜잭션`을 사용한다. 

###### ✅ 동작원리

- 트랜잭션은 Spring AOP 이용 (어노테이션 기반 AOP)
```java
import org.springframework.transaction
```

-    `@Transactional`이 선언된 클래스에 대해 트랜잭션이 적용된 ==프록시 객체 생성==
-  메서드 앞뒤로 트랜잭션 로직 삽입
	- 프록시 객체는 @Transactional이 포함된 메서드가 호출될 경우,==트랜잭션을 시작하고 Commit or Rollback을 수행==
-   CheckedException or 예외가 없을 때는 Commit
-   UncheckedException이 발생하면 Rollback
	- RuntimeException과 Error가 발생했을시 롤백

[주의점]
- 우선순위
	- 메서드 선언이 가장 높고 인터페이스 선언이 가장 낮다. 
	    ⭐ 클래스 메소드 -> 클래스 -> 인터페이스 메소드 -> 인터페이스
		* 인터페이스 보다는 클래스에 권고 
* 트랜잭션의 모드
	* Proxy Mode(Default) / AspectJ Mode 
	* Proxy Mode 동작하지 않는 경우
		- public 메소드 에만 적용이 된다 (proteced, private 은 에러는 안나지만 동작 안함)
		- non-public 에 적용하고 싶다면 AspectJ Mode 고려
	- @Transactional 이 적용되지 않은 public method 에서 @Transactional 이 적용된 Public method 호출시 트랜잭션이 동작하지 않음. 


###### ✅ 어노테이션 사용 방법
* 일반적으로 많이 사용하는 선언적 트랜잭션 방식
	* `@Transactional` 사용하고 싶으면 `@EnableTransactionManagement`를 추가하고 사용
		* spring boot 는 자동으로 설정되어 있음
	- 

[특성]
![[Pasted image 20230329085626.png]]

###### ✅ 사용 예시
* 하나의 작업 or 여러 작업
* 하나의 작업을 단위로 묶어 commit or rollback 처리 필요 

- JPA 를 사용하면 @Transactional 직접 선언 필요 없음
	- 구현체에 이미 선언이 되어 있어 rollback 을 자동으로 해줌. 

> 선언적 트랜잭션 방식을 사용하는 이유? 

비즈니스 로직이 트랜잭션 처리를 필요로 할 때, 트랜잭션 처리 코드와 비즈니스 로직이 공존한다면 코드 중복이 발생하고 비즈니스 로직에 집중하기 어렵다. 따라서 트랜잭션 처리와 비즈니스 로직을 분리할 수 있는 선언적 트랜잭션 방식을 자주 사용한다. (🔸Spring AOP)

> @Transactional 동작 원리는? 

@Transactional을 메소드 또는 클래스에 명시하면 AOP를 통해 Target이 상속하고 있는 인터페이스 또는 Target 객체를 상속한 Proxy 객체가 생성되며, Proxy 객체의 메소드를 호출하면 Target 메소드 전 후로 트랜잭션 처리를 수행한다.

> @Transactional 사용 시 주의 사항은? 

-   Proxy 객체의 Target Method가 내부 메소드를 호출하면 ==실제 메소드가 호출되기 때문에 Inner Method에서 @Transactional 어노테이션이 적용되지 않는 것==을 주의해야 한다.
-   @Transactional 어노테이션을 붙이면 트랜잭션 처리를 위해 Proxy 객체를 생성하는데, Proxy는 Target Class를 상속하여 생성된다. 따라서 ==상속이 불가능한 Private 메소드의 경우 @Transactional 어노테이션을 적용할 수 없다는 것을 주의==해야 한다.

> @Transactional를 스프링 Bean의 메소드 A에 적용하였고, 해당 Bean의 메소드 B가 호출되었을 때, B 메소드 내부에서 A 메소드를 호출하면 어떤 요청 흐름이 발생하는지 설명하라.

프록시는 클라이언트가 ==타겟 객체를 호출하는 과정에만 동작==하며, 타겟 객체의 메소드가 자기 자신의 다른 메소드를 호출할 때는 프록시가 동작하지 않는다. 
즉 A 메소드는 프록시로 감싸진 메소드가 아니므로 트랜잭션이 적용되지 않는 일반 코드가 수행된다.

> A라는 Service 객체의 메소드가 존재하고, 그 메소드 내부에서 로컬 트랜잭션 3개(다른 Service 객체의 트랜잭션 메소드를 호출했다는 의미)가 존재한다고 할 때, @Transactional을 A 메소드에 적용하면 어떤 요청 흐름이 발생하는지 설명하라.

트랜잭션 전파 수준에 따라 달라진다. 만약 기본 옵션인 REQUIRED를 가져간다면 로컬 트랜잭션 3개가 모두 부모 트랜잭션인 A에 합류하여 수행된다. 그래서 ==부모 트랜잭션이나 로컬 트랜잭션 3개나 모두 같은 트랜잭션이므로 어느 하나의 로직에서든 문제가 발생하면 전부 롤백==이 된다.

전파 단계는 REQUIRED, REQUIRES_NEW, MANDATORY, NESTED, NEVER, SUPPORTS, NOT_SUPPORTED 가 있습니다. 

전파 단계 설명 : https://deveric.tistory.com/86

-----
https://resilient-923.tistory.com/415
https://beaniejoy.tistory.com/68



