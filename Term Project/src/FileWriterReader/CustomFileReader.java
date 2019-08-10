package FileWriterReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Spondon on 12/12/2016.
 */
public class CustomFileReader implements AutoCloseable {
    private File file ;
    private BufferedReader in ;
    private String targetString ;

    public CustomFileReader(File file) throws Exception {
        this.file = file ;
        in = new BufferedReader(new FileReader(file)) ;
    }

    public boolean isPresent(String targetString) throws Exception{
        String tem ;
        this.targetString = targetString ;
        while ((tem=in.readLine())!=null){
            if(tem.equals(targetString)){
                return true ;
            }
        }

        return false ;
    }

    public String copy() throws Exception{
        String tem ;
        String sout ="" ;
        while ((tem=in.readLine())!=null){
                sout += tem + "\n" ;
        }
        return sout ;
    }


    @Override
    public void close() throws Exception {
        in.close();
    }
}
