package fr.iutfbleau.jbltool.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ClassManager {
	private File sourceCodeFile;
	private ClassNode classNode;
	private ClassReader clsReader;

	/**
	 * This class manages a .class file, meaning it reads from it, parse it and 
	 * makes available its content.
	 */
	public ClassManager() {
		this.sourceCodeFile = null;
		this.classNode = null;
	}

	/**
	 * Read and parse '.class' file.
	 * @param inputFile
	 * Non null java.io.File to read the class from.
	 * @throws IOException
	 * Thrown if the file cannot be read.
	 **/
	public void readFromFile(File inputFile) throws IOException {
		Objects.requireNonNull(inputFile);
		this.sourceCodeFile = inputFile;
		
		try {
			this.clsReader = new ClassReader(new FileInputStream(inputFile));
		} catch(IllegalArgumentException e) {
			throw new IOException(e.getMessage()); // override error type
		}

		if(this.classNode == null) {
			this.classNode = new ClassNode();
		}
		clsReader.accept(classNode, 0);
	}

	/**
	 * Read and parse '.class' file from the ressources/ folder.
	 * @param inputFilePath
	 * Non null String that represent the path for the file to read the class from.
	 * @throws IOException
	 * Thrown if the file is incorrect or an error occured while reading.
	 * @throws FileNotFoundException
	 * If the file doesn't exist.
	 **/
	public void readFromInternalFile(String inputFilePath) throws IOException, FileNotFoundException {
		Objects.requireNonNull(inputFilePath);

		URL fileURL = this.getClass().getResource(inputFilePath);
		if(fileURL == null) {
			throw new FileNotFoundException("Could not load file.");
		}
		this.readFromFile(new File(fileURL.getFile()));
	}

	/**
	 * Write ClassNode into the File where it was loaded from.
	 * @throws IllegalStateException
	 * If you did not load a class file before.
	 * @throws IOException
	 * If an error occured while writing the file.
	 * @see ClassManager#readFromFile(File)
	 **/
	public void overwriteToFile() throws IllegalStateException, IOException {
		if(this.sourceCodeFile == null) {
			throw new IllegalStateException("You need to load a class before writing it.");
		}
		this.writeToFile(this.sourceCodeFile);
	}
	
	/**
	 * Write ClassNode into a specified file (will be a .class file).
	 * @param outputFile
	 * File to output in.
	 * @throws IllegalStateException
	 * If you did not load a class file before.
	 * @throws IOException
	 * If an error occured while writing the file.
	 * @see ClassManager#readFromFile(File)
	 **/
	public void writeToFile(File outputFile) throws IllegalStateException, IOException {
		if(this.sourceCodeFile == null || this.classNode == null) {
			throw new IllegalStateException("You need to load a class before writing it.");
		}
		
		// parse class into a writable format
		ClassWriter clsWriter = new ClassWriter(0);
		this.classNode.accept(clsWriter);

		// write content
		byte[] b = clsWriter.toByteArray();
		FileOutputStream writer  = new FileOutputStream(outputFile);
		writer.write(b);
		writer.close();
	}

	/** Get the ClassNode representing the class (if one was loaded).
	 * @return ClassNode representing the loaded class (if one loaded).
	 * @see ClassManager#readFromFile(File)
	 **/
	public ClassNode getClassNode() {
		return this.classNode;
	}

	/** Get the File where the class file was loaded from (if one was loaded).
	 * @return The file where the class was loaded.
	 * @see ClassManager#readFromFile(File)
	 **/
	public File getSourceCodeFile() {
		return this.sourceCodeFile;
	}
}
