package pl.com.devmeet.devmeetcore.ui.admins;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.devmeet.devmeetcore.api.PlaceMapperApi;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceCrudService;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDto;
import pl.com.devmeet.devmeetcore.member_associated.place.domain.PlaceDtoApi;

import java.util.ArrayList;
import java.util.List;

@Route("admin/places")
class PlacesGui extends VerticalLayout {

    private PlaceCrudService place;
    private List<PlaceDtoApi> placeList;
    private PlaceMapperApi placeMapperApi;

    // vaadin components
    private H1 header;
    private Grid<PlaceDtoApi> placeGrid;

    @Autowired
    public PlacesGui(PlaceCrudService place, PlaceMapperApi placeMapperApi) {
        this.place = place;
        this.placeMapperApi = placeMapperApi;
        placeList = new ArrayList<>();
        List<PlaceDto> all = place.findAll();
        for (PlaceDto placeDto : all) {
            placeList.add(placeMapperApi
                    .getModelMapper()
                    .map(placeDto, PlaceDtoApi.class));
        }
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Notification.show("Places", 2000, Notification.Position.MIDDLE);

        header = new H1("devmeet app - places");

        placeGrid = new Grid<>(PlaceDtoApi.class);
        placeGrid.removeColumnByKey("id");
        placeGrid.removeColumnByKey("creationTime");
        placeGrid.removeColumnByKey("modificationTime");
        placeGrid.removeColumnByKey("active");

        refreshGrid(placeList);

        add(header, placeGrid);

    }

    private void refreshGrid(List<PlaceDtoApi> placeList) {
        placeGrid.setItems(placeList);
        placeGrid.getDataProvider().refreshAll();
    }
}
