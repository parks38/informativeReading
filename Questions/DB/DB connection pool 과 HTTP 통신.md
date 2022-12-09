----

connection pool 을 이용하지 않으면 
DB 서버 접속을 위한 과정에 HTTP 요청이 있을때마다 DB 서버에 지속적으로 접근하는 반복적인 작업이 필요하다. 
==DB 서버에 최초 연결하여 connection 객체를 생성하는 작업은 큰 성능 저하==를 야기하고 
이 문제를 사전 방지하기 위해 사용하는 것이` connection pool` 이다. 

#### | what is connection pool ?

```
DB Connection 방식: 

Web Application => JDBC API => JDBC(드라이버) => DB
```

> DB Connection 

DB 를 사용하기 위해 DB 와 어플리케이션 간에 통신할수 있는 수단으로 `Database Driver`과 `Database 연결정보`가 있는 URL 이 필요하다. 자바 어플리케이션에서는 `JDBC` 를 이용해 DB 연동을 하며 정의된 URL 형과 파라미터를 지정해 주어야 한다. 

> DB Connection 구조

* 2 Tier : 클라이언트 < -- DB -- > JSP
* 3 Tier: 자바 프로그램 <-- 미들웨어 (비즈니스 로직 구현, 트랜잭션 처리, 리소스 관리) --> DB

> JDBC 

자바 언어로 다양한 종류의 `관계형 DB` 에 접속하고 표준 SQL문을 수행 처리하는 ==SQL 인터페이스 API== 이다.
DBMS(mySql, Oracle, MsSql)에 상관없이 JDBC API 를 이용해 데이터베이스 작업을 처리할수 있다. 

* 실행 과정
![[Pasted image 20221208183816.png]]


> JDBC 드라이버

자바 프로그램을 요청한 DBMS가 인식할수 있도록 프로토콜을 변환해주는 클라이언트사이드 어댑터이다. 

==Connection==
-   DB 연결 객체
-   데이터베이스로의 연결 기능을 제공하며, Statement 객체를 생성하는 기능 제공
-   SQL문을 데이터베이스에 전송하거나, 이러한 SQL문을 커밋하거나 롤백하는데 사용
-   보통 ==Connection 하나 당 트랜잭션 하나를 관리==한다.
    -   Mybatis의 SqlSession, Hibernate의 TransactionManager 등의 Close가 이루어지면 Connection을 ConnectionPool에 반납함.
자바에서 DB 에 직접 연결해서 처리하는 경우 JDBC Driver 를 로드하고 커넥션 객체를 받아와야하며 매번 사용자가 요청할때마다 드라이버를 로드하는것은 연결하고 종료를 매번해야 하기 때문에 비효율적이다. 

> connection pool 의 개념 

==WAS 가 실행되며 일정량의 connection 객체를 미리 만들어 pool 에 저장==하고
클라이언트 요청이 오면 `connection`을 빌려주고 해당 객체의 임무가 완료되면 다시 반납 받아서 pool 에 저장한다. 

container 구동 시 일정 수 connection 객체를 생성하고 애플리케이션이 DBMS 작업을 수행하면 
connection pool 에서 connection 객체를 받아와 작업을 진행하고 끝난 후 반납한다. 

=> container : 물체를 격리하는 공간. (connection pool 을 container 이라고 말하는 것 같음. )

> connection pool 동작 원리 

DataSource interface 를 통해 커넥션 풀을 관리하며 
SpringBoot 2.0 이전에는 **tomcat-jdbc**를 사용하다,  
현재 2.0이후 부터는 `HikariCP`를 기본옵션으로 채택 하고있다.

1. Thread 가 connection 요청을 하면 Connection Pool 이 각자의 방식에 따라 유효 connection 을 찾아 반환한다. 
 - Hikari CP 경우, 이전 사용 connection 존재 확인하고이를 우선적으로 반환 특징을 가진자. 
 
![[Pasted image 20221209110501.png]]

2. 가능한 connection 이 존재하지 않으면 `HandOffQueue` 를 폴링하면서 다른 Thread 가 connection 반ㄴ바하기를 기다린다. (지정한 Timeout 시간까지 대기하다가 시간 만료되면 예외 던짐.)

