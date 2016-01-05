package module6;

import java.util.ArrayList;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class NaiveBayes {

    private DocumentStore docs;
    private String[] classes;
    private static final double SMOOTHING = 1;
    private static final int minOccurrences = 25;

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
            tokenLocations.add(loc);
//                System.out.println(normal + " : " + loc);
        }
        Integer[] res = new Integer[tokenLocations.size()];
        return tokenLocations.toArray(res);
    }

    public String estimate(String document) {
        String[] normalized = DocumentStore.normalizeString(document);
        String[] filtered = DocumentStore.filter(normalized);
        String[] pruned = docs.removeRareWords(minOccurrences, filtered);
        Integer[] tokenLocations = getTokenLocations(pruned);
        double[] score = new double[classes.length];
        for (int i = 0; i < classes.length; i++) {
            score[i] = Math.log10(docs.getPrior(i));
            for (int token : tokenLocations) {
                if (token > 0) {
                    score[i] += Math.log10(docs.getCondprob(token, i));
                } else {
                    double condProb = SMOOTHING / (docs.countTokensInClass(classes[i]) + SMOOTHING * docs.getVocab().length);
                    score[i] += Math.log10(condProb);
                }
            }
        }

        int max = 0;
        for (int i = 1; i < score.length; i++) {
            if (score[i] > score[max]) {
                max = i;
            }
        }
        String res = "Output = " + classes[max] + "  Scores: ";
        for (int i = 0; i < score.length; i++) {
            res += classes[i] + " " + score[i] + ", ";
        }
        return res;
    }
}
