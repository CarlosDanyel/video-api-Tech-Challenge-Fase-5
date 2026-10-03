package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port;
import java.io.InputStream;
public interface VideoStoragePort {
    void put(String key, InputStream input, long size, String contentType);
    InputStream get(String key);
    void delete(String key);
}
