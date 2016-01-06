package module6;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    NaiveBayes classifier = new NaiveBayes(1.0, 15, 0.90);
        DocumentStore docs = classifier.getDocumentStore();

        /**
         * STEPS TO CLASSIFFY:
         * 1: Initialise naivebayes classifier
         * 2: Initialise documentstore
         * 3: add documents
         * 4: train classifier (Change minOccurrences before this if you want it changed)
         * 5: estimate document (Change smoothing and amountOfCondProbsToUse before this if you want them changed)
         * 6: TODO: Add estimated document to store given the correct class (user corrected)
         * 7: goto step 4
         */

        //Training might work
//        docs.addDocument("This is for testing purposes", "test");
//        docs.addDocument("This is also for testing purposes", "test");
//        docs.addDocument("How to eat an apple, step one: eat apple", "food");
//        docs.addDocument("Add the egg to the milk and flour", "food");
//
//        classifier.train("test", "food");
//        double[] test = classifier.estimate("this egg and milk are not for testing");
//        System.out.println("values: " + test[0] + "  ,  " + test[1]);

        //Reading files works
//        try {
//            System.out.println(Utils.readFile("blogs/F/F-test1.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        for (int i = 1; i < 592; i++) {
//            try {
//               docs.addDocument(Utils.readFile("blogs/F/F-train"+i+".txt"), "Female");
//           } catch (IOException e) {
//                System.out.println("F-train" + i +".txt does not exist");
//            }
//        }
//        for (int i = 1; i < 601; i++) {
//            try {
//                docs.addDocument(Utils.readFile("blogs/M/M-train"+i+".txt"), "Male");
//            } catch (IOException e) {
//                System.out.println("M-train" + i +".txt does not exist");
//            }
//        }

        for (int i = 2; i < 566; i++) {
            for (int j = 0; j < 6; j++) {
                try {
                    docs.addDocument(Utils.readFile("corpus-mails/corpus/part1/3-"+i+"msg"+j+".txt"), "Female");
                } catch (IOException e) {
                    System.out.println("err");
                }
            }
        }
        for (int i = 1; i < 142; i++) {
            try {
                docs.addDocument(Utils.readFile("corpus-mails/corpus/part1/spmsga"+i+".txt"), "Male");
            } catch (IOException e) {
                System.out.println("err");
            }
        }


        System.out.println("starting to train...");

        ArrayList<String[]> arr = classifier.getDocumentStore().getDocuments();
        classifier.train();

        System.out.println("done training.");
        System.out.println("Start testing...");
        int ftot = 0;
        int mtot = 0;
        int finc = 0;
        int minc = 0;

        for (int i = 1211; i < 1225; i++) {
            String test = "F: ";
            for (int j = 0; j < 6; j++) {
                try {
                    String res = classifier.estimate(Utils.readFile("corpus-mails/corpus/part1/5-"+i+"msg"+j+".txt"));
                    test += res;
                    System.out.println(test);
                    ftot++;
                    if (res.startsWith("M")) {
                        finc++;
                    }
                } catch (IOException e) {
                }
            }
        }
        for (int i = 0; i < 30; i++) {
            String test = "M: ";
            try {
                String res = classifier.estimate(Utils.readFile("corpus-mails/corpus/part2/spmsga"+i+".txt"));
                test += res;
                System.out.println(test);
                mtot++;
                if (res.startsWith("F")) {
                    minc++;
                }
            } catch (IOException e) {
            }
        }
        double accuracy = 100 * (ftot-finc + mtot-minc) / (ftot+mtot);
        System.out.println("done testing, mistakes: M(" + minc + "/" + mtot + ")  F(" + finc + "/" + ftot + "),  Accuracy: " + accuracy );
    }
}
