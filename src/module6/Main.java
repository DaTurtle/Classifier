package module6;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    NaiveBayes classifier = new NaiveBayes();
        DocumentStore docs = classifier.getDocumentStore();

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

        for (int i = 1; i < 592; i++) {
            try {
               docs.addDocument(Utils.readFile("blogs/F/F-train"+i+".txt"), "Female");
           } catch (IOException e) {
                System.out.println("F-train" + i +".txt does not exist");
            }
        }
        for (int i = 1; i < 601; i++) {
            try {
                docs.addDocument(Utils.readFile("blogs/M/M-train"+i+".txt"), "Male");
            } catch (IOException e) {
                System.out.println("M-train" + i +".txt does not exist");
            }
        }
        System.out.println("starting to train...");

        ArrayList<String[]> arr = classifier.getDocumentStore().getDocuments();
        for (String[] st : arr) {
            for(String s : st) {
                System.out.print(s + " ");
            }
            System.out.println("");
        }
        classifier.train("Female", "Male");

        System.out.println("done training.");
        double[] test;
        try {
            test = classifier.estimate(Utils.readFile("blogs/F/F-test1.txt"));
            System.out.println("values: " + test[0] + "  ,  " + test[1]);
        } catch (IOException e) {
            System.out.println("Cant find file blogs/F/F-test1.txt");
        }
        System.out.println("done");
    }
}
