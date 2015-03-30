package controller;

import java.io.IOException;
import java.util.HashSet;

import parser.LJParserFactory;
import storage.StorageFactory;
import storage.StorageInterface;

public class MainController {

	private static String mode;
	private static int lastPage;
	private static int beginPage;
	private static int range;
	private static StorageInterface storage;
	private static final int maxCountPage=5000;
	
	private static void setDefaultMode(){
		mode="all";
	}
	
	private static void setDefaultLastPage(){
		lastPage=1;
	}
	
	private static void setDefaultBeginPage(){
		beginPage=0;
	}
	
	private static void setDefaultRange(){
		range=49;
	}

	private static void parseSettings(String[] args){
		try{
			for (int i=0;i<args.length;i++){
				if (i==0) mode=args[i];
				else if (i==1) beginPage=Integer.parseInt(args[i]);
				else if (i==2) lastPage=Integer.parseInt(args[i]);
				else if (i==3) range=Integer.parseInt(args[i])-1;
			}
			if (lastPage==0) setDefaultLastPage();
			else if (lastPage>maxCountPage) lastPage=maxCountPage;
			if (range==0) setDefaultRange();
			if (beginPage>lastPage){
				lastPage=beginPage+lastPage;
				beginPage=lastPage-beginPage;
				lastPage=lastPage-beginPage;
			}else if (beginPage==lastPage){
				setDefaultBeginPage();
			}
		}catch(ArrayIndexOutOfBoundsException e){
			setDefaultMode();
			setDefaultBeginPage();
			setDefaultLastPage();
			setDefaultRange();
		}
	}
	
	private static void printResult(){
		try {
			storage.printData();
		} catch (IOException e) {
			storage.addErrorMessage(e);
			e.printStackTrace();
		}
		storage.printError();
	}

	public static void main(String[] args) {
		parseSettings(args);
		storage=StorageFactory.getStorage();
		int id=0;
		Controller control;
		if (beginPage!=0){
			int begin=beginPage;
			int end=begin+range;
			HashSet<Thread> threadsSet=new HashSet<Thread>();
			do{
				if (end>lastPage) end=lastPage;
				control=new Controller(id, begin, end, mode);
				control.setParser(LJParserFactory.getLJParserInstance());
				control.setStorage(storage);
				Thread thread=new Thread(control);
				thread.start();
				threadsSet.add(thread);
				id++;
				begin=++end;
				end=begin+range;
			}while(begin<lastPage);
			for (Thread thread: threadsSet){
				try {
					thread.join();
				} catch (InterruptedException e) {
					storage.addErrorMessage(e);;
					e.printStackTrace();
				}
			}
			printResult();
			
		}else{
			control=new Controller(id, beginPage, lastPage, mode);
			control.setParser(LJParserFactory.getLJParserInstance());
			control.setStorage(storage);
			control.start();
			printResult();
		}		
	}
}
