# JVM 밑바닥까치 파헤치기
## Heap OutOfMemoryError
  - 자바 힙은 객체 인스턴스를 저장하는 공간. 무한 루프를 통해서 끝없이 객체를 생성하여 Heap 메모리 증가 
  - JVM option 을 사용하여 메모리 동적 증가를 제한하고, oom 시 heap 제공하도록 설정
    - -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
## StackOverFlowError
  - 스레드가 요구하는 스택 깊이가 vm 이 허용하는 최대 깊이보다 크면 발생
  - vm 은 스택 메모리를 동적으로 확장하는 기능을 지원하나, 가용 메모리가 부족해 스택을 더 확장할 수 없다면 OutOfMemoryError 던진다.
  - -Xss180k 를 통해 스택 메모리 용량을 줄여 StackOverFlowError 를 발생
  - 지역 변수를 많이 선언하여 메서드 프레임의 지역 변수 테이블 크기를 키워 StackOverFlowError 를 발생
