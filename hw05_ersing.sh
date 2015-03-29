# hw05_ersing.sh
#
# This is the driver program for my search engine (Snoogle).
# It calls the Java program which outputs a text file for each
# of the queries in queries.txt. This script then sorts and 
# trims each of the score files mentioned above.

queryCount="$( wc -l < queries.txt )"

# run Snoogle to calculate BM25 scores
java Snoogle

# sort & trim each scores file
for (( i=1; i<$queryCount; i++ ))
do
    inFile=q"${i}"_scores.txt
    outFile=q"${i}"_top100.txt
    sort -t $'\t' -rnk4 "$inFile" | head -n 100 > "$outFile"
    awk -F"\t" '{print $1,$2,$3,FNR,$4,$5,$6}' OFS="\t" "$outFile" > temp.txt
    mv temp.txt "$outFile"
done

# clean up
rm q*_scores.txt
