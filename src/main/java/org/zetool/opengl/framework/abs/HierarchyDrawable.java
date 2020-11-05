/*
 * zet evacuation tool copyright (c) 2007-20 zet evacuation team
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

import org.zetool.opengl.drawingutils.GLVector;

/**
 * A {@link Drawable} object that can contain other {@code Drawable}s as children.
 *
 * @param <U>
 * @author Jan-Philipp Kappmeier
 */
public abstract class HierarchyDrawable<U extends Drawable> implements Drawable, HierarchyNode {

    private final GLVector position;
    protected ArrayList<U> children;
    /**
     * Whether the static structure has to be redrawn.
     */
    private boolean repaint = true;
    protected int displayList = 0;

    public HierarchyDrawable(GLVector position) {
        this.position = position;
        children = new ArrayList<>();
    }

    /**
     *
     * @deprecated future versions will be immutable. Re-create the hierarchy to update or use models that supports
     * invisibility.
     */
    @Deprecated
    public void clear() {
        children.clear();
    }

    /**
     *
     * @param child
     * @deprecated future versions will be immutable. Re-create the hierarchy to update or use models that supports
     * invisibility.
     */
    @Deprecated
    public void addChild(U child) {
        children.add(child);
    }

    /**
     * Calls {@link #draw( GLAutoDrawable) } for all contained objects.
     *
     * @param gl
     */
    public void drawAllChildren(GL2 gl) {
        children.forEach(child -> child.draw(gl));
    }

    /**
     * Executes the static drawing of children. Static drawing is generally independent from time and can be done
     * efficiently. Updates do not happen continuously over time, but have to be triggered manually by the call of
     * {@link #update()}.
     *
     * @param gl
     */
    public void staticDrawAllChildren(GL2 gl) {
        children.forEach(child -> child.performStaticDrawing(gl));
    }

    @Override
    final public void draw(GL2 gl) {
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

    // Drawing:
    // erst staticDrawAllChildren
    // Dann: drawAllChildren -> calls again staticDraw
    // daraus folgt: doppelt gezeichnet
    
    
    public void performDrawing(GL2 gl) {
        if (repaint) {
            performStaticDrawing(gl);
        }

        gl.glCallList(displayList);

        drawAllChildren(gl);
    }

    @Override
    public void performStaticDrawing(GL2 gl) {
        if (displayList <= 0) {
            gl.glDeleteLists(displayList, 1);
        }
        displayList = gl.glGenLists(1);
        gl.glNewList(displayList, GL2.GL_COMPILE);
        staticDrawAllChildren(gl);
        gl.glEndList();
        repaint = false;
    }

    @Override
    public void update() {
        repaint = true;
    }

    /**
     * Clears the list of children and frees memory used for drawing.
     *
     * Notice: in future versions the list of children is immutable and this will not remove children any more.
     */
    @Override
    public void delete() {
        children.clear();
    }
}
