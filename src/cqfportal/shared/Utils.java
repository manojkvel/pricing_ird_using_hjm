package cqfportal.shared;

public class Utils {
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}
