package com.tor.projects.HeaderLinkedList;

import java.util.LinkedList;

public class HaederLinkedList<T> extends LinkedList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Header head;
	int numberHeader;
	int size;
	private static final int MIN_NUMBER_HEADER = 10;
	private static final double MIN_ELEMENT_THRESHOID = 0.5;
	private static final double MAX_ELEMENT_THRESHOID = 2.0;
	
	class Node{
		T data;
		Node next;
		Node prev;
		
			
		//constructor
		Node(T data){
			this.data = data;
			this.next = null;
			this.prev = null;
		}
		
		//constructor
		Node(Node prev, T data, Node next){
			this.data = data;
			this.next = next;
			this.prev = prev;
		}
	}
	
	class Header{
		Node head;
		//Node tail;
		Header prevHeader;
		Header nextHeader;
		int size;
		
		//constructor
		Header(){
			this.head = null;
			//this.tail = null;
			this.prevHeader = null;
			this.nextHeader = null;
			this.size = 0;
		}
	}
	
	//POJO utility class acting as a struct
	final class StructHeaderNode{
		public Header header;
		public Node node;
		
		//constructor
		StructHeaderNode(Header header, Node node){
			this.header = header;
			this.node = node;
		}
	}
	
	//constructor
	HaederLinkedList(){
		this.head = new Header();
		this.size = 0;
		this.numberHeader = 1;
	}
	
	/**
	 * 
	 */
	private StructHeaderNode getNodeByIndex(int index) {
		if(index == 0) {
			return new StructHeaderNode(this.head, this.head.head);
		}
		
		Header currentHeader = head;
		int count = 0;
		while(currentHeader != null) {
			if(count + currentHeader.size >= index+1) {
				Node currentNode = currentHeader.head;
				count++;
				while(count < index+1) {
					currentNode = currentNode.next;
					count++;
				}
				return new StructHeaderNode(currentHeader, currentNode);
			}
			count += currentHeader.size;
			currentHeader = currentHeader.nextHeader;
		}
		System.out.println("problems whith index = " + index);
		return new StructHeaderNode(null, null);
	}
	
	
	
	 /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
	public T remove(int index) {
		if(index < 0 || index >= size) {
    		throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
		
		
		//get Header and Node
    	StructHeaderNode hn = getNodeByIndex(index);
    	
    	Header currentHeader = hn.header;
    	Node currentNode = hn.node;
    	
    	this.size--;
    	
    	if(currentHeader.size == 1) {
    		//System.out.println("delite Header");
    		if(currentHeader.prevHeader == null) {
    			this.head = currentHeader.nextHeader;
    			currentHeader.nextHeader.prevHeader = null;
        	}
    		else {
    			currentHeader.prevHeader.nextHeader = currentHeader.nextHeader;
    		}
    		if(currentHeader.nextHeader == null) {
    			currentHeader.prevHeader.nextHeader = null;
    		}
    		else {
    			currentHeader.nextHeader.prevHeader = currentHeader.prevHeader;
        	}
    	}
    	else {
    		
    		if(currentNode.prev == null) {
    			currentHeader.head = currentNode.next;
    			//System.out.println("delite Head Node");
    		}
    		else {
    			currentNode.prev.next = currentNode.next;
    		}
    		if(currentNode.next != null) {
    			currentNode.next.prev = currentNode.prev;
    		}	
    		currentHeader.size--;
    	}
    	
    	normalizationAfterRemoving(currentHeader);
    	
    	return currentNode.data;
		
		
	}
	
	/**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, T element) {
    	if(index < 0 || index > size) {
    		throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
    	
    	if(index == 0) {
    		Node newNode = new Node(null, element, null);
    		
    		if(size > 0) {
    			this.head.head.prev = newNode;
    			newNode.next = this.head.head;
    		}
    		this.head.head = newNode;
    		this.size++;
    		this.head.size++;
    		
    		normalizationAfterAdding(this.head);
    		
    		return;
    	}
    	
    	    	
    	//get previous Header and Node
    	StructHeaderNode hn = getNodeByIndex(index-1);
    	
    	Node newNode = new Node(hn.node, element, hn.node.next);
    	
    	if(hn.node.next != null) {
    		hn.node.next.prev = newNode;
    	}
    	
    	hn.node.next = newNode;
    	
    	this.size++;
    	hn.header.size++;
    	
    	normalizationAfterAdding(hn.header);
    
    }
    
    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
    
    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }
    
    /**
     * aAdd elements of the second "Header" 
     * to the first "Header" 
     * and delete the second "Header"
     */
    private void margeHeader(Header h1, Header h2) {
    	//System.out.println(statisticsToString());
		//System.out.println("REM " + h1.size + " + " + h2.size);
						
		Node currentNode = h1.head;
		while (currentNode.next != null) {
			currentNode = currentNode.next;
		}
		currentNode.next = h2.head;
		
		h2.head.prev = currentNode;
		
		h1.nextHeader = h2.nextHeader;	
		h1.size += h2.size;
		if(h2.nextHeader != null) {
			h2.nextHeader.prevHeader = h1;
		}
		this.numberHeader --;
    	
    }
    
    /**
     * If there are fewer elements in the current "Header"
     * than the minimum allowed, then we combine it with a 
     * neighbor with the least number of elements 
     */
    private void normalizationAfterRemoving(Header h) {
    	if(h.size == 0) {
    		
    		//System.out.println(statisticsToString());
    		//System.out.println("0. REM !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		
    		if(h.prevHeader == null) {
    			this.head = h.nextHeader;
    		}
    		else {
    			h.prevHeader.nextHeader = h.nextHeader;
    		}
    		
    		System.out.println(statisticsToString());
    		return;
    	}
    	
    	if(numberHeader <= MIN_NUMBER_HEADER) {
    		return;
    	}
    	
    	int minHeaderSize =  (int) (Math.sqrt(size)*MIN_ELEMENT_THRESHOID);
    	int maxHeaderSize =  (int) (Math.sqrt(size)*MAX_ELEMENT_THRESHOID);
    	
    	if(h.size >= minHeaderSize) {
    		return;
    	}
    	
    	int sizePrev = Integer.MAX_VALUE;
    	if(h.prevHeader != null) {
    		sizePrev = h.prevHeader.size;
    	}
    	
    	int sizeNext = Integer.MAX_VALUE;
    	if(h.nextHeader != null) {
    		sizeNext = h.nextHeader.size;
    	}
    	
    	if(Math.min(sizePrev, sizeNext) + h.size >= maxHeaderSize) {
    		return;
    	}
    	
    	if(sizePrev < sizeNext) {
    		margeHeader(h.prevHeader, h);
    	}
    	else {
    		margeHeader(h, h.nextHeader);	
    	} 
    	//System.out.println(statisticsToString());
    }
    
    /**
     * If there are more elements in the current "Header"
     * than the allowed one, then we divide it into two
     */
    private void normalizationAfterAdding(Header h) {
    	
    	if(h.size <= MIN_NUMBER_HEADER) {
    		return;
    	}
    	
    	int maxHeaderSize =  (int) (Math.sqrt(size)*MAX_ELEMENT_THRESHOID);
    	
    	if(maxHeaderSize < MIN_NUMBER_HEADER) {
    		return;
    	}
    	
    	if(h.size < maxHeaderSize) {
    		return;
    	}
    	   
    	//find the middle of the chain of elements
    	Node currentNode = h.head;
    	int newSizeH = h.size/2;
    	for(int i = 0; i < newSizeH-1; i++) {
    		currentNode = currentNode.next;
    	}
    	
    	//Let's create a new "Header" and embed it in the "Headers" chain
    	Header newH = new Header();
    	newH.prevHeader = h;
    	newH.nextHeader = h.nextHeader;
    	h.nextHeader = newH;
    	if(newH.nextHeader != null) {
    		newH.nextHeader.prevHeader = newH;
    	}
    	
    	//cut the chain of elements and assign the second half to newH
    	newH.head = currentNode.next;
    	newH.head.prev = null;
    	currentNode.next = null;
    	

    	
    	//redefine the dimensions "Headers"
    	newH.size = h.size - newSizeH;
    	h.size = newSizeH;
    	
    	this.numberHeader++;
    	   	
    }
   
    /**
     * 
     */
    public String statisticsToString() {
    	int totalNumberN = 0;
    	StringBuilder sb = new StringBuilder();
    	
    	Header currentHeader = this.head;
    	while(currentHeader != null) {
    		sb.append('[').append(currentHeader.size).append("] ");
    		
    		
    		Node n = currentHeader.head;
    		int numberNode = 0;
    		while(n != null) {
    			numberNode++;
    			n = n.next;
    		}
    		
    		totalNumberN += numberNode;
    		if (numberNode != currentHeader.size) {
    			sb.append("SIZE! ");
    		}
    		
    		currentHeader = currentHeader.nextHeader;
    	}
    	
    	if (totalNumberN != this.size) {
			sb.append("   TOTAL SIZE! ");
		}
    	return sb.toString();
    }
    
    /**
     * 
     */
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	Header currentHeader = this.head;
    	while(currentHeader != null) {
    		
    		Node currentNode = currentHeader.head;
    		
    		while(currentNode != null) {
    			sb.append('[').append(currentNode.data).append("] ");
    			currentNode = currentNode.next;
    		}
    		
    		currentHeader = currentHeader.nextHeader;
    	}
    	
    	
    	return sb.toString();
    }

}
