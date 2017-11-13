





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
	public static void main(String[] args) {
	
		File outFile = new File("C:\\Users\\mcmaceac\\Desktop\\output.txt");
		File inFile = new File("C:\\Users\\mcmaceac\\Desktop\\mysql-server-mysql-8.0.2");
		try {
			output = new PrintWriter(outFile);
			output.println("Include File Dependency Extraction");
			listFilesForFolder(inFile);
			
			output.close();
		}
		catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	public static void listFilesForFolder(File folder) {
		List<String> list = new ArrayList<>();
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//recursively call this method to list the files in the directory
				listFilesForFolder(fileEntry);
			}
			else {
				String fileName = fileEntry.getPath();
				
				//only print the dependencies of the .cc and .c and .cpp files
				if (fileName.endsWith(".cc") || fileName.endsWith(".cpp") || fileName.endsWith(".c")|| fileName.endsWith(".h")) {
				
					
					/*try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
						list = stream.filter(line -> line.startsWith("#include")).collect(Collectors.toList());
					}*/
					try (BufferedReader br = new BufferedReader(new FileReader(fileEntry))) {
						String line;
						while ((line = br.readLine()) != null) {
							if (line.startsWith(("#include"))) {
								list.add(line);
							}
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					
					for (String l : list) {
						// 0123456789
						// #include <
						//this statement includes only the name of the file being included
						//ie it does not include #include < and removes the .h> from the file name
						String include_string = l.substring(10, l.length() - 3);
						String outString = fileEntry.getPath() + " -> " + include_string;
						output.println(outString);
					}
					list.clear();
				}
			}
		}
	}
}