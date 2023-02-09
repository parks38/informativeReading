----
[스크립트]

이 내용들은 이펙티브 자바를 참고한 내용들입니다.

일단 이 질문을 받는다면 답변은

자바8에는 [1]람다, [2]Stream API, [3]함수형 프로그래밍, [4]Optional, [5]인터페이스의 디폴트 메서드 기능이 추가되었다. 기존의 프로그램에 새로운 개념과 기능들을 제공에 더 효과적이고 간결하게 구현 가능하도록 했다고 보면 된다.

람다를 통해 메서드에 코드를 참조하는 동작 구현을 하며

Stream API 로 함수형 프로그래밍의 참조 투명성 성질을 통해 CPU가 멀티 코어 스로세스에서 교착 상태에 빠지지 않도록 해주어 동시성에 강한 프로그래밍 패러다임을 만들어 준다.

디폴트 메서드는 Stream API 를 다른 버전과 호환 가능하도록 해준다.

Optional 의 경우는 NPE 를 방지 할 수 있게 도와준다.

이에 더해서 자바 기능들의 흐름에 대하여도 자주 꼬리 질문이 나와 내용 정리해 보았습니다.

자바 1.0 : 스레드, 락, 메모리 모델 지원

자바 5 : 스레드 풀, concurrent collection

자바 7: 병렬 실행 (fork/join framework)

  - 재귀적으로 작은 작업으로 분할한 다음에 서브 태스크 결과들을 합치는 방식으로 여러 쓰레드가 동시 처리를 쉽게 만들어졌다. (Collection 의 parallelStream 경우 ForkJoinPool 을 사용한다.)

자바 8 : stream API/람다 등 멀티코어 CPU 대중화 하드웨어 변화에 따른 기술력 강화

자바 9 : reactive programming (병렬 실행) - RxJava

-----

동등성은 객체의 내용이 같을 경우를 말하고

동일성은 객체의 주소 값이 같을 때를 나타냅니다.

동등성은 equals 을 통해 나타내고 동일성은 == 를 통해 나타냅니다.

Primitive type 의 경우에는 주소값이 없기 때문에 내용이 같으면 동일하다

Reference Type 의 경우에는 같은 정보를 가지고 있어도 주소 값이 다르면 다르다.

Object 사용 시 default equals 메서드는 == (동일성) 기준으로 비교를 한다.

그래서 @Override 를 통해 메소드 내 주소 값이 같은지 비교를 해주어야 합니다. 주소값 기준으로 변경해주며

(List, Map, Set) 등 Collection 사용 시에는 hashCode()도 함께 오버라이드 해주어야 한다.

Set 같은 경우는 Set<Object> 로 사용할 시에는 같은 값이라도 자동으로 동일하다고 인지해 주지 못한다.

이럴 경우 hashCode() 를 오버라이딩 하여 HashTable 을 사용하는 자료구조에서 동등성을 보장 할 수 있다.

VO 클래스에 대해서는 equals, hashCode 오버라이드를 권장한다고 한다. 

Vs. Lombok - @EqualsAndHashCode(callSuper=false)

-   Equals (동등성 equality)
-   hashCode (동일성 identity)

자바 bean 에서 동등성 동일성 비교를 위해 해당 메소드들을 오버라이딩 하는데

callSuper 송성으로 자동 생성시 부모 클래스의 필드까지 감안 할지 여부 설정이 가능하다. True 설정하면 부모 클래스 필드값도 동일한지 체크하고 false(default) 일 경우에는 자신 클래스만 고려한다.

-   JPA 가 == 비교, 동등성을 보장해 준다.

JPA 는 해당 로직을 사용한다.

[1] PK 로만 equals, hashCode 구현한다

[2] PK 를 제외하고 equals 로 구현한다

[3] 비즈니스 키를 사용한 동등성을 구현한다.

[https://blog.yevgnenll.me/posts/jpa-entity-eqauls-and-hashcode-equality](https://blog.yevgnenll.me/posts/jpa-entity-eqauls-and-hashcode-equality)

----

웹 서버란 H