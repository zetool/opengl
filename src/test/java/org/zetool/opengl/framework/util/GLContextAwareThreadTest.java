/*
 * zet evacuation tool copyright © 2007-20 zet evacuation team
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
package org.zetool.opengl.framework.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.function.Supplier;

import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.testng.annotations.Test;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class GLContextAwareThreadTest {

    @Test
    public void testExecution() {
        Object testObject = new Object();
        Supplier<Object> x = () -> testObject;

        Object result = GLContextAwareThread.createWithGLContext(x);

        assertThat(result, is(sameInstance(testObject)));
    }

    @Test
    public void testGLRequirement() {
        Supplier<GLUquadric> x = () -> new GLUgl2().gluNewQuadric();

        Object result = GLContextAwareThread.createWithGLContext(x);

        assertThat(result, is(instanceOf(GLUquadric.class)));
    }

    @Test
    public void exceptionIsThrown() {
        RuntimeException toBeThrown = new RuntimeException();
        Supplier<Object> x = () -> {
            throw toBeThrown;
        };

        try {
            GLContextAwareThread.createWithGLContext(x);
        } catch (RuntimeException ex) {
            assertThat(ex, is(sameInstance(toBeThrown)));
        }
    }
}
