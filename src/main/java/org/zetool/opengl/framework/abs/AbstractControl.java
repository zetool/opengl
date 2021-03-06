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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.media.opengl.GL2;

/**
 *
 * @author Daniel R. Schmidt, Jan-Philipp Kappmeier
 *
 * @param <T> The type of the graphic object that is view by this class
 * @param <U> The type of the object in the data structure (the model) that is view by this class
 */
public abstract class AbstractControl<T, U extends Drawable> implements control, Iterable<T> {

    protected U view;
    protected ArrayList<T> childControls;

    /**
     * No view is set. During construction process, a view must be set! Call {@link #setView(U)}.
     */
    protected AbstractControl() {
        this.childControls = new ArrayList<>();
    }

    public AbstractControl(U controlled) {
        this.view = controlled;
        this.childControls = new ArrayList<>();
    }

    protected void setView(U view) {
        this.view = view;
    }

    @Override
    public U getView() {
        return view;
    }

    public List<T> getChildControls() {
        return Collections.unmodifiableList(childControls);
    }

    protected void add(T childControl) {
        childControls.add(childControl);
    }

    protected void clear() {
        childControls.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return childControls.iterator();
    }

    public int size() {
        return childControls.size();
    }

    public void draw(GL2 gl) {
        view.draw(gl);
    }
}
