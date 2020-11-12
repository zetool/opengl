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

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.media.opengl.GL2;

import mockit.Injectable;
import org.mockito.InOrder;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.zetool.opengl.drawingutils.GLVector;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class HierarchyDrawableTest {

    /**
     * Simple OpenGL mock to be injected in drawing methods.
     */
    @Injectable
    GL2 glMock;
    /**
     * A set of child elements.
     */
    private final static List<HierarchyDrawable> CHILDREN = rangeClosed(1, 3)
            .mapToObj(unused -> mock(HierarchyDrawable.class, withSettings().useConstructor())).collect(toList());

    /**
     * Verify that {@link HierarchyDrawable#draw(javax.media.opengl.GL2) } calls the required static and dynamic methods
     * as well as all children.
     */
    @Test
    public void draw_callsInOrder() {
        HierarchyDrawable mockedFixture = mock(HierarchyDrawable.class);
        doCallRealMethod().when(mockedFixture).draw(glMock);

        mockedFixture.draw(glMock);

        assertOrder(mockedFixture);
    }

    /**
     * Asserts that the {@link HierarchyDrawable#beginDraw(javax.media.opengl.GL2) begin} and
     * {@link HierarchyDrawable#endDraw(javax.media.opengl.GL2) end} draw methods are called before and after the
     * drawing process, repsectively.
     *
     * @param mockedFixture the mocked instance that is asserted
     */
    private void assertOrder(HierarchyDrawable mockedFixture) {
        InOrder inOrderStatic = inOrder(mockedFixture);
        inOrderStatic.verify(mockedFixture).beginDraw(glMock);
        inOrderStatic.verify(mockedFixture).performStaticDrawing(glMock);
        inOrderStatic.verify(mockedFixture).endDraw(glMock);

        InOrder inOrderDynamic = inOrder(mockedFixture);
        inOrderDynamic.verify(mockedFixture).beginDraw(glMock);
        inOrderDynamic.verify(mockedFixture).performDynamicDrawing(glMock);
        inOrderDynamic.verify(mockedFixture).endDraw(glMock);

        InOrder inOrderChildren = inOrder(mockedFixture);
        inOrderChildren.verify(mockedFixture).beginDraw(glMock);
        inOrderChildren.verify(mockedFixture).drawAllChildren(glMock);
        inOrderChildren.verify(mockedFixture).endDraw(glMock);
    }

    /**
     * Asserts that begin draw sets the position.
     */
    @Test
    public void beginDraw_translates() {
        // Position to which the location should be set
        GLVector mockPosition = mock(GLVector.class);
        AtomicBoolean called = new AtomicBoolean();
        doAnswer(unused -> called.getAndSet(true)).when(mockPosition).translate(glMock);

        // Create the fixture
        HierarchyDrawable mockedFixture = mock(HierarchyDrawable.class, withSettings().useConstructor(mockPosition));
        doCallRealMethod().when(mockedFixture).beginDraw(glMock);

        mockedFixture.beginDraw(glMock);

        assertThat(called.get(), is(true));
    }

    @Test
    public void drawAllChildren_callsChildren() {
        HierarchyDrawable mockedFixture = mock(HierarchyDrawable.class,
                withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        CHILDREN.forEach(mockedFixture::addChild);

        mockedFixture.drawAllChildren(glMock);

        CHILDREN.forEach(child -> {
            verify(mockedFixture).addChild(child);
            verify(child).draw(glMock);
            verifyNoMoreInteractions(child);
        });
        verify(mockedFixture).drawAllChildren(glMock);
        verifyNoMoreInteractions(mockedFixture);
    }

    @Test
    public void drawStatic_callsStatic() {
        HierarchyDrawable mockedFixture = mock(HierarchyDrawable.class,
                withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        CHILDREN.forEach(child -> {
            mockedFixture.addChild(child);
            doNothing().when(child).drawStatic(glMock);
        });

        mockedFixture.drawStatic(glMock);

        CHILDREN.forEach(child -> {
            verify(child).drawStatic(glMock);
            verifyNoMoreInteractions(child);
        });
    }

    @Test
    public void drawDynamic_callsDynamic() {
        HierarchyDrawable mockedFixture = mock(HierarchyDrawable.class,
                withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        CHILDREN.forEach(child -> {
            mockedFixture.addChild(child);
            doNothing().when(child).drawDynamic(glMock);
        });

        mockedFixture.drawDynamic(glMock);

        CHILDREN.forEach(child -> {
            verify(child).drawDynamic(glMock);
            verifyNoMoreInteractions(child);
        });
    }

    @AfterMethod
    public void validate() {
        validateMockitoUsage();
    }

}
