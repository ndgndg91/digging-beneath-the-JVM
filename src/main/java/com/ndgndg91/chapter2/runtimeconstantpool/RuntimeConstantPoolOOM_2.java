package com.ndgndg91.chapter2.runtimeconstantpool;

public class RuntimeConstantPoolOOM_2 {

    /**
     * JDK6 이하에서는 false
     * JDK7 이상에서는 true
     *
     * JDK6 intern() 처음 만나는 문자열 인스턴스를 영구 세대의 문자열 상수 풀에 복사한 다음, 영구 세대에 저장한 문자열 인스턴스의 참조를 반환
     * StringBuilder 로 생성한 문자열 인스턴스 str1 은 힙에 존재.
     * 영구 세대 인스턴스 == 힙 인스턴스 => false
     *
     * JDK7 이상에서는 문자열 상수 풀 위치가 힙이라서 true
     */
    public static void main(String[] args) {
        var str1 = new StringBuilder("컴퓨터").append(" 소프트웨어").toString();
        System.out.println(str1.intern() == str1);
    }
}
