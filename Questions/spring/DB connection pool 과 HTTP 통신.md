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




[참고]
https://junghyungil.tistory.com/129
https://steady-coding.tistory.com/564
=> connection pool 구조와 동작 방식 
