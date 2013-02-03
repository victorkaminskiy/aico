package aico.simbad.emulator.robot;

public enum Elements {
	START,		// Start the sequence
	FORWARD,	// Slide forward
	BACKWARD,	// Slide backward
	LEFTWARD,	// Slide to the left
	RIGHTWARD,	// Slide to the right
	STRAFE,		// Slide left or right and find path in front
	TURN_LEFT,	// Turn PI/2 left
	TURN_RIGHT,	// Turn PI/2 right
	UNFOLD,		// Turn PI (change direction to opposite)
	TAKE_OFF,	// Go to the maximum altitude
	LAND,		// Go to the 0 altitude
	FINISH,		// End the sequence
}
