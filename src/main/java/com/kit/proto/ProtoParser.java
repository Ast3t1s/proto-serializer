package com.kit.proto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProtoParser {

    public static HashMap<Integer, Object> deserialize(InputStream is) throws IOException {
        HashMap<Integer, Object> hashMap = new HashMap<Integer, Object>();
        while (!is.isEOF()) {
            long currentTag = is.readVarInt();

            int id = (int) (currentTag >> 3);
            int type = (int) (currentTag & 0x7);

            if (type == Const.TYPE_VARINT) {
                put(id, is.readVarInt(), hashMap);
            } else if (type == Const.TYPE_LENGTH_DELIMITED) {
                int size = (int) is.readVarInt();
                put(id, is.readBytes(size), hashMap);
            } else if (type == Const.TYPE_64BIT) {
                put(id, is.readLong(), hashMap);
            } else if (type == Const.TYPE_32BIT) {
                put(id, is.readUInt(), hashMap);
            } else {
                throw new IOException("Unknown Type #" + type);
            }
        }
        return hashMap;
    }

    private static void put(int id, Object res, HashMap<Integer,Object> hashMap) {
        if (hashMap.get(id) != null) {
            if (hashMap.get(id) instanceof List) {
                ((List) hashMap.get(id)).add(res);
            } else {
                ArrayList<Object> list = new ArrayList<Object>();
                list.add(hashMap.get(id));
                list.add(res);
                hashMap.put(id, list);
            }
        } else {
            hashMap.put(id, res);
        }
    }

    private ProtoParser() {

    }
}
