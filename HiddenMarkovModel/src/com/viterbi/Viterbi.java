/**
 * 
 */
package com.viterbi;

/**
 * @author MarkVII
 *
 */
public class Viterbi {


	private double[][] alpha;
	private int states;
	private int T;


	public void execute(double[][] A, double[][] B, double[] pi, int[] sequence) {
		/** 
		 * alpha.ROW = (sequence.length = T). Where 1 =< t =< T-1 but alpha goes til t+1 (t+1 = T)
		 * alpha.COL = number of states
		 */
		alpha = new double[sequence.length][A.length];
		states = A.length;
		T = sequence.length;

		
		// Foward variable alpha
		calculatesAlpha(A, B, pi, sequence);

		
		// P(O|x) - 1 indicates that is the probability for the values of Alpha
		double probabilityAlpha = calculatesSymbolProbability(1);
		System.out.println("Foward\nConditional probability: " + probabilityAlpha);

		
		// Backward variable beta
		calculatesBeta();

		
		//P(O|x) - 2 indicates that is the probability for the values of Beta
		double probabilityBeta = calculatesSymbolProbability(2);
		System.out.println("Foward\nConditional probability: " + probabilityBeta);

		
		// Sequence of states - Viterbi
	}


	private void calculatesAlpha(double[][] A, double[][] B, double[] pi, int[] sequence) {

		/** Initialization of alpha - OK
		 * 	alpha1(i) = pi(i)*bi(O1), i =< i =< N
		 */
		for(int i = 0; i < states; i++) {
			alpha[0][i] = pi[i] * B[i][0];
			// System.out.println(alpha[0][i]);
		}

		/** Induction - OK
		 *  alpha(t+1)(j) = sum(alpha(t)(i)*a(i)(j)) * b(j)(O(t+1))
		 *  
		 *  t = iterates over the T
		 *  j = iterates over the states
		 *  i = iterates over the states for the sum
		 */

		for(int t = 1; t < T; t++) {
			for(int j = 0; j < states; j++) {
				for(int i = 0; i < states; i++) {
					alpha[t][j] += (alpha[t-1][i] * A[i][j]);
				}
				alpha[t][j] *= B[j][sequence[t]];
				// System.out.println(alpha[t][j]);
			}
			// System.out.println();
		}
	}


	private double calculatesSymbolProbability(int n) {
		double value = 0;

		if(n == 1) {
			for(int i = 0; i < states; i ++) {
				value += alpha[T-1][i];
			}
		} else {
			// TODO
		}

		return value;
	}


	private void calculatesBeta() {
		
	}
}
