
Transactional 기능과 JPA Dirty Check 에 대해서 설명해 보겠습니다. 

먼저 스프링의 @Transactional 에 대해서 설명해 보겠습니다. 
Transaction이란 한 문장으로 정의해 보면 데이터베이스의 상태를 변화시키기 위해서 수행 하는 작업의 단위를 의미합니다. 

DB에서도 사용하는 언어이지만 
Spring에서 제공하는 트랜잭션 은 개발식 트랜잭션과 선언적 트랜잭션으로 구분되어 있습니다. 

[개별적 트랜잭션]은 트랜잭션 매니저에서 트랜잭션을 얻어오는 방법을 쓰는데 가독성을 떨어뜨리고, 휴먼에러가 유발될 수 있으므로 잘 사용하지 않습니다.

[선언적 트랜잭션] (어노테이션)은 스프링의 AOP를 적극적으로 체감가능하게 해주는데 xml에서 AOP 설정으로 트랜잭션을 선언하는 방법을 사용하며  @Transactional 을이용 하여 선언해 줍니다. 

선언적 트랜잭션 기반의 @Transactional 의 동작원리를 알아보겠습니다. 
트랜잭션은 Spring AOP 를 사용하며 import.org.springframework.transaction을 import 받아와야 합니다. 

@Transactional 이 선언된 클래스에 대해 트랜잭션이 적용된 ==프록시 객체 생성== 메서드 앞뒤로 트랜잭션 로직 삽입하고 프록시 객체는 @Transactional이 포함된 메서드가 호출될 경우,==트랜잭션을 시 작하고 Commit or Rollback을 수행==합니다. 
CheckedException or 예외가 없을 때는 Commit / UncheckedException이 발생하면 Rollback RuntimeException과 Error가 발생했을시 롤백을 합니다. 

해당 어노테이션을 사용할때 주의사항이 있는데요
일단 우선순위는 메서드 선언이 가장 높고 인터페이스 선언이 가장 낮다고 합니다. 
그래서 인터페이스 보다는 클래스에 사용하기를 권고 한다고 하네요. 

또, 트랜잭션 모드의 Proxy Mode 가 디폴트인데 프록시 모드에서는 public 메소드에만 적용이 된다고합니다. protected, private 는 에러는 안나지만 동작이 안한다고 합니다. 
만약 non-public 에 적용하고 싶다면 AspectJ Mode 사용을 고려해야 합니다.
그리고 @Transactional 이 적용되지 않은 퍼블릭 메서드에서 적용이 된 메서드를 호출시에는 트랜잭션이 동작하지 않는다고 합니다. 

다음은 어노테이션 사용법입니다.
일반적으로 많이 사용하는 선언적 트랜잭션 방식 @Transactional 사용하고 싶으면 @EnableTransactionManagement 를 추가하고 사용해야 합니다만 spring boot 는 자동으로 설정되어 있다고 합니다. 

이 어노테이션은 
하나의 작업 or 여러 작업을 단위로 묶 commit or rollback 처리 필요할 경우 사용하며 JPA 를 사용하면 @Transactional 직접 선언 필요 없고 구현체에 이미 선언이 되어 있어 rollback 을 자동으로 해줍니다. 

아래는 간단하게 꼬리 질문들을 가져와 봤습니다. 

> 선언적 트랜잭션 방식을 사용하는 이유? 

비즈니스 로직이 트랜잭션 처리를 필요로 할 때, 트랜잭션 처리 코드와 비즈니스 로직이 공 존한다면 코드 중복이 발생하고 비즈니스 로직에 집중하기 어렵다. 따라서 트랜잭션 처리와 비즈니스 로직을 분리할 수 있는 선언적 트랜잭션 방식을 자주 사용한다. (🔸Spring AOP)

> @Transactional 동작 원리는? 

