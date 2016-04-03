package org.jtwig.render.node.renderer;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.model.expression.Expression;
import org.jtwig.model.tree.EmbedNode;
import org.jtwig.model.tree.OverrideBlockNode;
import org.jtwig.render.RenderRequest;
import org.jtwig.render.RenderResourceRequest;
import org.jtwig.render.context.model.BlockContext;
import org.jtwig.render.expression.CalculateExpressionService;
import org.jtwig.renderable.Renderable;
import org.jtwig.renderable.impl.EmptyRenderable;
import org.jtwig.resource.Resource;
import org.jtwig.resource.exceptions.ResourceNotFoundException;
import org.jtwig.support.MatcherUtils;
import org.jtwig.value.WrappedCollection;
import org.jtwig.value.convert.Converter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.support.MatcherUtils.theSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EmbedNodeRenderTest {
    private EmbedNodeRender underTest = new EmbedNodeRender();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void renderIfNotFoundNotIgnoring() throws Exception {
        String pathExpressionValueAsString = "path";
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        EmbedNode node = mock(EmbedNode.class);
        Expression pathExpression = mock(Expression.class);
        Resource resource = mock(Resource.class);
        CalculateExpressionService calculateExpressionService = mock(CalculateExpressionService.class);
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        Object pathExpressionValue = "path";

        when(node.getResourceExpression()).thenReturn(pathExpression);
        when(node.isIgnoreMissing()).thenReturn(false);
        when(request.getEnvironment()).thenReturn(environment);
        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(resource);
        when(environment.getRenderEnvironment().getCalculateExpressionService()).thenReturn(calculateExpressionService);
        when(environment.getResourceEnvironment().getResourceResolver().resolve(environment, resource, pathExpressionValueAsString)).thenReturn(Optional.<Resource>absent());
        when(calculateExpressionService.calculate(request, pathExpression)).thenReturn(pathExpressionValue);
        when(environment.getValueEnvironment().getStringConverter().convert(pathExpressionValue)).thenReturn(pathExpressionValueAsString);

        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage(containsString("Resource 'path' not found"));

        underTest.render(request, node);
    }

    @Test
    public void renderIfNotFoundIgnoringMissing() throws Exception {
        String pathExpressionValueAsString = "pathExpressionValueAsString";
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        EmbedNode node = mock(EmbedNode.class);
        Expression pathExpression = mock(Expression.class);
        Resource resource = mock(Resource.class);
        CalculateExpressionService calculateExpressionService = mock(CalculateExpressionService.class);
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        Object pathExpressionValue = new Object();

        when(node.getResourceExpression()).thenReturn(pathExpression);
        when(node.isIgnoreMissing()).thenReturn(true);
        when(request.getEnvironment()).thenReturn(environment);
        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(resource);
        when(environment.getValueEnvironment().getStringConverter().convert(pathExpressionValue)).thenReturn(pathExpressionValueAsString);
        when(environment.getRenderEnvironment().getCalculateExpressionService()).thenReturn(calculateExpressionService);
        when(environment.getResourceEnvironment().getResourceResolver().resolve(environment, resource, pathExpressionValueAsString)).thenReturn(Optional.<Resource>absent());
        when(calculateExpressionService.calculate(request, pathExpression)).thenReturn(pathExpressionValue);

        Renderable result = underTest.render(request, node);

        assertThat(result, is((Renderable) EmptyRenderable.instance()));
    }

    @Test
    public void renderIfFoundInheritModel() throws Exception {
        String pathExpressionValueAsString = "pathExpressionValueAsString";
        boolean inheritModel = true;
        Renderable renderable = mock(Renderable.class, "expected");
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        EmbedNode node = mock(EmbedNode.class);
        Expression pathExpression = mock(Expression.class, "pathExpression");
        Resource resource = mock(Resource.class, "parentResource");
        CalculateExpressionService calculateExpressionService = mock(CalculateExpressionService.class);
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        Object pathExpressionValue = new Object();
        Resource newResource = mock(Resource.class, "new");
        Expression mapExpression = mock(Expression.class, "map");
        WrappedCollection wrappedCollection = mock(WrappedCollection.class, "collection");
        OverrideBlockNode node1 = mock(OverrideBlockNode.class, "node1");
        OverrideBlockNode node2 = mock(OverrideBlockNode.class, "node2");
        Object mapExpressionValue = new Object();

        when(node.getResourceExpression()).thenReturn(pathExpression);
        when(node.isIgnoreMissing()).thenReturn(true);
        when(node.isInheritModel()).thenReturn(inheritModel);
        when(node.getMapExpression()).thenReturn(mapExpression);
        when(node.getNodes()).thenReturn(asList(node1, node2));
        when(request.getEnvironment()).thenReturn(environment);
        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(resource);
        when(environment.getValueEnvironment().getStringConverter().convert(pathExpressionValue)).thenReturn(pathExpressionValueAsString);
        when(environment.getRenderEnvironment().getCalculateExpressionService()).thenReturn(calculateExpressionService);
        when(environment.getResourceEnvironment().getResourceResolver().resolve(environment, resource, pathExpressionValueAsString)).thenReturn(Optional.of(newResource));
        when(calculateExpressionService.calculate(request, pathExpression)).thenReturn(pathExpressionValue);
        when(calculateExpressionService.calculate(request, mapExpression)).thenReturn(mapExpressionValue);
        when(environment.getValueEnvironment().getCollectionConverter().convert(mapExpressionValue)).thenReturn(Converter.Result.defined(wrappedCollection));
        when(environment.getRenderEnvironment().getRenderResourceService().render(eq(request), argThat(theSame(new RenderResourceRequest(newResource, false, !inheritModel, wrappedCollection)))))
                .thenReturn(renderable);

        Renderable result = underTest.render(request, node);

        assertSame(renderable, result);
        verify(request.getRenderContext().getBlockContext()).start(argThat(MatcherUtils.theSame(BlockContext.newContext())));
        verify(request.getRenderContext().getBlockContext()).end();
        verify(environment.getRenderEnvironment().getRenderNodeService()).render(request, node1);
        verify(environment.getRenderEnvironment().getRenderNodeService()).render(request, node2);
    }

    @Test
    public void renderIfFoundDoNotInheritModel() throws Exception {
        String pathExpressionValueAsString = "pathExpressionValueAsString";
        boolean inheritModel = false;
        Renderable renderable = mock(Renderable.class, "expected");
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        EmbedNode node = mock(EmbedNode.class);
        Expression pathExpression = mock(Expression.class, "pathExpression");
        Resource resource = mock(Resource.class, "parentResource");
        CalculateExpressionService calculateExpressionService = mock(CalculateExpressionService.class);
        Environment environment = mock(Environment.class, RETURNS_DEEP_STUBS);
        Object pathExpressionValue = new Object();
        Resource newResource = mock(Resource.class, "new");
        Expression mapExpression = mock(Expression.class, "map");
        WrappedCollection wrappedCollection = mock(WrappedCollection.class, "collection");
        OverrideBlockNode node1 = mock(OverrideBlockNode.class, "node1");
        OverrideBlockNode node2 = mock(OverrideBlockNode.class, "node2");
        Object mapExpressionValue = new Object();

        when(node.getResourceExpression()).thenReturn(pathExpression);
        when(node.isIgnoreMissing()).thenReturn(true);
        when(node.isInheritModel()).thenReturn(inheritModel);
        when(node.getMapExpression()).thenReturn(mapExpression);
        when(node.getNodes()).thenReturn(asList(node1, node2));
        when(request.getEnvironment()).thenReturn(environment);
        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(resource);
        when(environment.getValueEnvironment().getStringConverter().convert(pathExpressionValue)).thenReturn(pathExpressionValueAsString);
        when(environment.getRenderEnvironment().getCalculateExpressionService()).thenReturn(calculateExpressionService);
        when(environment.getResourceEnvironment().getResourceResolver().resolve(environment, resource, pathExpressionValueAsString)).thenReturn(Optional.of(newResource));
        when(calculateExpressionService.calculate(request, pathExpression)).thenReturn(pathExpressionValue);
        when(calculateExpressionService.calculate(request, mapExpression)).thenReturn(mapExpressionValue);
        when(environment.getValueEnvironment().getCollectionConverter().convert(mapExpressionValue)).thenReturn(Converter.Result.defined(wrappedCollection));
        when(environment.getRenderEnvironment().getRenderResourceService().render(eq(request), argThat(theSame(new RenderResourceRequest(newResource, false, !inheritModel, wrappedCollection)))))
                .thenReturn(renderable);

        Renderable result = underTest.render(request, node);

        assertSame(renderable, result);
        verify(request.getRenderContext().getBlockContext()).start(argThat(MatcherUtils.theSame(BlockContext.newContext())));
        verify(request.getRenderContext().getBlockContext()).end();
        verify(environment.getRenderEnvironment().getRenderNodeService()).render(request, node1);
        verify(environment.getRenderEnvironment().getRenderNodeService()).render(request, node2);
    }
}