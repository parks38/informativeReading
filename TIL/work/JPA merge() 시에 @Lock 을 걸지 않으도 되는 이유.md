----

JPA `EntityManager` 에서 제공하는 save() 는 
1. 영속이 한번도 안된 경우 -> `save()` / INSERT
2. 영속 상태인 경우/ 한번이라도 영속이 된 경우 -> `merge()` / UPDATE

이때 merge()/save() 어떤 것을 사용할건지를 ==1차 캐시 snapshot==을 통해ㅔ ==변경감지==를 통해 확인한다. 
merge/save 자체는 DB 삽입 역할만 해주는 거지 1차 캐시에서 값을 먼저 가져 와야 한다. 

```java
Example example = exampleRepository.findById();
exampleRepository.save(example);
```

find()를 통해 Entity 를 먼저 가지고 와야 하는데 이때 1차 캐시를 보고
값이 있으면 DB 를 가져오지 않고 
값이 없을 경우 DB 조회해서 값을 반환한다. 

> DB 의 ACID 를 보장하기 위해서 @Lock 을 걸어야 한는가? 

JPA는 save() 시에 낙관적인 락을 자동 세팅해 준다고 합니다. (SimpleJPARepository) 
- 동시에 리소스를 수정하려고 할 시에는 `OptiomisticLockException`을 던져준다고한다. 

⭐ KeyWord
- 비관적 lock
- 낙관적 lock 
- @Lock 

> DB 격리수준을 통해 락을 생성할 수 있는데 이와는 관련이 없는가? 

- JPA 는 트랜잭션 commit 을 모아서 한번에 flush() 형태로 동작하는데 transaction 단위가 끝날때까지는 DB 접근을 안하므로 딱히 해결을 해줄수 없을 것 같다. 
- 격리 수준
	- READ UNCOMMITED
	- READ COMMITED 
	- REPEATABLE READ
	- SNAPSHOT
	- SERIALIZABLE 


https://velog.io/@haron/JPA-JPA%EC%9D%98-%EB%82%99%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88Optimistic-Lock-%EB%B9%84%EA%B4%80%EC%A0%81-%EC%9E%A0%EA%B8%88Pessimistic-Lock




위 코드에서 version 필드에 @Version 애노테이션이 붙어 있습니다. 이를 통해 SimpleJpaRepository 클래스에서는 엔티티 객체의 버전 정보를 자동으로 체크하고, 업데이트할 수 있는지 여부를 판단합니다. 따라서 SimpleJpaRepository 클래스에서 제공하는 save() 메서드를 호출할 때는 별도로 낙관적 락을 설정하지 않아도 자동으로 낙관적 락이 사용됩니다.