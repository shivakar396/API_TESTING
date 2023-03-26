package generic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;
/**
 * 
 * @author Xuriti
 *
 *@FileLib This Class is use  to read the data and write the data in text file
 *This class contains getPropertyData method to read the data from file and setPropertydata method to write data into the file
 */
public class FileLib {
	Properties p=new Properties();
	/**
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 * 
	 */
	public String getPropertyData(String key) throws IOException
	{
		FileInputStream file=new FileInputStream("./src/test/resources/data/commondata.property");
		p.load(file);
		return p.getProperty(key);
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 * 
	 */
	public void setPropertyData(String key,String value) throws IOException
	{
		FileInputStream file=new FileInputStream("./src/test/resources/data/commondata.property");
		p.load(file);
		p.setProperty(key, value);
		FileOutputStream fos=new FileOutputStream("./src/test/resources/data/commondata.property");
		p.store(fos, "Updated");
	}
	public int getExcelRowCount() throws EncryptedDocumentException, IOException
	{
		FileInputStream fis=new FileInputStream("./src/test/resources/data/DevInvoice1.xls");
		Workbook wb=WorkbookFactory.create(fis);
		int numofrow=wb.getSheet("Sheet1").getLastRowNum();
		return numofrow;
	}
	public double getExcelNumericCellData(int row,int col) throws EncryptedDocumentException, IOException
	{
		FileInputStream fis=new FileInputStream("./src/test/resources/data/DevInvoice1.xls");
		Workbook wb=WorkbookFactory.create(fis);
		
		//Row and column starts with zero(0).
		double data=wb.getSheet("Sheet1").getRow(row).getCell(col).getNumericCellValue();
		return data;
	}
	public String getExcelStringCellData(int row,int col) throws EncryptedDocumentException, IOException
	{
		FileInputStream fis=new FileInputStream("./src/test/resources/data/DevInvoice1.xls");
		Workbook wb=WorkbookFactory.create(fis);
		
		//Row and column starts with zero(0).
		String data=wb.getSheet("Sheet1").getRow(row).getCell(col).getStringCellValue();
		return data;
	}
	public void setExcelPropertyData(int row,int col,String data) throws EncryptedDocumentException, IOException
	{
		FileInputStream fis=new FileInputStream("./src/test/resources/data/DevInvoice1.xls");
		Workbook wb=WorkbookFactory.create(fis);
		wb.getSheet("Sheet1").getRow(row).getCell(col).setCellValue(data);
		
		FileOutputStream fos=new FileOutputStream("./src/test/resources/data/DevInvoice1.xls");
		wb.write(fos);
		wb.close();
	}
}
