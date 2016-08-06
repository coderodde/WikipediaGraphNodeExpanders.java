package net.coderodde.wikipedia.graph.expansion;

import java.util.List;
import net.coderodde.graph.pathfinding.uniform.delayed.AbstractNodeExpander;

/**
 * This class implements a forward node expander in the Wikipedia article graph.
 * If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>B</tt> whenever asked to process <tt>A</tt>. We can say that this 
 * expander traverses each directed arc from tail to head.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 6, 2016)
 */
public class ForwardWikipediaGraphNodeExpander 
extends AbstractNodeExpander<String> {

    @Override
    public List<String> expand(String node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidNode(String node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
