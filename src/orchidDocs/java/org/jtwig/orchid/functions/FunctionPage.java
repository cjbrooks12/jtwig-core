package org.jtwig.orchid.functions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.SimpleResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.ClassDoc;

public class FunctionPage extends OrchidPage {

    private ClassDoc classDoc;

    public FunctionPage(OrchidContext context, ClassDoc classDoc) {
        super(new SimpleResource(context, "functions/"
                        + classDoc.containingPackage().name().replaceAll("org\\.jtwig\\.functions\\.impl\\.", "") + "/"
                        + classDoc.typeName().replaceAll("Function", "") + ".html"),
                "jtwigFunction",
                classDoc.typeName().replaceAll("Function", ""));
        this.classDoc = classDoc;
    }
}
