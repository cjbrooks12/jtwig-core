package org.jtwig.orchid.tags;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.SimpleResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.ClassDoc;

public class TagPage extends OrchidPage {

    private ClassDoc classDoc;

    public TagPage(OrchidContext context, ClassDoc classDoc) {
        super(new SimpleResource(context, "tags/" + classDoc.typeName().replaceAll("Node", "") + ".html"),
                "jtwigTag",
                classDoc.typeName().replaceAll("Node", ""));
        this.classDoc = classDoc;
    }
}
