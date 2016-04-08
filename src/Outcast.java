import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet net;

    public Outcast(WordNet wordnet) {
        net = wordnet;       
    }
    
    public String outcast(String[] nouns) {
        String outcast = null;
        int max = 0;
        for (String nounA : nouns) {
            int dist = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    dist += net.distance(nounA, nounB);
                }
            }
            if (dist > max) {
                max = dist;
                outcast = nounA;
            }
        }
        return outcast;
    
    }
   public static void main(String[] args) {
        long start = System.nanoTime();
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        long end = System.nanoTime();
        System.out.println("Duration: " + (end - start) / 1000000);
    }
}