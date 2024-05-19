package com.ndgndg91.chapter3;

public class MemoryAllocation {
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
//        allocationToEden();
//        allocateImmediatelyTenured();
        tenuringThreshold();
    }

    /**
     * VM Args: -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc*
     * heap 크기 20MB 제한
     * 10M 신세대, 10M 구세대
     * 에덴과 생존자 공간 8:1 로 eden 8MB from survivor 1MB to survivor 1MB
     * ------------------------------------------------------------------------
     * 대부분의 경우 객체는 신세대 에덴에 할당된다. 에덴 공간이 부족해지면 minor gc 를 시작한다.
     * 2MB 3개, 4M 1개 생성
     * (2 * 3) + 4 = 6MB + 4MB = 10MB
     * 4MB 할당 실패
     * 구세대로 2MB 3개 이동 후, 4MB 에덴에 할당
     * ------------------------------------------------------------------------
     * [0.094s][info][gc,start    ] GC(0) Pause Young (Allocation Failure)
     * [0.098s][info][gc,heap     ] GC(0) DefNew: 6635K(9216K)->69K(9216K) Eden: 6635K(8192K)->0K(8192K) From: 0K(1024K)->69K(1024K)
     * [0.098s][info][gc,heap     ] GC(0) Tenured: 1036K(10240K)->7180K(10240K)
     * [0.098s][info][gc,metaspace] GC(0) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.098s][info][gc          ] GC(0) Pause Young (Allocation Failure) 7M->7M(19M) 4.206ms
     * [0.098s][info][gc,cpu      ] GC(0) User=0.01s Sys=0.00s Real=0.00s
     *
     * [0.099s][info][gc,heap,exit] Heap
     * [0.099s][info][gc,heap,exit]  def new generation   total 9216K, used 4247K [0x00000007fec00000, 0x00000007ff600000, 0x00000007ff600000)
     * [0.099s][info][gc,heap,exit]   eden space 8192K,  51% used [0x00000007fec00000, 0x00000007ff014830, 0x00000007ff400000)
     * [0.099s][info][gc,heap,exit]   from space 1024K,   6% used [0x00000007ff500000, 0x00000007ff511660, 0x00000007ff600000)
     * [0.099s][info][gc,heap,exit]   to   space 1024K,   0% used [0x00000007ff400000, 0x00000007ff400000, 0x00000007ff500000)
     * [0.099s][info][gc,heap,exit]  tenured generation   total 10240K, used 7180K [0x00000007ff600000, 0x0000000800000000, 0x0000000800000000)
     * [0.099s][info][gc,heap,exit]    the space 10240K,  70% used [0x00000007ff600000, 0x00000007ffd031c8, 0x00000007ffd03200, 0x0000000800000000)
     * [0.099s][info][gc,heap,exit]  Metaspace       used 76K, committed 320K, reserved 1114112K
     * [0.099s][info][gc,heap,exit]   class space    used 4K, committed 128K, reserved 1048576K
     */
    public static void allocationToEden() {
        byte[] alloc1, alloc2, alloc3, alloc4;
        alloc1 = new byte[2 * _1MB];
        alloc2 = new byte[2 * _1MB];
        alloc3 = new byte[2 * _1MB];
        alloc4 = new byte[4 * _1MB]; // minor gc 발생
    }

    /**
     * 커다란 '연속된' 메모리 공간을 필요로 하는 객체는 큰 객체로 매운 긴 문자열 혹은 원소가 많은 배열이다.
     * 여유 공간이 있음에도 연속된 공간을 확보하기 위해 수많은 다른 객체를 옮겨야 하므로 심각한 메모리 복사 오버헤드를 동반한다.
     * 이 때 -XX:PretenureSizeThreshold 를 설정하여 큰 객체를 바로 구세대에 할당하여 세대간 대규모 복사를 줄인다.
     * ------------------------------------------------------------------------
     * VM Args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc* -XX:PretenureSizeThreshold=3M
     *
     * [0.089s][info][gc,heap,exit] Heap
     * [0.089s][info][gc,heap,exit]  def new generation   total 9216K, used 655K [0x00000007fec00000, 0x00000007ff600000, 0x00000007ff600000)
     * [0.089s][info][gc,heap,exit]   eden space 8192K,   8% used [0x00000007fec00000, 0x00000007feca3e60, 0x00000007ff400000)
     * [0.089s][info][gc,heap,exit]   from space 1024K,   0% used [0x00000007ff400000, 0x00000007ff400000, 0x00000007ff500000)
     * [0.089s][info][gc,heap,exit]   to   space 1024K,   0% used [0x00000007ff500000, 0x00000007ff500000, 0x00000007ff600000)
     * [0.089s][info][gc,heap,exit]  tenured generation   total 10240K, used 5132K [0x00000007ff600000, 0x0000000800000000, 0x0000000800000000)
     * [0.090s][info][gc,heap,exit]    the space 10240K,  50% used [0x00000007ff600000, 0x00000007ffb031a8, 0x00000007ffb03200, 0x0000000800000000)
     * [0.090s][info][gc,heap,exit]  Metaspace       used 77K, committed 320K, reserved 1114112K
     * [0.090s][info][gc,heap,exit]   class space    used 4K, committed 128K, reserved 1048576K
     */
    public static void allocateImmediatelyTenured() {
        byte[] alloc;
        alloc = new byte[4 * _1MB];
    }

    /**
     * VM Args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc* -Xlog:gc+age=trace -XX:MaxTenuringThreshold=1
     * 또는
     * VM Args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc* -Xlog:gc+age=trace -XX:MaxTenuringThreshold=15 -XX:TargetSurvivorRatio=80
     * 객체 헤더에 세대 나이 카운터를 둔다. 에덴에서 보통 태어나고, 나이는 0이다. minor GC 에서 살아 남은 객체는 생존자 공간으로 이동하고 나이는 1 증가한다.
     * 생존자 공간에서 minor GC 를 겪을 때 마다 1씩 증가한다. 특정 나이가 되면 구세대로 승격된다. 기본값은 컬렉터 종류와 JDK 버전에 따라 다르다.
     * -XX:MaxTenuringThreshold 값을 통해 확인.
     *
     * [0.105s][info][gc,start    ] GC(0) Pause Young (Allocation Failure)
     * [0.108s][debug][gc,age      ] GC(0) Desired survivor size 524288 bytes, new threshold 1 (max threshold 1)
     * [0.108s][trace][gc,age      ] GC(0) Age table with threshold 1 (max threshold 1)
     * [0.108s][trace][gc,age      ] GC(0) - age   1:     202672 bytes,     202672 total
     * [0.108s][info ][gc,heap     ] GC(0) DefNew: 4715K(9216K)->197K(9216K) Eden: 4715K(8192K)->0K(8192K) From: 0K(1024K)->197K(1024K)
     * [0.108s][info ][gc,heap     ] GC(0) Tenured: 1036K(10240K)->5132K(10240K)
     * [0.108s][info ][gc,metaspace] GC(0) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.108s][info ][gc          ] GC(0) Pause Young (Allocation Failure) 5M->5M(19M) 3.237ms
     * [0.108s][info ][gc,cpu      ] GC(0) User=0.00s Sys=0.00s Real=0.00s
     * [0.108s][info ][gc,start    ] GC(1) Pause Young (Allocation Failure)
     * [0.109s][debug][gc,age      ] GC(1) Desired survivor size 524288 bytes, new threshold 1 (max threshold 1)
     * [0.109s][trace][gc,age      ] GC(1) Age table with threshold 1 (max threshold 1)
     * [0.109s][info ][gc,heap     ] GC(1) DefNew: 4293K(9216K)->0K(9216K) Eden: 4096K(8192K)->0K(8192K) From: 197K(1024K)->0K(1024K)
     * [0.109s][info ][gc,heap     ] GC(1) Tenured: 5132K(10240K)->5330K(10240K)
     * [0.109s][info ][gc,metaspace] GC(1) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.109s][info ][gc          ] GC(1) Pause Young (Allocation Failure) 9M->5M(19M) 0.387ms
     * [0.109s][info ][gc,cpu      ] GC(1) User=0.00s Sys=0.00s Real=0.00s
     * [0.109s][info ][gc,heap,exit] Heap
     * [0.109s][info ][gc,heap,exit]  def new generation   total 9216K, used 4178K [0x00000007fec00000, 0x00000007ff600000, 0x00000007ff600000)
     * [0.109s][info ][gc,heap,exit]   eden space 8192K,  51% used [0x00000007fec00000, 0x00000007ff014830, 0x00000007ff400000)
     * [0.109s][info ][gc,heap,exit]   from space 1024K,   0% used [0x00000007ff400000, 0x00000007ff400000, 0x00000007ff500000)
     * [0.109s][info ][gc,heap,exit]   to   space 1024K,   0% used [0x00000007ff500000, 0x00000007ff500000, 0x00000007ff600000)
     * [0.109s][info ][gc,heap,exit]  tenured generation   total 10240K, used 5330K [0x00000007ff600000, 0x0000000800000000, 0x0000000800000000)
     * [0.109s][info ][gc,heap,exit]    the space 10240K,  52% used [0x00000007ff600000, 0x00000007ffb34958, 0x00000007ffb34a00, 0x0000000800000000)
     * [0.109s][info ][gc,heap,exit]  Metaspace       used 77K, committed 320K, reserved 1114112K
     * [0.109s][info ][gc,heap,exit]   class space    used 4K, committed 128K, reserved 1048576K
     *
     *
     * 0.078s][info][gc,start    ] GC(0) Pause Young (Allocation Failure)
     * [0.081s][debug][gc,age      ] GC(0) Desired survivor size 838856 bytes, new threshold 15 (max threshold 15)
     * [0.081s][trace][gc,age      ] GC(0) Age table with threshold 15 (max threshold 15)
     * [0.081s][trace][gc,age      ] GC(0) - age   1:     202672 bytes,     202672 total
     * [0.081s][info ][gc,heap     ] GC(0) DefNew: 4715K(9216K)->197K(9216K) Eden: 4715K(8192K)->0K(8192K) From: 0K(1024K)->197K(1024K)
     * [0.081s][info ][gc,heap     ] GC(0) Tenured: 1036K(10240K)->5132K(10240K)
     * [0.081s][info ][gc,metaspace] GC(0) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.081s][info ][gc          ] GC(0) Pause Young (Allocation Failure) 5M->5M(19M) 2.933ms
     * [0.081s][info ][gc,cpu      ] GC(0) User=0.00s Sys=0.00s Real=0.00s
     * [0.081s][info ][gc,start    ] GC(1) Pause Young (Allocation Failure)
     * [0.082s][debug][gc,age      ] GC(1) Desired survivor size 838856 bytes, new threshold 15 (max threshold 15)
     * [0.082s][trace][gc,age      ] GC(1) Age table with threshold 15 (max threshold 15)
     * [0.082s][trace][gc,age      ] GC(1) - age   2:     202672 bytes,     202672 total
     * [0.082s][info ][gc,heap     ] GC(1) DefNew: 4293K(9216K)->197K(9216K) Eden: 4096K(8192K)->0K(8192K) From: 197K(1024K)->197K(1024K)
     * [0.082s][info ][gc,heap     ] GC(1) Tenured: 5132K(10240K)->5132K(10240K)
     * [0.082s][info ][gc,metaspace] GC(1) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.082s][info ][gc          ] GC(1) Pause Young (Allocation Failure) 9M->5M(19M) 0.342ms
     * [0.082s][info ][gc,cpu      ] GC(1) User=0.00s Sys=0.00s Real=0.00s
     * [0.082s][info ][gc,heap,exit] Heap
     * [0.082s][info ][gc,heap,exit]  def new generation   total 9216K, used 4375K [0x00000007fec00000, 0x00000007ff600000, 0x00000007ff600000)
     * [0.082s][info ][gc,heap,exit]   eden space 8192K,  51% used [0x00000007fec00000, 0x00000007ff014830, 0x00000007ff400000)
     * [0.082s][info ][gc,heap,exit]   from space 1024K,  19% used [0x00000007ff400000, 0x00000007ff4317b0, 0x00000007ff500000)
     * [0.082s][info ][gc,heap,exit]   to   space 1024K,   0% used [0x00000007ff500000, 0x00000007ff500000, 0x00000007ff600000)
     * [0.082s][info ][gc,heap,exit]  tenured generation   total 10240K, used 5132K [0x00000007ff600000, 0x0000000800000000, 0x0000000800000000)
     * [0.082s][info ][gc,heap,exit]    the space 10240K,  50% used [0x00000007ff600000, 0x00000007ffb031a8, 0x00000007ffb03200, 0x0000000800000000)
     * [0.082s][info ][gc,heap,exit]  Metaspace       used 77K, committed 320K, reserved 1114112K
     * [0.082s][info ][gc,heap,exit]   class space    used 4K, committed 128K, reserved 1048576K
     */
    public static void tenuringThreshold() {
        byte[] alloc1, alloc2, alloc3;
        // 구세대 이동 시기는 -XX:MaxTenuringThreshold 로 결정
        alloc1 = new  byte[_1MB / 8];
        alloc2 = new byte[4 * _1MB];
        alloc3 = new byte[4 * _1MB];
        alloc3 = null;
        alloc3 = new byte[4 * _1MB];
    }
}
