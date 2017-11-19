import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;



public class compareDependencies {

	public static void main(String[] args) {
		File inFile = new File(args[0]);
		File inFile2 = new File(args[1]);
		int commonDependencies = 0;

		int c1 = countTotalDependencies(inFile);
		System.out.println("Number of dependencies in " + inFile.getName() + ": " + c1);
		int c2 = countTotalDependencies(inFile2);
		System.out.println("Number of dependencies in " + inFile.getName() + ": " + c2);

		if (c1 > c2) {
			commonDependencies = compareFiles(inFile, inFile2);
		}
		else {
			commonDependencies = compareFiles(inFile2, inFile);
		}
		System.out.println("Common dependencies between " + inFile.getName() + " and " + inFile2.getName() + ": " + commonDependencies);
	}

	//compare files f1 and f2
	public static int compareFiles(File f1, File f2) {
		int commonDependencies = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f1));
			String line = br.readLine();
			while (line != null) {
				if (searchForDependency(line, f2)) {
					commonDependencies++;
				}
				line = br.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return commonDependencies;
	}

	public static boolean searchForDependency(String d, File f) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			while (line != null) {
				if (line.equals(d)) 
					return true;
				line = br.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	//count the number of dependencies in file f (number of lines in the dependency file)
	public static int countTotalDependencies(File f) {
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			//BufferedReader br2 = new BufferedReader(new FileReader(f2));

			//String line1 = br.readLine();
			//String line2 = br2.readLine();
			while (br.readLine() != null) {
				count++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
}