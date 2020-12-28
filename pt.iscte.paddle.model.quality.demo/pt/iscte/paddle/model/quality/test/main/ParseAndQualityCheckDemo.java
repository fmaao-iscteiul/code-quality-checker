package pt.iscte.paddle.model.quality.test.main;

import java.io.File;
import java.util.ArrayList;

import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.javaparser.Java2Paddle;
import pt.iscte.paddle.quality.client.Linter;
import pt.iscte.paddle.quality.client.LintingResult;
import pt.iscte.paddle.quality.examples.Contains;

public class ParseAndQualityCheckDemo {

	public static void main(String[] args) {

		File f = new File("/Users/andresantos/Desktop/Trabalhos35/1");
		Linter linter = new Linter();
		searchDirectoryForModules(f).forEach(module -> {
			System.out.println("Casos detetados: " + new LintingResult(linter.analyse(module)));
		});
		
		
//		Contains issue = new Contains();
//		issue.setup();
//		IModule module = issue.getModule();
//		System.out.println("Casos detetados: " + new LintingResult(linter.analyse(module)));
		
	}

	static ArrayList<IModule> searchDirectoryForModules(File f) {
		ArrayList<IModule> modules = new ArrayList<IModule>();
//		File[] files = f.listFiles(file -> file.getName().equals("Project92.java"));
		File[] files = f.listFiles();
		for (File file : files) {
			if(file.isDirectory()) {
				try {
					Java2Paddle jparser = new Java2Paddle(file, file.getName().replace(".java", ""));
					IModule m = jparser.parse();
					modules.add(m);
				} catch (Exception e) {
					System.out.println("ups");
					e.printStackTrace();
				}
				searchDirectoryForModules(file);
			} else {
				try {
					Java2Paddle jparser = new Java2Paddle(file, file.getName().replace(".java", ""));
					IModule m = jparser.parse();
					modules.add(m);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("ups");
				}
			}
		}
		return modules;
	}

}
