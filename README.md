# JVM 밑바닥까치 파헤치기
<img src="https://www.boardinfinity.com/blog/content/images/2022/11/Your-paragraph-text--57-.jpg" alt="jvm">

# Chapter2
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

## Method Area OutOfMemoryError
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

# Chapter 3
## JVM 은 참조 카운팅을 사용하지 않는다. 
    참조 카운팅은 순환참조 객체 해제를 해결하지 못한다.
## 추적 GC 라는 도달 가능성 분석 알고리즘을 사용한다. 
    GC 루트라 하는 객체들을 시작 노드 집합으로 사용한다. 루트 노드 열거 시 Pause 를 피할 수 없다.
  - 가상 머신 스택 참조하는 객체 (현재 실행중인 메서드에서 쓰는 매개 변수, 지역 변수, 임시 변수 등)
  - 메서드 영역에서 클래스가 정적 필드로 참조하는 객체: 문자열 테이블 안의 참조
  - 네이티브 메서드 스택에서 JNI 가 참조하는 객체
  - JVM 내부에서 쓰이는 참조: 기본 데이터 타입에 해당하는 Class 객체, 일부 상주 예외 객체(NPE, OutOfMemoryError 등), 시스템 클래스 로더
  - 동기화 락(synchronized 키워드)으로 잠겨 있는 모든 객체
  - JVM 내부 상황을 반영하는 JMXBean
## 참조 개념
  - 강한 참조(strong reference): new Object() 와 같이 전통적인 코드 참조로 GC 대상이 되지 않는다.
  - 부드러운 참조(soft reference): 유용하지만 필수는 아닌 객체를 표현. 메모리 오버플로가 나기 직전에 두 번째 회수를 위한 회수 목록에 추가 대상.
  - 약한 참조(weak reference): 부드러운 참조와 비슷하지만 연결 강도가 더 약함. 다음번 GC 까지만 생존한다. 메모리가 넉넉해도 모두 회수된다.
  - 유령 참조(phantom reference): 가장 약한 참조
## 메서드 영역 회수
  - 사용 되지 않는 "상수"와 "클래스 언로딩"
  - -Xnoclassgc -verbose:class, -Xlog:class+load=info, -Xlog:class+unload=info
  - 리플렉션, 동적 프락시, CGLIB 같은 바이트코드 프레임워크를 많이 사용하거나, JSP를 동적으로 생성하고 클래스 로더를 자주 사용화하는 OSGi 환경에서는 타입 언로딩을 지원해야 한다.
## 세대 단위 컬렉션 이론
  - 약한 세대 가설(weak generational hypothesis): 대다수 객체는 단명한다.
  - 강한 세대 가설(strong generational hypothesis): GC 과정에서 살아남은 횟수가 늘어날수록 더 오래 살 가능성이 커진다.
  - 세대 간 참조 가설(intergenerational reference hypothesis): 세대 간 참조의 개수는 같은 세대 안에서의 참조보다 훨씬 적다.
  - 자바 힙을 몇 개의 영역으로 나눈다.
    - 신세대
    - 구세대
  - 나이에 따라 영역에 할당한다.
  - 수집 시간와 메모리 공간 효율을 높이기 위함.
  - 부분 GC: 자바 힙 일부만 대상
    - 마이너(신세대): 신세대만
    - 메이저(구세대): 구세대만(CMS 만)
    - 혼합: 신세대 전체와 구세대 일부를 대상 (G1 만)
  - 전체 GC: 자바 힙 전체와 메서드 영역까지 모두를 대상
  - 각 영역에 담긴 객체들의 생존 특성에 따라 아래 방식을 구분해 적용 
    - mark-sweep
      - 기본적이지만, 객체가 많을 수록 실행 효율이 떨어진다. 그리고 메모리 파편화가 심하다.
    - mark-copy (서바이버 공간)
      - 가용 메모리를 같은 크기의 블록 2개로 나누어 살아남은 객체만 이동시키고 기존 블록을 청소하는 방법.
      - 메모리를 차곡차곡 쌓아서 파편화를 방지
      - 메모리를 낭비가 심하다.
      - 많이 살아남을 수록 효율이 안좋다.
    - mark-compact
      - 메모리 파편화를 방지하기 위해서 한쪽 끝으로 모은 다음, 나머지 공간을 한꺼번에 비운다.
      - 메모리 이동은 양날의 검
        - 객체 이동은 회수 작업이 복잡 (stop the world 관점)
        - 객체 이동이 없으면 할당 작업이 복잡 (application throughput 관점)
## 기억 집합
    세대 간의 참조 객체를 기록하여 GC 루트 스캔 범위를 줄이기 위함
  - 카드 정밀도: 레코드 하나(카드)가 메모리 블록 하나에 매핑. 특정 레코드가 마킹되어 있다면, 해당 블록에 세대 간 참조를 지닌 객체가 존재.
  - 카드 테이블과 기억 집합의 관계는 자바에서 HashMap 과 Map 정도로 이해
## 쓰기 장벽
    카드 테이블 관리 기법으로 AOP 와 유사하다. G1 이전에는 사후 쓰기 장벽만을 사용함.
## 동시 접근 가능성 분석
    도달 가능성 분석 알고리즘을 사용하기 때문에, 일관성이 보장되는 스냅샷 상태에서 전체 과정을 진행해야 한다.
    루트 노드 열거 단계에서 GC 루트는 전체 자바 힙에 존재하는 모든 객체 보다는 수가 적다.
    OopMap 같은 다양한 최적화 기법 덕에 스레드가 멈춰 있는 시간은 매우 짧고 상대적으로 일정하다.
    일시 정지 시간은 힙크기에 비례한다.
    일시 정지 시간을 줄이기 위해서 삼색 표시(tri-color marking) 기법을 활용한다.
- 흰색: GC 가 방문한 적 없는 객체. 처음은 모두 흰색이다. 분석을 마친 후에도 흰색은 도달 불가능함을 의미.
- 검은색: GC 방문한 적 있는 객체. 생존을 뜻한다. 다른 객체에서 검은 객체를 가리키는 참조가 있다면 다시 스캔하지 않아도 된다. 검은 객체가 흰 객체를 곧바로 가리키는 건 불가능하다. 회색 객체를 거쳐 가리킬 수는 있다.
- 회색: GC 가 방문한 적 있으나 이 객체를 가리키는 참조 중 스캔을 완료하지 않는 참조가 존재.

## Select Collector
- 최대 100MB 정도 작은 데이터 -> 시리얼 컬렉터
- 단일 프로세서만 이용하고 일시 정지 시간 관련 제약이 없다면 -> 시리얼 컬렉터
- 최대 성능이 중요하고, 지연 시간 관련 제약이 없거나 1초 이상의 지연 시간도 허용 -> 기본 컬렉터 or 패럴렐 컬렉터
- 응답 시간 > 처리량 중요도, 일시 정지가 짧아야 한다면 -> G1
- 응답 시간이 매우 중요하면 -> 세대 구분 ZGC