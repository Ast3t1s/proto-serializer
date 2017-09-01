package com.kit.proto;

import java.io.IOException;

public final class ProtoUtils {

    public static <T extends ProtoObject> T parse(ProtoWrapper<T> creator, byte[] data) throws IOException {
        return parse(creator.createInstance(), new InputStream(data, 0, data.length));
    }

    public static <T extends ProtoObject> T parse(T res, byte[] data) throws IOException {
        return parse(res, new InputStream(data, 0, data.length));
    }

    public static <T extends ProtoObject> T parse(T res, InputStream inputStream) throws IOException {
        ProtoValues reader = new ProtoValues(ProtoParser.deserialize(inputStream));
        res.deserializeBody(reader);
        return res;
    }

    private ProtoUtils(){}
}
