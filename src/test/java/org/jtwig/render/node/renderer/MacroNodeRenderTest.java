package org.jtwig.render.node.renderer;

import org.jtwig.model.tree.MacroNode;
import org.jtwig.model.tree.Node;
import org.jtwig.render.RenderRequest;
import org.jtwig.render.context.model.Macro;
import org.jtwig.render.context.model.MacroAliasesContext;
import org.jtwig.render.context.model.MacroDefinitionContext;
import org.jtwig.renderable.Renderable;
import org.jtwig.renderable.impl.EmptyRenderable;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.jtwig.support.MatcherUtils.theSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class MacroNodeRenderTest {
    private MacroNodeRender underTest = new MacroNodeRender();

    @Test
    public void noContext() throws Exception {
        RenderRequest renderRequest = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        MacroNode macroNode = mock(MacroNode.class);
        MacroDefinitionContext macroDefinitionContext = mock(MacroDefinitionContext.class);

        when(renderRequest.getRenderContext().hasCurrent(MacroDefinitionContext.class)).thenReturn(false);
        when(renderRequest.getRenderContext().getCurrent(MacroDefinitionContext.class)).thenReturn(macroDefinitionContext);

        Renderable result = underTest.render(renderRequest, macroNode);

        assertSame(EmptyRenderable.instance(), result);
        verify(macroDefinitionContext, never()).add(anyString(), any(Macro.class));
    }

    @Test
    public void withContextWithMacroContextNotDefined() throws Exception {
        String identifier = "identifier";
        List<String> arguments = Collections.singletonList("arg");
        MacroDefinitionContext macroDefinitionContext = mock(MacroDefinitionContext.class);
        RenderRequest renderRequest = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        MacroNode macroNode = mock(MacroNode.class, RETURNS_DEEP_STUBS);
        Node node = mock(Node.class);

        when(renderRequest.getRenderContext().getCurrent(MacroDefinitionContext.class)).thenReturn(macroDefinitionContext);
        when(renderRequest.getRenderContext().hasCurrent(MacroDefinitionContext.class)).thenReturn(true);
        when(renderRequest.getRenderContext().hasCurrent(MacroAliasesContext.class)).thenReturn(false);
        when(macroNode.getMacroName().getIdentifier()).thenReturn(identifier);
        when(macroNode.getContent()).thenReturn(node);
        when(macroNode.getMacroArgumentNames()).thenReturn(arguments);

        Renderable result = underTest.render(renderRequest, macroNode);

        assertSame(EmptyRenderable.instance(), result);
        verify(macroDefinitionContext).add(eq(identifier), argThat(theSame(new Macro(MacroAliasesContext.newContext(), arguments, node))));
    }

    @Test
    public void withContextWithMacroContextDefined() throws Exception {
        String identifier = "identifier";
        List<String> arguments = Collections.singletonList("arg");
        MacroAliasesContext macroAliasesContext = mock(MacroAliasesContext.class);
        MacroDefinitionContext macroDefinitionContext = mock(MacroDefinitionContext.class);
        RenderRequest renderRequest = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        MacroNode macroNode = mock(MacroNode.class, RETURNS_DEEP_STUBS);
        Node node = mock(Node.class);

        when(renderRequest.getRenderContext().getCurrent(MacroDefinitionContext.class)).thenReturn(macroDefinitionContext);
        when(renderRequest.getRenderContext().hasCurrent(MacroDefinitionContext.class)).thenReturn(true);
        when(renderRequest.getRenderContext().hasCurrent(MacroAliasesContext.class)).thenReturn(true);
        when(renderRequest.getRenderContext().getCurrent(MacroAliasesContext.class)).thenReturn(macroAliasesContext);
        when(macroNode.getMacroName().getIdentifier()).thenReturn(identifier);
        when(macroNode.getContent()).thenReturn(node);
        when(macroNode.getMacroArgumentNames()).thenReturn(arguments);

        Renderable result = underTest.render(renderRequest, macroNode);

        assertSame(EmptyRenderable.instance(), result);
        verify(macroDefinitionContext).add(eq(identifier), argThat(theSame(new Macro(macroAliasesContext, arguments, node))));
    }
}