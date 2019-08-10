package FileWriterReader;

import java.io.*;

/**
 * Created by Spondon on 12/12/2016.
 */
public class CustomFileWriter implements AutoCloseable{
    private File file ;
    private BufferedWriter out ;
    private String targetString ;

    public CustomFileWriter(File file) throws Exception{
        this.file = file ;
        this.targetString = targetString ;
        out = new BufferedWriter(new FileWriter(file,true)) ;
    }

    public void write(String targetString) throws Exception{
        this.targetString = targetString ;
        out.write(targetString);
    }


    @Override
    public void close() throws Exception {
        out.close();
    }
}
