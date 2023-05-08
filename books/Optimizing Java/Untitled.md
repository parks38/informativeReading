----
jvm 관려련 
https://doozi0316.tistory.com/entry/1%EC%A3%BC%EC%B0%A8-JVM%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EB%A9%B0-%EC%9E%90%EB%B0%94-%EC%BD%94%EB%93%9C%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%8B%A4%ED%96%89%ED%95%98%EB%8A%94-%EA%B2%83%EC%9D%B8%EA%B0%80

클래스 로딩 과정 
https://engkimbs.tistory.com/606
https://homoefficio.github.io/2018/10/13/Java-%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94-%ED%9B%91%EC%96%B4%EB%B3%B4%EA%B8%B0/

https://azderica.github.io/til/docs/java/optimizing-java/ch2/




![[Pasted image 20230131140233.png]]


> 자바 class loading mechanism

: Java에서 클래스가 로딩 과정은 클래스 로더(Class Loader)가 확장자가 .class 클래스 파일의 위치를 찾아 그것을 JVM위에 올려놓는 과정

![[Pasted image 20230131140030.png]]

JVM을 동작하고 각 클래스들을 로딩하기 위해 JVM을 실행했을 시 각 클래스 로더들은 자신이 호출할 수 있는 클래스들을 호출하여 JVM을 동작하고 클래스들을 JVM에 로딩하기 됩니다.

**부트스트랩 클래스 로더(Bootstrap Class Loader)**
위에 $JAVA_HOME/jre/lib/rt.jar 에서 rt.jar에 있는 JVM을 실행시키기 위한 핵심 클래스들을 로딩합니다. `jdk 파일 로더 `
* `String.class.getClassLoader()`는 그냥 `null`을 반환
	- Java 8  
	jre/lib/rt.jar 및 기타 핵심 라이브러리와 같은 JDK의 내부 클래스를 로드한다.
	
	- Java 9 이후  
	더 이상 /re.jar이 존재하지 않으며, /lib 내에 모듈화되어 포함됐다. 
	이제는 정확하게 ClassLoader 내 최상위 클래스들만 로드
**확장 클래스 로더(Extenstion Class Loader)**
$JAVA_HOME/jre/lib/ext 경로에 위치해 있는 자바의 확장 클래스들을 로딩하는 역할
* `sun.misc.Launcher` 클래스 안에 static 클래스로 구현
* `UrlClassLoader` 상속 
* 특정한 OS 나 플렛폼에 네이티브 코드를 제공하고 기본 환경을 Override 가능 
* 자바8 => `자바스크립트 런타입 내시혼`
	* ⁉️ ✅ 꼬리물기 > 자바스크립트 런타임 내시혼 이란?? 
	* Nashorn provides 2 to 10 times better performance (than Rhino), as it directly compiles the code in memory and passes the bytecode to JVM

**Application 클래스 로더(System Class Loader)**
$CLASSPATH에 설정된 경로를 탐색하여 그곳에 있는 클래스들을 로딩하는 역할
javac.exe 통해 만든 .class 파일을 시스템 로더가 로딩한다. 
* `-classpath(또는 -cp)`나 JAR 파일 안에 있는 Manifest 파일의 `Class-Path` 속성값으로 지정된 폴더에 있는 클래스를 로딩
* `sun.misc.Launcher` 클래스 안에 static 클래스로 구현되어 있으며, `URLClassLoader`를 상속

❌ 애플리케이션 로더를 시스템 클래스 로더라고 부르는 경우가 있는데
	부트 스트랩 클래스로더가 로드하는 시스템 클래스는 로드하지 않기 때문에 해당 언어는 잘못 되었다! 
	- 확장 클래스 로더의 자식인 애플리케이션 클래스 로더가 사용되는 것! 

클래스 로더들은 ==계층적 구조==를 가지도록 생성이 가능하고 
 부모 클래스 클래스 로더에서 자식 클래스 로더를 가지는 형태로 클래스 로더를 만들 수 있습니다. 
 > 		**Bootstrap <- Extention <- System <- User-defined** 
 
