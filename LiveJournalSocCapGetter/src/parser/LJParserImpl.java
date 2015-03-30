package parser;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import storage.StorageInterface;

public class LJParserImpl implements LJParserInterface{

	private String urlString;
	private LinkedHashMap<String,String> pagesMap=new LinkedHashMap<>();
	
	private final int countUser=20;
	
	private final String usersTableQuery="body > div.s-layout.s-logged-out > div.s-body > div.s-main.b-service > div > div > table > tbody > tr > td > table > tbody";
	private final String userId_1="tr:eq(";
	private final String userNicknameQuery=") > td.b-ratings-item-desc > table > tbody > tr > td:nth-child(2) > div > span > a.i-ljuser-username > b";
	private final String userSocialCapitalQuery=") > td.b-ratings-item-val > span";
	
	@Override
	public void parseSocialCapital(String mode, int page, StorageInterface storage) {
		parseSocialCapital(mode, 0, page, storage);
	}

	@Override
	public void parseSocialCapital(String mode,int beginPage,int lastPage, StorageInterface storage) {
		setMode(mode);
		if (beginPage<=0){
			parse(lastPage);
		}
		else{
			for (int i=beginPage; i<=lastPage; i++){
				parse(i);
				System.out.println(Thread.currentThread().getName()+" - "+pagesMap.size());
			}
		}
	}
	
	public LinkedHashMap<String,String> getResult(){
		return pagesMap;
	}

	private void setMode(String mode) {
		if (mode.trim().equalsIgnoreCase(ModeEnum.day.name())){
			urlString=perDayUrl;
		}else urlString=forAllTime;
	}
	
	private void parse(int page){
		try{
			Document doc=Jsoup.connect(urlString+pageAttr+page).get();
			Elements usersTable=doc.select(usersTableQuery);
			for (int i=0;i<countUser;i++){
				String nickname=usersTable.select(userId_1+i+userNicknameQuery).text();
				String capital=usersTable.select(userId_1+i+userSocialCapitalQuery).text();
				pagesMap.put(nickname, capital);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Repeat connect");
			parse(page);
		}
	}

}
