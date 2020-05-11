import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class JavaSoundRecorder
{
    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private AudioFormat audioFormat;
    private DbxClientV2 client;

    public JavaSoundRecorder(DbxClientV2 client)
    {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        audioFormat = new AudioFormat(
            sampleRate, sampleSizeInBits, channels, signed, bigEndian
        );
        DataLine.Info info = new DataLine.Info(
            TargetDataLine.class, audioFormat
        );
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        fileType = AudioFileFormat.Type.WAVE;
        this.client = client;
    }

    public void recordSound(long milliseconds, String fileName)
    {
        File file = new File(fileName);
        start(file);
        delayFinish(milliseconds, file);
    }

    private void start(File file)
    {
        new Thread(() ->
        {
            try {
                line.open(audioFormat);
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void delayFinish(long delayTime, File file)
    {
        new Thread(() ->
        {
            try
            {
                Thread.sleep(delayTime);
                line.stop();
                line.close();
                try
                {
                    //TODO: upload to Dropbox - DONE
                    InputStream in = new FileInputStream(file.getName());
                    FileMetadata metadata = client.files().uploadBuilder("/" + file.getName())
                            .uploadAndFinish(in);
                    in.close();
                    //TODO: delete file - DONE
                    file.delete();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}