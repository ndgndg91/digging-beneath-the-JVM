# JVM 밑바닥까치 파헤치기
- Heap OutOfMemoryError
  - JVM option 을 사용하여 메모리 동적 증가를 제한하고, oom 시 heap 제공하도록 설정
    - -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
  - 무한 루프를 통해서 끝없이 객체를 생성하여 Heap 메모리 증가