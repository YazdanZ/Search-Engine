package finalproject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchEngine {
    public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)
    public MyWebGraph internet;
    public XmlParser parser;

    public SearchEngine ( String filename ) throws Exception {
        this.wordIndex = new HashMap<String, ArrayList<String>> ();
        this.internet = new MyWebGraph ();
        this.parser = new XmlParser ( filename );
    }

    /*
     * This does a graph traversal of the web, starting at the given url.
     * For each new page seen, it updates the wordIndex, the web graph,
     * and the set of visited vertices.
     *
     */
    public void crawlAndIndex ( String url ) throws Exception {

        // add a vertex for the current url
        this.internet.addVertex ( url );
        this.internet.setVisited ( url, true );

        // update the wordIndex
        ArrayList<String> words = this.parser.getContent ( url );
        for (String word : words) {
            // we will consider our searches to be case insensitive
            word = word.toLowerCase ();

            if (this.wordIndex.containsKey ( word )) {
                if (!this.wordIndex.get ( word ).contains ( url ))
                    this.wordIndex.get ( word ).add ( url );
            } else {
                ArrayList<String> values = new ArrayList<> ();
                values.add ( url );
                this.wordIndex.put ( word, values );
            }
        }

        ArrayList<String> outLinks = this.parser.getLinks ( url );
        for (String outlink : outLinks) {
            this.internet.addVertex ( outlink );
            this.internet.addEdge ( url, outlink );

            if (!this.internet.getVisited ( outlink )) {
                crawlAndIndex ( outlink );
            }
        }

    }


    /*
     * This computes the pageRanks for every vertex in the web graph.
     * It will only be called after the graph has been constructed using
     * crawlAndIndex().
     * To implement this method, refer to the algorithm described in the
     * assignment pdf.
     *
     * This method will probably fit in about 30 lines.
     */
    public void assignPageRanks ( double epsilon ) {
        // TODO : Add code here
    }

    /*
     * The method takes as input an ArrayList<String> representing the urls in the web graph
     * and returns an ArrayList<double> representing the newly computed ranks for those urls.
     * Note that the double in the output list is matched to the url in the input list using
     * their position in the list.
     */
    public ArrayList<Double> computeRanks ( ArrayList<String> vertices ) {
        // TODO : Add code here
        return null;
    }


    /* Returns a list of urls containing the query, ordered by rank
     * Returns an empty list if no web site contains the query.
     *
     * This method should take about 25 lines of code.
     */
    public ArrayList<String> getResults ( String query ) {
        // TODO: Add code here

        return null;
    }
}
