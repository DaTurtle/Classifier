package module6;

public class Main {

    public static void main(String[] args) {
	    NaiveBayes classifier = new NaiveBayes();
        DocumentStore docs = classifier.getDocumentStore();

        docs.addDocument("This is for testing purposes", "test");
        docs.addDocument("This is also for testing purposes", "test");
        docs.addDocument("How to eat an apple, step one: eat apple", "food");
        docs.addDocument("Add the egg to the milk and flour", "food");

        classifier.train("test", "food");
        double[] test = classifier.estimate("this egg and milk are not for testing");
        System.out.println("values: " + test[0] + "  ,  " + test[1]);
    }
}
