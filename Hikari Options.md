----
* ***connectionTimeout**
 * pool에서 커넥션을 얻어오기전까지 기다리는 최대 시간, 허용가능한 wait time을 초과하면 SQLException을 던짐
* ***maxLifetime**
 * 커넥션 풀에서 살아있을 수 있는 커넥션의 최대 수명시간.
* ***minimumIdle**
* 아무런 일을 하지않아도 적어도 이 옵션에 설정 값 size로 커넥션들을 유지해주는 설정. 최적의 성능과 응답성을 요구한다면 이 값은 설정하지 않는게 좋음. default값을 보면 이해할 수있음. (default: same as maximumPoolSize)
-   **maximumPoolSize:** 
	- pool에 유지시킬 수 있는 최대 커넥션 수. pool의 커넥션 수가 옵션 값에 도달하게 되면 idle인 상태는 존재하지 않음.(default: 10)
-   **poolName:** 
	- 이 옵션은 사용자가 pool의 이름을 지정함. 로깅이나 JMX management console에 표시되는 이름.(default: auto-generated)
-   **initializationFailTimeout:** 
	- 이 옵션은 pool에서 커넥션을 초기화할 때 성공적으로 수행할 수 없을 경우 빠르게 실패하도록 해준다. 상세 내용은 한국말보다 원문이 더 직관적이라 생각되어 다음 글을 인용함.


[참고]
https://effectivesquid.tistory.com/entry/HikariCP-%EC%84%B8%ED%8C%85%EC%8B%9C-%EC%98%B5%EC%85%98-%EC%84%A4%EB%AA%85
