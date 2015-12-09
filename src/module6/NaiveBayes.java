package module6;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class NaiveBayes {

    private DocumentStore docs;
    private Map prior = new HashMap<String, Double>();
    private Map condprob = new HashMap<String, HashMap<String, Double>>();

    public NaiveBayes(DocumentStore ds) {
        docs = ds;
    }

    public NaiveBayes(String... classes) {
        docs = new DocumentStore(classes);
    }


    public void train(String[] classes) {

        //assert classes.length = docs.getnr();
        String[] vocab = docs.getVocab();



        double[] priorii = new double[classes.length];
        double[][] condprobii = new double[vocab.length][classes.length];
        int n = docs.getnr();

        for (int i = 0; i < classes.length; i++) {
            int docsInClass = docs.countDocsInClass(classes[i]);
            prior.put(classes[i], (double) docsInClass/ (double) n);
            priorii[i] = (double) docsInClass/ (double) n;

            for(String token : vocab) {
                int tokencount = docs.countTokensOfTermInClass(token, classes[i]);
                for (int j = 0; j < vocab.length; j++) {
                    condprobii[j][i] = (double) (tokencount+1) / 1; //TODO
                }
            }
        }
    }

    public void estimate() {

    }
}
