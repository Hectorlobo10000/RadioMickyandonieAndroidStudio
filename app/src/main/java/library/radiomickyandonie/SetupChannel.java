package library.radiomickyandonie;

import java.util.Hashtable;

/**
 * Created by lobo on 03-21-18.
 */

public class SetupChannel {
    public static ChannelFactory GetFactory(final Hashtable hashtable) {
        return new ChannelFactory(new Hashtable<String, IChannel>(){{
            put("english", new English(hashtable));
            put("spanish", new Spanish(hashtable));
        }});
    }
}
