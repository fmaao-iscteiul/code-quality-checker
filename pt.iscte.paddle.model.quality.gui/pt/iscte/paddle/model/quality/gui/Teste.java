package pt.iscte.paddle.model.quality.gui;

public class Teste {

	public static boolean contains(int[] list, int number) {
		boolean contains = false;
		for (int i = 0; i < list.length; i++) {
			if (list[i] == number)
				contains = true;
		}
		if (contains == true) {
			contains = false;
			return contains;
		} else if (contains == false) {
			contains = false;
			return false;
		} else
			return contains;
	}
}