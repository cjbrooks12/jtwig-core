package org.jtwig.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.jtwig.orchid.functions.FunctionPage;
import org.jtwig.orchid.tags.TagPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class JtwigGenerator extends OrchidGenerator implements OptionsHolder {

    private JtwigOrchidModel jtwigModel;
    private TemplateResolutionStrategy templateResolutionStrategy;

    @Inject
    public JtwigGenerator(
            OrchidContext context,
            OrchidResources resources,
            OrchidRenderer renderer,
            JtwigOrchidModel jtwigModel,
            TemplateResolutionStrategy templateResolutionStrategy) {
        super(500, "jtwigTags", context, resources, renderer);
        this.jtwigModel = jtwigModel;
        this.templateResolutionStrategy = templateResolutionStrategy;
    }

    @Override
    public String getDescription() {
        return "Renders a page for each registered Jtwig tag.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        jtwigModel.initialize();

        try {
            RootDoc rootDoc = context.getInjector().getInstance(RootDoc.class);

            // Get tag classes from org.jtwig.model.tree package
            for(ClassDoc classDoc : rootDoc.packageNamed("org.jtwig.model.tree").allClasses()) {
                if(classDoc.isStatic()) {
                    continue;
                }
                jtwigModel.getTags().add(new TagPage(context, classDoc));
            }

            // Get tag classes from org.jtwig.model.tree package
            Set<PackageDoc> functionPackages = new HashSet<>();

            for (ClassDoc classDoc : rootDoc.classes()) {
                if(classDoc.containingPackage().name().startsWith("org.jtwig.functions.impl")) {
                    functionPackages.add(classDoc.containingPackage());
                }
            }

            for(PackageDoc functionPackage : functionPackages) {
                for(ClassDoc classDoc : functionPackage.allClasses()) {
                    if(classDoc.isStatic()) {
                        continue;
                    }
                    if(!jtwigModel.getFunctions().containsKey(functionPackage)) {
                        jtwigModel.getFunctions().put(functionPackage, new ArrayList<FunctionPage>());
                    }
                    jtwigModel.getFunctions().get(functionPackage).add(new FunctionPage(context, classDoc));
                }
            }

            return jtwigModel.getAllPages();
        }
        catch (Exception e) {
            Clog.w("Jtwig Tag generator can only be used when Orchid is run from Javadoc");
            return null;
        }
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        for(OrchidPage page : pages) {
            renderer.renderTemplate(page);
        }
    }
}
