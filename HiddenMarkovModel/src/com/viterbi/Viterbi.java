/**
 * 
 */
package com.viterbi;

public class Viterbi {


	private double[][] alpha;
	private double[][] beta;
	private double[][] delta;
	private int[][] psi;
	private int[] q;
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
		
		
		delta = new double[T][states];
		
		
		psi = new int[T][states];
		
		
		q = new int[T];


		// Forward variable alpha
		calculatesAlpha(A, B, pi, observations);


		// P(O|x)
		double probabilityAlpha = calculatesSymbolProbability();
		System.out.println("Forward\nConditional probability: " + probabilityAlpha);


		// Backward variable beta
		calculatesBeta(A, B, observations);


		//P(O|x)
		double probabilityBeta = calculatesSymbolProbability(pi, B, observations);
		System.out.println("\nBackward\nConditional probability: " + probabilityBeta);


		// Sequence of states - Viterbi
		bestSequence(pi, A, B, observations);
		System.out.println("\nBest sequence");
		for(int i : q) {
			System.out.print((i+1) + "\t");
		}
	}


	private void calculatesAlpha(double[][] A, double[][] B, double[] pi, int[] observations) {

		/** Initialization of alpha
		 *	alpha(1)(i) = pi(i) * b(i)(O1), 1 =< i =< N
		 */
		for(int i = 0; i < states; i++) {
			alpha[0][i] = pi[i] * B[i][0];
			// System.out.println(alpha[0][i]);
		}

		/** Induction
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

		/** Initialization of beta
		 *	beta(T)(i) = 1, 1 =< i =< N
		 */
		for(int i = 0; i < states; i++) {
			beta[T][i] = 1;
			// System.out.println(beta[T][i]);
		}

		/** Induction
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


	private void bestSequence(double[] pi, double[][] A, double[][] B, int[] observations) {
				
		/** Initialization of delta
		 *	delta(1)(i) = pi(i) * b(i)(O1), 1 =< i =< N
		 *  psi1(i) = 0
		 */
		for(int i = 0; i < states; i++) {
			delta[0][i] = pi[i] * B[i][0];
			psi[0][i] = 0;
			//System.out.println(delta[0][i]);
		}
		
		
		/** 
		 *  delta(t+1)(j) = max(delta(t)(i)*a(i)(j)) * b(j)(O(t+1))
		 *  psi(t+1)(j) = max(delta(t)(i)*a(i)(j))
		 *  
		 *  t = iterates over the T
		 *  j = iterates over the states
		 */
		
		double[] aux = new double[states];
		int index = 0;
		
		for(int t = 1; t < T; t++) {
			for(int j = 0; j < states; j++) {
				for(int i = 0; i < states; i++) {
					aux[i] = (delta[t-1][i] * A[i][j]);
				}

				delta[t][j] = findMax(aux) * B[j][observations[t]];
				//System.out.println(delta[t][j]);
				
				index = findMaxIndex(aux);
				psi[t][j] = index;
				//System.out.println("Psi: " + psi[t][j]);
			}
			//System.out.println();
		}
		
		
		/**
		 * P = max[delta(T)(i)]
		 * Q = max[index of P]
		 */
		
		q[T-1] = findMaxIndex(delta[T-1]);
		
		for(int t = T-2; t >= 0; t--) {
			q[t] = psi[t+1][q[t+1]];
		}
		
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
			value += pi[i] * B[i][observations[1]] * beta[1][i];
		}

		return value;
	}
	
	
	private double findMax(double[] values) {
		double max = values[0];

		for (int i = 1; i < values.length; i++) {
		    if ( values[i] > max) {
		      max = values[i];
		    }
		}
		
		return max;
	}
	
	
	private int findMaxIndex(double[] values) {
		double max = values[0];
		int index = 0;

		for (int i = 1; i < values.length; i++) {
		    if ( values[i] > max) {
		      index = i;
		    }
		}
		
		return index;
	}
}
