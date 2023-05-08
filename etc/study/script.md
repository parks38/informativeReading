2023년 2월 23일 목요일

오전 11:52

JVM 에 대한 간단한 설명 및 JVM 이 client request 가 controller 에 전달 되는 과정 까지를 다루어 보았습니다.

런 타임 시, .class 파일은 JVM 에 의해 실행되며 가장 먼저

[1] class loader 에 적재되게 됩니다. 클래스 로더는 Runtime Data Area 에 메모리를 올리는 역할을 하는데 [Method, Heap, Stack, PCRegister, Native Method Stack]의 데이터 영역에 배치 시킵니다. 그리고 [1] 실행 엔진으로 보내 Interpreter/ JIT 방식으로 바이트 코드를 한줄씩 실행합니다.

Runtime Data Area 에 객체는 Heap 에 그리고 Method Area 는 클래스에 대한 정보 (메소드 처리) 를 저장 합니다. Heap 과 Method Area 는 공유 자원들이고 [Stack, PC Register, Native Method Stack]들은 각각의 스레드마다 하나의 공간을 할당 받습니다.