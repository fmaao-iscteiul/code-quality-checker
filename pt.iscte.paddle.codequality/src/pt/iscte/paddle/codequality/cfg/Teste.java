package pt.iscte.paddle.codequality.cfg;

public class Teste {
	
	// Exemplo: Verificação de length antes do ciclo. Isto com if vazio e ciclo no else.
	
	
	public static int somatorio(int[] lista) {
		int somatorio = 0;
		
		if(lista.length == 0) {
			return somatorio;
		} else {
			for (int i = 0; i < lista.length; i++) {
				somatorio = somatorio + lista[i];
			}
		}
		
		return somatorio;
	}
	
//	public static boolean contains(int number, int[] list) {
//		boolean contains = false;
//		for (int i = 0; i < list.length; i++) {
//			if(list[i] == number)
//				contains = true;
//		}
//		if(contains)
//			return true;
//		else
//			return false;
//	}
	
	/**
	 * Checks the supplied list for the existence of the number parameter.
	 * @param number The number that will be searched.
	 * @param list The list that will be checked for the existence of the supplied number.
	 * @return True if the list contains the supplied number, otherwise, returns false.
	 */
	public static boolean contains(int number, int[] list) {
		if(list.length == 0) {
			return false;
		} else {
			boolean contains = false;
			for (int i = 0; i < list.length; i++) {
				if(list[i] == number)
					contains = true;
			}
			if(contains)
				return true;
			else
				return false;
		}
	}
	
	public int max(int[] array) {
		int m;
		int i;
		m = array[0];
		i = 1;
		while(i < array.length) {
			if(array[i] > m) {
				m = array[i];
			}
			i = i + 1;
		}
		return m;
	}
	
	
	
	
	
	
	
	
//	public static boolean contains(int number, int[] list) {
//		for (int i = 0; i < list.length; i++) {
//			if(list[i] == number) return true;
//		}
//		return false;
//	}

}
