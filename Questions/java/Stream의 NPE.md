----
Java Stream API 를 사용하다면 NPE 가 발생하는 경우가 있다. 

```java
Object aNull = list.stream()
					.map(e -> e.getValue())
					.findAny()
					.orElse("널");
```

이럴 경우 orElse("널") 을 설정해 두었기 때문에 
"널" 이 print 될 것이라고 예상할 것이지만 `NullPointerException` 이 발생한다. 

```
Exception in thread "main" java.lang.NullPointerException at java.util.Objects.requireNonNull(Objects.java:203) at java.util.Optional.<init>(Optional.java:96) at java.util.Optional.of(Optional.java:108)
```

----
[footnote]
https://cfdf.tistory.com/35