@Transactional을 메소드 또는 클래스에 명시하면 AOP를 통해 Target이 상속하고 있는 인터페이스 또는 Target 객체를 상속한 Proxy 객체가 생성되며, Proxy 객체의 메소드를 호 출하면 Target 메소드 전 후로 트랜잭션 처리를 수행한다.

> @Transactional 사용 시 주의 사항은?

Proxy 객체의 Target Method가 내부 메소드를 호출하면 실제 메소드가 호출되기 때문 에 Inner Method에서 @Transactional 어노테이션이 적용되지 않는 것을 주의해야 한 다. @Transactional 어노테이션을 붙이면 트랜잭션 처리를 위해 Proxy 객체를 생성하는 데, Proxy는 Target Class를 상속하여 생성된다. 따라서 상속이 불가능한 Private 메소 드의 경우 @Transactional 어노테이션을 적용할 수 없다는 것을 주의해야 한다.

> @Transactional를 스프링 Bean의 메소드 A에 적용하였고, 해당 Bean의 메소드 B가 호출되었을 때, B 메소드 내부에서 A 메소드를 호 출하면 어떤 요청 흐름이 발생하는지 설명하라. 

프록시는 클라이언트가 타겟 객체를 호출하는 과정에만 동작하며, 타겟 객체의 메소드가 자 기 자신의 다른 메소드를 호출할 때는 프록시가 동작하지 않는다. 즉 A 메소드는 프록시로 감싸진 메소드가 아니므로 트랜잭션이 적용되지 않는 일반 코드가 수행된다. 

> A라는 Service 객체의 메소드가 존재하고, 그 메소드 내부에서 로컬 트 랜잭션 3개(다른 Service 객체의 트랜잭션 메소드를 호출했다는 의미) 가 존재한다고 할 때, @Transactional을 A 메소드에 적용하면 어떤 요 청 흐름이 발생하는지 설명하라. 

트랜잭션 전파 수준에 따라 달라진다. 만약 기본 옵션인 REQUIRED를 가져간다면 로컬 트 랜잭션 3개가 모두 부모 트랜잭션인 A에 합류하여 수행된다. 그래서 부모 트랜잭션이나 로 컬 트랜잭션 3개나 모두 같은 트랜잭션이므로 어느 하나의 로직에서든 문제가 발생하면 전 부 롤백이 된다. 전파 단계는 REQUIRED, REQUIRES_NEW, MANDATORY, NESTED, NEVER, SUPPORTS, NOT_SUPPORTED 가 있습니다.

-----
그 다음 연결해 보자면 

JPA Dirty Checking 이란 
* *'상태의 변화가 생겼다' 정도의 의미로 '상태 변경 검사' 를 의미하며 
* JPA에서는 트랜잭션이 끝나는 시 점에 변화가 생긴 모든 엔티티들을 데이터베이스에 자동으로 반영하는데 조회 상태로 '스냅샷'을 만들고 트랜잭션 끝나는 시점에 비교해서 update 을 DB 에 전달합니다. 

사용 조건은 
1) 변경하려는 Entity 가 영속 상태여야하고 
2) 트랜잭션 안에 묶여 있어야 하며 
3) 마지막으로 트랜잭션이 제대로 commit 되어 flush 동작 합니다. 

📌@Transactional 사용 시 save() 필요 없는 이유?

오해하면 안되는 부분은 save 를 해주는 것이 아니라 update 을 해주는 것이라고 합니다. 
서비스를 @Transactional 이용하여 감싸 주면 Dirty Checking은 transaction이 commit 될 때 작동하고 Transactional 어노테이션을 붙여주면 Dirty Checking을 하게 되고,  데이 터베이스에 commit을 해서 수정된 사항을 save 없이도 반영할 수 있도록 한다.

주의점은 연관관계에 의해 다른 엔티티들도 save 나 update를 해야 하는 등의 경우에서 transaction이 완료되면 전체가 반영되어야 하고 실패하면 전체를 취소시켜서 이전 상황으로 롤백을 꼭 해줘야 한다.

