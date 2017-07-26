package org.jtwig.orchid;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.PackageDoc;
import lombok.Data;
import org.jtwig.orchid.functions.FunctionPage;
import org.jtwig.orchid.tags.TagPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Data
public class JtwigOrchidModel {

    private List<TagPage> tags;
    private Map<PackageDoc, List<FunctionPage>> functions;

    @Inject
    public JtwigOrchidModel() {
        initialize();
    }

    void initialize() {
        this.tags = new ArrayList<>();
        this.functions = new LinkedHashMap<>();
    }

    public List<OrchidPage> getAllPages() {
        List<OrchidPage> allPages = new ArrayList<>();
        allPages.addAll(this.tags);

        for (Map.Entry<PackageDoc, List<FunctionPage>> functionGroup : functions.entrySet()) {
            allPages.addAll(functionGroup.getValue());
        }

        return allPages;
    }
}
