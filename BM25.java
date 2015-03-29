/* BM25.java
 *
 * This program calculates the BM25 score for a given document and query.
 */

import java.util.*;
import java.io.IOException;

public class BM25 {
    Inverted index;
    List<String> terms;
    int docID;
    double b = 0.75;
    double k1 = 1.2;
    double k2 = 100;

    public BM25(Inverted index, List<String> queryTerms, int docID){
        this.index = index;
        this.terms = queryTerms;
        this.docID = docID;
    }

    public double calcScore() throws IOException {
        double score = 0;
        double avdl = index.getAvgDocLen();     // avg length of a doc in collection
        double dl = index.getDocLen(docID);     // length of given document
        double K = k1 * ((1 - b) + b * dl / avdl);
        for(String term : terms){
            int fi = 0;
            List<Tuple> values = index.getValues(term);
            for(Tuple value : values){
                if(value.getDocID() == docID){
                    fi = value.getTermFreq();
                    break;
                }
            }
            int qfi = countTermRepeats(terms, term);    // num occurences of term i in query
            int ri = 0;     // set to zero because we have no relevance data
            int R = 0;      // set to zero because we have no relevance data
            int ni = index.getListLen(term);    // num docs containing term i
            int N = index.getDocQty();          // num docs in collection

            double idf = Math.log(((ri + 0.5) / (R - ri + 0.5)) / ((ni - ri + 0.5) / (N - ni - R + ri + 0.5)) + 1);
            score += idf * (((k1 + 1) * fi) / (K + fi)) * (((k2 + 1) * qfi) / (k2 + qfi));
        }
        return score;
    }

    /* calculates qfi - the number of occurences of the given term in the given query */
    private int countTermRepeats(List<String> qTerms, String currTerm){
        int termCount = 0;
        for(String qt : qTerms){
            if(qt.equals(currTerm)){
                termCount++;
            }
        }
        return termCount;
    }

}

