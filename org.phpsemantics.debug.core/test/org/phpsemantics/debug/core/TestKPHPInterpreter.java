package org.phpsemantics.debug.core;

import java.io.IOException;

public class TestKPHPInterpreter {
	
	public static void main(){
		String kphp = "";
		String ktool = "/home";
		String file = "2";
		
		KPHPInterpreter ki = new KPHPInterpreter(ktool, kphp);
		try {
			ki.printOutput(ki.run(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
