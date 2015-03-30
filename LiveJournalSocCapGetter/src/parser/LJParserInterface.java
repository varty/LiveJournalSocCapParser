package parser;

import java.util.LinkedHashMap;

import storage.StorageInterface;

public interface LJParserInterface {
	
	final String perDayUrl="http://www.livejournal.com/ratings/users/visitors/?country=cyr";
	final String forAllTime="http://www.livejournal.com/ratings/users/authority/?country=cyr";
	final String pageAttr="&page=";
	
	public void parseSocialCapital(String mode, int page, StorageInterface storage);
	
	public void parseSocialCapital(String mode, int beginPage, int lastPage, StorageInterface storage);
	
	public LinkedHashMap<String,String> getResult();

}
