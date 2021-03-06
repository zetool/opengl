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

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.zetool.math.vectormath.Vector3;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class JFlyingEyePanel extends JOpenGLPanel {

    protected Vector3 pos = new Vector3(); // position
    protected Vector3 view = new Vector3(0, 0, 1); // direction of view (z-axis)
    protected Vector3 up = new Vector3(0, 1, 0); // direction of up   (y-axis)
    private int initMouseX;
    private int initMouseY;
    private boolean mouseMove;
    private Vector3 initView;
    private Vector3 initUp;
    double speed = 0.05;
    double speedStep = 0.01;
    double minSpeed = 0;
    double maxSpeed = 1;

    /**
     *
     */
    public JFlyingEyePanel() {
        super();
        useKeyListener();
        useMouseListener();
        useMouseMotionListener();
    }

    /**
     *
     * @param caps
     */
    public JFlyingEyePanel(GLCapabilities caps) {
        super(caps);
        useKeyListener();
        useMouseListener();
        useMouseMotionListener();
    }

    @Override
    public void updateViewport(GLAutoDrawable drawable, int x, int y, int width, int height) {
        super.updateViewport(drawable, x, y, width, height); // calculate viewport
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLUgl2();

        if (height <= 0) // avoid a divide by zero error!
        {
            height = 1;
        }
        final float aspect = (float) width / (float) height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    //@Override
    //public void renderScene( GLAutoDrawable drawable ) {
    //    super.renderScene( drawable ); // let clear the screen
    //}
    // look-method
    public void look() {
        GLU glu = new GLUgl2();
        // set eye position and direction of view
        glu.gluLookAt(pos.x, pos.y, pos.z, pos.x - view.x, pos.y - view.y, pos.z - view.z, up.x, up.y, up.z);
    }

}
