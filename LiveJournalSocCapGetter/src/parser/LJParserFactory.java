package parser;

public class LJParserFactory {
	
	public static LJParser getLJParserInstance(){
		return new LJParserImpl();
	}
	
}
