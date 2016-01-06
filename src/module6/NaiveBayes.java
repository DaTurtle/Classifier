package module6;

import java.util.ArrayList;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class NaiveBayes {

    private DocumentStore docs;
    private String[] classes;
    private double smoothing;
    private int minOccurrences;
    private double amountOfCondProbsToUse;

    public NaiveBayes(DocumentStore ds) {
        docs = ds;
    }

    public NaiveBayes(double smoothing, int minOccurrences, double amountOfCondProbsToUse) {
        docs = new DocumentStore();
        this.smoothing = smoothing;
        this.minOccurrences = minOccurrences;
        this.amountOfCondProbsToUse = amountOfCondProbsToUse;
    }

    public void train(String... classes) { //TODO efficienter maken dan de aardappel die het nu is...

        this.classes = classes;
        //assert classes.length = docs.getnr();
        String[] vocab = docs.getVocab();


        double[] prior = new double[classes.length];
        double[][] condprob = new double[vocab.length][classes.length];
        int n = docs.getnr();
        ArrayList<Double> condprobs = new ArrayList<>();

        for (int i = 0; i < classes.length; i++) {
            System.out.println("Training class: " +  i);
            int docsInClass = docs.countDocsInClass(classes[i]);
            prior[i] = (double) docsInClass/ (double) n;
            System.out.println("prior: " + i+ " = " + docsInClass +" / " + n);
            double sumOfTokensInVocab = 0;
            for (int j = 0; j < vocab.length; j++) {
                if (!(docs.getWordcount(vocab[j]) < minOccurrences)) {
                    sumOfTokensInVocab += docs.countTokensOfTermInClass(vocab[j], classes[i]) + 1;
                }
            }
            for (int k = 0; k < vocab.length; k++) {
                double condprobby = (double) (docs.countTokensOfTermInClass(vocab[k], classes[i]) + 1) / sumOfTokensInVocab;
                if (!(docs.getWordcount(vocab[k]) < minOccurrences)) {
                    condprob[k][i] = condprobby;
                    condprobs.add(condprobby);
                } else {
                    condprob[k][i] = -1.0;
                }
                System.out.println("process: " +k +"/" + vocab.length);
            }
        }

        docs.setCondprobs(condprobs);
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
        Integer[] tokenLocations = getTokenLocations(filtered);
        double[] score = new double[classes.length];
        for (int i = 0; i < classes.length; i++) {
            score[i] = Utils.log2(docs.getPrior(i));
            for (int token : tokenLocations) {
                if (token > 0) {
                    double condprob = docs.getCondprob(token, i);
                    if ( condprob >= docs.getCondprobsPercent(amountOfCondProbsToUse)) {
                        score[i] += Utils.log2(condprob);
                    } else {
                        condprob = smoothing / (docs.countTokensInClass(classes[i]) + smoothing * docs.getVocab().length);
                        score[i] += Utils.log2(condprob);
                    }
                } else {
                    double condprob = smoothing / (docs.countTokensInClass(classes[i]) + smoothing * docs.getVocab().length);
                    score[i] += Utils.log2(condprob);
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
        res += " " +docs.getPrior(0);
        return res;
    }
}
