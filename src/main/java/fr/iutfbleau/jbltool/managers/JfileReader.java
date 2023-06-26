package fr.iutfbleau.jbltool.managers;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class JfileReader {

	/**
	 * Avoid instanciation.
	 */
	private JfileReader() {
	}

	/**
	 * Read the content of a file a return it.
	 * @param inputFile
	 * File to get content from.
	 * @return
	 * The content of the file.
	 * @throws IOException
	 * If the file cannot be read or a I/O error occurs.
	 */
	public static String readFile(File inputFile) throws IOException{
		Objects.requireNonNull(inputFile);

		FileInputStream  input = new FileInputStream(inputFile);
		String content = new String(input.readAllBytes());
		input.close(); // dont forget to close it :D

		return content; // return file as it is
	}
}