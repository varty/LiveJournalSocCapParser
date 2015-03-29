package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import parser.LJParser;
import parser.LJParserFactory;
import storage.StorageFactory;
import storage.StorageInterface;

public class Controller {

	private LJParser parser;
	private StorageInterface storage;
	private String mode;
	private int lastPage;
	private int beginPage;
	private final int maxCountPage=5000;
	
	public void start(String[] args) {
		setParser(LJParserFactory.getLJParserInstance());
		setStorage(StorageFactory.getStorage());
		parseSettings(args);
		try {
			parser.parseSocialCapital(mode, beginPage, lastPage, storage);
		} catch (IOException e) {
			setErrorMessage(e);
		}
		try {
			storage.printData(getStringDate());
		} catch (IOException e) {
			setErrorMessage(e);
			e.printStackTrace();
		}
		storage.printError(getStringDate());
	}
	

	private void setParser(LJParser parser){
		this.parser=parser;
	}
	
	private void setStorage(StorageInterface storage){
		this.storage=storage;
	}
	
	private void parseSettings(String[] args){
		try{
			if (args.length>2){
				mode=args[0];
				beginPage=Integer.parseInt(args[1]);
				lastPage=Integer.parseInt(args[2]);
				if (beginPage>lastPage){
					lastPage=beginPage+lastPage;
					beginPage=lastPage-beginPage;
					lastPage=lastPage-beginPage;
				}else if (beginPage==lastPage){
					beginPage=0;
				}
			}else{
				mode=args[0];
				lastPage=Integer.parseInt(args[1]);
			}
			if (lastPage>maxCountPage)
				lastPage=maxCountPage;
		}catch(ArrayIndexOutOfBoundsException e){
			setDefaultSettings();
		}
	}
	
	private void setDefaultSettings(){
		mode="all";
		lastPage=1;
		beginPage=0;
	}
	
	private void setErrorMessage(Exception message){
		String errorDate=getStringDate();
		storage.addErrorMessage(errorDate+": "+message.getMessage());
	};
	
	private String getStringDate(){
		Date date=new Date(System.currentTimeMillis());
		date.setTime(date.getTime());
		DateFormat formatDate=new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String dateString=formatDate.format(date);
		return dateString;
	}
	
}
