package com.kit.proto;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProtoWriter {

    private OutputStream stream;

    private HashMap<Integer, Boolean> writtenFields = new HashMap<Integer, Boolean>();

    public ProtoWriter(OutputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream can not be null");
        }

        this.stream = stream;
    }

    public void writeBytes(int fieldNumber, @NotNull byte[] value) throws IOException {
        if (value == null) {
            throw new IllegalArgumentException("Value can not be null");
        }
        if (value.length > Const.MAX_BLOCK_SIZE) {
            throw new IllegalArgumentException("Unable to write more than 1 MB");
        }
        writtenFields.put(fieldNumber, true);
        writeBytesField(fieldNumber, value);
    }

    public void writeString(int fieldNumber, @NotNull String value) throws IOException {
        if (value == null) {
            throw new IllegalArgumentException("Value can not be null");
        }
        writtenFields.put(fieldNumber, true);
        writeBytesField(fieldNumber, value.getBytes());
    }

    public void writeBool(int fieldNumber, boolean value) throws IOException {
        writeVarIntField(fieldNumber, value ? 1 : 0);
    }

    public void writeInt(int fieldNumber, int value) throws IOException {
        writeVarIntField(fieldNumber, value);
    }

    public void writeIntFixed(int fieldNumber, int value) throws IOException {
        writeVar32Fixed(fieldNumber, value);
    }

    public void writeDouble(int fieldNumber, double value) throws IOException {
        writeVar64Fixed(fieldNumber, Double.doubleToLongBits(value));
    }

    public void writeLongFixed(int fieldNumber, long value) throws IOException {
        writeVar64Fixed(fieldNumber, Double.doubleToLongBits(value));
    }

    public void writeLong(int fieldNumber, long value) throws IOException {
        writeVarIntField(fieldNumber, value);
    }

    public void writeRepeatedLong(int fieldNumber, @NotNull List<Long> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (Long l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeVarIntField(fieldNumber, l);
        }
    }

    public void writeRepeatedInt(int fieldNumber, @NotNull List<Integer> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (Integer l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeVarIntField(fieldNumber, l);
        }
    }

    public void writeRepeatedBool(int fieldNumber, @NotNull List<Boolean> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (Boolean l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeBool(fieldNumber, l);
        }
    }

    public void writeRepeatedString(int fieldNumber, @NotNull List<String> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (String l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeString(fieldNumber, l);
        }
    }

    public void writeRepeatedBytes(int fieldNumber, @NotNull List<byte[]> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (byte[] l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeBytes(fieldNumber, l);
        }
    }

    public <T extends ProtoObject> void writeRepeatedObj(int fieldNumber, @NotNull List<T> values) throws IOException {
        if (values == null) {
            throw new IllegalArgumentException("Values can not be null");
        }
        if (values.size() > Const.MAX_PROTO_REPEATED) {
            throw new IllegalArgumentException("Too many values");
        }
        writtenFields.put(fieldNumber, true);
        for (T l : values) {
            if (l == null) {
                throw new IllegalArgumentException("Value can not be null");
            }
            writeObject(fieldNumber, l);
        }
    }

    public void writeObject(int fieldNumber, @NotNull ProtoObject value) throws IOException {
        if (value == null) {
            throw new IllegalArgumentException("Value can not be null");
        }
        writtenFields.put(fieldNumber, true);
        writeTag(fieldNumber, Const.TYPE_LENGTH_DELIMITED);
        OutputStream outputStream = new OutputStream();
        ProtoWriter writer = new ProtoWriter(outputStream);
        value.serializeBody(writer);
        writeBytes(outputStream.toByteArray());
    }

    public void writeRaw(byte[] raw) throws IOException {
        if (raw == null) {
            throw new IllegalArgumentException("Raw can not be null");
        }

        stream.writeBytes(raw, 0, raw.length);
    }

    private void writeTag(int fieldNumber, int wireType) throws IOException {
        fieldNumber = (fieldNumber & 0xFFFF);
        if (fieldNumber <= 0) {
            throw new IllegalArgumentException("Field Number must greater than zero");
        }

        long tag = ((long) (fieldNumber << 3) | wireType);
        stream.writeVarInt(tag);
    }

    private void writeVarIntField(int fieldNumber, long value) throws IOException {
        writeTag(fieldNumber, Const.TYPE_VARINT);
        writeVarInt(value);
    }

    private void writeBytesField(int fieldNumber, @NotNull byte[] value) throws IOException {
        writeTag(fieldNumber, Const.TYPE_LENGTH_DELIMITED);
        writeBytes(value);
    }

    private void writeVar64Fixed(int fieldNumber, long value) throws IOException {
        writeTag(fieldNumber, Const.TYPE_64BIT);
        writeLong(value);
    }

    private void writeVar32Fixed(int fieldNumber, long value) throws IOException {
        writeTag(fieldNumber, Const.TYPE_32BIT);
        writeInt(value);
    }

    private void writeVarInt(long value) throws IOException {
        stream.writeVarInt(value & 0xFFFFFFFF);
    }

    private void writeLong(long v) throws IOException {
        stream.writeLong(v & 0xFFFFFFFF);
    }

    private void writeInt(long v) throws IOException {
        stream.writeInt((int) (v & 0xFFFF));
    }

    private void writeBytes(@NotNull byte[] data) throws IOException {
        stream.writeProtoBytes(data, 0, data.length);
    }
}