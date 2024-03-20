package net.coderodde.wikipedia.graph.expansion.benchmark;

import java.util.List;
import net.coderodde.wikipedia.graph.expansion.BackwardWikipediaGraphNodeExpander;
import net.coderodde.wikipedia.graph.expansion.ForwardWikipediaGraphNodeExpander;

public final class Benchmark {
    
    private static final String START_PAGE_TITLE = 
            "https://en.wikipedia.org/wiki/Bugatti";
    
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        
        List<String> forwardLinks = 
                new ForwardWikipediaGraphNodeExpander("en")
                        .generateSuccessors("Bugatti");
        
        long end = System.currentTimeMillis();
        
        System.out.printf("Forward request in %d milliseconds.\n", end - start);
        
        forwardLinks.forEach(System.out::println);
        
        start = System.currentTimeMillis();
        
        List<String> backwardLinks = 
                new BackwardWikipediaGraphNodeExpander(START_PAGE_TITLE)
                        .generateSuccessors("Bugatti");
        
        end = System.currentTimeMillis();
        
        System.out.printf("Backward request in %d milliseconds.\n", end - start);
        
        forwardLinks.forEach(System.out::println);
    }
}
