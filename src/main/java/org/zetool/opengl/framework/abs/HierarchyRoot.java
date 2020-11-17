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

import javax.media.opengl.GL2;

import org.zetool.opengl.drawingutils.GLVector;

/**
 * An implementation of a node in the drawable hierarchy that efficiently uses display lists. It can be used as root
 * node of the hierarchy due to its handling of dynamic and static children.
 *
 * The abstract methods of {@link HierarchyDrawable} for drawing the
 * {@link HierarchyDrawable#staticDrawAllChildren(javax.media.opengl.GL2) static} and
 * {@link HierarchyDrawable#dynamicDrawAllChildren(javax.media.opengl.GL2) dynamic} content of the node itself are
 * implemented empty. They can be overriden by sub classes.
 *
 * @param <U> the child node type
 * @author Jan-Philipp Kappmeier
 */
public class HierarchyRoot<U extends HierarchyDrawable> extends HierarchyDrawable<U> {

    private int displayList = 0;

    /**
     * Whether the static structure has to be redrawn.
     */
    private boolean repaintStatic = true;

    /**
     * Initializes the drawable root node of the hierarchy at the origin.
     */
    public HierarchyRoot() {
        this(new GLVector());
    }

    /**
     * Initializes the drawable node in the hierarchy with a position. The scene is translated to the {@code position}
     * prior to {@link #draw(javax.media.opengl.GL2) drawing}.
     *
     * @param position the offset
     */
    public HierarchyRoot(GLVector position) {
        super(position);
    }

    /**
     * Draws the scene efficiently by storing static parts as a display list. The first call and any call after
     * {@link #update() an update} store the {@link #drawStatic(javax.media.opengl.GL2) static} part of the scene as
     * display list. For other calls the only the {@link #drawDynamic(javax.media.opengl.GL2) dynamic} part is drawn and
     * the static parts are drawn from the display list.
     *
     * @param gl the OpenGL object to draw
     */
    @Override
    final public void draw(GL2 gl) {
        if (repaintStatic) {
            redrawStatic(gl);
        } else {
            gl.glCallList(displayList);
        }

        drawDynamic(gl);
    }

    /**
     * Draws the static part of the hierarchy as display list.
     *
     * @param gl
     */
    private void redrawStatic(GL2 gl) {
        if (displayList <= 0) {
            gl.glDeleteLists(displayList, 1);
        }
        displayList = gl.glGenLists(1);
        gl.glNewList(displayList, GL2.GL_COMPILE);
        drawStatic(gl);
        gl.glEndList();
        repaintStatic = false;
    }

    @Override
    public void update() {
        repaintStatic = true;
    }

    @Override
    protected void performDynamicDrawing(GL2 gl) {
        // empty
    }

    @Override
    protected void performStaticDrawing(GL2 gl) {
        // empty
    }
}
