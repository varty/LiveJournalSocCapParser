package storage;

import java.io.IOException;
import java.util.Map;

public interface StorageInterface {
	
	public void setData(Map<String,String> map);
	
	public void addData(Map<String,String> map);
	
	public void printData(String date) throws IOException;
	
	public void addErrorMessage(String error);

	public void printError(String date);
	
}
