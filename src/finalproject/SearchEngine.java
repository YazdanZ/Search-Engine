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

            if (!this.internet.getVisited ( outlink ))
                crawlAndIndex ( outlink );
        }
    }


    /*
     * This computes the pageRanks for every vertex in the web graph.
     * It will only be called after the graph has been constructed using
     * crawlAndIndex().
     */
    public void assignPageRanks ( double epsilon ) {
        // start by initializing page ranks to 1 for all urls
        ArrayList<String> vertices = this.internet.getVertices ();
        for (String vertice : vertices) {
            this.internet.setPageRank ( vertice, 1 );
        }

        // repeat until convergence
        boolean converge = false;
        while (!converge) {
            ArrayList<Double> computedRanks = computeRanks ( vertices );

            boolean allConverge = true;
            for (int i = 0; i < computedRanks.size (); i++) {
                double diff = this.internet.getPageRank ( vertices.get ( i ) ) - computedRanks.get ( i );
                if (!(Math.abs ( diff ) < epsilon)) {
                    allConverge = false;
                }
                this.internet.setPageRank ( vertices.get ( i ), computedRanks.get ( i ) );
            }
            converge = allConverge;
        }
    }

    /*
     * The method takes as input an ArrayList<String> representing the urls in the web graph
     * and returns an ArrayList<double> representing the newly computed ranks for those urls.
     * Note that the double in the output list is matched to the url in the input list using
     * their position in the list.
     */
    public ArrayList<Double> computeRanks ( ArrayList<String> vertices ) {
        double dampingFactor = 0.5;
        ArrayList<Double> computedRanks = new ArrayList<> ();
        for (String vertice : vertices) {
            double coef = 0;
            ArrayList<String> inVertices = this.internet.getEdgesInto ( vertice );
            for (String inVertice : inVertices) {
                coef += this.internet.getPageRank ( inVertice ) / this.internet.getOutDegree ( inVertice );
            }
            double rank = (1 - dampingFactor) + dampingFactor * coef;
            computedRanks.add ( rank );
        }
        return computedRanks;
    }


    /* Returns a list of urls containing the query, ordered by rank
     * Returns an empty list if no web site contains the query.
     */
    public ArrayList<String> getResults ( String query ) {
        query = query.toLowerCase ();
        ArrayList<String> results = new ArrayList<> ();

        // return an empty list if no website contains the query
        if (!this.wordIndex.containsKey ( query ))
            return results;

        // get urls containing the word
        ArrayList<String> urls = this.wordIndex.get ( query );
        HashMap<String, Double>  unsortedResults = new HashMap<> ();

        for (String url : urls)
            unsortedResults.put ( url, this.internet.getPageRank ( url ) );

        results = Sorting.fastSort ( unsortedResults );
        return results;
    }
}
