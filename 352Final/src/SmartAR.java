import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class SmartAR{
	static int totalEntries = 0;
	static ArrayList<Car> arrList = new ArrayList<Car>(); 
	static LinkedHashMap<String, Car> linkedHM = new LinkedHashMap<>();
	static LinkedList<Car> duplicateList = new LinkedList<Car>();
	static int dupCounter = 0;
	static int keyLength = 6;
	static boolean hashADT = false;
	static boolean seqADT = false;
	static long threshold = 0;
	boolean swap = false;
		
	public static void store(String path) throws FileNotFoundException {
		Car possibleDup = null;
		Scanner sc = null;
		File f1 = new File(path);
		sc = new Scanner(f1);										
		LinkedHashMap<String, Car> temp = new LinkedHashMap<>(); // remove duplicates before counting 
	    while (sc.hasNextLine()) {
	    	String key = sc.nextLine().trim();
	    	possibleDup = temp.put(key,new Car(key,"no make",0));
	    	if(possibleDup != null) {							// if it returns a car, its a duplicate, store it
	    		duplicateList.add(possibleDup);			
	    	}
		} 
	    totalEntries += temp.size();
	    swap();							  // verify if size is good for threshold defined
	    if(totalEntries >= threshold ) {
	    	linkedHM.putAll(temp);
	    	hashADT = true;
	    }
	    else {
	    	Iterator<Car> it = temp.values().iterator();
			Car c = null;
        	while (it.hasNext()) {
	       		c = it.next();
	       		arrList.add(c);
	       	}
	       	temp.clear();  		 // clear temp hashmap 								  
		}
	    if(hashADT == true) 
	    	System.out.println("Stored in linkedHashMap");
	    else
	    	System.out.println("Stored in arrayList");
	    sc.close();
	}
	
	public static void swap() {
		if ( hashADT == true && totalEntries < threshold ) {
			Iterator<Car> it = linkedHM.values().iterator();
			Car c = null;
        	while (it.hasNext()) {
        		c = it.next();
        		arrList.add(c);
        	}
			linkedHM.clear();
			hashADT = false;	
			System.out.println("Swapped to arrayList");
		}
		else if (hashADT == false && totalEntries >= threshold) {
			Iterator<Car> it = arrList.iterator(); // iterate through arrList cars
			Car c = null;
        	while (it.hasNext()) {
        		c = it.next();
        		linkedHM.put(c.getKey(), c);
        	}
			arrList.clear();
			hashADT = true;
			System.out.println("Swapped to linkedHashMap");
		}
	}
	
	public static void setThreshold(int T) {
		if(T < 0) {
			System.out.println("Cannot be negative");
			threshold = 0;
		}
		else {
			 threshold = T;
			 System.out.println("Threshold set to " + T);
		}
		   
		swap();
	}
	
	 public static void setKeyLength(int len) {
	     if (len >= 6 && len <= 12) {
	         keyLength = len;
	         System.out.println("Key length set to " + len);
	     }
	     else
	         System.out.println("Key Length must be between 6 and 12 inclusively");
	}
	
	public static String[] generate(int n) {
		 String[] newKeys = new String[n];
		 String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
		 StringBuilder sb = new StringBuilder(n);
		 for(int i = 0; i < n ; i++) {
			 sb.delete(0, sb.length());
		  for (int j = 0; j < keyLength; j++) {
	            int index  = (int)(alphaNumericString.length()* Math.random());
	            sb.append(alphaNumericString.charAt(index)); 
	        } 
		  	newKeys[i] = sb.toString();
		 }
		 for(String s: newKeys) {
			 System.out.println(s);
		 }
	        return newKeys; 
	} 
	
	public static void allKeys(){
		if(hashADT == true) {
			 ArrayList<String> v = new ArrayList<String>(linkedHM.keySet()); // put keyset in arraylist and sort
			 Collections.sort(v);
			    for (String str : v) {
			      System.out.println(str);
			    }
			    v.clear();
		}
		else {
			if(arrList.size() == 0) {
				System.out.println("Empty! nothing to sort");
			}
			else {
				ArrayList<Car> temp = new ArrayList<Car>(); 
				temp.addAll(arrList);
				temp.sort(Comparator.comparing(Car::getKey)); // use comparator to compare car attribute
				for (Car c: temp){
				     System.out.println(c.getKey());
				}
				temp.clear();
			}
		}
	}
	
	public static void add(String key, Car car) {
		boolean isDup = false;
		int index = 0;
		Car duplicate = null;
		if(key.length() < 6 || key.length() > 12) {
			System.out.println("Key Length must be between 6 and 12 inclusively");
		}
		else {
			if(hashADT == true) {
				duplicate = linkedHM.put(key, (new Car(key,car.getMake(),car.getYear())));
				if(duplicate != null)
					duplicateList.add(duplicate);
				else
					totalEntries++;
			}
			else {
				for(Car c : arrList){            
	                if(c.getKey().equals(key)) {
	                	System.out.println("IM IN");
	                    index = ((arrList.indexOf(c)));
	                    duplicate = arrList.get(index);         // if was linked list would of had to traverse O(n^2)
	                    arrList.set(index,car);
	                    duplicateList.add(duplicate);
	                    isDup = true;
	                }              
	            }
				if(isDup == false) {
				    totalEntries++;
					arrList.add(car);
				}
			}
			System.out.println("New entry added");
			swap();
		}	
	}
	
	public static void remove(String key) { 
		Car deletedCar = null;
		boolean removed = false;
		if(hashADT == true) {
			deletedCar = linkedHM.remove(key); // returns value
			if(deletedCar != null) {
				removed = true;
				duplicateList.add(deletedCar);
			}
		}
		else {
			for(ListIterator<Car> it = arrList.listIterator(); it.hasNext();){   // list iterator or it throws exception, cannot remove/add looping arrlist
			    Car value = it.next();
			    if(value.getKey().equals(key)) {
			    	removed = true;
			    	duplicateList.add(value);
			        it.remove();     
			    }
			}
		}
		if(removed == true) {
			totalEntries--;
			System.out.println("Entry removed");
			swap();
		}
		else
			System.out.println("No such key");
	}
	
	public static Car getValues(String key) {
        Car value = null;
        if (hashADT == true) {
        	value = linkedHM.get(key);
        } else {
            for (Car c : arrList) { 
                if (c.getKey().equals(key)) {
                    value = c;
                }
            }
        }
        return value;
    }
	
	public static String nextKey(String key) {
        String nextKey = "";
        int nextIndex = 0;
        if(hashADT == true) {
        	Iterator<String> it = linkedHM.keySet().iterator(); // iterator for keysets
        	while (it.hasNext()) {
        		String temp = it.next();
        		if(temp.equals(key))
        	    nextKey = it.next();    
        	}
        }
        else {
            for(Car c : arrList){            
                if(c.getKey().equals(key)) {
                    nextIndex = ((arrList.indexOf(c))+ 1);
                    c = arrList.get(nextIndex);
                    nextKey = c.getKey();
                }              
            }
            if(nextKey.equals(""))
            nextKey = "No such key";
        }
        return nextKey;    
    }
	
	public static String prevKey(String key) {
        String prevKey = "";
        int prevIndex = 0;
        if(hashADT == true) {
        	String prev = "";
        	Iterator<String> it = linkedHM.keySet().iterator(); // iterator for keysets
        	while (it.hasNext()) {
        		String temp = it.next();
        		if(temp.equals(key))
            	prevKey = prev;  
        		prev = temp;	  
        	}
        }
        else {
            for(Car c: arrList) {
                if(c.getKey().equals(key)) {
                    prevIndex = ((arrList.indexOf(c))-1);
                    c = arrList.get(prevIndex);
                    prevKey = c.getKey();
                }
            }
            if(prevKey.equals(""))
                prevKey = "No such key";
        }
        return prevKey;
    }
	
	public static void previousCars(String key) {
		LinkedList<Car> prevCars = new LinkedList<Car>();
		for(Car c : duplicateList){            
            if(c.getKey().equals(key)) {
                prevCars.add(c);
            }              
        }
		Collections.reverse(prevCars);
		System.out.println(prevCars.toString());
	}
	
	public static void methods(){
		System.out.println("\nMethods: \n"
				+ "0: exit\n"
				+ "1: store from file\n"
				+ "2: Total entries\n"
				+ "3: setThreshold\n"
				+ "4: setKeyLength\n"
				+ "5: generate\n"
				+ "6: allKeys\n"
				+ "7: add\n"
				+ "8: remove\n"
				+ "9: getValues\n"
				+ "10:nextKey\n"
				+ "11:prevKey\n"
				+ "12:previousCars\n"
				+ "13:method list");
	}
	public static void main(String[] args) {
		System.out.println("##############\n"
							+ "   SmartAr\n"
							+ "##############");
		methods();
		int method = 0;
		boolean isRunning = true;
		Scanner user = new Scanner(System.in);
		do {
			System.out.print("\nEnter value: ");
			if(user.hasNextInt()) {
				method = user.nextInt();
				switch(method) {
				case 0:
					System.out.println("Closing smartAr");
					isRunning = false;
					break;
				case 1:
					System.out.print("Enter file path: ");
					String temp = user.next();
					try {	
						store(temp); 
					    System.out.println("Total entries: " + totalEntries);
					}
					catch (FileNotFoundException e) {
						System.out.println("File not found");
					}
					break;
				case 2:
					System.out.println("Total entries: " + totalEntries);
					System.out.println("Arraylist size: " + arrList.size());
					System.out.println("LinkedHashmap size:" + linkedHM.size());
					break;
				case 3:
					System.out.print("Set threshold: ");
					try {
						int temp1 = user.nextInt();
						setThreshold(temp1);	
					}
					catch(InputMismatchException e) {
						System.out.println("Not a number");
					}
					break;
				case 4:
					try {
						System.out.print("Key length: ");
						int temp2 = user.nextInt();
						setKeyLength(temp2);
					}
					catch(InputMismatchException e) {
						System.out.println("Not a number");
					}
					break;
				case 5:
					try {
						System.out.print("Number of keys to generate: ");
						int temp3 = user.nextInt();
						generate(temp3);
					}
					catch(InputMismatchException e) {
						System.out.println("Not a number");
					}
					break;
				case 6:
					System.out.println("all keys as a sorted sequence (lexicographic order)");   // it says return sequence
					allKeys();
					break;
				case 7:
					System.out.print("Enter Key: ");
					String tempK= user.next();
					System.out.print("Enter make: ");
					String tempM = user.next();
					System.out.print("Enter year: ");
					if(user.hasNextInt()) {
						int tempY = user.nextInt();
						Car c = new Car(tempK,tempM,tempY);
						add(tempK,c);
					}
					else {
						user.nextLine();
						System.out.println("Year cannot be a string");
					}
					break;
				case 8:
					System.out.print("Remove Key: ");
					String temp4 = user.next(); 
					remove(temp4);
					break;
				case 9:
					System.out.print("Key for values: ");
					String temp5 = user.next();
					Car v =	getValues(temp5);
					System.out.print(v);
					break;
				case 10:
					System.out.print("Getnext key: ");
					try {
						String temp6 = user.next();
						System.out.println(nextKey(temp6));
					}
					catch(IndexOutOfBoundsException e) {
						System.out.println("No next key!");
					}
					break;
				case 11:
					System.out.print("Getprevious key: ");
					try {
						String temp7 = user.next();
						System.out.println(prevKey(temp7));
					}
					catch(IndexOutOfBoundsException e) {
						System.out.println("No previous key!");
					}
					break;
				case 12: 
					System.out.print("Previouscars: ");
					String temp8 = user.next();
					previousCars(temp8);
					break;
				case 13:
					methods();
					break;
				}			
			}
				user.nextLine();		
		}
		while(isRunning);
		user.close();
	}
}
// C:\\Users\\sacha\\Documents\\Dev.Tools\\Projects\\java\\352Final\\test1.txt