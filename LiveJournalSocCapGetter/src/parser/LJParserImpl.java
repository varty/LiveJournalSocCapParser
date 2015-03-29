package parser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import storage.StorageInterface;

public class LJParserImpl implements LJParser{

	private String urlString;
	private StorageInterface storage;
	
	private final String pageAttr="&page=";
	private final int countUser=20;
	
	private final String usersTableQuery="body > div.s-layout.s-logged-out > div.s-body > div.s-main.b-service > div > div > table > tbody > tr > td > table > tbody";
	private final String userId_1="tr:eq(";
	private final String userNicknameQuery=") > td.b-ratings-item-desc > table > tbody > tr > td:nth-child(2) > div > span > a.i-ljuser-username > b";
	private final String userSocialCapitalQuery=") > td.b-ratings-item-val > span";
	
	@Override
	public void parseSocialCapital(String mode, int page, StorageInterface storage) throws IOException {
		parseSocialCapital(mode, 0, page, storage);
	}

	@Override
	public void parseSocialCapital(String mode,int beginPage,int lastPage, StorageInterface storage) throws IOException {
		setMode(mode);
		setStorage(storage);
		if (beginPage<=0){
			this.storage.setData(parse(lastPage));
		}
		else{
			for (int i=beginPage; i<=lastPage; i++){
				this.storage.addData(parse(i));
			}
		}
	}

	private void setMode(String mode) {
		if (mode.trim().equalsIgnoreCase(ModeEnum.day.name())){
			urlString=LJParser.perDayUrl;
		}else urlString=LJParser.forAllTime;
	}

	private void setStorage(StorageInterface storage) {
		this.storage=storage;
	}
	
	private Map<String, String> parse(int page) throws IOException {
		LinkedHashMap<String,String> lhMap=new LinkedHashMap<>();
		
		/*URL url = new URL(urlString+pageAttr+page);
        String content = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                content += line;
                //System.out.println(line);
            }
        }
        System.out.println(Jsoup.parse(content).text());
        Document doc=Jsoup.parse(content);*/
		
		Document doc=Jsoup.connect(urlString+pageAttr+page).get();
		Elements usersTable=doc.select(usersTableQuery);
		for (int i=0;i<countUser;i++){
			String nickname=usersTable.select(userId_1+i+userNicknameQuery).text();
			String capital=usersTable.select(userId_1+i+userSocialCapitalQuery).text();
			lhMap.put(nickname, capital);
		}
		return lhMap;
	}

}
