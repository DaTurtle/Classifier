package module6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class DocumentStore {

    private double[] prior;
    private double[][] condprob;

    private boolean trained;
    private boolean priorB;

    private int nrOfDocuments;

    //Lijst van Stringarrays waarvan de eerste string de classname is.
    private ArrayList<String[]> documents;
    private ArrayList<String> vocab;

    public static String[] normalizeString(String text) {
        return text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
    }

    public static String[] concat(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c = new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }


    public DocumentStore() {
        trained = false;
        priorB = false;
        nrOfDocuments = 0;
    }

    public void addDocument(String document, String cls) {
        String[] normalized = normalizeString(document);
        for (String token : normalized) {
            if (!vocab.contains(token)) {
                vocab.add(token);
            }
        }
        documents.add(concat(new String[]{cls}, normalized));
        nrOfDocuments++;
    }

    public void setPrior(double[] prior) {
        this.prior = prior;
        priorB = true;
    }

    public void setCondprob(double[][] condprob) {
        if(priorB) {
            this.condprob = condprob;
            trained = true;
        }
    }

    public int getIndexOfToken(String token) {
        return vocab.indexOf(token);
    }

    public String[] getVocab() {
        String[] res = new String[vocab.size()];
        return vocab.toArray(res);
    }

    public  int getnr() {
        return nrOfDocuments;
    }

    public int countDocsInClass(String cls) {
        int res = 0;
        for (String[] doc : documents) {
            if(doc[0].equals(cls)){
                res++;
            }
        }
        return res;
}

    public int countTokensOfTermInClass(String token, String classValue) {
        int res = 0;
        for (String[] doc : documents) {
            if (doc[0].equals(classValue)) {
                for (int i = 1; i < doc.length; i++) { //SKIP CLASSNAME
                    if (doc[i].equals(token)) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    public double getPrior(int cls) {
        if (trained) {
            return prior[cls];
        } else {
            return -1;
        }
    }

    public double getCondprob(int token, int cls) {
        if (trained) {
            return condprob[token][cls];
        } else {
            return -1;
        }
    }
}
