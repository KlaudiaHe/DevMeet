package pl.com.devmeet.devmeetcore.place.domain.status_and_exceptions;

public enum PlaceCrudStatusEnum {
    PLACE_ALREADY_EXISTS("Place already exists"),
    PLACE_NOT_FOUND("Place not found"),
   PLACES_NOT_FOUND("Places not found"),
    PLACE_FOUND_BUT_NOT_ACTIVE("Place was found but is not active"),
    PLACE_INCORRECT_VALUES("Place has incorrect value / values"),
    METHOD_NOT_IMPLEMENTED("Method not implemented");

    private String status;

    PlaceCrudStatusEnum(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
