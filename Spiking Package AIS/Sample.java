import static java.lang.System.*;

public class Sample implements Comparable{
	public String Header;
	public String Sequence;
	public String PlusSign;
	public String Quality;
	
	public Sample (){
		Header = new String();
		Sequence = new String();
		PlusSign = new String("+");
		Quality = new String();
	}
	
	public Sample (String header, String sequence, String plusSign, String quality){
		Header = header;
		Sequence = sequence;
		PlusSign = plusSign;
		Quality = quality;
	}
	
	public boolean DoesClassMatch(String type){
		if (Header.contains(type))
			return true;
		return false;
	}
	
	public String GetClass(){
		int start = FindStart(Header);
		if (start == -1) out.println("CANNOT FIND = IN HEADER STRING");
		int end = Header.length() - 1;
		out.println(Header.substring(start, end));
		return Header.substring(start, end);
		
	}
	
	public int FindStart(String str){
		int i;
		for (i = 0; i < str.length(); i++){
			if (str.charAt(i) == '='){
				return i + 1;
			}
		}
		return -1;
	}

	@Override
	public int compareTo(Object arg0) {
		return this.GetClass().compareTo(((Sample) arg0).GetClass());
	}
}
