package org.jtwig.orchid.functions;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.SimpleResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Tag;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FunctionPage extends OrchidPage {

    private ClassDoc classDoc;

    private String functionName;
    private URL compatibilityUrl;

    public FunctionPage(OrchidContext context, ClassDoc classDoc) {
        super(new SimpleResource(context, "functions/"
                        + classDoc.containingPackage().name().replaceAll("org\\.jtwig\\.functions\\.impl\\.", "") + "/"
                        + classDoc.typeName().replaceAll("Function", "") + ".html"),
                "jtwigFunction",
                classDoc.typeName().replaceAll("Function", ""));
        this.classDoc = classDoc;

        functionName = "";
        for(Tag tag : classDoc.tags("functionName")) {
            functionName += tag.text();
        }

        String compatibilityUrlString = "";
        for(Tag tag : classDoc.tags("compatibilityUrl")) {
            compatibilityUrlString += tag.text();
        }
        if(!EdenUtils.isEmpty(compatibilityUrlString)) {
            try {
                compatibilityUrl = new URL(compatibilityUrlString);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionDescription() {
        return classDoc.commentText();
    }

    public URL compatibilityUrl() {
        return compatibilityUrl;
    }

    public List<MethodDoc> getVariants() {
        List<MethodDoc> variants = new ArrayList<>();
        for(MethodDoc method : classDoc.methods()) {
            if(method.name().equals("apply")) {
                variants.add(method);
            }
        }
        return variants;
    }
}
