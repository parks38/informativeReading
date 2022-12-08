----

connection pool 을 이용하지 않으면 
DB 서버 접속을 위한 과정에 HTTP 요청이 있을때마다 DB 서버에 지속적으로 접근하는 반복적인 작업이 필요하다. 
==DB 서버에 최초 연결하여 connection 객체를 생성하는 작업은 큰 성능 저하==를 야기하고 
이 문제를 사전 방지하기 위해 사용하는 것이` connection pool` 이다. 

> what is connection pool 


[참고]
https://junghyungil.tistory.com/129