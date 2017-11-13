import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DependencyExtractor {
	
	public static PrintWriter output;

	static int totalFiles = 0;
	
	public static void main(String[] args) {
		
		File inFile = new File(args[0]);
		File outFile = new File("./output.txt");
		
		try {
			output = new PrintWriter(outFile);
			output.println("Include File Dependency Extraction");
			System.out.println("Starting dependency scan...");
			
			listFilesForFolder(inFile);
			
			output.close();

			System.out.println("Dependency scan complete with " + totalFiles + " files scanned.");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void listFilesForFolder(File folder) {
		
		List<String> list = new ArrayList<>();
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			}
			else {
				
				String fileName = fileEntry.getPath();
				
				if (fileName.endsWith(".cc") || fileName.endsWith(".cpp") || 
					fileName.endsWith(".c")|| fileName.endsWith(".h") ||
					fileName.endsWith(".java")) {
				
					totalFiles++;
					
					try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
						String line;
						
						line = br.readLine();
						while (line != null) {
							if (line.startsWith("#include")) {
								list.add(line.substring(10, line.length() - 3));
							}
							else if (line.startsWith("import")) {
								list.add(line.substring(7, line.length() - 1));
							}
							line = br.readLine();
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					
					for (String l : list) {
						//String include_string = l.substring(10, l.length() - 3);
						String outString = fileEntry.getPath() + " -> " + l;
						output.println(outString);
					}
					list.clear();
				}
			}
		}
	}
}