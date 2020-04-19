package org.zetool.opengl.framework;

import junit.framework.TestCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class CameraTest extends TestCase {

    /**
     * Checks wheather the setSpeed() method works correct, or not.
     *
     * @throws Exception
     */
    public void testSpeed() throws Exception {
        Camera tc = new Camera();
        tc.setMinSpeed(-10);
        tc.setMaxSpeed(20);
        assertThat("Speed initialized incorrect.", tc.getSpeed(), is(closeTo(20, 0.0)));

        tc.setSpeed(10);
        assertThat("Normal speed assignment incorrect.", tc.getSpeed(), is(closeTo(10, 0.0)));

        tc.setSpeed(-10);
        assertThat("Speed exact min incorrect.", tc.getSpeed(), is(closeTo(-10, 0.0)));

        tc.setSpeed(20);
        assertThat("Speed exact max incorrect.", tc.getSpeed(), is(closeTo(20, 0.0)));

        tc.setSpeed(-11);
        assertThat("Speed to slow incorrect.", tc.getSpeed(), is(closeTo(-10, 0.0)));

        tc.setSpeed(21);
        assertThat("Speed to high incorrect.", tc.getSpeed(), is(closeTo(20, 0.0)));
    }
}
