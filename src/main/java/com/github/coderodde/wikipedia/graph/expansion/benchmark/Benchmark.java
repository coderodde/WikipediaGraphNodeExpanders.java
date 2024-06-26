package com.github.coderodde.wikipedia.graph.expansion.benchmark;

import java.util.List;
import com.github.coderodde.wikipedia.graph.expansion.BackwardWikipediaGraphNodeExpander;
import com.github.coderodde.wikipedia.graph.expansion.ForwardWikipediaGraphNodeExpander;

public final class Benchmark {
    
    public static void main(String[] args) throws Exception {
        System.out.println("[Forward expansion]");
        
        long start;
        long end;
        
        start = System.currentTimeMillis();
        
        List<String> forwardLinks = 
                new ForwardWikipediaGraphNodeExpander("en")
                        .getNeighbors("Life");
        
        end = System.currentTimeMillis();
        
        System.out.printf("Forward request in %d milliseconds.\n", end - start);
        
        forwardLinks.forEach(System.out::println);
        
        System.out.println("[Backward expansion]");
        
        start = System.currentTimeMillis();
       
        List<String> backwardLinks = 
                new BackwardWikipediaGraphNodeExpander("en")
                        .getNeighbors("Life");
        
        end = System.currentTimeMillis();
        
        System.out.printf("Backward request in %d milliseconds.\n", end - start);
        
        backwardLinks.forEach(System.out::println);
    }
}
