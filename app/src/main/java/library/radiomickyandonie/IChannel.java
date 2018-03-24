package library.radiomickyandonie;

/**
 * Created by lobo on 03-21-18.
 */

public interface IChannel {
    void prepare();
    void start();
    void stop();
    void release();
}
