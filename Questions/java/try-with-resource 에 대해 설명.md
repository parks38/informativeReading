----
: `try` 에서 선언된 객체들에 대해 종료될때 자동으로 자원을 해제해주는 기능 이다. 
`AutoCloaeable` 을 구현하면 try 구문이 종료될때 객체의 `close()` 메소드를 호출한다. 


#### | ## try-catch-finally vs. try-with-resource 자원 해제

Java 7 이전에는 try-catch-finally 로 자원을 해제하였고 그 코드 양은 많아 지저분 했다. 

```java
public static void main(String args[]) throws IOException {
    FileInputStream is = null;
    BufferedInputStream bis = null;
    try {
        is = new FileInputStream("file.txt");
        bis = new BufferedInputStream(is);
        int data = -1;
        while((data = bis.read()) != -1){
            System.out.print((char) data);
        }
    } finally {
        // close resources
        if (is != null) is.close();
        if (bis != null) bis.close();
    }
}
```

==try== 에서 `InputStream` 객체를 생성하고 ==finally== 에서 close 를 해준다. 
Exception 이 발생하는 경우 모든 코드가 실행되지 않을 수 있기에 ==finally== 에 close 를 해준다. 

Java 7 이후로 부터는 try-with-resources 구문을 지원하고` 자원을 쉽게 해제`할 수 있다. 

```java
public static void main(String args[]) {
    try (
        FileInputStream is = new FileInputStream("file.txt");
        BufferedInputStream bis = new BufferedInputStream(is)
    ) {
        int data = -1;
        while ((data = bis.read()) != -1) {
            System.out.print((char) data);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

==try== 안에 InputStream 객체 선언 및 할당을 하고 ==try==문을 벗어나면
==close()== 메서드를  호츨하기 때문에 `finally 에서 명시적으로 자원을 해제해줄 필요가 없다.`

`try-with-resource` 에서 자동으로 close 가 호출되는 것은 `AutoClosable` 구현한 객체에만 해당 된다. 
장점은 코드가 짧고 간결하며 유지보수가 쉬워진다는 것이다. 
명시적으로 close 호출 중에 if/ try-catch 를 많이 사용하여 실수로 close 를 까먹는 경우도 있기 때문에
자잘한 버그들이 발생할 가능성이 적어진다. 

#### | Autoclosable

```java
package java.lang;

public interface AutoCloseable {
    void close() throws Exception;
}
```

`try-with-resource` 가 모든 객체를 close() 해주는것은 아니고 `AutoCloseable`이 구현된 객체만 close() 를 호출한다. 

왜 InputStream 에는 적용이 되는가? 

```txt
java.lang.Object
  java.io.InputStream
    java.io.FilterInputStream
      java.io.BufferedInputStream
```

[InputStream](https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html)은 AutoCloseable를 상속받은 Closeable을 구현하였다. 

```java
public abstract class InputStream extends Object implements Closeable {
  ....
}

public interface Closeable extends AutoCloseable {
    void close() throws IOException;
}
```

> AutoCloseable 구현제 만들기 

* implement AutoClosable class 
```java
public static void main(String args[]) {
    try (CustomResource cr = new CustomResource()) {
        cr.doSomething();
    } catch (Exception e) {
    }
}

private static class CustomResource implements AutoCloseable {
    public void doSomething() {
        System.out.println("Do something...");
    }

    @Override
    public void close() throws Exception {
        System.out.println("CustomResource.close() is called");
    }
}
```

