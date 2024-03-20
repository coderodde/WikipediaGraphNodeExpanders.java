package net.coderodde.wikipedia.graph.expansion;

import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class BackwardWikipediaGraphNodeExpanderTest {
    
    private BackwardWikipediaGraphNodeExpander generateSuccessorser;
    
    @Test
    public void testExpand1() {
        generateSuccessorser = 
                new BackwardWikipediaGraphNodeExpander("en");
        
        assertTrue(generateSuccessorser.isValidNode("Disc_jockey"));
        assertTrue(!generateSuccessorser.generateSuccessors("Disc_jockey").isEmpty());
        
        assertFalse(generateSuccessorser.isValidNode("Disc_jfdsfsockey"));
        assertTrue(generateSuccessorser.generateSuccessors("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test
    public void testExpand2() {    
        generateSuccessorser = 
                new BackwardWikipediaGraphNodeExpander("en");
        
        assertTrue(generateSuccessorser.isValidNode("DJ"));
        assertTrue(!generateSuccessorser.generateSuccessors("DJ").isEmpty());
        
        assertFalse(generateSuccessorser.isValidNode("DJJJfdsfJJ"));
        assertTrue(generateSuccessorser.generateSuccessors("DJJJfdsafd").isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL1() {
        new BackwardWikipediaGraphNodeExpander("shit");
    }
}