➡ 서비스 단에 필요한 이유? 
repository 에는 JPA 특성상 @Trnasactional 로 묶여 있다. 하지만 해당 쿼리메소드에만 트랜잭션이 적용되기 때문에 트랜잭션 커밋이 일어나지 않는다. Service 단에 묶지 않고 트 랜잭션을 repository 단위로 트랜잭션을 묶는 경우는 마지막에 꼭 save() 를 해줘야 한다. 

➡ @Transactional(readOnly = true)를 공통으로 사용해야 하는 중 요한 이유? 

트랜잭션 어노테이션이 없을 경우 @OneToMany, @ManyToMany 등 Lazy loding(지연로 딩)을 Default로 사용하는 엔티티들을 정상적으로 조회할 수 없습니다. 
* Lazy loading(지연로딩) 
	* JPA 구현체들은 프록시 패턴을 통해서 객체를 조회할 때 연관된 객체를 바로 조회 하지 않고 실제로 사용할 때만 조회 
	* 프록시를 사용해서 조회할 경우에는 해당 객체에 접근 할 때 조회 하겠다고 요청 

Transaction이 붙어있지 않을 경우, 준영속 상태에 있는 엔티티들은 지연로딩을 할 수 없습 니다. 지연로딩(Lazy loading)을 사용해서 프록시 객체로 존재했을 때, 해당 객체에서 실제 로 값을 뽑으려고 하는 행위는 불가능한 것이죠. 따라서 트랜잭션이 있어야 지연로딩(Lazy loading)이 필요한 엔티티들을 정상 조회 할 수 있습니다.

-----

그 다음 제가 궁금했던 부분은 JPA 사용시 @Lock 을 왜 사용하지 않는지 였습니다. 

JPA 사용시 save() 시에 낙관적인 락을 자동 세팅해 주기 때문에 @Lock 을 걸지 않아도 됩니다. save() 를 구현하는 SimpleJpaRepository 클래스에서 @Version 에노테이션이 붙을 필드를 통해 버 전 정보를 사용합니다. JPA에서 기본적으로 제공하는 낙관적 락 기능으로 충분히 충돌을 방지할 수 있기 때문에 @Lock이 잘 사용되지는 않지만 비관적 락 을 걸어야 할 때 사용할 수 있습니다.


JPA save 에 대해 이전에 했지만 한번더 리뷰해 보도록 하겠습니다. 
JPA  EntityManager 에서 제공하는 save() 는 
1. 영속이 한번도 안된 경우 ->  save()  / ==INSERT== 
2. 영속 상태인 경우/ 한번이라도 영속이 된 경우 ->  merge()  / ==UPDATE== 

이때 merge()/save() 어떤 것을 사용할건지를 ==1차 캐시 snapshot==을 통해 ==변경감지==를 확ㅇ니하고. merge/save 자체는 DB 삽입 역할만 해주는 거지 1차 캐시에서 값을 먼저 가져 와야 합니다. find()를 통해 Entity 를 먼저 가지고 와야 하는데 이때 1차 캐시를 보고 값이 있으면 DB 를 가져오지 않고 값이 없을 경우 DB 조회해서 값을 반환합니다. 

 merge() 사용 시, find()를 통해 entity 를 먼저 가지고 오고 transaction 단위로 DB 수정을 한다고 했는데 이때 리소스 접근에 대한 ACID 를 보장하기 위해 @Lock 을 사용해야 하지 않을까는 의문이 생겼는데  NO🙅 ! JPA는 save() 시에 낙관적인 락을 자동 세팅해 준다고 합니다. (SimpleJPARepository) 동시에 리소스를 수정하려고 할 시에는 OptiomisticLockException 을 던져줍다고 합니다. 

> ➡ JPA SimpleJPARepository의 역할은

SimpleJpaRepository 클래스는 JPA의 기본 구현체 중 하나로, JpaRepository 인터페이스를 구현하는 클래스로 
JPA의 EntityManager를 이용하여 데이터베이스와 연동을 하고 
JpaRepository 인터페이스에서 제공하는 save() 메서드도 SimpleJpaRepository 클래스에서 구현을 합니다.  

