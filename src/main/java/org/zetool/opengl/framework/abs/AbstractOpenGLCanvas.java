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
package org.zetool.opengl.framework.abs;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.PrintStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import org.zetool.math.Conversion;
import org.zetool.opengl.helper.Util;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
abstract public class AbstractOpenGLCanvas extends GLCanvas implements GLEventListener, OpenGLRenderer, Animatable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    protected int clearBits = GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT;
    protected AnimatorBase animator;
    private int maxFPS = 200;
    private long lastFrameTime = 0;
    private long animationStartTime = 0;
    private int fps = 0;
    private int frameCount = 0;
    private long deltaTime;
    protected long lastTime = 0;
    protected GL2 gl;
    protected GLU glu;
    private boolean loop = false;

    /**
     *
     */
    public AbstractOpenGLCanvas() {
        super(new GLCapabilities(null));
        super.setBackground(Color.black);
        addGLEventListener(this);
        animator = new FPSAnimator(this, maxFPS);
    }

    /**
     *
     * @param caps
     */
    public AbstractOpenGLCanvas(GLCapabilities caps) {
        super(caps);
        super.setBackground(Color.black);
        addGLEventListener(this);
        animator = new Animator(this);
    }

    /**
     * Starts animation and resets the counter.
     */
    public void startAnimation() {
        animationStartTime = System.nanoTime();
        animator.start();
        //animator.setRunAsFastAsPossible( true );
        lastTime = System.nanoTime();
    }

    /**
     * Stops the animation.
     */
    public void stopAnimation() {
        animator.stop();
        fps = 0;
    }

    /**
     * Decides, wheather animation is turned on or of.
     *
     * @return {@code true} if animation is on, {@code false} otherwise
     */
    public final boolean isAnimating() {
        return animator.isAnimating();
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    /**
     * Sets the maximal framerate per second.
     *
     * @param maxFPS the framerate
     */
    public final void setMaxFPS(int maxFPS) {
        this.maxFPS = maxFPS;
        if (isAnimating()) {
            animator.stop();
            animator = new FPSAnimator(this, maxFPS);
            startAnimation();
        } else {
            animator = new FPSAnimator(this, maxFPS);
        }
    }

    /**
     * Returns the maximal allowed framerate per second.
     *
     * @return the maximal allowed framerate per second
     */
    public final int getMaxFPS() {
        return maxFPS;
    }

    /**
     * Returns the time passed in nano seconds since the last frame was drawn.
     *
     * @return the time passed in nano seconds since the last frame was drawn
     */
    public long getDeltaTime() {
        return deltaTime;
    }

    /**
     * Returns the elapsed time in nano seconds since the animation was started.
     *
     * @return the elapsed time in nano seconds since the animation was started
     */
    public long getTimeSinceStart() {
        return lastTime - animationStartTime;
    }

    /**
     * Returns the current frame rate per second.
     *
     * @return the current frame rate per second
     */
    public final int getFPS() {
        return fps;
    }

    /**
     * Computes the current frame rate and updates the elapsed time from the last rendered frame. This method should be
     * called in the display-method if animation is on.
     */
    final public void computeFPS() {
        // calculate real FPS and delay time for animation
        final long currentTime = System.nanoTime();
        lastTime = currentTime;
        if (currentTime - lastFrameTime >= Conversion.SEC_TO_NANO_SECONDS) {
            lastFrameTime = currentTime;
            fps = frameCount;
            frameCount = 0;
        } else {
            frameCount++;
        }
    }

    @Override
    public void animate() {
        final long currentTime = System.nanoTime();
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;

    }

    /**
     * This methods is used to draw our stuff to the GL context. It is called every frame.
     *
     * @param drawable the GL context that we can use
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        // compute real FPS and delay time for animation
        this.gl = drawable.getGL().getGL2();
        if (animator.isAnimating() == true) {
            animate();
        }
        gl.glClear(clearBits);
    }

    /**
     * Initializes this {@code OpenGL} component. This method is called directly after the component is created. Do all
     * you init-gfx stuff here.
     *
     * @param drawable the GL context that we can use
     */
    @Override
    final public void init(GLAutoDrawable drawable) {
        this.initGFX(drawable);
    }

    /**
     * This method is called everytime the GL context is resized. Calculates the current viewport and aspect ratio of
     * the visible area.
     *
     * @param drawable the GL context that we can use
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the context
     * @param height the height of the context
     */
    @Override
    final public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        updateViewport(drawable, x, y, width, height);
    }

    /**
     * Empty default implementation. Override to release OpenGL resources.
     *
     * {@inheritDoc}
     *
     * @param drawable the GL context that is rendered into
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * Called everytime when the mode or the device of the GL context are changed.
     * <p>
     * This method must not to be implemented as it is not yet supported by JOGL!</p>
     *
     * @param drawable the GL context that we can use
     * @param modeChanged true if mode has changed
     * @param deviceChanged true if display device has changed
     */
    final public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public int getClearBits() {
        return clearBits;
    }

    public void setClearBits(int clear) {
        this.clearBits = clear;
    }

    public void useListener() {
        useKeyListener();
        useMouseListener();
        useMouseMotionListener();
        useMouseWheelListener();
    }

    public void removeListener() {
        removeKeyListener(this);
        removeMouseListener(this);
        removeMouseMotionListener(this);
        removeMouseWheelListener(this);
    }

    public void useKeyListener() {
        addKeyListener(this);
    }

    public void useMouseListener() {
        addMouseListener(this);
    }

    public void useMouseMotionListener() {
        this.addMouseMotionListener(this);
    }

    public void useMouseWheelListener() {
        addMouseWheelListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    /**
     * Prints out all error messages that are in the error queue to {@code System.err}.
     */
    protected void printErrors() {
        printErrors(System.err);
    }

    /**
     * Gives out all error messages to a submitted {@link PrintStream}.
     *
     * @param stream
     */
    protected void printErrors(PrintStream stream) {
        Util.printErrors(gl, stream);
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c);
        if (gl != null) {
            gl.glClearColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }
    }
}
