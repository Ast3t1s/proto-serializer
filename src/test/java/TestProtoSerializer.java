import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestProtoSerializer {


    @Test
    public void testUser() throws IOException {
        User user = new User();
        user.setId(31);
        user.setName("MyName");
        user.setSomeBool(true);
        user.setSomeDouble(3.0);
        user.setTimestamp(System.currentTimeMillis());
        List<String> strings = new ArrayList<String>();
        strings.add("first");
        strings.add("seconds");
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        user.setSomeListOfInts(list);
        user.setSomeListOfStrings(strings);

        byte[] data = user.toByteArray();

        User desUser = User.fromBytes(data);

        Assert.assertEquals(user.getId(), desUser.getId());
        Assert.assertEquals(user.getName(), desUser.getName());
        Assert.assertEquals(user.isSomeBool(), desUser.isSomeBool());
        Assert.assertEquals(user.getTimestamp(), desUser.getTimestamp());
        Assert.assertEquals(user.getSomeListOfInts(), desUser.getSomeListOfInts());
        Assert.assertEquals(user.getSomeListOfStrings(), desUser.getSomeListOfStrings());
    }

}
