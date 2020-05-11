import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args)
    {
        String ACCESS_TOKEN = "EyWGEJUMRYAAAAAAAAAAKFXcN7W5FvJR6BNKztZVAz3ba9IQIE9gmivdW9QbFSsG";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        JavaSoundRecorder recorder = new JavaSoundRecorder(client);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyyMMdd'_'hhmmss");
        int recordingTime = 60000;
        int nRecord = 3;

        for (int i = 0; i < nRecord; i++)
        {
            String fileName = (formatForDateNow.format(new Date()) + ".wav");
            recorder.recordSound(recordingTime, fileName);
            try
            {
                Thread.sleep(recordingTime + 100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
