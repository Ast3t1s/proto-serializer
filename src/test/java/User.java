import com.kit.proto.*;

import java.io.IOException;
import java.util.List;

public class User extends ProtoObject {

    public static User fromBytes(byte[] data) throws IOException {
        return ProtoUtils.parse(new User(), data);
    }

    public static ProtoWrapper<User> creator = new ProtoWrapper<User>() {
        @Override
        public User createInstance() {
            return new User();
        }
    };

    private int id;
    private long timestamp;
    private String name;
    private double someDouble;
    private boolean someBool;
    private List<String> someListOfStrings;
    private List<Integer> someListOfInts;

    public User(int id, long timestamp, String name, double someDouble, boolean someBool,
                List<String> someListOfStrings,
                List<Integer> someListOfInts) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.someDouble = someDouble;
        this.someBool = someBool;
        this.someListOfStrings = someListOfStrings;
        this.someListOfInts = someListOfInts;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSomeDouble() {
        return someDouble;
    }

    public void setSomeDouble(double someDouble) {
        this.someDouble = someDouble;
    }

    public boolean isSomeBool() {
        return someBool;
    }

    public void setSomeBool(boolean someBool) {
        this.someBool = someBool;
    }

    public List<String> getSomeListOfStrings() {
        return someListOfStrings;
    }

    public void setSomeListOfStrings(List<String> someListOfStrings) {
        this.someListOfStrings = someListOfStrings;
    }

    public List<Integer> getSomeListOfInts() {
        return someListOfInts;
    }

    public void setSomeListOfInts(List<Integer> someListOfInts) {
        this.someListOfInts = someListOfInts;
    }

    @Override
    public void deserializeBody(ProtoValues values) throws IOException {
        id = values.getInt(1);
        timestamp = values.getLong(2);
        name = values.getString(3);
        someDouble = values.getDouble(4);
        someBool = values.getBool(5);
        someListOfStrings = values.getRepeatedString(6);
        someListOfInts = values.getRepeatedInt(7);
    }

    @Override
    public void serializeBody(ProtoWriter write) throws IOException {
        write.writeInt(1, id);
        write.writeLong(2, timestamp);
        write.writeString(3, name);
        write.writeDouble(4, someDouble);
        write.writeBool(5, someBool);
        write.writeRepeatedString(6, someListOfStrings);
        write.writeRepeatedInt(7, someListOfInts);
    }
}
