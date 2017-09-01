package com.kit.proto;

import java.io.IOException;

public abstract class ProtoObject {

    public ProtoObject() {}

    protected void load(byte[] data) throws IOException {
        ProtoValues values = new ProtoValues(ProtoParser.deserialize(new InputStream(data, 0, data.length)));
        deserializeBody(values);
    }

    public byte[] toByteArray() {
        OutputStream output = new OutputStream();
        ProtoWriter writer = new ProtoWriter(output);
        try {
            serializeBody(writer);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IO exception");
        }
        return output.toByteArray();
    }

    public abstract void deserializeBody(ProtoValues values) throws IOException;

    public abstract void serializeBody(ProtoWriter write) throws IOException;

}
