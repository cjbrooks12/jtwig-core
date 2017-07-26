package org.jtwig.orchid;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import org.jtwig.orchid.functions.FunctionsMenuItemType;
import org.jtwig.orchid.tags.TagsMenuItemType;

public class JtwigModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, JtwigGenerator.class);

        addToMap(OrchidMenuItemType.class, "jtwigTags", TagsMenuItemType.class);
        addToMap(OrchidMenuItemType.class, "jtwigFunctions", FunctionsMenuItemType.class);
    }

}
