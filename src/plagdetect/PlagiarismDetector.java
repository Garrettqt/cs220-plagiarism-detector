package plagdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlagiarismDetector implements IPlagiarismDetector {
	int NgramLength = -1;
	Map<String, Collection<String>> FileNamesToNgrams = new HashMap<String, Collection<String>>();
	Map<String, Map<String, Integer>> results = new HashMap<String, Map<String, Integer>>();

	
	public PlagiarismDetector(int n) {
		NgramLength = n;
	}
	
	@Override
	public int getN() {
		return NgramLength;
	}

	public Collection<String> getFilenames() {
		return results.keySet();
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) throws FileNotFoundException {
		return FileNamesToNgrams.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) throws FileNotFoundException {
		return FileNamesToNgrams().get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		return results;
	}

	@Override
	public void readFile(File file) throws IOException {
		
		Map<String, Integer> fill = new HashMap<String, Integer>();
		for(String name: FileNamesToNgrams.keySet()) {
			Collection<String> N1 = new HashSet<>(FileNamesToNgrams.get(name));
			N1.retainAll(FileNamesToNgrams.get(file.getName()));
			fill.put(name, N1.size());
		}
		results.put(file.getName(), fill);
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) throws FileNotFoundException {
		return results.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) throws FileNotFoundException {
		Collection<String> sus = new HashSet<String>();
		Collection<String> first = results.keySet();
		Collection<String> second = results.keySet();
		for(String f: first) {
			for(String s: second) {
				if(results.get(f).get(s) >= minNgrams && f.compareTo(s) < 0) {
					sus.add(f + " " + s + " " + getNumNGramsInCommon(f , s));
				}
			}
		}
		return sus;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File file : dir.listFiles()) {
			Scanner words = new Scanner(file);
			Set<String> Ngrams = new HashSet<String> ();
			String Ngram = "";
			while(words.hasNext()) {
				String[] w = words.nextLine().split(" ");
			for(int i = 0; i <= w.length - getN(); i++) {
				for(int j = i; j < getN()+i; j++) {
					Ngram += (w[j] + " ");
				}
				Ngrams.add(Ngram.substring(0, Ngram.length() - 1));
				Ngram = "";
			}
			}
			words.close();
			FileNamesToNgrams.put(file.getName(), Ngrams);
		}
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
	
	@Override
	public Map<String, Collection<String>> FileNamesToNgrams() {
		return FileNamesToNgrams;
	}
}
