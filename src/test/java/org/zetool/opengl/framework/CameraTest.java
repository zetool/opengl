/* zet evacuation tool copyright (c) 2007-20 zet evacuation team
 *
 * This program is free software; you can redistribute it and/or
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.zetool.opengl.framework;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.testng.annotations.Test;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class CameraTest {

    /**
     * Checks wheather the setSpeed() method works correct, or not.
     *
     * @throws Exception
     */
    @Test
    public void minAndMaxSpeed_areRespected() throws Exception {
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
