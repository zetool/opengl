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
import java.util.Iterator;
import java.util.Objects;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import org.zetool.opengl.drawingutils.GLVector;

/**
 * A {@link Drawable} object that can contain other {@code Drawable}s as children with static and dynamic part.
 *
 * @param <U>
 * @author Jan-Philipp Kappmeier
 */
public abstract class HierarchyDrawable<U extends HierarchyDrawable> implements Drawable, HierarchyNode<U> {

    private final GLVector position;
    protected final ArrayList<U> children;

    public HierarchyDrawable() {
        this(new GLVector());
    }

    public HierarchyDrawable(GLVector position) {
        this.position = position;
        children = new ArrayList<>();
    }

    /**
     * Returns an iterator over the children of type {@code T}.
     *
     * @return an iterator
     */
    @Override
    public Iterator<U> iterator() {
        return children.iterator();
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
     * Draws a given node in an OpenGL hierarchy. The following steps are executed:
     * <ol>
     * <li> Translate the position to the position of this hierarchy element.
     * <li> Draw static parts of the hierarchy element.
     * <li> Draw dynamic parts of the hierarchy element.
     * <li> Recurse {@link #draw(javax.media.opengl.GL2) the drawing} for children.
     * <li> Revert the translation back to the original position.
     * </ol>
     *
     * @param gl the graphics context to draw into
     */
    @Override
    public void draw(GL2 gl) {
        beginDraw(gl);
        performStaticDrawing(gl);
        performDynamicDrawing(gl);
        drawAllChildren(gl);
        endDraw(gl);
    }

    /**
     * Calls {@link #draw( GLAutoDrawable) } for all contained objects.
     *
     * @param gl
     */
    protected void drawAllChildren(GL2 gl) {
        children.forEach(child -> child.draw(gl));
    }

    protected void dynamicDrawAllChildren(GL2 gl) {
        children.forEach(child -> child.drawDynamic(gl));
    }

    /**
     * Executes the static drawing of children. Static drawing is generally independent from time and can be done
     * efficiently. Updates do not happen continuously over time, but have to be triggered manually by the call of
     * {@link #update()}.
     *
     * @param gl
     */
    protected void staticDrawAllChildren(GL2 gl) {
        children.forEach(child -> child.drawStatic(gl));
    }

    /**
     * This method is called prior to performing the actual drawing. In its default behavior, it translates the
     * {@code AbstractDrawable} to the origin.
     *
     * @param gl
     */
    protected void beginDraw(GL2 gl) {
        gl.glPushMatrix();
        position.translate(gl);
    }

    /**
     * This method is called after the drawing has been performed. In its default behavior, it tranlates the
     * {@code AbstractDrawable} back to where it has been.
     *
     * @param gl the {@code OpenGL} context on which it is drawn
     */
    protected void endDraw(GL2 gl) {
        gl.glPopMatrix();
    }

    protected final void drawDynamic(GL2 gl) {
        beginDraw(gl);
        performDynamicDrawing(gl);
        dynamicDrawAllChildren(gl);
        endDraw(gl);
    }

    protected final void drawStatic(GL2 gl) {
        beginDraw(gl);
        performStaticDrawing(gl);
        staticDrawAllChildren(gl);
        endDraw(gl);
    }

    abstract protected void performDynamicDrawing(GL2 gl);

    abstract protected void performStaticDrawing(GL2 gl);

    @Override
    public void update() {
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
