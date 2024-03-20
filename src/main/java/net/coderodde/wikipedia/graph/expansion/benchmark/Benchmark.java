package net.coderodde.wikipedia.graph.expansion.benchmark;

import java.util.List;
import net.coderodde.wikipedia.graph.expansion.BackwardWikipediaGraphNodeExpander;
import net.coderodde.wikipedia.graph.expansion.ForwardWikipediaGraphNodeExpander;

public final class Benchmark {
    
    public static void main(String[] args) {
        System.out.println("[Forward expansion]");
        
        long start = System.currentTimeMillis();
        
        List<String> forwardLinks = 
                new ForwardWikipediaGraphNodeExpander("fi")
                        .generateSuccessors("Bugatti");
        
        long end = System.currentTimeMillis();
        
        System.out.printf("Forward request in %d milliseconds.\n", end - start);
        
        forwardLinks.forEach(System.out::println);
        
        System.out.println("[Backward expansion]");
        
        start = System.currentTimeMillis();
        
        List<String> backwardLinks = 
                new BackwardWikipediaGraphNodeExpander("fi")
                        .generateSuccessors("Bugatti");
        
        end = System.currentTimeMillis();
        
        System.out.printf("Backward request in %d milliseconds.\n", end - start);
        
        backwardLinks.forEach(System.out::println);
    }
}
