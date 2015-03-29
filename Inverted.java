/* Inverted.java
 *
 * This program reads in a text file (in a specific format) and
 * creates an inverted index. The format is a HashMap of ArrayList<Tuple>,
 * where the Tuples contain docID and termFreq.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.io.IOException;

public class Inverted {

    /* HashMap of ArrayLists - each key holds an ArrayList of (docID,termFreq) Tuples */
    Map<String, List<Tuple>> postings = new HashMap<String, List<Tuple>>();

    /* ArrayList of document lengths - used in BM25 */
    List<Integer> dLens = new ArrayList<Integer>();

    /* read in file and create inverted index */
    public void indexFile(String filename) throws IOException {
        File tcFile = new File(filename);
        BufferedReader tcBr =  new BufferedReader(new FileReader(tcFile));

        int dLen = 0;
        int docID = 0;
        while(tcBr.ready()){
            String tcLine = tcBr.readLine();
            String[] lineContents = tcLine.split(" ");
            if(lineContents[0].equals("#")){
                if(dLen != 0){
                    dLens.add(dLen);
                }
                dLen = 0;
                docID = Integer.parseInt(lineContents[1]);
            }
            if(!lineContents[0].equals("#")){
                for(String word : lineContents){
                    dLen++;
                    String key = word;
                    List<Tuple> values = postings.get(key);
                    if(values == null){
                        values = new ArrayList<Tuple>();
                        values.add(new Tuple(docID, 1));
                        postings.put(key, values);
                    }else{
                        boolean containsID = false;
                        for(Tuple value : values){
                            Tuple currTup = value;
                            int currID =  currTup.getDocID();
                            int currFreq = currTup.getTermFreq();
                            if(currID == docID){
                                containsID = true;
                                currFreq++;
                                currTup.setTermFreq(currFreq);
                                break;
                            }
                        }
                        if(!containsID){
                            values.add(new Tuple(docID, 1));
                        }
                    }
                }
            }
        }
        dLens.add(dLen);
        tcBr.close();
    }

    /* getter for values ArrayList */
    public List<Tuple> getValues(String keyVal){
        return postings.get(keyVal);
    }

    /* getter for length of a given index */
    public int getListLen(String keyVal){
        return postings.get(keyVal).size();
    }

    /* getter for document length of a given docID */
    public int getDocLen(int id){
        return dLens.get(id).intValue();
    }

    /* getter for collection size */
    public int getDocQty(){
        return dLens.size();
    }

    /* getter for avg document length */
    public double getAvgDocLen(){
        int lenSum = 0;
        for(Integer curLen : dLens){
            lenSum += curLen.intValue();
        }
        int avgDocLen = lenSum / dLens.size();
        return avgDocLen;
    }

    /* override toString method - prints entire inverted index */
    public String toString(){
        StringBuilder printout = new StringBuilder();
        for(Map.Entry<String, List<Tuple>> entry : postings.entrySet()){
            String currKey = entry.getKey();
            List<Tuple> currList = entry.getValue();
            printout.append(currKey);
            for(Tuple currTuple : currList){
                printout.append("-");
                printout.append(currTuple);
            }
            printout.append("\n");
        }
        return printout.toString();
    }
}
