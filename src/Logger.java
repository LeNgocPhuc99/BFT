import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private File logFile;
	private int nodeID;
	
	public Logger(int id)
	{
		nodeID = id;
		try {
            logFile = new File(nodeID + ".log");
            if(logFile.exists())
            {
                if(!logFile.delete())
                    throw new IOException("Fatal : cannot delete the old log file !!!");
            }
            if (logFile.createNewFile()) {
                System.out.println("Log created: " + logFile.getName());
            } else {
                System.out.println("Fatal : cannot create new log file !!!");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void LogMessage(String message)
	{
		 try{
	            FileWriter fw = new FileWriter(logFile, true);
	            BufferedWriter bw = new BufferedWriter(fw);

	            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            Date date = new Date();
	            
	            bw.write("Thoi gian: " + dateFormat.format(date));
	            bw.newLine();
	            bw.write("Noi dung: {" + message + "}");
	            bw.newLine();
	            bw.close();

	        }
	        catch (IOException err)
	        {
	            err.printStackTrace();
	        }
	}
}
