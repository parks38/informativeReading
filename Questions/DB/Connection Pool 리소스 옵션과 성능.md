----
[JDBC 커넥션 풀들의 리소스 관리 방식 이해하기]
https://kakaocommerce.tistory.com/45

mysql-connector 에서 MySQL 서버 통신 문제 원인
1. MySQL 서버에서 끊어버린 Idle 상태의 커넥션  
: DBC 커넥션이 Idle인 상태로 wait_timeout(default 28800초) 이상 사용되지 않을 경우 
 해당 커넥션을 close 하여 할당된 리소스를 회수
✅ 해결 방법 : 
두 커넥션 풀 모두 testWhileIdle 옵션을 true로 설정하여 idle 상태의 커넥션들에 대해서 **일정한 주기로 valid 여부를 확인 하게하여 MySQL 서버에서 인지하는 해당 커넥션의 idle 시간이 초기화**되게 할 수 있다. 즉, 주기적인 health check 기능의 부수 효과로 MySQL 서버의 wait_timeout으로 인해 커넥션이 close 되는 것을 막을 수 있다.

2. TCP socketTimeout 옵션을 Response Timout 성격으로 사용
: 요청을 처리하기 위한 query가 slow query로 실행 시간이 오래 걸렸을 경우 발생하는데
 **TCP socketTimeout 옵션을 Response Timeout 성격으로 잘 못 사용하고 있는 상황**인 경우
✅ 해결 방법: 
timeout이 발생한 커넥션은 query의 실행이 interrupted 되었다는 의미의 Error Code = 1317을 응답으로 받게 되고 그후에는 **SocketTimeoutException 때처럼 커넥션을 close 하는 것이 아니라 단순 Transaction "rollback"만을 실행하고 커넥션 풀로 반환**하여 커넥션 풀을 좀 더 효율적으로 사용할 수 있다.


[DB 커넥션풀 성능튜닝 개념]
https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=iter10&logNo=221270990625

- 참고 : [Common DBCP]  https://d2.naver.com/helloworld/5102792

| 종류        | 설명                                                                                              |
| ----------- | ------------------------------------------------------------------------------------------------- |
| initialSize | BasicDataSource 생성 후 최초로 getConnection() 메서드 호출할 때 커넥션 풀에 채워 넣을 커넥션의 수 |
| maxActive   | 동시 사용 할 수 있는 최대 커넥션 수 (기본: 8)                                                     |
| maxIdle     | 커넥션 풀에 반납할때 최대로 유지 될수 있는 커넥션 개수 (기본: 8)                                  |
| minIdle     | 최소한 유지할 커넥션 개수(기본: 0)                                                                |

기본 규칙
* maxActive >= intialSize
* maxIdle >= middle
* maxActive == maxIdle

* 참고 : [DB connection pool & JDBC] https://technet.tmaxsoft.com/upload/download/online/jeus/pver-20150722-000001/server/chapter_datasource.html#sect_DB_Connection_Pool_Config

![[Pasted image 20221209114001.png]]
![[Pasted image 20221209114010.png]]

[서비스를 얼마나 많은 사용자가 사용 가능한가? ]
https://hyuntaeknote.tistory.com/12

1.  Connection을 생성하는 과정은 ==3-way-handshaking을 해야 하기 때문에 시간상 비용이 비싼 작업==입니다.
2.  반복되는 Connection 생성을 줄이기 위해서 Connection Pool 방식을 활용합니다.
3.  Connection Pool이== ==적으면 Thread의 대기시간이 길어져 성능 저하==가 발생하고, Connection Pool의 크기 증가에도 Context Switching, Disk I/O 등 다양한 원인에 의해서 한계가 존재합니다.
4.  Connection Pool의 크기를 공식을 통해 추정할 수 있지만, 정확한 측정을 위해서는 성능 테스트를 진행해서 확인하는 과정이 필요합니다.
	* Thread Pool의 크기가 default인 조건에서 현재 Connection Pool의 개수는 10개가 적절하다고 판단 가능 






