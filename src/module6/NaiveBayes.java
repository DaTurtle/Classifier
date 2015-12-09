package module6;

import java.util.ArrayList;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class NaiveBayes {

    private DocumentStore docs;
    private String[] classes;

    public NaiveBayes(DocumentStore ds) {
        docs = ds;
    }

    public NaiveBayes() {
        docs = new DocumentStore();
    }


    public void train(String... classes) { //TODO efficienter maken dan de aardappel die het nu is...

        this.classes = classes;
        //assert classes.length = docs.getnr();
        String[] vocab = docs.getVocab();


        double[] prior = new double[classes.length];
        double[][] condprob = new double[vocab.length][classes.length];
        int n = docs.getnr();

        for (int i = 0; i < classes.length; i++) {
            System.out.println("Training class: " +  i);
            int docsInClass = docs.countDocsInClass(classes[i]);
            prior[i] = (double) docsInClass/ (double) n;
            System.out.println("prior: " + i+ " = " + docsInClass +" / " + n);
            double sumOfTokensInVocab = 0;
            for (int j = 0; j < vocab.length; j++) {
                sumOfTokensInVocab += docs.countTokensOfTermInClass(vocab[j], classes[i]) + 1;
            }
            for (int k = 0; k < vocab.length; k++) {
                System.out.println("process: " +k +"/" + vocab.length);
                condprob[k][i] = (double) (docs.countTokensOfTermInClass(vocab[k], classes[i]) + 1) / sumOfTokensInVocab;
            }
        }

        docs.setPrior(prior);
        docs.setCondprob(condprob);
        System.out.println("condprob:");
        for (double[] d : condprob) {
            for (double dd : d) {
                System.out.print(" " + dd);
            }
            System.out.println();
        }
    }

    public DocumentStore getDocumentStore() {
        return docs;
    }

    public Integer[] getTokenLocations(String[] normalised) {
        ArrayList<Integer> tokenLocations = new ArrayList<>();
        for (String normal : normalised) {
            int loc = docs.getIndexOfToken(normal);
            if (loc >= 0) {
                tokenLocations.add(loc);
//                System.out.println(normal + " : " + loc);
            }
        }
        Integer[] res = new Integer[tokenLocations.size()];
        return tokenLocations.toArray(res);
    }

    public String estimate(String document) {
        Integer[] tokenLocations = getTokenLocations(DocumentStore.normalizeString(document));
        double[] score = new double[classes.length];
        for (int i = 0; i < classes.length; i++) {
            score[i] = Math.log10(docs.getPrior(i));
            for (int token : tokenLocations) {
                score[i] += Math.log10(docs.getCondprob(token, i));
            }
        }

        int max = 0;
        for (int i = 1; i < score.length; i++) {
            max = Math.max(max, i);
        }
        String res = "Output = " + classes[max] + "  Scores: ";
        for (int i = 0; i < score.length; i++) {
            res += classes[i] + " " + score[i] + ", ";
        }
        return res;
    }
}
