package storage;

import java.io.IOException;
import java.util.Map;

public interface StorageInterface {

	public void mergeResult(int id, Map<String, String> map) throws InterruptedException;	
	
	public void printData() throws IOException;
	
	public void printError();

	public void addErrorMessage(Exception e);
	
}
