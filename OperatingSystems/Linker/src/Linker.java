import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Linker {

	public static ArrayList<Module> modules = new ArrayList<Module>();
	public static ArrayList<Integer> memoryMap = new ArrayList<Integer>();

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Invalid usage of input \n PLease give a single" + "argument for the input file name");
			System.exit(0);
		}

		File inputFile = new File(args[0]);

		if (!inputFile.canRead()) {
			System.err.printf("Cannot read from file %s\n.", inputFile.getAbsolutePath());
			System.exit(0);
		}

		Scanner scanner = null;

		try {
			scanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.err.printf("File %s not found\n", inputFile.getAbsolutePath());
			System.exit(0);

		}

		ArrayList<String> input = new ArrayList<>();
		while (scanner.hasNextLine()) {
			if (!scanner.hasNext()) {
				break;
			} else {
				input.add(scanner.next());
			}
		}

		for (int i = 0; i < input.size(); i++) {
			System.out.print(input.get(i) + ", ");
		}

		int numberOfModules = Integer.parseInt(input.get(0));

		// System.out.print("\n" + numberOfModules);

		// initialize the modules
		for (int i = 0; i < numberOfModules; i++) {
			modules.add(new Module());
		}

		firstPass(input);
		printSymbolTable();
		secondPass();
		memoryMapPrinter();

	}

	public static void firstPass(ArrayList<String> input) {

		int moduleCounter = 0;
		int lineCounter = 1;
		int i = 1;
		while (moduleCounter < modules.size()) {

			// definition line, read and store definitions
			if (lineCounter % 3 == 1) {
				System.out.println(input.get(i));
				if (Integer.parseInt(input.get(i)) == 0) {
					i++;
					lineCounter++;
				} else {
					int offset = i;

					while (i < offset + Integer.parseInt(input.get(offset)) * 2) {

						Symbol sym = new Symbol(input.get(i + 1), Integer.parseInt(input.get(i + 2)));
						// System.out.println(sym.toString());
						modules.get(moduleCounter).addSymbol(sym);
						i += 2;

					}
					lineCounter++;
					i++;
				}

			}
			// use line, have to skip this one

			if (lineCounter % 3 == 2) {
				int numberOfUses = Integer.parseInt(input.get(i));
				// System.out.println("\nThe line count is: " + lineCounter);
				// System.out.println("The module count is: " + moduleCounter);
				// System.out.println("The number of uses is: " + numberOfUses);
				if (numberOfUses == 0) {
					// System.out.println("There were zero uses so we skip");
					i++;
					lineCounter++;
				} else {
					int j = i + 1;

					int endIndex = j + numberOfUses;
					while (j < endIndex) {
						modules.get(moduleCounter).addUse(input.get(j));
						j++;
					}
					// skip the whole thing
					i += Integer.parseInt(input.get(i)) + 1;
					lineCounter++;
				}

			}
			// modules line, have to store the length of the module
			if (lineCounter % 3 == 0) {
				int numberOfUses = Integer.parseInt(input.get(i));
				// Add the base address of every module to the corresponding
				// data field
				if (moduleCounter != modules.size() - 1) {
					modules.get(moduleCounter + 1).setbaseAddress(
							Integer.parseInt(input.get(i)) + modules.get(moduleCounter).getbaseAddress());
				}

				int j = i + 1;
				int endIndex = j + numberOfUses;

				while (j < endIndex) {

					modules.get(moduleCounter).addWord(input.get(j));
					j++;
				}

				i += Integer.parseInt(input.get(i)) + 1;
				lineCounter++;

			}

			moduleCounter++;
		}

	}

	public static void printSymbolTable() {

		for (int taint = 0; taint < modules.size(); taint++) {
			System.out.println(modules.get(taint).toString());
		}
		System.out.println("Symbol Table");
		for (int j = 0; j < modules.size(); j++) {
			for (int k = 0; k < modules.get(j).getSymbols().size(); k++) {
				System.out.println(modules.get(j).getSymbol(k).getVariable() + "="
						+ (modules.get(j).getSymbol(k).getLocation() + modules.get(j).getbaseAddress()));

			}

		}
	}

	public static void secondPass() {

		for (int i = 0; i < modules.size(); i++) {
			for (int j = 0; j < modules.get(i).getWords().size(); j++) {
				ArrayList<String> currentWordList = modules.get(i).getWords();

				String currentWordString = currentWordList.get(j);
				// System.out.println("current word " + currentWordString);
				String lastCharacter = "" + currentWordString.charAt(currentWordString.length() - 1);
				int lastChar = Integer.parseInt(lastCharacter);
				// System.out.println(lastChar);
				// don't do anything

				if (lastChar == 1 || lastChar == 2) {
					String address = currentWordString.substring(0, currentWordString.length() - 1);
					int addressInteger = Integer.parseInt(address);
					// System.out.println("When lastcar 1 or 2 I'm adding " +
					// addressInteger);
					memoryMap.add(addressInteger);

				} else if (lastChar == 3) {

					String address = currentWordString.substring(0, currentWordString.length() - 1);
					// System.out.println("address is :" + address);
					int addressInteger = Integer.parseInt(address);
					addressInteger += modules.get(i).getbaseAddress();
					// System.out.println("When lastcar is 3 I'm adding " +
					// addressInteger);
					memoryMap.add(addressInteger);
					// int currentWord =

				} else {
					String address = currentWordString.substring(0, currentWordString.length() - 1);
					String symbolReference = currentWordString.substring(1, currentWordString.length() - 1);

					int addressInteger = Integer.parseInt(address);
					int symbolReferenceInt = Integer.parseInt(symbolReference);
					// System.out.println("The symbol reference int is " +
					// symbolReferenceInt);

					String referencedSymbol = modules.get(i).getUses().get(symbolReferenceInt);

					// System.out.println("The referenced symbol was " +
					// referencedSymbol);
					int referencedSymbolLocation = symbolLocationFinder(referencedSymbol);
					// System.out.println("The adress of the referenced symbol
					// is" + referencedSymbolLocation);
					// have to find which module the symbol is defined in and
					// its location
					// System.out.println("The adress integer is : " +
					// addressInteger);
					addressInteger = addressInteger / 100;
					addressInteger *= 100;
					// System.out.println("After flipping address integer is:" +
					// trial);

					addressInteger += referencedSymbolLocation;

					memoryMap.add(addressInteger);

					// System.out.println("When lastcar is 4 I'm adding" +
					// addressInteger);
				}

			}
		}
	}

	public static int symbolLocationFinder(String variable) {
		int symbolLocation = 0;
		for (int i = 0; i < modules.size(); i++) {
			for (int j = 0; j < modules.get(i).getSymbols().size(); j++) {
				Symbol currentSymbol = modules.get(i).getSymbols().get(j);
				if (currentSymbol.getVariable().equals(variable)) {
					// System.out.println("definition and adress found!!!!!");
					symbolLocation = currentSymbol.getLocation() + modules.get(i).getbaseAddress();
				}
			}
		}

		return symbolLocation;
	}

	public static void memoryMapPrinter() {
		System.out.println("Memory Map");
		for (int i = 0; i < memoryMap.size(); i++) {
			System.out.println(i + ":  " + memoryMap.get(i));
		}
	}

}
