-----
###### ✅ Interface vs. Abstract

> Abstract Class 

추상클래스도 살펴보면 상속을 통해서 구현되는 구체 클래스가 추상 메소드를 구현하는 것을 강제하는데, 인터페이스와 달리 생성자, 필드, 일반 메소드도 포함할 수 있습니다.

* **상속을 통해서 자손 클래스에서 완성하도록 유**
* 미완성 설계도 : 상속을 위한 클래스이므로 따로 객체를 생성 할 수 없음 
```java
abstract class 클래스이름 {
    public abstract void 메서드이름();
}
```

![[Pasted image 20230320184118.png]]


| 구분 | 인터페이스                                                            | 추상 클래스                 |
| ---- | --------------------------------------------------------------------- | --------------------------- |
| 용도 | 껍대기 함수의 구현 강제하는데 사용 <br> - 구현은 다르지만 동일한 동작 보장 | 클래스를 상속받아 기능 확장 |
| 상속 | 다중 상속 X                                                           | 다중상속                    |
|      | 내부 구현은 알 필요 없이 설계도                                       | 부모 클래스에서 정의한 내용을 자식 클래스에서 구현하고 확장<br> 다른 가능 확장및 추가 가능                            |


![[Pasted image 20230320184300.png]]




Spring AOP는 인터페이스를 활용하는 이유는 다음과 같습니다:

-   Spring AOP는 기본적으로 J2SE의 동적 프록시를 사용하여 AOP 프록시를 생성합니다. 동적 프록시는 인터페이스만 프록시할 수 있으므로 타깃 객체가 인터페이스를 구현해야 합니다[1](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/aop.html).
-   Spring AOP는 CGLIB 프록시도 사용할 수 있습니다. CGLIB 프록시는 클래스도 프록시할 수 있으므로 타깃 객체가 인터페이스를 구현하지 않아도 됩니다. 하지만 CGLIB 프록시는 성능상의 단점이 있고 final 메소드나 클래스에 적용할 수 없습니다[2](https://stackoverflow.com/questions/26158913/spring-aop-for-interface-and-for-annotated-methods-within-it)[1](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/aop.html).
-   Spring AOP는 인터페이스 기반의 설계와 개발을 권장하고 있으며, 인터페이스를 사용하면 코드의 결합도가 낮아지고 유연성이 높아집니다[3](https://goldengiant.tistory.com/151).

Spring AOP에서 인터페이스를 활용하는 방법은 다음과 같습니다:

-   타깃 객체가 하나 이상의 인터페이스를 구현하도록 합니다.
-   Aspect 클래스에서 @Aspect 어노테이션을 선언하고 @Pointcut 어노테이션으로 포인트컷을 정의합니다. 포인트컷 표현식에서 타깃 객체의 인터페이스나 메소드에 매칭되도록 작성합니다.
-   Aspect 클래스에서 @Before, @After, @Around 등의 어노테이션으로 어드바이스를 선언하고 포인트컷을 참조합니다.

Spring AOP에서 인터페이스를 활용하는 예제는 다음과 같습니다:

```java
// Assume that ServiceInterface is an interface implemented by the target object ServiceClass
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {

    // Define a pointcut that matches any method in ServiceInterface
    @Pointcut("execution(* com.example.ServiceInterface.*(..))")
    public void serviceMethod() {}

    // Define an advice that runs before any service method
    @Before("serviceMethod()")
    public void logBefore() {
        System.out.println("Logging before service method");
    }
}
```



Chat GPT 답변 : 

인터페이스(Interface)와 추상 클래스(Abstract Class)는 둘 다 추상화된 클래스이며, 다형성을 구현하는 방식이나 메서드 선언 방식 등에서 차이점이 있습니다.

1.  역할

-   인터페이스: 구현 객체가 어떤 메서드를 제공해야 하는지 명시하는 역할
-   추상 클래스: 공통적인 특징을 가진 객체들의 추상화된 개념을 나타내는 역할

2.  선언 방식

-   인터페이스: 모든 메서드는 추상 메서드(abstract method)로 선언되며, 상수(constant)와 디폴트 메서드(default method)를 가질 수 있음
-   추상 클래스: 추상 메서드를 포함할 수 있으며, 일반 메서드도 포함할 수 있음

3.  상속

-   인터페이스: 다중 상속을 지원하며, 인터페이스끼리만 상속 가능
-   추상 클래스: 단일 상속만을 지원하며, 클래스끼리 상속 가능

4.  인스턴스화

-   인터페이스: 인스턴스화가 불가능하며, 구현 클래스에서 메서드를 구현하여 사용
-   추상 클래스: 추상 클래스 자체로는 인스턴스화가 불가능하지만, 구현 클래스에서 상속받아 구현하여 인스턴스화 가능

5.  사용 시점

-   인터페이스: 구현 객체의 동작을 보장하기 위한 명세(specification)를 제공하는 경우 사용
-   추상 클래스: 여러 개의 클래스에서 공통적인 특징을 뽑아내 추상화된 개념을 나타내는 경우 사용

따라서, 인터페이스와 추상 클래스는 목적과 사용 방식에 따라 선택하여 사용해야 합니다. 인터페이스는 구현 객체의 동작을 보장하고, 다중 상속을 지원하기 때문에 유연한 설계가 가능합니다. 반면 추상 클래스는 여러 개의 클래스에서 공통적인 특징을 뽑아내 추상화된 개념을 나타내는 경우 사용됩니다.