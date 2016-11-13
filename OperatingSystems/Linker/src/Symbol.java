
public class Symbol {
	private String variable;
	private int location;

	public Symbol(String variable, int location) {
		this.variable = variable;
		this.location = location;

	}

	public void addVariable(String var) {
		this.variable = var;
	}

	public String getVariable() {
		return this.variable;
	}

	public void addLocation(int loc) {
		this.location = loc;
	}

	public int getLocation() {
		return this.location;
	}

	public String toString() {
		return "variable is:" + this.variable + "location is" + this.location;

	}
}