![[Pasted image 20221209110414.png]]

3. connection이 반납되면 connection pool 에 사용내역을 기록하고 `HandOffQueue`에 반납된 connection 을 삽입한다. 
	* Polling 하던 Thread 는 connection 을 획득하고 작업을 이어 나간다. 

![[Pasted image 20221209110814.png]]


> connection  pool 장접 및 유의 사항 

[장점]
* DB 접속 객체를 미리 만들어 연결하여 ==메모리상에 등록하기 때문에 불필요한 작업==이 사라져 접속이 빠르게 가능
* 수를 제한 할수 있에 ==과도한 접속으로 서버 자원 고갈 방지==
* 서버 환경이 바뀔 경우 쉬운 유지보수
* 재사용으로 비용을 줄일 수 있다. 

Q. 이벤트와 같이 한번에 많은 요청자가 요청을 보낼때는 수를 얼마까지 제한하는가? 
A. 
Hikari CP의 공식 문서에 의하면, 
`1 connections = ((core_count) * 2) + effective_spindle_count)` 로 정의하고 있다.

- core_count는 현재 사용하는 서버 환경에서의 CPU 개수를 의미한다.
	- `core_count * 2` 를 하는 이유는 Context Switching 및 Disk I/O와 관련이 있다.
		- Disk I/O(혹은 DRAM이 처리하는 속도)보다 CPU 속도가 월등히 빠르다.
		- 로킹되는 시간에 다른 Thread의 작업을 처리할 수 있는 여유가 생기고, 멀티 스레드 작업을 수행 가능하다. 
		-  공식에서는 계수를 2로 선정하여 Thread 개수를 지정
- effective_spindle_count는 기본적으로 DB 서버가 관리할 수 있는 동시 I/O 요청 수이다.
	- 디스크가 16개 있는 경우, 시스템은 동시에 16개의 I/O 요청을 처리할 수 있다

[유의 사항]
* 동시 접속자가 많을 경우 커넥션은 한정되어 있기때문에 커넥션이 반납될 때 까지 기달려야 한다. 
	* too many connection -> waste of memory leading to low program effiency 
* Connection 의 주체는 Thread 이므로 스레드와 함께 커넥션풀 사이즈도 고려 해야한다. 
	* Thread Pool < Connection Pool
		: Thread Pool 트랜잭션 처리 Thread 가 사용하는 Connection 외 남는 커넥션은 메모리 공간만 차지한다. 
	* Thread Pool && Connection Pool 증가
		* Thread 증가로 인한 많은 `context switching` 발생
		* Disk 경합 측면 성능 한계 발생 
			* DB는 하드 디스크 하나당 하나의 IO 처리 하므로 `블로킹` 발생 
			* ==특정 시점 부터는 Disk 병목으로 인해 성능적 증가가 미비하다==

![[disk병목.png]]

> connection pool 종류

* commons-dbcp (아파치 제공)

| 종류        | 설명                                                                                              |
| ----------- | ------------------------------------------------------------------------------------------------- |
| initialSize | BasicDataSource 생성 후 최초로 getConnection() 메서드 호출할 때 커넥션 풀에 채워 넣을 커넥션의 수 |
| maxActive   | 동시 사용 할 수 있는 최대 커넥션 수 (기본: 8)                                                     |
| maxIdle     | 커넥션 풀에 반납할때 최대로 유지 될수 있는 커넥션 개수 (기본: 8)                                  |
| minIdle     | 최소한 유지할 커넥션 개수(기본: 0)                                                                |

![[Pasted image 20221209112306.png]]


[참고]
https://junghyungil.tistory.com/129
https://steady-coding.tistory.com/564
=> connection pool 구조와 동작 방식 

**connection pool을 사용해야 하는 이유**
[https://devkly.com/db/db-connection-pool/](https://devkly.com/db/db-connection-pool/)

**JDBC 관련 튜닝**
[https://jiku90.tistory.com/14](https://jiku90.tistory.com/14)

**리소스 관리 방식 이해하기**
[https://kakaocommerce.tistory.com/45](https://kakaocommerce.tistory.com/45)

**질문 목록**
[https://aspdotnet.tistory.com/2309](https://aspdotnet.tistory.com/2309)