package pl.com.devmeet.devmeetcore.ui.normal_users.availability_view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.com.devmeet.devmeetcore.ui.normal_users.MainViewNormalUser;

@Route(value = "availabilityview", layout = MainViewNormalUser.class)
@PageTitle("AvailabilityView")
//@CssImport("styles/views/normal/availability/availability-view.css")
public class AvailabilityView extends Div implements AfterNavigationObserver {

    private final String divId = "availability-view";

    Text text;

    public AvailabilityView() {
        setId(divId);
        text = new Text("Availability view constructor");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        text = new Text("Availability view after select tab");
        add(text);
    }
}
