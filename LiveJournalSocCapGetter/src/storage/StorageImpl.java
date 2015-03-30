package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class StorageImpl implements StorageInterface {
 
	private static Map<String,String> map;
	private static int queueNumber=0;
	private static ArrayList<String> errors;

	@Override
	public synchronized void mergeResult(int number, Map<String,String> map) throws InterruptedException {
		if (StorageImpl.map==null)
			StorageImpl.map=Collections.synchronizedMap(new LinkedHashMap<>());
		while (queueNumber!=number) 
			wait();
		StorageImpl.map.putAll(map);
		queueNumber++;
		notifyAll();
	}

	@Override
	public void printData() throws IOException {
		String date=getStringDate();
		FileOutputStream fOutput=new FileOutputStream(new File("./RESULT_"+date+".txt"));
		BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(fOutput,"UTF-8"));
		int number=1;
		for(Map.Entry<String, String> entry : map.entrySet()){
			bWriter.append(entry.getKey()+" "+entry.getValue());
			bWriter.append(number+". "+entry.getKey()+" : "+entry.getValue());
			bWriter.append("\r\n");
			number++;
		}
		bWriter.flush();
		bWriter.close();
	}

	@Override
	public synchronized void addErrorMessage(Exception e) {
		if (errors==null){
			errors=new ArrayList<String>();
		}
		errors.add(setErrorMessage(e));
	}

	@Override
	public void printError(){
		String date=getStringDate();
		if (errors!=null){
			try{
				FileOutputStream fOutput=new FileOutputStream((new File("./ERROR_"+date+".txt")));
				BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(fOutput,"UTF-8"));
				for (int i=0; i<errors.size();i++){
					bWriter.append(errors.get(i)+"\r\n");
				}
				bWriter.flush();
				bWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String setErrorMessage(Exception exception){
		String errorDate=getStringDate();
		return errorDate+": "+exception.getMessage();
	};
	
	private String getStringDate(){
		Date date=new Date(System.currentTimeMillis());
		date.setTime(date.getTime());
		DateFormat formatDate=new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String dateString=formatDate.format(date);
		return dateString;
	}
	
}
