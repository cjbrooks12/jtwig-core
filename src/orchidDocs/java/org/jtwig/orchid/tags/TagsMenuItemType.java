package org.jtwig.orchid.tags;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;
import org.jtwig.orchid.JtwigOrchidModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TagsMenuItemType implements OrchidMenuItemType {

    private OrchidContext context;
    private JtwigOrchidModel jtwigModel;

    @Inject
    public TagsMenuItemType(OrchidContext context, JtwigOrchidModel jtwigModel) {
        this.context = context;
        this.jtwigModel = jtwigModel;
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> items = new ArrayList<>();
        List<OrchidPage> pages = new ArrayList<>();
        pages.addAll(jtwigModel.getTags());
        items.add(new OrchidMenuItem(context, "Tags", pages));
        return items;
    }
}
