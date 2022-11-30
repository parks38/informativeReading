**✔️ @Overloading** 
: 메소드 이름은 같고 매개변수가 다른 경우 
-> 메소드 이름을 사용하면서 여러 기능을 제공한다는 의미 
- 같은 역할을 하는 메소드는 같은 메소드 이름을 가져야 한다.  

- implements Compile time Polymorphism  
 - 같은 클래스 내에서 생성   
 - 같은 메소드 이름을 가지나 매개변수는 다를수 있음  
 - compile-time 에 결정  
 - compile-error를 통해 쉽게 수정 가능   

**✔️ @Overriding**
: 메소드 이름과 매개변수 모두 같은 superclass 와 child class 에서 동일하게 구현해 주어야함. 
-> 부모 클래스에 정의 되어 있는 메소드를 자식 클래스에서 재정의

- implements Runtime Polymorphism  
 - superclass 와 subclass 관계에서 생성   
 - 동일한 시그니처 이름, 매개변수   
 - object type 에 따라 runtime  에 결정 
	 - 런타임에 확인가능하기에 에러가 생기면 프로그램에 악영향 끼침.



----
[footnote]
[www.journaldev.com/32182/overriding-vs-overloading-in-java](https://www.journaldev.com/32182/overriding-vs-overloading-in-java)

https://parks38.tistory.com/16

