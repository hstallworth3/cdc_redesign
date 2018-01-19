package com.rtts.utilities;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Keyboard {

	public static void type(String text, int delay) throws Exception {
		Robot robot = new Robot();
		charHash CharMap = new charHash();
		HashMap<Character, Integer> hashMap = CharMap.CharHash();
		char dir2[] = text.toCharArray();
		char key;

		for (int i = 0; i < dir2.length; i++) {
			key = dir2[i];
			if ((key > 64) && (key < 91)) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.delay(delay);
				robot.keyPress(hashMap.get(key));
				robot.delay(delay);
				robot.keyRelease(hashMap.get(key));
				robot.delay(delay);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else if ((key == 58 || key == 64 || key == 94) || ((key > 32) && (key < 39))
					|| ((key > 39) && (key < 43))) {
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.delay(delay);
				robot.keyPress(hashMap.get(key));
				robot.delay(delay);
				robot.keyRelease(hashMap.get(key));
				robot.delay(delay);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			} else {
				robot.keyPress(hashMap.get(key));
				robot.delay(delay);
				robot.keyRelease(hashMap.get(key));
				robot.delay(delay);
			}
		}

	}
}