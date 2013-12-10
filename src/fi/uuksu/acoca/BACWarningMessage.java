package fi.uuksu.acoca;

public class BACWarningMessage {
	
	public static int GetWarningMessage(double BAC) {
		
		if (BAC >= 0.50) {
			return R.string.bac_over_050;
		}
		
		return -1;
	}
}
