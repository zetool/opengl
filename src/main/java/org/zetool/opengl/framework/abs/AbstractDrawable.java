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

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.zetool.opengl.drawingutils.GLVector;

/**
 * H
 *
 * @param <U> The type of the children view object
 * @param <V> the model
 * @author Jan-Philipp Kapmeier
 * @author Daniel Plümpe
 */
public abstract class AbstractDrawable<U extends Drawable, V> implements Drawable, HierarchyNode {

    /** Access to OpenGL Utility Library for implementing classes. Currently fixed on OpenGL 2 profile.*/
    protected static final GLU GLU_INSTANCE = new GLUgl2();
    /** Access to OpenGL Utility Library quadric object for implementing classes. */
    protected static final GLUquadric GLU_QUADRIC = GLU_INSTANCE.gluNewQuadric();
    protected V control;
    protected int displayList = 0;
    protected boolean repaint = true;
    protected boolean callChildren = true;
    protected GLVector position = new GLVector();
    protected ArrayList<U> children;

    public AbstractDrawable(V control) {
        children = new ArrayList<>();
        this.control = control;
    }

    public void clear() {
        children.clear();
    }

    public void addChild(U child) {
        children.add(child);
    }

    public V getControl() {
        return control;
    }

    public boolean isVisible() {
        return true;
    }

    /**
     * Calls {@link #draw( GLAutoDrawable) } for all contained objects.
     *
     * @param gl
     */
    public void drawAllChildren(GL2 gl) {
        children.forEach(child -> child.draw(gl));
    }

    public void staticDrawAllChildren(GL2 gl) {
        children.forEach(child -> child.performStaticDrawing(gl));
    }

    @Override
    final public void draw(GL2 gl) {
        if (!callChildren) {
            performDrawing(gl);
            return;
        }
        beginDraw(gl);
        performDrawing(gl);
        endDraw(gl);
    }

    /**
     * This method is called prior to performing the actual drawing. In its default behavior, it translates the
     * {@code AbstractDrawable} to the origin.
     *
     * @param gl
     */
    public void beginDraw(GL2 gl) {
        gl.glPushMatrix();
        position.translate(gl);
    }

    /**
     * This method is called after the drawing has been performed. In its default behavior, it tranlates the
     * {@code AbstractDrawable} back to where it has been.
     *
     * @param gl the {@code OpenGL} context on which it is drawn
     */
    public void endDraw(GL2 gl) {
        gl.glPopMatrix();
    }

    public void performDrawing(GL2 gl) {
        drawAllChildren(gl);
    }

    @Override
    public abstract void update();

    /**
     * Sets the control object to {@code null} and clears the list of children.
     */
    @Override
    public void delete() {
        control = null;
        children.clear();
    }
}
