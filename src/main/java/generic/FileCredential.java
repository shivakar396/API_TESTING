package generic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileCredential {
	Properties p=new Properties();
	public String readCredential(String key) throws IOException
	{
		FileInputStream fis=new FileInputStream("./src/test/resources/data/credential.property");
		p.load(fis);
		
		
		return p.getProperty(key);
	}
}