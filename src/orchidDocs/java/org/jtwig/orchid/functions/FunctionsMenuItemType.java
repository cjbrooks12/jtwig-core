package org.jtwig.orchid.functions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.PackageDoc;
import org.json.JSONObject;
import org.jtwig.orchid.JtwigOrchidModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionsMenuItemType implements OrchidMenuItemType {

    private OrchidContext context;
    private JtwigOrchidModel jtwigModel;

    @Inject
    public FunctionsMenuItemType(OrchidContext context, JtwigOrchidModel jtwigModel) {
        this.context = context;
        this.jtwigModel = jtwigModel;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> items = new ArrayList<>();

        for (Map.Entry<PackageDoc, List<FunctionPage>> functionGroup : jtwigModel.getFunctions().entrySet()) {
            List<OrchidPage> pages = new ArrayList<>();
            pages.addAll(functionGroup.getValue());
            items.add(new OrchidMenuItem(context, functionGroup.getKey().name().replaceAll("org\\.jtwig\\.functions\\.impl\\.", ""), pages));
        }

        return items;
    }
}
