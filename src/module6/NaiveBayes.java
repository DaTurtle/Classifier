package module6;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class NaiveBayes {

    private DocumentStore docs;

    public NaiveBayes(DocumentStore ds) {
        docs = ds;
    }

    public NaiveBayes() {
        docs = new DocumentStore();
    }


    public void train(String[] classes) {

        //assert classes.length = docs.getnr();
        String[] vocab = docs.getVocab();


        double[] prior = new double[classes.length];
        double[][] condprob = new double[vocab.length][classes.length];
        int n = docs.getnr();

        for (int i = 0; i < classes.length; i++) {
            int docsInClass = docs.countDocsInClass(classes[i]);
            prior[i] = (double) docsInClass/ (double) n;
                for (int k = 0; k < vocab.length; k++) {

                    double sumOfTokensInVocab = 0;
                    for (int j = 0; j < vocab.length; j++) {
                        sumOfTokensInVocab += docs.countTokensOfTermInClass(vocab[j], classes[i] + 1);
                    }
                    condprob[k][i] = (double) (docs.countTokensOfTermInClass(vocab[k], classes[i])) / sumOfTokensInVocab;
                }
        }

        docs.setPrior(prior);
        docs.setCondprob(condprob);
    }

    public void estimate() {

    }
}
