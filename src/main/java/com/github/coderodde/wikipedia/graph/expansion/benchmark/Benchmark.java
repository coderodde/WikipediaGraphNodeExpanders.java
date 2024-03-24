package com.github.coderodde.wikipedia.graph.expansion.benchmark;

import java.util.List;
import com.github.coderodde.wikipedia.graph.expansion.BackwardWikipediaGraphNodeExpander;
import com.github.coderodde.wikipedia.graph.expansion.ForwardWikipediaGraphNodeExpander;
import java.io.IOException;
import java.net.MalformedURLException;

public final class Benchmark {
    
    public static void main(String[] args) throws MalformedURLException, 
                                                  IOException {
        System.out.println("[Forward expansion]");
        
        long start = System.currentTimeMillis();
        
        List<String> forwardLinks = 
                new ForwardWikipediaGraphNodeExpander("fi")
                        .getNeighbors("Bugatti");
        
        long end = System.currentTimeMillis();
        
        System.out.printf("Forward request in %d milliseconds.\n", end - start);
        
        forwardLinks.forEach(System.out::println);
        
        System.out.println("[Backward expansion]");
        
        start = System.currentTimeMillis();
        
        List<String> backwardLinks = 
                new BackwardWikipediaGraphNodeExpander("fi")
                        .getNeighbors("Bugatti");
        
        end = System.currentTimeMillis();
        
        System.out.printf("Backward request in %d milliseconds.\n", end - start);
        
        backwardLinks.forEach(System.out::println);
    }
}
