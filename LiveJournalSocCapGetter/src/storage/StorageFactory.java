package storage;

public class StorageFactory {
	
	public static StorageInterface getStorage(){
		return new StorageImpl();
	}
	
}
