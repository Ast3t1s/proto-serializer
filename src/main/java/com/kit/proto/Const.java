package com.kit.proto;

public final class Const {

    public static final int MAX_BLOCK_SIZE = 1024 * 1024;// 1 MB
    public static final int MAX_PROTO_REPEATED = 1024 * 1024;
    public static final int TYPE_VARINT = 0;
    public static final int TYPE_32BIT = 5;
    public static final int TYPE_64BIT = 1;
    public static final int TYPE_LENGTH_DELIMITED = 2;

    private Const() {
    }
}
