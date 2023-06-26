import java.io.IOError;
import java.io.Serializable;
import javax.swing.JPanel;

public class Test extends JPanel implements Serializable {

	public static final String someString = "I am a string";

	public static String anotherString = "I am another string";

	final Integer someInt = 41;

	private Integer uninitializedInt;

	private static final float someFloat = 1234.5678f;

	enum Days {
		Mon,
		Tue,
		Sun
	}

	private native static Integer nativeFunc(String s, Object o);

	public static void main(String[] args) {
		// a simple test file
		
		String a = "Hello World!";
		String[] elts = a.split(" ");
		
		for(int i = 0; i < elts.length; i++) {
			System.out.print(elts[i] + " ");
		}
		
		System.out.println("");
		
		System.out.println("Elements count: " + convert(elts.length));
		System.out.println("String length" + a.length());

		try {
			System.out.println("Float value:" + someFloat);
		} catch(IOError e) {
			System.out.println("Error that should not occur.");
		}

		hasVarargs("test", "arg1", 2, 'c', 1.23456f, Days.Mon);
	}

	public static String convert(int val) throws Error {
		return String.valueOf(val);
	}

	public static int convert(String val) throws OutOfMemoryError {
		return Integer.valueOf(val);
	}

	private static void hasVarargs(String name, Object ... args) {
		System.err.println(name);
		for(Object arg : args) {
			System.err.println(arg);
		}
	}
}
