package controller;

import parser.LJParserInterface;
import storage.StorageInterface;

public class Controller implements Runnable {

	private static StorageInterface storage;
	private LJParserInterface parser;
	private String mode;
	private int lastPage;
	private int beginPage;
	private int id;
	
	public Controller(int id, int begin, int end, String mode){
		beginPage=begin;
		lastPage=end;
		this.mode=mode;
		this.id=id;
	}
	
	@Override
	public void run() {
		start();
	}
	
	public void start(){
		parser.parseSocialCapital(mode, beginPage, lastPage, storage);
		try {
			storage.mergeResult(id, parser.getResult());
		} catch (InterruptedException e) {
			storage.addErrorMessage(e);
			e.printStackTrace();
		}
	}
	

	public void setParser(LJParserInterface parser){
		this.parser=parser;
	}
	
	public void setStorage(StorageInterface storage){
		Controller.storage=storage;
	}
	
}
