package com.rtts.utilities;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class Mouse {

	/*
	 * This function moves the mouse cursor has a delay before and after the
	 * move can be set to zero
	 */
	public static void move(int x, int y, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseMove(x, y);
		robot.delay(eDelay);
	}

	/*
	 * This function moves the mouse cursor and presses the left button and
	 * releases This function has a delay at the end of the press and after the
	 * press can be set to zero
	 */
	public static void movePressL(int x, int y, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseMove(x, y);
		robot.delay(20);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(20);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(eDelay);
	}

	/*
	 * This function moves the mouse cursor and presses the right button and
	 * releases This function has a delay at the end of the press and after the
	 * press can be set to zero
	 */
	public static void movePressR(int x, int y, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseMove(x, y);
		robot.delay(20);
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.delay(20);
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		robot.delay(eDelay);
	}

	/*
	 * This function moves the mouse cursor and presses the right button and
	 * releases This function has a delay at the end of the press and after the
	 * press can be set to zero
	 */
	public static void scroll(int scrollAmount, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseWheel(scrollAmount);
		robot.delay(eDelay);
	}

	/*
	 * This function scrolls the mouse wheel and and presses the left mouse
	 * button
	 */
	public static void scrollPressL(int scrollAmount, int x, int y, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseWheel(scrollAmount);
		robot.delay(20);
		robot.mouseMove(x, y);
		robot.delay(20);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(20);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(eDelay);
	}

	/*
	 * This function scrolls the mouse wheel and and presses the right mouse
	 * button
	 */
	public static void scrollPressR(int scrollAmount, int x, int y, int bDelay, int eDelay) throws Exception {
		Robot robot = new Robot();

		robot.delay(bDelay);
		robot.mouseWheel(scrollAmount);
		robot.delay(20);
		robot.mouseMove(x, y);
		robot.delay(20);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(20);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(eDelay);
	}
}
