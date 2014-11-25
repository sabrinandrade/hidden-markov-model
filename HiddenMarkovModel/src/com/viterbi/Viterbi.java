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
	private double[][] beta;
	private int states;
	private int T;


	public void execute(double[][] A, double[][] B, double[] pi, int[] observations) {
		states = A.length;
		T = observations.length;


		/** 
		 * alpha.ROW = (sequence.length = T). Where 1 =< t =< T-1 but alpha goes til t+1 (t+1 = T)
		 * alpha.COL = number of states
		 */
		alpha = new double[T][states];


		/**
		 * beta.ROW = sequence.length + 1. Includes the initial state
		 * beta.COL = number of states
		 */
		beta = new double[T + 1][states];


		// Forward variable alpha
		calculatesAlpha(A, B, pi, observations);


		// P(O|x) - 1 indicates that is the probability for the values of Alpha
		double probabilityAlpha = calculatesSymbolProbability();
		System.out.println("Foward\nConditional probability: " + probabilityAlpha);


		// Backward variable beta
		calculatesBeta(A, B, observations);


		//P(O|x) - 2 indicates that is the probability for the values of Beta
		double probabilityBeta = calculatesSymbolProbability(pi, B, observations);
		System.out.println("\nBackward\nConditional probability: " + probabilityBeta);


		// Sequence of states - Viterbi
//		int[] sequence = bestSequence();
//		for(int i : sequence) {
//			System.out.print(i + "\t");
//		}
	}


	private void calculatesAlpha(double[][] A, double[][] B, double[] pi, int[] observations) {

		/** Initialization of alpha - OK
		 *	alpha(1)(i) = pi(i) * b(i)(O1), 1 =< i =< N
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
				for(int i = 0; i < states; i++) 
					alpha[t][j] += (alpha[t-1][i] * A[i][j]);

				alpha[t][j] *= B[j][observations[t]];
				// System.out.println(alpha[t][j]);
			}
			// System.out.println();
		}
	}


	private void calculatesBeta(double[][] A, double[][] B, int[] observations) {

		/** Initialization of beta - OK
		 *	beta(T)(i) = 1, 1 =< i =< N
		 */
		for(int i = 0; i < states; i++) {
			beta[T][i] = 1;
			// System.out.println(beta[T][i]);
		}

		/** Induction - OK
		 *  beta(t)(i) = sum[a(i)(j) * b(j)(O(t+1) * beta(t+1)(j)]
		 *  
		 *  t = iterates over the T
		 *  j = iterates over the states
		 *  i = iterates over the states for the sum
		 */

		for(int t = T-1; t >= 0; t--) {
			for(int i = 0; i < states; i++) {			
				for(int j = 0; j < states; j++) {
					beta[t][i] += A[i][j] * B[j][observations[t]] * beta[t+1][j];
				}
				
				//System.out.println(beta[t][i]);				
			}
			//System.out.println();
		}

	}


	private int[] bestSequence() {
		// TODO
		// int[] value = new int[];

		return null;
	}


	private double calculatesSymbolProbability() {
		double value = 0;

		for(int i = 0; i < states; i ++) {
			value += alpha[T-1][i];
		}

		return value;
	}


	private double calculatesSymbolProbability(double[] pi, double[][] B, int[] observations) {
		double value = 0;

		for(int i = 0; i < states; i ++) {
			value += pi[i] * B[i][observations[1]] * beta[1][i]; // confirmar
		}

		return value;
	}

}
