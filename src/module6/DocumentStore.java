package module6;

import java.io.IOException;
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
    private HashMap<String, HashMap<String, Integer>> wordcountPerClass;
    private HashMap<String, Integer> wordcount;
    private HashMap<String, Integer> docsPerClass;
    private static String[] stopWords;

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
        vocab = new ArrayList<>();
        wordcountPerClass = new HashMap<>();
        wordcount = new HashMap<>();
        documents = new ArrayList<>();
        docsPerClass = new HashMap<>();
        try {
            stopWords = normalizeString(Utils.readFile("stopwords.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] filter(String[] text) {
        ArrayList<String> res = new ArrayList<>();
        for (String s: text) {
            boolean isStopW = false;
            for (String filt: stopWords) {
                if(s.equals(filt)) {
                    isStopW = true;
                    break;
                }
            }
            if (!isStopW) {
                res.add(s);
            }
        }
        String[] ret = new String[res.size()];
        return res.toArray(ret);
    }

    public void addDocument(String document, String cls) {
        String[] normalized = normalizeString(document);
        String[] filtered = filter(normalized);
        if (docsPerClass.containsKey(cls)) {
            docsPerClass.replace(cls, docsPerClass.get(cls) + 1);
        } else {
            docsPerClass.put(cls, 1);
        }
        for (String token : filtered) {
            addWordToCount(token);
            if (wordcountPerClass.containsKey(cls)) {
                HashMap classMap = wordcountPerClass.get(cls);
                if (classMap.containsKey(token)) {
                    classMap.replace(token, (Integer) classMap.get(token) + 1);
                } else {
                    classMap.put(token, 1);
                }
                wordcountPerClass.replace(cls, classMap);
            } else {
                HashMap<String, Integer> classMap = new HashMap<>();
                classMap.put(token, 1);
                wordcountPerClass.put(cls, classMap);
            }
            if (!vocab.contains(token)) {
                vocab.add(token);
            }
        }
        documents.add(concat(new String[]{cls}, filtered));
        nrOfDocuments++;
    }

    private void addWordToCount(String word) {
        if (wordcount.containsKey(word)) {
            wordcount.replace(word, wordcount.get(word) + 1);
        } else {
            wordcount.put(word, 1);
        }
    }

    public ArrayList<String[]> getDocuments() {
        return documents;
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
        if (docsPerClass.containsKey(cls)) {
            return docsPerClass.get(cls);
        } else {
            System.out.println("no docs with given class found");
            return 0;
        }
}

    public int countTokensOfTermInClass(String token, String cls) {
        if (wordcountPerClass.containsKey(cls)) {
            HashMap classMap = wordcountPerClass.get(cls);
            if(classMap.containsKey(token)){
                return (Integer) classMap.get(token);
            }
        }
        return 0;
    }

    public int countTokensInClass(String cls) {
        if (wordcountPerClass.containsKey(cls)) {
            HashMap classMap = wordcountPerClass.get(cls);
            int count = 0;
            for(Object key : classMap.keySet()) {
                count += (int) classMap.get(key);
            }
            return count;
        }
        return 0;
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
