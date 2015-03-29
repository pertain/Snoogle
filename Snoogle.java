/* Snoogle.java
 *
 * This is the driver for hw05 in csc320. The program is a
 * simple search engine that uses an inverted index and the
 * BM25 algorithm.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.io.IOException;

public class Snoogle {
    private static final String TC_FILE = "tccorpus.txt";
    private static final String Q_FILE = "queries.txt";

    public static void main(String[] args) throws IOException {
        Inverted iIndex = new Inverted();
        iIndex.indexFile(TC_FILE);

        File qFile = new File(Q_FILE);
        BufferedReader qBr = new BufferedReader(new FileReader(qFile));

        /* read Q_FILE into 2D arraylist - my BM25 takes an arraylist of query terms */
        List<List<String>> queries = new ArrayList<List<String>>();
        String qLine;
        String[] qLineContents;
        String[] qTerms;
        while(qBr.ready()){
            qLine = qBr.readLine();
            qLineContents = qLine.split("\t");
            qTerms = qLineContents[1].split(" ");
            queries.add(Arrays.asList(qTerms));
        }

        /* pass each query to BM25 to calculate scores - writes a file for each query */
        for(int i = 0; i < queries.size(); i++){
            String currTerm = "";
            for(String s : queries.get(i)){
                currTerm = currTerm + s + " ";
            }
            //currTerm = currTerm.substring(0, currTerm.length()-1);
            String scoresFilename = "q" + (i+1) + "_scores.txt";
            File scoresFile = new File(scoresFilename);
            if(scoresFile.exists()){
                scoresFile.delete();
                scoresFile.createNewFile();
            }
            BufferedWriter scoresBw = new BufferedWriter(new FileWriter(scoresFile, true));
            for(int j = 0; j < iIndex.getDocQty(); j++){
                BM25 bm = new BM25(iIndex, queries.get(i), j);
                scoresBw.write((i+1) + "\t" + currTerm + "\t" + String.format("%04d", j+1) + "\t" + String.format("%.16f", bm.calcScore()) + "\tSnoogle");
                scoresBw.newLine();
            }
            scoresBw.flush();
            scoresBw.close();
        }
    }
}
