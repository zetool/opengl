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

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.media.opengl.glu.gl2.GLUgl2;

import org.zetool.opengl.drawingutils.GLVector;

/**
 * H
 *
 * @param <C> The type of the children view object
 * @param <M> the model
 * @author Jan-Philipp Kapmeier
 * @author Daniel Plümpe
 */
public abstract class AbstractDrawable<C extends Drawable, M> extends HierarchyDrawable<C> {

    /**
     * Access to OpenGL Utility Library for implementing classes. Currently fixed on OpenGL 2 profile.
     */
    protected static final GLU GLU_INSTANCE = new GLUgl2();
    /**
     * Access to OpenGL Utility Library quadric object for implementing classes.
     */
    protected static final GLUquadric GLU_QUADRIC = GLU_INSTANCE.gluNewQuadric();
    protected M model;

    public AbstractDrawable(M model) {
        this(model, new GLVector());
    }

    public AbstractDrawable(M model, GLVector position) {
        super(position);
        this.model = model;
    }

    public M getModel() {
        return model;
    }

    public boolean isVisible() {
        return true;
    }

    /**
     * Sets the control object to {@code null} and clears the list of children.
     */
    @Override
    public void delete() {
        model = null;
        children.clear();
    }

}
