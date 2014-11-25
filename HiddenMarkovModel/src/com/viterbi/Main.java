package com.viterbi;

public class Main {

	public static void main(String[] args) {
		// state transition
		double[][] A = new double[][] {
				new double[] {0.3, 	0.5, 	0.2},
				new double[] {0, 	0.3, 	0.7},
				new double[] {0, 	0, 		1}};
		
		// symbols
		double[][] B = new double[][] {
				new double[] {1, 	0},
				new double[] {0.5, 	0.5},
				new double[] {0, 	1}};
		
		// initial probability
		double[] pi = new double[] {0.6, 0.4, 0};
		
		// a = 0, b = 1
		int[] sequence = new int[] {0, 0, 1, 1}; 
		
		Viterbi viterbi = new Viterbi();
		viterbi.execute(A, B, pi, sequence);		
	}

}
