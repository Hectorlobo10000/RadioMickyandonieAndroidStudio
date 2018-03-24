package library.radiomickyandonie;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by lobo on 03-21-18.
 */

public class ChannelFactory {
    private Hashtable<String, IChannel> channel;
    public ChannelFactory(Hashtable player) {
        this.channel = player;
    }

    public IChannel CreateChannel(String channel) {
        return this.channel.get(channel);
    }
}
