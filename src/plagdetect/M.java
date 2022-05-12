
package plagdetect;

import java.io.File;
import java.io.IOException;

public class M {
	public static void main (String args []) throws IOException {
		PlagiarismDetector P = new PlagiarismDetector(3);
		//Collection<String> x = P.getFilenames();
		//Iterator<String> l = x.iterator();
		//String f1 = l.next();
		//String f2 = l.next();
		//P.getNumNGramsInCommon(f1, f2);
		File folder = new File("src\\plagdetect\\docs\\smalldocs");
		P.readFilesInDirectory(folder);
		System.out.println(P.getSuspiciousPairs(3));
	}
}
