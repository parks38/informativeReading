- RuntimeException
=> 실행 중에 발생하며 시스템 환경적으로나 인풋 값이 잘못된 경우, 
	혹은 의도적으로 프로그래머가 잡아내기 위한 조건등에 부합할때 발생되게 만든다. 
  -`unchecked Exception`
	  - 예외처리 필요없음
- Object 가 필요한 상황에서 null 을 사용하려고 하는 경우 발생 
	*  Calling the instance method of a `null` object.
	-   Accessing or modifying the field of a `null` object.
	-   Taking the length of `null` as if it were an array.
	-   Accessing or modifying the slots of `null` as if it were an array.
	-   Throwing `null` as if it were a `Throwable` value.

*case*
```java
ArrayList<String> test = new ArrayList<>();
test.addAll(testRepository.findById(id));
// 여기서 문제가 repository could return a null
// and this is when the nullPointerException happens 


```

[ArrayList 비어있는지 확인하는 3가지 방법]
* .isEmpty()
* ArrayList.size() => null 
* ArrayList 가 null 인지 empty 인지 확인하는 함수
	* 위에 두가지는 비어있는지 확인할수는 있지만 ArrayList 객체가 null 일 경우에는 exception 발생 
	```java
if (list == null || list.isEmpty()) {
    // list is null or empty
}
```

만약 자주 null check /empty check 를 하는 경우에는 함수 만들어도 좋다 
```java
public static boolean isNullOrEmpty(final ArrayList list) {
        return list == null || list.isEmpty();
    }

    public static void main(String[] args) {

        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = 
	        new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
        ArrayList<String> list3 = null;

        if (isNullOrEmpty(list1)) {
            System.out.println("list1 is null or empty");
        }

        if (!isNullOrEmpty(list2)) {
            System.out.println("list2 is not null or empty");
        }

        if (isNullOrEmpty(list3)) {
            System.out.println("list3 is null or empty");
        }
    }
```

