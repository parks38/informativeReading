----
#### | Exception 종류 
* `FileNotFoundException` : 존재하지 않는 파일을 열러고 시도할 경우 
* `ArithmeticeException` : 
* `ArrayIndexOutOfBoundException`
* `IOException`
* `SQL Exception`
* `ClassNotFoundException`

#### | 예외 처리하기 
```java
try {
	// statement(s) that might cause exception
} catch (예외1) {
	// statement(s) that handle an exception
    // examples, closing a connection, closing
    // file, exiting the process after writing
    // details to a log file.
} catch (예외2) {

} finally {
  // 예외가 발생하면 프로그램 중지되거나 예외 처리에 대한 catch 구문 실행하지만
  // 어떤 예외가 발생하더라도 반드시 실행되야 하는 부분이 들어간다. 
}
```

- ==throw==: The throw keyword is used to transfer control from the try block to the catch block. 

- ==throws==: The throws keyword is used for exception handling without try & catch block. It specifies the exceptions that a method can throw to the caller and does not handle itself.

