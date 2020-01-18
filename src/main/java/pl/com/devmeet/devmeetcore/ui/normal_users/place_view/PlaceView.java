package pl.com.devmeet.devmeetcore.ui.normal_users.place_view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.com.devmeet.devmeetcore.ui.normal_users.MainViewNormalUser;

@Route(value = "placeview", layout = MainViewNormalUser.class)
@PageTitle("PlaceView")
//@CssImport("styles/views/normal/place/place-view.css")
public class PlaceView extends Div implements AfterNavigationObserver {

    private final String divId = "place-view";

    Text text;

    public PlaceView() {
        setId(divId);
        text = new Text("Place view constructor");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        text = new Text("Place view after select tab");
        add(text);
    }
}
