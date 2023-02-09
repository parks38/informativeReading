----
```java
java.lang.IllegalStateException: Failed to execute ApplicationRunner
Caused by: org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; 
bad SQL grammar [SELECT JOB_INSTANCE_ID, JOB_NAME from BATCH_JOB_INSTANCE 
				 where JOB_NAME = ? and JOB_KEY = ?]; 
				 nested exception is java.sql.SQLSyntaxErrorException: 
				 Table 'batch_practice.batch_job_instance' doesn't exist
```

**Spring Batch는 어플리케이션 코드만 작성하면 되는구나**! 라고 생각하실수 있으실텐데요.  
실제로는 그렇지 않습니다.  
Spring Batch에선 메타 데이터 테이블들이 필요합니다.

[참고]
https://jojoldu.tistory.com/325?category=902551

![[Pasted image 20230201180052.png]]

![[Pasted image 20230201180218.png]]
https://khj93.tistory.com/entry/Spring-Batch%EB%9E%80-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
