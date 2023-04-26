
```java

public class DataArchive {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name="ID")
	private CodeType type;
}
```

이럴때 Cascade 타입을 넣지 않거나 CasecadeType.PERSIST 이용시 특정 경우  throws an exception 

[HHH000346: Error during managed flush [org.hibernate.PersistentObjectException: detached entity passed to persist:]


> solution 

JPA 사용시 ==자동으로 생성되는 값을 가진 필드에 직접 값을 할당해 저장==하고자 할 때 발생되는 에러다.

Self Join 도메인에서 @ManyToOne 의 옵션으로 Cascade 를 CascadeType.All 로 지정했는데 이 때문이었다.


-   CascadeType.RESIST – 엔티티를 생성하고, 연관 엔티티를 추가하였을 때 persist() 를 수행하면 연관 엔티티도 함께 persist()가 수행된다.  만약 연관 엔티티가 DB에 등록된 키값을 가지고 있다면 detached entity passed to persist Exception이 발생한다.
-   CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용된다.(연관 엔티티의 추가 및 수정 모두 반영됨)
-   CascadeType.REMOVE – 삭제 시 연관된 엔티티도 같이 삭제됨
-   CascadeType.DETACH – 부모 엔티티가 detach()를 수행하게 되면, 연관된 엔티티도 detach() 상태가 되어 변경사항이 반영되지 않는다.
-   CascadeType.ALL – 모든 Cascade 적용


```java
@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
```



