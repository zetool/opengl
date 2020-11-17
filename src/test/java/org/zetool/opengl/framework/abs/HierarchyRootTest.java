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

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

import javax.media.opengl.GL2;

import mockit.Injectable;
import mockit.Verifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class HierarchyRootTest {

    /**
     * Simple OpenGL mock to be injected in drawing methods.
     */
    @Injectable
    GL2 glMock;

    /**
     * Verify that {@link HierarchyRoot#draw(javax.media.opengl.GL2) } calls static and dynamic draw methods on initial
     * draw.
     */
    @Test
    public void draw_callsInOrder() {
        HierarchyRoot mockedFixture = mock(HierarchyRoot.class, withSettings().useConstructor());
        doCallRealMethod().when(mockedFixture).draw(glMock);

        // First call, redraw complete
        mockedFixture.draw(glMock);

        verify(mockedFixture).draw(glMock);
        verify(mockedFixture).drawStatic(glMock);
        verify(mockedFixture).drawDynamic(glMock);
        verifyNoMoreInteractions(mockedFixture);
    }

    /**
     * Verify that {@link HierarchyRoot#draw(javax.media.opengl.GL2) } only calls the dynamic draw methods on
     * consecutive calls.
     */
    @Test
    public void draw_skipsStatic_whenCalledMultipleTimes() {
        HierarchyRoot mockedFixture = mock(HierarchyRoot.class, withSettings().useConstructor());

        // Initial complete draw call, do not count
        doCallRealMethod().when(mockedFixture).draw(glMock);
        mockedFixture.draw(glMock);
        reset(mockedFixture);

        // Second call, should not call static drawing
        doCallRealMethod().when(mockedFixture).draw(glMock);
        mockedFixture.draw(glMock);

        // Only dynamic drawing is called
        verify(mockedFixture).draw(glMock);
        verify(mockedFixture).drawDynamic(glMock);
        verifyNoMoreInteractions(mockedFixture);
        // Instead, the display list is called
        new Verifications() {
            {
                glMock.glCallList(0);
            }
        };
    }

    /**
     * Verify that {@link HierarchyRoot#draw(javax.media.opengl.GL2) } calls the required static and dynamic methods as
     * well as all children.
     */
    @Test
    public void update_forcesStaticRedraw() {
        HierarchyRoot mockedFixture = mock(HierarchyRoot.class, withSettings().useConstructor());

        // Initial complete draw call, do not count
        doCallRealMethod().when(mockedFixture).draw(glMock);
        mockedFixture.draw(glMock);

        // Second call, should not call static drawing
        doCallRealMethod().when(mockedFixture).update();
        mockedFixture.update();
        reset(mockedFixture);

        // Verify the next draw calls static drawing
        doCallRealMethod().when(mockedFixture).draw(glMock);
        mockedFixture.draw(glMock);

        verify(mockedFixture).draw(glMock);
        verify(mockedFixture).drawStatic(glMock);
        verify(mockedFixture).drawDynamic(glMock);
        verifyNoMoreInteractions(mockedFixture);
    }

    @AfterMethod
    public void validate() {
        validateMockitoUsage();
    }
}
