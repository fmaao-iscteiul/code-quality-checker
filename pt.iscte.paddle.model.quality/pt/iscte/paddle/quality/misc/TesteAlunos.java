package pt.iscte.paddle.quality.misc;


public class TesteAlunos {
	TesteAlunos() {
	}

	/*
	 * Dada uma lista de inteiros e um número inteiro, verificar a existência do
	 * número na lista fornecida. Caso lista contenha o valor do número inteiro,
	 * devolver verdadeiro, caso contrário, devolver falso.
	 */

	static boolean contains(int[] vector, int number) {
		boolean contains = false;
		int i = 0;
		while (i < vector.length && !contains) {
			if (vector[i] == number) {
				contains = true;
			}
		}
		if (contains)
			return true;
		else
			return false;
	}

	/*
	 * Dado um número inteiro, verificar se o mesmo é primo.
	 */


	static boolean isPrime(int number) {
		int sqrt = (int) Math.sqrt(number) + 1;
		int i = 2;
		while (i < sqrt) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Dada uma lista de números inteiros, devolver a quantidade de números não
	 * primos que a mesma contém. {2,3,4,5,6} -> 2
	 */


	static int numberOfNonPrimes(int[] vector) {
		int nonPrimeAmount = 0;
		int i = 0;
		while (i < vector.length) {
			if (isPrime(vector[i])) {

			} else {
				nonPrimeAmount++;
			}
			i++;
		}
		return nonPrimeAmount;
	}

	/*
	 * Dada um vetor de números inteiros, deve ser devolvido um novo vetor, apenas
	 * contendo os números primos do vetor fornecido nas suas respetivas posições
	 * originais, caso contrário, a posição deve ser representada como zero.
	 * {2,3,4,5} -> {2,3,0,5}
	 */


	static int[] returnPrimesOrZero(int[] vector) {
		int[] result = new int[vector.length];
		int i = 0;
		while (i < vector.length) {
			if (isPrime(vector[i]) && i < vector.length)
				result[i] = vector[i];
			i++;
		}
		return result;
	}

	/*
	 * Dada uma lista de inteiros, deve ser devolvida uma nova lista que contenha os
	 * números ímpares presentes na lista fornecida, nas suas posições originais,
	 * caso contrário, deve o valor da posição deve ser zero.
	 */

	static int[] returnOddNumbers(int[] vector) {

		int[] oddNumbers = new int[vector.length];
		int i = 0;
		while (i < vector.length) {
			if (vector[i] % 2 != 0) {
				oddNumbers[i] = vector[i];
			}
		}
		return oddNumbers;
	}

	/*
	 * Dada uma lista de inteiros, a mesma deve ser percorrida e caso alguma das
	 * suas posições contenha um valor impar, o mesmo deve ser alterado para 1.
	 */


	static void changeOddNumbersToOne(int[] vector) {
		int[] oddNumbers = new int[vector.length];
		oddNumbers = returnOddNumbers(vector);
		for (int i = 0; i < oddNumbers.length; i++) {
			oddNumbers[i] = 1;
		}
	}

	static class Calculator {

		static double area(double radius) {
			return 3.14 * radius * radius;
		}
		static double perimeter(double radius) {
			return 2 * 3.14 * radius;
		}
	}

	static double sumCircleAreaAndPerimeter(int radius) {
		double per = 2 * 3.14 * radius;
		double area = 3.14 * radius * radius;
		return per + area;
	}

	/*
	 * Dada um vector de inteiros e um número, o mesmo deve ser percorrido e caso
	 * alguma posição seja igual ao número fornecido, o valor da mesma deve ser
	 * alterado para zero. Após isto, a função dá-se por terminada.
	 *
	 */

	static void changeNumberOccurencesToZero(int[] vector, int number) {
		int i = 0;
		while (i < vector.length) {
			if (vector[i] == number)
				vector[i] = 0;
		}
		return;
	}

	static int maxValueInVector(int[] vector) {
		int m;
		m = vector[0];
		int i;
		i = 1;
		while ((i < vector.length)) {
			if ((vector[i] > m)) {
				m = vector[i];
			} else {
				m = m;
			}
			i = (i + 1);
		}
		return m;
	}

	static void changeToZeroIfPrimeOrOdd(int[] vector) {
		int i = 0;
		while (i < vector.length) {
			if (isPrime(vector[i]) || vector[i] % 2 != 0) {
				vector[i] = 0;
				i++;
			} else {
				i++;
			}
		}
	}

}
