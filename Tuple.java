public class Tuple{
    private int docID;
    private int termFreq;

    public Tuple(int docID, int termFreq){
        this.docID = docID;
        this.termFreq = termFreq;
    }

    public int getDocID(){
        return docID;
    }

    public int getTermFreq(){
        return termFreq;
    }

    public void setDocID(int newDocID){
        docID = newDocID;
    }

    public void setTermFreq(int newTermFreq){
        termFreq = newTermFreq;
    }
    public String toString(){
        return "(" + docID + ", " + termFreq + ")";
    }
}
