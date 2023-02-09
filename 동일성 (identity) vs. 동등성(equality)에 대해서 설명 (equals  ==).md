----

==Question : 동일성과 동등성의 차이는 무엇인가?==
>  두 객체가 할당된 메모르 주소가 같으면 동일하고, 
>  두 객체의 내용이 같으면 동등하다. 
>  동일성은 == 연산자 통해 판별하고, 동등성은 equals 연산자 통해 판별하다. 

==Question : == 연산자와 eqauls 연산자의 차이는?==
>  == 는 동일성을 판별하고 EQUALS 는 두 객체의 동등성 판별을 위해 사용된다.
>  equals 는 재정의 하지 않으면 내부적으로는 == 연산자와 같은 로직을 수행하므로 객체의 특성에 맞게 재정의 해야 동등성의 기능을 수행한다. 

##### > 동일성 
: 두 개의 객체가 완전히 같은 경우 의미 (사실상 하나의 객체로 봐도 무방하며 주소값이 같아 같은 객체를 가르킨다.)
 해당 변수가 동일한지 `==` 연산자를 통해 판별하며 
 primitive 타입 객체는 주소가 없으므로 내용이 같으면 동일하다고 한다. 

![[Pasted image 20230206105704.png]]

 
##### > 동등성 
두 개의 객체가 같은 정보를 가지고 있으나 참조하고 있는 객체의 주소는 다르다. 
동일하면 동등하지만 동등하다고 동일한 것은 아니다. 
해당 변수가 동등한지 equals 통해 판별이 가능하다. 

 - Object 클래스의 equals 메소드
```java
public boolean equals(Object obj) { 
	return (this == obj); 
}
```

* 메모리 내 주소 값이 같은지 비교
	-> 메소드 오버라이드 해서 주소값이 아닌 다른 기준으로 비교한 결과 반환하도록 변경 가능하며 hashCode() 도 함께 오버라이드 해주어야 한다. 
     주소값을 찾아가서 해싱된 결과에 대한 같은 자료를 확인하는데 이때 사용 되는 데이터가 hashCode 이기 때문에 객체의 동등성을 위해 equals 오버라이드 하는 경우, hashCode도 함께 오버라이드 해주어야 한다. 


```java
public class Example1 {

    public static class ObjectTest {
        private String item1;

        public ObjectTest(String item1) {
            this.item1 = item1;
        }

        public String getItem1 () {

            return this.item1;
        }

        public void setItem1(String item1) {
            this.item1 = item1;
        }
        /**
         *    // Object 클래스의 original equals 
         *     public boolean equals(Object obj) {
         *         return (this == obj);
         *     }
         */
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            ObjectTest objectTest = (ObjectTest)o;
            return item1.equals(((ObjectTest) o).item1);
        }
        
	    @Override
        public int hashCode() {

            return Objects.hash(item1);
        }
    }

    public static void main (String[] args) {

        ObjectTest objectTest1 = new ObjectTest("aaaaa");
        ObjectTest objectTest2 = new ObjectTest("aaaaa");

        //1975012498  1808253012
        System.out.println(System.identityHashCode(objectTest1) + "  " 
			        + System.identityHashCode(objectTest2));
        // equals
        System.out.println(objectTest1.equals(objectTest2)); //false  // @Overrid true
        System.out.println(objectTest1.getItem1().equals(objectTest2.getItem1())); //true

        // ==
        System.out.println(objectTest1 == objectTest2); //false
        System.out.println(objectTest1.getItem1() == objectTest2.getItem1()); //true

        String ans = "aaaaa";
        // 589431969  589431969
        System.out.println(System.identityHashCode(ans) + "  " 
			        + System.identityHashCode(objectTest1.getItem1()));
        System.out.println(ans.equals(objectTest1.getItem1())); // true
        System.out.println(ans == objectTest1.getItem1()); // true
    }
}
```
[ 참고] 

https://steady-coding.tistory.com/534
https://creampuffy.tistory.com/140