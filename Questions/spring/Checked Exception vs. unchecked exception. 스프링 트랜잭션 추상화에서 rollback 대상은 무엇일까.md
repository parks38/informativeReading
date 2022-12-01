----

#### | Exception 클래스 
* `checked Exception`
	* RuntimeException 상속하지 않은 것 
		* 명시적인 처리가 필요한 예외
		* 반드시 예외 처리 코드 작성해야 한다. (catch/ throws)
		* ex. IOException, SQLException 
* `unCheckedException` 
	* RuntimeException 상속한 것 

![[Pasted image 20221201161402.png]]

| unchecked                                                                                             | checked                                                                                   |
| ----------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| NullPointerException, ClassCastException, ArithmeticException, DateTimeException, ArrayStoreException | ClassNotFoundException, SocketException, SQLException, IOException, FileNotFoundException |
| 런타임 중                                                                              | 예외 확인: 컴파일 단계                                                                                  |
| Rollback 처리                                                                                   | Rollback 하지 않음                                                                                           |

* `throws` 발생 예외 
	* Error
		* JVM 에서 발생
		* 애플리케이션 코드에서 잡으려고 하면 안된다. 
		* ex. OurOfMemoryError, ThreadDeath 
	* Exception 과 체크 예외 
		* 개발자가 작성한 애플리케이션 코드의 작업 중 예외상황 발생 시 사용
	* RuntimeException 과 uncheck/runtime exception 
		* 명시적인 예외처리를 강제하지 않는다. 
		* catch/ throw 선언하지 않음. 
		* 프로그램의 오류가 있을때 발생하도록 의도된 것
			* NullPointerException
			* IllegalArgumentException 


#### | Spring 의 rollback 기능 
Checked Exception `Compile Exception` 는 예외 발생시 롤백처리를 하지 않는다. RuntimeException이거나 Error인 경우만 롤백을 실행하는 것을 알 수 있다. 스프링프레임워크는 EJB의 관습을 따르기 때문이다.

이외에도==Spring에서 Transaction이 Rollback이 안되는 경우== 가 있다. 첫번째는 ==Unchecked Exception으로, Transaction 어노테이션이 선언==된 메소드가 내부적으로 this.함수()를 호출시 this.함수()는 ==트랜잭션 처리를 해주는 프록시 객체가 아니라, 원본 객체의 함수()를 호출==한다. 즉, 그때문에 트랜잭션으로 감싸지지 않은 DB접근, 연산이 수행된다. 때문에 도중에 오류가 나도 Rollback이 일어나지 않는다.

```java
@Override
public boolean rollbackOn(Throwable ex) {
  return (ex instanceof RuntimeException || ex instanceof Error);
}
```

[참고]
> checked exception 에서 rollback 안되는 이유?

 Spring 의 `@Transactional` 의 기본 정책은 ==Unchecked Exception== 그리고 ==Error== 이다. 
 이유는 #EJB_관습을 따르기 때문이다. 
 
> **하지만 checked exception의 경우에도 롤백 시켜야될 상황이 있다면 어떻게 해야할까?**

`@Transactional` 속성을 보면 rollbackOn이 있다. 이 속성에 해당하는 exception을 추가해주면 된다. exception은 콤마(,)로 구분하여 여러개 추가할 수 있다. (rollbackFor인 경우도 있음.. 버전에 따라 다른듯?)

```java
@Transactional(rollbackOn = {Exception.class})
```

catch 부분에 RuntimeException을 상속받은 클래스를 throw 해주기

```java
try{
	//생략
} catch(InvalidObjectException e){
	throw new TestException(e.getMessage());
} catch(Exception e){
	throw new TestException(e.getMessage());
}
```


#### | 정리 
-   `Checked Exception` 일때는 Rollback을 하지 않는다.
-   ``@Transactional(rollbackFor=Exception.class)`` 옵션으로 모든 예외에 대해서 롤백할 수 있다.
-   `Checked Exception`을 ==try-catch문==으로 더 구체적인 `Unchecked Exception`으로 감싸주면 롤백이 가능하다.

[추가 참고 사항]
-  Transcational Propagation
-  자바의 예외처리 전략

----
[footnote]
https://ynzu-dev.tistory.com/entry/Spring-Transactional%EC%9D%B4-%EC%A0%81%EC%9A%A9%EB%90%98%EC%A7%80-%EC%95%8A%EC%9D%84-%EA%B2%BD%EC%9A%B0%EB%A1%A4%EB%B0%B1%EC%9D%B4-%EC%95%88%EB%90%98%EB%8A%94-%EC%9D%B4%EC%9C%A0
