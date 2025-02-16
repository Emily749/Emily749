package qnmc;

import java.util.Set;
import java.util.TreeSet;

public class GetMintermList {
	static Set<String> set=new TreeSet<>();
	
	@SuppressWarnings("unused")
	private static final String[] minList=new String[256];
	
	public void setMinList(String x){
	
		set.add(x);
	}
public static Set<String> getMin(){
	return set;
}
}