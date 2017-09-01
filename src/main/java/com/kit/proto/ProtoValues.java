package com.kit.proto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProtoValues {

    private HashMap<Integer, Object> fields;

    private HashMap<Integer, Boolean> touched = new HashMap<Integer, Boolean>();

    public ProtoValues(@NotNull HashMap<Integer, Object> fields) {
        this.fields = fields;
    }

    public long optLong(int id) throws IOException {
        return getLong(id, 0);
    }

    public long getLong(int id) throws IOException {
        if (!fields.containsKey(id)) {
            throw new IOException("Unable to find field #" + id);
        }
        return getLong(id, 0);
    }

    public long getLong(int id, long defValue) throws IOException {
        if (fields.containsKey(id)) {
            touched.put(id, true);
            Object obj = fields.get(id);
            if (obj instanceof Long) {
                return (Long) obj;
            }
            throw new RuntimeException("Expected type: long, got " + obj.getClass().getSimpleName());
        }
        return defValue;
    }


    public int optInt(int id) throws IOException {
        return StreamUtils.convertInt(optLong(id));
    }

    public int getInt(int id) throws IOException {
        return StreamUtils.convertInt(getLong(id));
    }

    public int getInt(int id, int defValue) throws IOException {
        return StreamUtils.convertInt(getLong(id, defValue));
    }

    public double optDouble(int id) throws IOException {
        return Double.longBitsToDouble(optLong(id));
    }

    public double getDouble(int id) throws IOException {
        return Double.longBitsToDouble(getLong(id));
    }

    public double getDouble(int id, double defValue) throws IOException {
        return Double.longBitsToDouble(getLong(id, Double.doubleToLongBits(defValue)));
    }

    public boolean optBool(int id) throws IOException {
        return optLong(id) != 0;
    }

    public boolean getBool(int id) throws IOException {
        return getLong(id) != 0;
    }

    public boolean getBool(int id, boolean defValue) throws IOException {
        return getLong(id, defValue ? 1 : 0) != 0;
    }

    @Nullable
    public byte[] optBytes(int id) throws IOException {
        return getBytes(id, null);
    }

    @Nullable
    public byte[] getBytes(int id) throws IOException {
        if (!fields.containsKey(id)) {
            throw new IOException("Unable to find field #" + id);
        }
        return getBytes(id, null);
    }

    @NotNull
    public byte[] getBytes(int id, @NotNull byte[] defValue) throws IOException {
        // TODO: Check defValue == null?
        if (fields.containsKey(id)) {
            touched.put(id, true);
            Object obj = fields.get(id);
            if (obj instanceof byte[]) {
                return (byte[]) obj;
            }
            throw new RuntimeException("Expected type: byte[], got " + obj.getClass().getSimpleName());
        }
        return defValue;
    }

    @Nullable
    public String optString(int id) throws IOException {
        return StreamUtils.convertString(optBytes(id));
    }

    @NotNull
    public String getString(int id) throws IOException {
        return StreamUtils.convertString(getBytes(id));
    }

    @NotNull
    public String getString(int id, @NotNull String defValue) throws IOException {
        return StreamUtils.convertString(getBytes(id, defValue.getBytes("UTF-8")));
    }

    @Nullable
    public <T extends ProtoObject> T optObj(int id, @NotNull T obj) throws IOException {
        byte[] data = optBytes(id);
        if (data == null) {
            return null;
        }
        return ProtoUtils.parse(obj, data);
    }

    @NotNull
    public <T extends ProtoObject> T getObj(int id, @NotNull T obj) throws IOException {
        byte[] data = optBytes(id);
        if (data == null) {
            throw new IOException("Unable to find field #" + id);
        }
        return ProtoUtils.parse(obj, new InputStream(data, 0, data.length));
    }

    public int getRepeatedCount(int id) throws IOException {
        if (fields.containsKey(id)) {
            touched.put(id, true);
            Object val = fields.get(id);
            if (val instanceof List) {
                return ((List) val).size();
            } else {
                return 1;
            }
        }
        return 0;
    }

    @NotNull
    public List<Long> getRepeatedLong(int id) throws IOException {
        ArrayList<Long> res = new ArrayList<Long>();
        if (fields.containsKey(id)) {
            touched.put(id, true);
            Object val = fields.get(id);
            if (val instanceof Long) {
                res.add((Long) val);
            } else if (val instanceof List) {
                List<Object> rep = (List) val;
                for (Object val2 : rep) {
                    if (val2 instanceof Long) {
                        res.add((Long) val2);
                    } else {
                        throw new IOException("Expected type: long, got " + val2.getClass().getSimpleName());
                    }
                }
            } else {
                throw new IOException("Expected type: long, got " + val.getClass().getSimpleName());
            }
        }
        return res;
    }

    @NotNull
    public List<Integer> getRepeatedInt(int id) throws IOException {
        List<Long> src = getRepeatedLong(id);
        ArrayList<Integer> res = new ArrayList<Integer>();
        for (Long l : src) {
            res.add(StreamUtils.convertInt(l));
        }
        return res;
    }

    @NotNull
    public List<byte[]> getRepeatedBytes(int id) throws IOException {
        ArrayList<byte[]> res = new ArrayList<byte[]>();
        if (fields.containsKey(id)) {
            touched.put(id, true);
            Object val = fields.get(id);
            if (val instanceof byte[]) {
                res.add((byte[]) val);
            } else if (val instanceof List) {
                List<Object> rep = (List) val;

                for (Object val2 : rep) {
                    if (val2 instanceof byte[]) {
                        res.add((byte[]) val2);
                    } else {
                        throw new IOException("Expected type: byte[], got " + val2.getClass().getSimpleName());
                    }
                }
            } else {
                throw new IOException("Expected type: byte[], got " + val.getClass().getSimpleName());
            }
        }
        return res;
    }

    @NotNull
    public List<String> getRepeatedString(int id) throws IOException {
        List<byte[]> src = getRepeatedBytes(id);
        ArrayList<String> res = new ArrayList<String>();
        for (byte[] l : src) {
            res.add(StreamUtils.convertString(l));
        }
        return res;
    }
}
