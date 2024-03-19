package net.coderodde.wikipedia.graph.expansion;

import java.util.List;

/**
 * This class implements a forward node expander in the Wikipedia article graph.
 * If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>B</tt> whenever asked to process <tt>A</tt>. We can say that this 
 * expander traverses each directed arc from tail to head.
 * 
 * @version 1.0.0 (Mar 19, 2024)
 */
public class ForwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {

    public ForwardWikipediaGraphNodeExpander(final String wikipediaUrl) {
        super(wikipediaUrl);
    }
    
    @Override
    public List<String> generateSuccessors(String node) {
        return baseGetNeighbors(node, true);
    }
}
