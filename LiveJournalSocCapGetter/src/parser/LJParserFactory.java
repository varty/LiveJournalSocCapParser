package parser;

public class LJParserFactory {
	
	public static LJParserInterface getLJParserInstance(){
		return new LJParserImpl();
	}
	
}