save() 메서드에서는 엔티티 객체를 저장할 때 버전 정보를 자동으로 관리를 하는데 낙관적 락 기 능 자동으로 사용하여 SimpleJpaRepository 클래스에서는 @Version 애노테이션이 붙은 필드를 버전 정보를 사용하여 변경감지를 합니다. 

➡ @Version 에 대해서 알아보자면 
낙관적 잠금 데이터 갱신시 충돌이 발생하지 않을 것 이라고 낙관적으로 보고 잠금을 거는 기법입니다. 
디비에 락을 걸기 보다 충돌 방지 (Conflict Detection)에 가까우며 
동시성 처리를 위해 Optimistic Lock 제공하고 있습니다. 
@Version 이라는 속성을 확인하여 Entity의 변경사항을 감지하는 메커니즘인데 동시에 동일한 데이터에 대한 여러 업데이트가 서로 간섭하지 않도록 방지합니다. 
주의 사항 각 엔티티 클래스에는 하나의 버전 속성만 있어야 하고 
여러 테이블에 매핑 된 엔티티의 경우 기본 테이블에 배치되어야 한다. 버전에 명시할 타입 은 int ,  Integer ,  long ,  Long ,  short ,  Short ,  java.sql.Timestamp 중 하나 여야합니다. 

그러면 동작 방식을 보겠습니다. 

JPA는 Select시에 트랜잭션 내부에 버전 속성의 값을 보유하고 트랜젝션이 업데이트를 하기 전 에 버전 속성을 다시 확인한다.
2. 그 동안에 버전 정보가 변경이 되면 OptimisticLockException 이 발생하고 변경되지 않으면 트랜 잭션은 버전속성을 증가하는 업데이트 하게 된다. 

정리하지면 엔티티의 Version 과 커밋 시 확인한 Version 이 동일하면 Version 을 1 증가시키고 다르다 면 OptimisticLockException 발생합니다. 

> DB 격리수준을 통해 락을 생성할 수 있는데 JPA 락 과는 연관성이 있는가? 

> DB 격리 수준은 여러 트랜잭션에서 동시에 같은 데이터를 접근 할때 발생할 수 있는 문제로 락을 이용 하여 데이터 일관성 유지합니다. JPA 의 낙관적 락은 동시에 여러 트랜잭션이 같은 데이터를 업데이 트 할때 발생할 수 있는 ==충돌을 방지하기 위한 것==입니다. JPA 의 낙관적 락과 DB 격리 수준은 연관이 있는데, 낙관적 락은 DB격리 수준이 READ COMMITTED 이상인 경우에만 사용할 수 있습니다. 

비관적인 락 실무에서는 @Lock 애노테이션을 사용하지 않는 경우가 일반적이며 이는 JPA에서 기본적으로 제공 하는 낙관적 락 기능으로 충분히 충돌을 방지할 수 있기 때문입니다.
@Lock은 비관적 락 을 걸어야 할 때 사용하는데 예를 들어, 여러 트랜잭션이 동시에 동일한 데이터를 업데이트하거나, 데이터의 일 관성이 중요한 경우에는 비관적 락을 사용할 수 있습니다.

-----

파일 단위를 읽을때 필요한 Scanner, InputStream, InputStreamReader, BufferedReader. 

 콘솔 창에서 키보드로 입력하기 위한 방법을 배울때 저희는
 Scanner 객체명 = new Scanner(System.in); 이게 입력을 하기 위해 생성한 객체라고 배웠습니다. 
```
Scanner scan = new Scanner(System.in);
```

```
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
```

그리고 콘솔창에 입력할때 위 코드를 많이 보았습니다. 그래서 이에 대해서 정리해 보려고 합니다. 

-----

