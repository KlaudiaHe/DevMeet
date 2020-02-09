package pl.com.devmeet.devmeetcore.ui.admins;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupCrudService;
import pl.com.devmeet.devmeetcore.group_associated.group.domain.GroupDto;

import java.util.ArrayList;
import java.util.List;

@Route("admin/groups")
class GroupsGui extends VerticalLayout {

    private GroupCrudService group;
    private List<GroupDto> groupList;

    // vaadin components
    private H1 header;
    private Grid<GroupDto> groupGrid;

    public GroupsGui(GroupCrudService group) {
        this.group = group;
        groupList = new ArrayList<>();
        groupList = group.findAll();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        Notification.show("Groups", 2000, Notification.Position.MIDDLE);

        header = new H1("devmeet app - groups");

        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems("active", "not active", "all");
        radioButtonGroup.setValue("all");

        groupGrid = new Grid<>(GroupDto.class);
        groupGrid.removeColumnByKey("creationTime");
        groupGrid.removeColumnByKey("website");
        groupGrid.removeColumnByKey("description");
        groupGrid.removeColumnByKey("modificationTime");
        groupGrid.removeColumnByKey("active");

        groupGrid.addThemeVariants(
                GridVariant.LUMO_NO_BORDER,
                GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        groupGrid.getColumns().forEach(c -> c.setAutoWidth(true));
        refreshGrid(groupList);

        radioButtonGroup.addValueChangeListener(e -> {
            if (e.getValue().equals("active"))
                groupList = group.findByActive(true);
            else if (e.getValue().equals("not active"))
                groupList = group.findByActive(false);
            else groupList = group.findAll();
            refreshGrid(groupList);
        });

        add(header, radioButtonGroup, groupGrid);

    }

    private void refreshGrid(List<GroupDto> groupList) {
        groupGrid.setItems(groupList);
        groupGrid.getDataProvider().refreshAll();
    }
}
