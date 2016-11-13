import java.util.ArrayList;

public class Module {
	private int baseAddress;
	private ArrayList<Symbol> symbols;
	private ArrayList<String> useList;
	private ArrayList<String> words;

	public Module() {
		this.baseAddress = 0;
		this.symbols = new ArrayList<Symbol>();
		this.useList = new ArrayList<String>();
		this.words = new ArrayList<String>();
	}

	// public Module()
	public void setbaseAddress(int len) {
		this.baseAddress = len;
	}

	public int getbaseAddress() {
		return this.baseAddress;
	}

	public void addSymbol(Symbol sym) {
		this.symbols.add(sym);
	}

	public ArrayList<Symbol> getSymbols() {
		return this.symbols;
	}

	public Symbol getSymbol(int i) {
		return symbols.get(i);
	}

	public void addUse(String use) {
		this.useList.add(use);
	}

	public ArrayList<String> getUses() {
		return this.useList;
	}

	public void addWord(String word) {
		this.words.add(word);
	}

	public ArrayList<String> getWords() {
		return this.words;
	}

	public int getSymbolAddress(String wantedSymbol) {

		for (int i = 0; i < this.symbols.size(); i++) {
			if (wantedSymbol.equals(this.symbols.get(i).getVariable())) {
				return this.symbols.get(i).getLocation();
			}
		}
		System.out.println("not found returning 0");
		return 0;
	}

	public String toString() {
		String syms = "";
		String uses = "";
		String word = "";
		for (int i = 0; i < symbols.size(); i++) {
			syms += symbols.get(i).toString() + "\n";

		}
		for (int i = 0; i < useList.size(); i++) {
			uses += useList.get(i).toString() + " ";

		}

		for (int i = 0; i < words.size(); i++) {
			word += words.get(i).toString() + " ";

		}

		return "Base address is :" + baseAddress + "\n symbols: " + syms + "\n uses:" + uses + "\n words:" + word;

	}

}