* 특징 
	[1] 계층적인 구조 특징 
	[2] 로딩 요청 위임 (delegate load request)
	[3] 가시성 제약 조건 (visibility constraint)
	 * 부모 로더에서 찾지 못한 클래스는 자식 로더를 이용해서 클래스를 찾지 못하지만
	    반대로 자식로더에서 찾지 못한 클래스는 부모 로더에게 위힘해서 찾을 수 있다. 
	[4] 언로드 불가 (cannot unload classes) 
	  * 클래스 로더에 의해 로딩된 클래스들은 다시 JVM 상에서 없앨 수 없다. 

> 로딩 요청 위임이란? 

![[Pasted image 20230131143405.png]]

1.  JVM의 메소드 영역에 클래스가 로드되어 있는지 확인한다. 만일 로드되어 있는 경우 해당 클래스를 사용한다.
2. `ClassLoaderRunner`는 자기 자신을 로딩한 `Application ClassLoader`에게 `Internal` 클래스 로딩을 요청  
3.  시스템 클래스 로더는 확장 클래스 로더에 요청을 위임한다.
4.  확장 클래스 로더는 부트스트랩 클래스 로더에 요청을 위임한다.
5.  부트스트랩 클래스 로더는 부트스트랩 Classpath(JDK/JRE/LIB)에 해당 클래스가 있는지 확인한다. 클래스가 존재하지 않는 경우 확장 클래스 로더에게 요청을 넘긴다.
6.  확장 클래스 로더는 확장 Classpath(JDK/JRE/LIB/EXT)에 해당 클래스가 있는지 확인한다. 클래스가 존재하지 않을 경우 시스템 클래스 로더에게 요청을 넘긴다.
7.  시스템 클래스 로더는 시스템 Classpath에 해당 클래스가 있는지 확인한다. 클래스가 존재하지 않는 경우 ClassNotFoundException을 발생시킨다.


System Loader가 클래스를 로딩할때 요청은 부모 로더들로 거슬러 올라가 
부트 스트랩 로더에 다다른 후 밑으로 로딩 요청을 수행한다. 
==클래스들이 로딩이 안 될 때 **java.lang.ClassNotFoundException** 이 발생한다==

ApplicationClassLoader 에서도 파일을 못 찾으면 User-defined Class Loader 까지 가지 않고 Exception을 던지며
클래스 파일을 부모 로더 중 하나라도 찾는데 성공하면 그 클래스 파일 정보를 자식 로더에게 넘겨준다. 

| java 8                  | java 9               | 차이점                                                                                                                                                                                                                                                                                                             |
| ----------------------- | -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Bootstrap ClassLoader   | -                    | rt.jar 등이 없어짐에 따라 로딩할 수 있는 클래스의 범위가 전반적으로 축소  <br> 따라서 parent classloader 인자로 `null`을 넘겨주며 Bootstrap ClassLoader를 parent classloader로 사용했던 코드 수정 필요할 수 있음                                                                                                   |
| Extension ClassLoader   | Platform ClassLoader | -`jre/lib/ext`, `java.ext.dirs`를 지원하지 않음  <br>- Java SE의 모든 클래스와 Java SE에는 없지만 JCP에 의해 표준화 된 모듈 내의 클래스를 볼 수 있으며, Java 8에 비해 볼 수 있는 범위가 확장됨  <br> - `URLClassLoader`가 아닌 `BuiltinClassLoader`를 상속받아 `ClassLoaders` 클래스의 내부 static 클래스로 구현됨 |
| Application ClassLoader | System ClassLoader   | - 클래스패스, 모듈패스에 있는 클래스 로딩<br> - URLClassLoader 아닌 BuiltinClassLoader 상속 받음 <br> - ClassLoaders 클래수의 내부 static 클래스로 구현                                                                                                                                                                                                                                                                                                                   |

