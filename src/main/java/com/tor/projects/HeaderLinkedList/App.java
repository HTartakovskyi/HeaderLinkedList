package com.tor.projects.HeaderLinkedList;
import java.util.LinkedList;
import java.util.Random;


public class App {
    public static void main( String[] args ){
    	int numberAction = 2;
    	
    	for(int i=0; i<16; i++) {
    		numberAction = numberAction * 2;
    		LinkedList<Integer> testList = new LinkedList<Integer>();
    		double rez1 = test(testList, numberAction);
    		System.out.println("type = LinkedList, numberAction = " + numberAction + " rez1 = " + rez1 + "sec.");	
    	}
    	
    	numberAction = 2;
    	for(int i=0; i<16; i++) {
        	numberAction = numberAction * 2;
        	HaederLinkedList<Integer> testList = new HaederLinkedList<Integer>();	
        	double rez1 = test(testList, numberAction);
        	System.out.println("type = HaederLinkedList, numberAction = " + numberAction + " rez1 = " + rez1 + "sec.");
        	System.out.println(testList.statisticsToString());	
        }
    	
    }
    
    public static double test(LinkedList<Integer> testList, int numberAction) {
    	    	
    	Random rand = new Random();
    	
     	testList.add(100); 
    	int size = 1;
    	
    	long time_0 = System.currentTimeMillis();
    	for(int i = 0; i < numberAction; i ++){
    		//action #1 - add random int in random index
            int rand_int = rand.nextInt(1000);
            int rand_index = rand.nextInt(size);
            testList.add(rand_index, rand_int);
            size++;
                        
            //action #2 - add random int in random index
            rand_int = rand.nextInt(1000);
            rand_index = rand.nextInt(size);
            testList.add(rand_index, rand_int);
            size++;
            
            //action #3 - remove random index
            rand_index = rand.nextInt(size-1);
            testList.remove(rand_index);
            size--;  
    	}
    	long time_1 = System.currentTimeMillis();
    	return (1.0*time_1 - time_0)/1000;
    	
    }
}
