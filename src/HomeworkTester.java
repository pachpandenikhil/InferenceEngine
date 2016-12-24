import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Test;

public class HomeworkTester {
	
	/**
	 * Reads the file content into a string.
	 * @param path the file path.
	 * @param encoding the encoding to be used.
	 * @return the file contents.
	 * @throws IOException File reading exceptions.
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	/**
	 * Validates the inference engine results against ground truth.
	 */
	@Test
	public void testHomework() {
		
		long startTime = System.currentTimeMillis();
		int testCases = 13;
		int startTestCase = 1;
		for(int i = startTestCase; i <= testCases; i++) {
			
			File testcaseInputFile = new File("C:\\InferenceEngine\\bin\\input" + i + ".txt");
			File inputFile = new File("C:\\InferenceEngine\\bin\\input.txt");
			if(testcaseInputFile.renameTo(inputFile)) {
				homework.main(null);
				File testcaseOutputFile = new File("C:\\InferenceEngine\\testing\\testcases1\\output" + i + ".txt");
				File outputFile = new File("C:\\InferenceEngine\\bin\\output.txt");
				try {
					String expectedOutput = readFile(testcaseOutputFile.getAbsolutePath(), Charset.defaultCharset());
					String actualOutput = readFile(outputFile.getAbsolutePath(), Charset.defaultCharset());
					assertThat("TestCase "+ i + " failed!",expectedOutput, IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(actualOutput));	
				} catch (IOException e) {
					assertTrue("Exception occured : " + e.getMessage(), false);
				}
				finally {
					inputFile.renameTo(testcaseInputFile);
				}			
				
			} else {
				assertTrue("Failed to rename file!", false);
			}
			//System.out.println("Done");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("That took " + (endTime - startTime) + " milliseconds");
	}
}
