package storage;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;

public class Storage implements StorageInterface {

	private Map<String,String> map;
	private ArrayList<String> errors;
	
	@Override
	public void setData(Map<String, String> map) {
		this.map=map;
	}
	
	@Override
	public void addData(Map<String, String> map) {
		if (this.map==null){
			this.map=map;
		}else{
			this.map.putAll(map);
		}
	}

	@Override
	public void printData(String date) throws IOException {
		FileOutputStream fOutput=new FileOutputStream("RESULT_"+date+".txt");
		BufferedWriter bWriter=new BufferedWriter(new OutputStreamWriter(fOutput,"UTF-8"));
		int number=1;
		for(Map.Entry<String, String> entry : map.entrySet()){
			bWriter.append(number+": "+entry.getKey()+" "+entry.getValue());
			bWriter.append("\r\n");
			number++;
		}

		bWriter.flush();
		bWriter.close();
	}

	@Override
	public void addErrorMessage(String error) {
		if (errors==null){
			errors=new ArrayList<String>();
		}
		errors.add(error);
	}

	@Override
	public void printError(String date){
		if (errors!=null){
			try{
				FileOutputStream fOutput=new FileOutputStream("ERRORS_"+date+".txt");
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
	
}
