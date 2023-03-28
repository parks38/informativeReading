
###### ✅ Answer 
JPA 사용시 save() 시에 낙관적인 락을 자동 세팅해 주기 때문에 @Lock 을 걸지 않아도 됩니다. 
save() 를 구현하는 SimpleJpaRepository 클래스에서 @Version에노테이션이 붙을 필드를 통해 버전 정보를 사용합니다. 

JPA에서 기본적으로 제공하는 낙관적 락 기능으로 충분히 충돌을 방지할 수 있기 때문에 @Lock이 잘 사용되지는 않지만  `비관적 락`을 걸어야 할 때 사용할 수 있습니다. 

###### ➡️ JPA Save() 리뷰 
JPA `EntityManager` 에서 제공하는 save() 는 
1. 영속이 한번도 안된 경우 -> `save()` / INSERT
2. 영속 상태인 경우/ 한번이라도 영속이 된 경우 -> `merge()` / UPDATE

이때 merge()/save() 어떤 것을 사용할건지를 ==1차 캐시 snapshot==을 통해 ==변경감지==를 확인한다. 
merge/save 자체는 DB 삽입 역할만 해주는 거지 1차 캐시에서 값을 먼저 가져 와야 한다. 

```java
Example example = exampleRepository.findById();
exampleRepository.save(example);
```

find()를 통해 Entity 를 먼저 가지고 와야 하는데 이때 1차 캐시를 보고
값이 있으면 DB 를 가져오지 않고 값이 없을 경우 DB 조회해서 값을 반환한다. 

###### ➡️ JPA의 락 기능  

merge() 사용 시, find()를 통해 entity 를 먼저 가지고 오고 transaction 단위로 DB 수정을 한다고 했는데 이때 리소스 접근에 대한 ACID 를 보장하기 위해 @Lock 을 사용해야 하지 않을까? 

=> NO! JPA는 save() 시에 낙관적인 락을 자동 세팅해 준다고 합니다. (SimpleJPARepository) 
- 동시에 리소스를 수정하려고 할 시에는 `OptiomisticLockException`을 던져줍니다. 

###### ➡️ JPA SimpleJPARepository의 역할
- SimpleJpaRepository 클래스는 JPA의 기본 구현체 중 하나로, JpaRepository 인터페이스를 구현하는 클래스
- JPA의 EntityManager를 이용하여 데이터베이스와 연동
- JpaRepository 인터페이스에서 제공하는 save() 메서드도 SimpleJpaRepository 클래스에서 구현
- ⭐`save()` 메서드에서는 엔티티 객체를 저장할 때 버전 정보를 자동으로 관리 => 낙관적 락 기능 자동으로 사용 
	- 변경감지 : 엔티티 객체의 상태를 저장하기 전에 버전 정보를 체크하여 업데이트할 수 있는지 여부를 판단

* SimpleJpaRepository 클래스에서는 @Version 애노테이션이 붙은 필드를 버전 정보로 사용
```java
@MappedSuperclass
public class AbstractPersistable<PK extends Serializable> 
										implements Persistable<PK> {

    private static final long serialVersionUID = -5554308939380869754L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private PK id;

    @Version
    private Long version;
}
```

###### ➡️ @Version - 낙관적 잠금 
* 데이터 갱신시 `충돌이 발생하지 않을 것` 이라고 낙관적으로 보고 잠금을 거는 기법
	 * 디비에 락을 걸기 보다 충돌 방지 (Conflict Detection)에 가까움. 
* 동시성 처리를 위해 `Optimistic Lock` 제공 
* `@Version`이라는 속성을 확인하여 Entity의 변경사항을 감지하는 메커니즘
* 동시에 동일한 데이터에 대한 여러 업데이트가 서로 간섭하지 않도록 방지
	* 주의 사항 
		* 각 엔티티 클래스에는 하나의 버전 속성만 있어야 한다.
		-   여러 테이블에 매핑 된 엔티티의 경우 기본 테이블에 배치되어야 한다.
		-   버전에 명시할 타입은 `int`, `Integer`, `long`, `Long`, `short`, `Short`, `java.sql.Timestamp` 중 하나 여야합니다.

[동작 방식]

![[Pasted image 20230328103611.png]]

1.  JPA는 Select시에 트랜잭션 내부에 버전 속성의 값을 보유하고 트랜젝션이 업데이트를 하기 전에 버전 속성을 다시 확인한다.
2.  그 동안에 버전 정보가 변경이 되면 `OptimisticLockException`이 발생하고 변경되지 않으면 트랜잭션은 버전속성을 증가하는 업데이트 하게 된다.

🔺 엔티티의 Version 과 커밋 시 확인한 Version 이 동일하면 Version 을 1 증가시키고 다르다면 `OptimisticLockException` 발생 

> DB 격리수준을 통해 락을 생성할 수 있는데 JPA 락 과는 연관성이 있는가? 

DB  격리 수준은 여러 트랜잭션에서 동시에 같은 `데이터를 접근`할때 발생할 수 있는 문제로 락을 이용하여 데이터 일관성 유지합니다. 
JPA 의 낙관적 락은 동시에 여러 트랜잭션이 같은 `데이터를 업데이트` 할때 발생할 수 있는 ==충돌을 방지하기 위한 것==입니다. 
JPA 의 낙관적 락과 DB 격리 수준은 연관이 있는데, 낙관적 락은 DB격리 수준이 `READ COMMITTED` 이상인 경우에만 사용할 수 있습니다. 

###### ➡️ @Lock - 비관적인 락 
실무에서는 @Lock 애노테이션을 사용하지 않는 경우가 일반적이며 
이는 JPA에서 기본적으로 제공하는 낙관적 락 기능으로 충분히 충돌을 방지할 수 있기 때문입니다.
@Lock은  `비관적 락`을 걸어야 할 때 사용하는데
예를 들어, 여러 트랜잭션이 동시에 동일한 데이터를 업데이트하거나, 데이터의 일관성이 중요한 경우에는 비관적 락을 사용할 수 있습니다.


-----
[참고]
https://velog.io/@haron/JPA-JPA%EC%9D%98-%EB%82%99%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88Optimistic-Lock-%EB%B9%84%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88Pessimistic-Lock