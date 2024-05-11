# JVM 밑바닥까치 파헤치기
<img src="https://www.boardinfinity.com/blog/content/images/2022/11/Your-paragraph-text--57-.jpg" alt="jvm">

## Heap OutOfMemoryError
  - 자바 힙은 객체 인스턴스를 저장하는 공간. 무한 루프를 통해서 끝없이 객체를 생성하여 Heap 메모리 증가 
  - JVM option 을 사용하여 메모리 동적 증가를 제한하고, oom 시 heap 제공하도록 설정
    - -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError

## Stack StackOverFlowError, OutOfMemoryError
  - 스레드가 요구하는 스택 깊이가 vm 이 허용하는 최대 깊이보다 크면 발생
  - vm 은 스택 메모리를 동적으로 확장하는 기능을 지원하나, 가용 메모리가 부족해 스택을 더 확장할 수 없다면 OutOfMemoryError 던진다.
  - -Xss180k 를 통해 스택 메모리 용량을 줄여 StackOverFlowError 를 발생
  - 지역 변수를 많이 선언하여 메서드 프레임의 지역 변수 테이블 크기를 키워 StackOverFlowError 를 발생

## Runtime Constant Pool OutOfMemoryError
- JDK8 이상 버전 핫스팟 JVM 은 힙에 runtime constant pool 을 구현했다.
- JDK7 이하 버전 핫스팟 JVM 은 Method Area 에 있는 PermGen space 에 힙과를 별개의 영역으로 runtime constant pool 을 구현했다.

## Method Area
  - 타입 관련 정보 저장
    - 클래스 이름
    - 접근 제한자
    - 상수 풀
    - 필드 설명
    - 메서드 설명
  - CGLIB 와 같이 런타임에 바이트코드를 직접 조작하여 다량의 클래스를 동적으로 생성할 경우 메서드 영역의 OOM 발생할 수 있다.
  - JDK8 부터는 영구 세대가 사라지고 메타스페이스를 이용한다.
    - -XX:MaxMetaspaceSize : 메타스페이스 최대 크기 설정. 기본값 -1 로 제한이 없다. "네이티브 메모리 크기가 허용하는 만큼"
    - -Xx:MetaspaceSize: 메타스페이스 초기 크기를 바이트 단위로 지정. 해당 크기가 가득 차면 GC 가 클래스 언로딩을 시도한 다음 크기를 조정. 클래스 언로딩 후 공간이 넉넉하게 확보되면 이 값을 줄이고, 확보 하지 못했다면 적절한 값으로 증가. -XX:MaxMetaspaceSize 를 설정한 경우 해당 값을 초과할 수 없음.
    - -XX:MinMetaspaceFreeRatio: GC 후 가장 작은 메타스페이스 여유 공간의 비율을 지정. 메타스페이스 공간이 부족해 발생하는 GC 빈도를 줄일 수 있다.
