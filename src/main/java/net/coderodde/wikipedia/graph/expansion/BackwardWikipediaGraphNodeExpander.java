package net.coderodde.wikipedia.graph.expansion;

import java.util.List;

/**
 * This class implements a backward node expander in the Wikipedia article 
 * graph. If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>A</tt> whenever asked to process <tt>B</tt>. We can say that this 
 * expander traverses each directed arc from head to tail.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Mar 19, 2024)
 */
public class BackwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {
    
    public BackwardWikipediaGraphNodeExpander(final String wikipediaUrl) {
        super(wikipediaUrl);
    }
    
    @Override
    public List<String> generateSuccessors(final String node) {
        return baseGetNeighbors(node, false);
    }
}