스트림이란 데이터가 들어온 순서대로 흐르는 단방향 통로입니다. 단방향 이기 때문에 데이터의 흐름 입력스트립 ,  출력스트림 으로 나눕니다. 
Stream 을 통해 byte/byte[]  변환하여 사용하며 동기적, blocking 방식 동작이 가능합니다. 
데이터를 읽거나 쓰기 위해 스트림에 요청하면 스트림은 다시 데이터를 읽거나 쓸 수 있을때까지 다른 작업을 하지 않고 기다린다. 
닫아주지 않으면 메모리 누수 발생 예외처리 필요합니다.

자바에서 가장 기본이 되는 입력 스트림은 InputStream 이고 출력 스트림은 OutputStream 입니다. 그러면 inputStream 과 System.in의 연관관계는 무엇일까요? 
System.in 은 InputStream 타입의 정적 필드이며 System.in 변수는 표준 입력 스트림으로 console 명령줄 인수를 입력 받을 수 잇습니다. 

(in) 이라는 변수는 InputStream 의 변수로 InputStream 타입을 새 변수로 선언학소 System.in 을 할당시키는 것입니다. 그래서 InputStream 과 System.in 을 묶어서 설명하게 되는 것입니다. 

InputStream 이란 
추상화 클래스로 데이터를 가져오기 위한 통로입니다. 
데이터를 가져오기/전송되기 위한 통로 네트워크, 메모리, HDD, 등 모든 곳에서 파일 가져오기 기능이 가능합니다. 
inputStream 의 특징 인코딩 형식의 10진수로 변슈에 저장 1 byte 만 읽습니다. 

InputStream의 입력 메소드 read() 는 1 바이트 단위로 읽어 들이고 문자가 2 byte 이상 구성되어 있는 인코딩 사용 경우 1 byte 만 읽고 나머지는 스트림에 남아있기 때문에 인코 딩 값을 10진수로 변환한 값이 출력됨.

Scanner(system.in) 은 입력 바이트 스트림인 InputStream 을 통해 표준을 입력 받고 scanner 안에 inputStream 이 들어가는이유는 Scanner 생성자가 Overloading 하고 있기 때문입니ㅏㄷ. 


InputStreamReader
InputStream 과 어떻게 다른가면 InputStream 은 아까 언급했듯이  입력받은 데이터는 int 형으로 저장되는데 10진수의 utf-16으로 저장 1 BYTE 만 읽습니다. 이 특징을 해결하기 위고 온전히 문자를 읽어들이기 위헤 확장한 것입니다. 

바이트 단위로 읽어 드리는 문자단위로 데이터 변환시키는 중개자 역할입니다. 

BufferedReader
기본적으로 바이트 스트림인 InputStream 을 통해 바이트 단위로 데이터를 입력 받고
입력 데이터를 char 형태로 처리하기 위해 중개자 역할인 문자스트림 InputStreamReader 로 감싸줍니다. 

 BufferedReader 필요한 이유?
 Scanner -> InputStreamReader : 문자를 처리함 (문자열이 아님!) 
  문자열을 입력하고 싶다면 매번 배열을 선언해야 하는 단점이 남아있음
   Buffer를 통해 입력받은 문자를 쌓아둔 뒤 한번에 문자열 처럼 처리 readLine() 을 통해 한줄 전체를 읽어 char 배열 생성 필요 없이 String으로 return
   문자를 모아둔 후 보내니 속도가 빠르고 별다른 정규식 검사 없으니 더 빠름
Byte Type = InputStream 
Char Type = InputStreamReader
Char Type 의 직렬화 = BufferedReader

InputStream 은 바이트 단위로 데이터를 처리한다. 또한 System.in 의 타입도 InputStream 이다. InputStreamReader 은 문자(character) 단위로 데이터를 처리할 수 있도록 돕는다. InputStream 의 데이터를 문자로 변환하는 중개 역할을 한다. BufferedReader 은 스트림에 버퍼를 두어 문자를 버퍼에 일정 정도 저장해둔 뒤 한 번에 보낸다.