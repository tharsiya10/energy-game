package model;

public enum Component {
	
	SOURCE("S"),
	WIFI("W"),
	LAMP("L"),
	EMPTY(".");
	
	private final String componentCode;
	
	Component(String code) {
		this.componentCode = code;
	}
	
	public String componentCode() {
		return this.componentCode;
	}
	
	public static Component componentFromCode(String s) {
		switch(s) {
			case "S" :
				return Component.SOURCE;
				
			case "W" :
				return Component.WIFI;
			case "L" :
				return Component.LAMP;
			case "." :
				return Component.EMPTY;	
		}
		throw new IllegalArgumentException("Unknown component code");
	}
}