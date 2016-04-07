import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private final SAP sap;
    private final Map<Integer, String> idToSynset;
    private final Map<Integer, Set<Integer>> nounToIds;
    
   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) throw new NullPointerException("no synsets");
        if (hypernyms == null) throw new NullPointerException("no hypernyms");
        initSynsets(synsets);
        Digraph graph = initHypernyms(hypernyms);
        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle() || !rootedDAG(graph)) {
            throw new IIllegalArgumentException("not a rooted DAG");
        }
        sap = new SAP(graph);    
    }
    
    private boolean rootedDAG(Digraph g) {
        int roots = 0;
        for (int i = 0; i<g.V(); i++) {
            if (!g.adj(i).iterator.hasNext()) {
                roots += 1;
                if (roots > 1) return false;
            }
        }
        return roots == 1;
    }
    
    private void initSynsets(String synset) {
        In file = new In(sysnset);
        while (file.hasNextline()) {
            String[] line = file.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String words = line[1];
            String[] nouns = words.split(" ");
      
            for (String noun:nouns) {
                if (nounToIds.containsKey(noun)) {
                    nounToIds.get(noun).add(id);
                } else {
                    Set<Integer> ids = new HashSet<Integer>();
                    ids.add(id);
                    nounToIds.put(noun, ids);
                }
            }
        }
    
    }
    
    private Digraph initHypernyms(String hypernyms) {
        Digraph graph = new Digraph(idToSynset.size());
        In file = new In(hypernyms);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            int start = Integer.valueOf(line[0]);
            for (int i = 1; i< line.length; i++) {
                graph.addEdge(start, Integer.valueOf(line[i]));
            }
        }
        return graph;   
    }

   // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null || "".equals(word)) {
            return false;
        }
        return nounToIds.containsKey(word);
    }

   // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Both words should be valid nouns!");
        }
        Set<Integer> idsOfNounA = nounToIds.get(nounA);
        Set<Integer> idsOfNounB = nounToIds.get(nounB);
        return sap.length(idsOfNounA, idsOfNounB);    
    }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Both words should be valid nouns!");
        }
        Set<Integer> idsOfNounA = nounToIds.get(nounA);
        Set<Integer> idsOfNounB = nounToIds.get(nounB);
        int ancestor = sap.ancestor(idsOfNounA, idsOfNounB);
        return idToSynset.get(ancestor);
    
    }

   // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0]. args[1]);
        while (!Stdin.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            if (!wordNet.isNoun(v)) {
                StdOut.println(v + " not in the word net");
                continue;
            }
            if (!wordNet.isNoun(w)) {
                StdOut.println(w + " not in the word net");
                continue;
            }
            int distance = wordNet.distance(v, w);
            String ancestor = wordNet.sap(v, w);
            StdOut.printf("distance = %d, ancestor = %s \n", distance, ancestor);
        }
    }
}