package com.ndgndg91.chapter3;

/**
 * VM Args : -Xlog:gc*
 */
public class ReferenceCountingGC {
    public Object instance = null;
    private static final int _1MB = 1024 * 1024;
    // 메모리를 많이 차지하여 GC 로그에서 회수 여부를 명확히 알아볼 수 있게 한다.
    private byte[] bigSize = new byte[2 * _1MB];

    public static void testGC() {
        // 두 객체 생성
        var objA = new ReferenceCountingGC();
        var objB = new ReferenceCountingGC();
        // 내부 필드로 서로를 참조
        objA.instance = objB;
        objB.instance = objA;
        // 참조 해제
        objA = null;
        objB = null;

        // GC 가 수행된다면 objA objB 회수가 될까?
        System.gc();
    }

    /**
     * [0.012s][info][gc,init] CardTable entry size: 512
     * [0.012s][info][gc     ] Using G1
     * [0.015s][info][gc,init] Version: 21.0.1+12-LTS (release)
     * [0.015s][info][gc,init] CPUs: 12 total, 12 available
     * [0.015s][info][gc,init] Memory: 32768M
     * [0.015s][info][gc,init] Large Page Support: Disabled
     * [0.015s][info][gc,init] NUMA Support: Disabled
     * [0.015s][info][gc,init] Compressed Oops: Enabled (Zero based)
     * [0.015s][info][gc,init] Heap Region Size: 4M
     * [0.015s][info][gc,init] Heap Min Capacity: 8M
     * [0.015s][info][gc,init] Heap Initial Capacity: 512M
     * [0.015s][info][gc,init] Heap Max Capacity: 8G
     * [0.015s][info][gc,init] Pre-touch: Disabled
     * [0.015s][info][gc,init] Parallel Workers: 10
     * [0.015s][info][gc,init] Concurrent Workers: 3
     * [0.015s][info][gc,init] Concurrent Refinement Workers: 10
     * [0.015s][info][gc,init] Periodic GC: Disabled
     * [0.080s][info][gc,metaspace] CDS archive(s) mapped at: [0x0000000129000000-0x0000000129ca1000-0x0000000129ca1000), size 13242368, SharedBaseAddress: 0x0000000129000000, ArchiveRelocationMode: 1.
     * [0.080s][info][gc,metaspace] Compressed class space mapped at: 0x000000012a000000-0x000000016a000000, reserved size: 1073741824
     * [0.080s][info][gc,metaspace] Narrow klass base: 0x0000000129000000, Narrow klass shift: 0, Narrow klass range: 0x100000000
     * [0.120s][info][gc,start    ] GC(0) Pause Full (System.gc())
     * [0.120s][info][gc,task     ] GC(0) Using 4 workers of 10 for full compaction
     * [0.121s][info][gc,phases,start] GC(0) Phase 1: Mark live objects
     * [0.122s][info][gc,phases      ] GC(0) Phase 1: Mark live objects 1.157ms
     * [0.122s][info][gc,phases,start] GC(0) Phase 2: Prepare compaction
     * [0.122s][info][gc,phases      ] GC(0) Phase 2: Prepare compaction 0.427ms
     * [0.122s][info][gc,phases,start] GC(0) Phase 3: Adjust pointers
     * [0.123s][info][gc,phases      ] GC(0) Phase 3: Adjust pointers 0.802ms
     * [0.123s][info][gc,phases,start] GC(0) Phase 4: Compact heap
     * [0.124s][info][gc,phases      ] GC(0) Phase 4: Compact heap 0.648ms
     * [0.124s][info][gc,phases,start] GC(0) Phase 5: Reset Metadata
     * [0.124s][info][gc,phases      ] GC(0) Phase 5: Reset Metadata 0.167ms
     * [0.128s][info][gc,heap        ] GC(0) Eden regions: 1->0(2)
     * [0.128s][info][gc,heap        ] GC(0) Survivor regions: 0->0(0)
     * [0.128s][info][gc,heap        ] GC(0) Old regions: 1->2
     * [0.128s][info][gc,heap        ] GC(0) Humongous regions: 2->0
     * [0.129s][info][gc,metaspace   ] GC(0) Metaspace: 76K(320K)->76K(320K) NonClass: 72K(192K)->72K(192K) Class: 3K(128K)->3K(128K)
     * [0.129s][info][gc             ] GC(0) Pause Full (System.gc()) 10M->1M(28M) 8.488ms
     * [0.129s][info][gc,cpu         ] GC(0) User=0.01s Sys=0.02s Real=0.01s
     * [0.129s][info][gc,heap,exit   ] Heap
     * [0.129s][info][gc,heap,exit   ]  garbage-first heap   total 28672K, used 1186K [0x0000000600000000, 0x0000000800000000)
     * [0.129s][info][gc,heap,exit   ]   region size 4096K, 1 young (4096K), 0 survivors (0K)
     * [0.129s][info][gc,heap,exit   ]  Metaspace       used 77K, committed 320K, reserved 1114112K
     * [0.129s][info][gc,heap,exit   ]   class space    used 4K, committed 128K, reserved 1048576K
     */

    public static void main(String[] args) {
        testGC();
    }
}
