package parser;

import java.io.IOException;

import storage.StorageInterface;

public interface LJParser {
	
	final String perDayUrl="http://www.livejournal.com/ratings/users/visitors/?country=cyr";
	final String forAllTime="http://www.livejournal.com/ratings/users/authority/?country=cyr";
	final String page="&page=";
	
	public void parseSocialCapital(String mode, int page, StorageInterface storage) throws IOException;
	
	public void parseSocialCapital(String mode, int beginPage, int lastPage, StorageInterface storage) throws IOException;

}
